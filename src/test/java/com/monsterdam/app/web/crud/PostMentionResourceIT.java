package com.monsterdam.app.web.crud;

import static com.monsterdam.app.domain.PostMentionAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PostMention;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.repository.PostMentionRepository;
import com.monsterdam.app.service.dto.PostMentionDTO;
import com.monsterdam.app.service.mapper.PostMentionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PostMentionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostMentionResourceIT {

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELETED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/crud/post-mentions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostMentionRepository postMentionRepository;

    @Autowired
    private PostMentionMapper postMentionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostMentionMockMvc;

    private PostMention postMention;

    private PostMention insertedPostMention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostMention createEntity(EntityManager em) {
        PostMention postMention = new PostMention()
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .deletedDate(DEFAULT_DELETED_DATE);
        // Add required entity
        UserLite userLite;
        if (TestUtil.findAll(em, UserLite.class).isEmpty()) {
            userLite = UserLiteResourceIT.createEntity();
            em.persist(userLite);
            em.flush();
        } else {
            userLite = TestUtil.findAll(em, UserLite.class).get(0);
        }
        postMention.setMentionedUser(userLite);
        return postMention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostMention createUpdatedEntity(EntityManager em) {
        PostMention updatedPostMention = new PostMention()
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        // Add required entity
        UserLite userLite;
        if (TestUtil.findAll(em, UserLite.class).isEmpty()) {
            userLite = UserLiteResourceIT.createUpdatedEntity();
            em.persist(userLite);
            em.flush();
        } else {
            userLite = TestUtil.findAll(em, UserLite.class).get(0);
        }
        updatedPostMention.setMentionedUser(userLite);
        return updatedPostMention;
    }

    @BeforeEach
    void initTest() {
        postMention = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPostMention != null) {
            postMentionRepository.delete(insertedPostMention);
            insertedPostMention = null;
        }
    }

    @Test
    @Transactional
    void createPostMention() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PostMention
        PostMentionDTO postMentionDTO = postMentionMapper.toDto(postMention);
        var returnedPostMentionDTO = om.readValue(
            restPostMentionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postMentionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PostMentionDTO.class
        );

        // Validate the PostMention in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPostMention = postMentionMapper.toEntity(returnedPostMentionDTO);
        assertPostMentionUpdatableFieldsEquals(returnedPostMention, getPersistedPostMention(returnedPostMention));

        insertedPostMention = returnedPostMention;
    }

    @Test
    @Transactional
    void createPostMentionWithExistingId() throws Exception {
        // Create the PostMention with an existing ID
        postMention.setId(1L);
        PostMentionDTO postMentionDTO = postMentionMapper.toDto(postMention);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostMentionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postMentionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostMention in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postMention.setCreatedDate(null);

        // Create the PostMention, which fails.
        PostMentionDTO postMentionDTO = postMentionMapper.toDto(postMention);

        restPostMentionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postMentionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostMentions() throws Exception {
        // Initialize the database
        insertedPostMention = postMentionRepository.saveAndFlush(postMention);

        // Get all the postMentionList
        restPostMentionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postMention.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPostMention() throws Exception {
        // Initialize the database
        insertedPostMention = postMentionRepository.saveAndFlush(postMention);

        // Get the postMention
        restPostMentionMockMvc
            .perform(get(ENTITY_API_URL_ID, postMention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postMention.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPostMention() throws Exception {
        // Get the postMention
        restPostMentionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostMention() throws Exception {
        // Initialize the database
        insertedPostMention = postMentionRepository.saveAndFlush(postMention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postMention
        PostMention updatedPostMention = postMentionRepository.findById(postMention.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostMention are not directly saved in db
        em.detach(updatedPostMention);
        updatedPostMention
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        PostMentionDTO postMentionDTO = postMentionMapper.toDto(updatedPostMention);

        restPostMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postMentionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postMentionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PostMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPostMentionToMatchAllProperties(updatedPostMention);
    }

    @Test
    @Transactional
    void putNonExistingPostMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postMention.setId(longCount.incrementAndGet());

        // Create the PostMention
        PostMentionDTO postMentionDTO = postMentionMapper.toDto(postMention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postMentionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postMention.setId(longCount.incrementAndGet());

        // Create the PostMention
        PostMentionDTO postMentionDTO = postMentionMapper.toDto(postMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postMention.setId(longCount.incrementAndGet());

        // Create the PostMention
        PostMentionDTO postMentionDTO = postMentionMapper.toDto(postMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMentionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postMentionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostMentionWithPatch() throws Exception {
        // Initialize the database
        insertedPostMention = postMentionRepository.saveAndFlush(postMention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postMention using partial update
        PostMention partialUpdatedPostMention = new PostMention();
        partialUpdatedPostMention.setId(postMention.getId());

        partialUpdatedPostMention
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPostMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostMention.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostMention))
            )
            .andExpect(status().isOk());

        // Validate the PostMention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostMentionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPostMention, postMention),
            getPersistedPostMention(postMention)
        );
    }

    @Test
    @Transactional
    void fullUpdatePostMentionWithPatch() throws Exception {
        // Initialize the database
        insertedPostMention = postMentionRepository.saveAndFlush(postMention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postMention using partial update
        PostMention partialUpdatedPostMention = new PostMention();
        partialUpdatedPostMention.setId(postMention.getId());

        partialUpdatedPostMention
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPostMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostMention.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostMention))
            )
            .andExpect(status().isOk());

        // Validate the PostMention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostMentionUpdatableFieldsEquals(partialUpdatedPostMention, getPersistedPostMention(partialUpdatedPostMention));
    }

    @Test
    @Transactional
    void patchNonExistingPostMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postMention.setId(longCount.incrementAndGet());

        // Create the PostMention
        PostMentionDTO postMentionDTO = postMentionMapper.toDto(postMention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postMentionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postMention.setId(longCount.incrementAndGet());

        // Create the PostMention
        PostMentionDTO postMentionDTO = postMentionMapper.toDto(postMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postMention.setId(longCount.incrementAndGet());

        // Create the PostMention
        PostMentionDTO postMentionDTO = postMentionMapper.toDto(postMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMentionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(postMentionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostMention() throws Exception {
        // Initialize the database
        insertedPostMention = postMentionRepository.saveAndFlush(postMention);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the postMention
        restPostMentionMockMvc
            .perform(delete(ENTITY_API_URL_ID, postMention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return postMentionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PostMention getPersistedPostMention(PostMention postMention) {
        return postMentionRepository.findById(postMention.getId()).orElseThrow();
    }

    protected void assertPersistedPostMentionToMatchAllProperties(PostMention expectedPostMention) {
        assertPostMentionAllPropertiesEquals(expectedPostMention, getPersistedPostMention(expectedPostMention));
    }

    protected void assertPersistedPostMentionToMatchUpdatableProperties(PostMention expectedPostMention) {
        assertPostMentionAllUpdatablePropertiesEquals(expectedPostMention, getPersistedPostMention(expectedPostMention));
    }
}
