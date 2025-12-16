package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.CommentMentionAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.CommentMention;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.repository.CommentMentionRepository;
import com.monsterdam.app.service.dto.CommentMentionDTO;
import com.monsterdam.app.service.mapper.CommentMentionMapper;
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
 * Integration tests for the {@link CommentMentionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommentMentionResourceIT {

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

    private static final String ENTITY_API_URL = "/api/comment-mentions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommentMentionRepository commentMentionRepository;

    @Autowired
    private CommentMentionMapper commentMentionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentMentionMockMvc;

    private CommentMention commentMention;

    private CommentMention insertedCommentMention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentMention createEntity(EntityManager em) {
        CommentMention commentMention = new CommentMention()
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
        commentMention.setMentionedUser(userLite);
        return commentMention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentMention createUpdatedEntity(EntityManager em) {
        CommentMention updatedCommentMention = new CommentMention()
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
        updatedCommentMention.setMentionedUser(userLite);
        return updatedCommentMention;
    }

    @BeforeEach
    void initTest() {
        commentMention = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCommentMention != null) {
            commentMentionRepository.delete(insertedCommentMention);
            insertedCommentMention = null;
        }
    }

    @Test
    @Transactional
    void createCommentMention() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CommentMention
        CommentMentionDTO commentMentionDTO = commentMentionMapper.toDto(commentMention);
        var returnedCommentMentionDTO = om.readValue(
            restCommentMentionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commentMentionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommentMentionDTO.class
        );

        // Validate the CommentMention in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCommentMention = commentMentionMapper.toEntity(returnedCommentMentionDTO);
        assertCommentMentionUpdatableFieldsEquals(returnedCommentMention, getPersistedCommentMention(returnedCommentMention));

        insertedCommentMention = returnedCommentMention;
    }

    @Test
    @Transactional
    void createCommentMentionWithExistingId() throws Exception {
        // Create the CommentMention with an existing ID
        commentMention.setId(1L);
        CommentMentionDTO commentMentionDTO = commentMentionMapper.toDto(commentMention);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentMentionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commentMentionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CommentMention in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        commentMention.setCreatedDate(null);

        // Create the CommentMention, which fails.
        CommentMentionDTO commentMentionDTO = commentMentionMapper.toDto(commentMention);

        restCommentMentionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commentMentionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommentMentions() throws Exception {
        // Initialize the database
        insertedCommentMention = commentMentionRepository.saveAndFlush(commentMention);

        // Get all the commentMentionList
        restCommentMentionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentMention.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCommentMention() throws Exception {
        // Initialize the database
        insertedCommentMention = commentMentionRepository.saveAndFlush(commentMention);

        // Get the commentMention
        restCommentMentionMockMvc
            .perform(get(ENTITY_API_URL_ID, commentMention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commentMention.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCommentMention() throws Exception {
        // Get the commentMention
        restCommentMentionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommentMention() throws Exception {
        // Initialize the database
        insertedCommentMention = commentMentionRepository.saveAndFlush(commentMention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commentMention
        CommentMention updatedCommentMention = commentMentionRepository.findById(commentMention.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommentMention are not directly saved in db
        em.detach(updatedCommentMention);
        updatedCommentMention
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        CommentMentionDTO commentMentionDTO = commentMentionMapper.toDto(updatedCommentMention);

        restCommentMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentMentionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commentMentionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommentMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommentMentionToMatchAllProperties(updatedCommentMention);
    }

    @Test
    @Transactional
    void putNonExistingCommentMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentMention.setId(longCount.incrementAndGet());

        // Create the CommentMention
        CommentMentionDTO commentMentionDTO = commentMentionMapper.toDto(commentMention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentMentionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commentMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommentMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentMention.setId(longCount.incrementAndGet());

        // Create the CommentMention
        CommentMentionDTO commentMentionDTO = commentMentionMapper.toDto(commentMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commentMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommentMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentMention.setId(longCount.incrementAndGet());

        // Create the CommentMention
        CommentMentionDTO commentMentionDTO = commentMentionMapper.toDto(commentMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMentionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commentMentionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommentMentionWithPatch() throws Exception {
        // Initialize the database
        insertedCommentMention = commentMentionRepository.saveAndFlush(commentMention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commentMention using partial update
        CommentMention partialUpdatedCommentMention = new CommentMention();
        partialUpdatedCommentMention.setId(commentMention.getId());

        partialUpdatedCommentMention
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restCommentMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentMention.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommentMention))
            )
            .andExpect(status().isOk());

        // Validate the CommentMention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommentMentionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommentMention, commentMention),
            getPersistedCommentMention(commentMention)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommentMentionWithPatch() throws Exception {
        // Initialize the database
        insertedCommentMention = commentMentionRepository.saveAndFlush(commentMention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commentMention using partial update
        CommentMention partialUpdatedCommentMention = new CommentMention();
        partialUpdatedCommentMention.setId(commentMention.getId());

        partialUpdatedCommentMention
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restCommentMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentMention.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommentMention))
            )
            .andExpect(status().isOk());

        // Validate the CommentMention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommentMentionUpdatableFieldsEquals(partialUpdatedCommentMention, getPersistedCommentMention(partialUpdatedCommentMention));
    }

    @Test
    @Transactional
    void patchNonExistingCommentMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentMention.setId(longCount.incrementAndGet());

        // Create the CommentMention
        CommentMentionDTO commentMentionDTO = commentMentionMapper.toDto(commentMention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentMentionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commentMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommentMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentMention.setId(longCount.incrementAndGet());

        // Create the CommentMention
        CommentMentionDTO commentMentionDTO = commentMentionMapper.toDto(commentMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commentMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommentMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentMention.setId(longCount.incrementAndGet());

        // Create the CommentMention
        CommentMentionDTO commentMentionDTO = commentMentionMapper.toDto(commentMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMentionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(commentMentionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommentMention() throws Exception {
        // Initialize the database
        insertedCommentMention = commentMentionRepository.saveAndFlush(commentMention);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the commentMention
        restCommentMentionMockMvc
            .perform(delete(ENTITY_API_URL_ID, commentMention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return commentMentionRepository.count();
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

    protected CommentMention getPersistedCommentMention(CommentMention commentMention) {
        return commentMentionRepository.findById(commentMention.getId()).orElseThrow();
    }

    protected void assertPersistedCommentMentionToMatchAllProperties(CommentMention expectedCommentMention) {
        assertCommentMentionAllPropertiesEquals(expectedCommentMention, getPersistedCommentMention(expectedCommentMention));
    }

    protected void assertPersistedCommentMentionToMatchUpdatableProperties(CommentMention expectedCommentMention) {
        assertCommentMentionAllUpdatablePropertiesEquals(expectedCommentMention, getPersistedCommentMention(expectedCommentMention));
    }
}
