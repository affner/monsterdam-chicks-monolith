package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.PostPollAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.domain.PostPoll;
import com.monsterdam.app.repository.PostPollRepository;
import com.monsterdam.app.service.dto.PostPollDTO;
import com.monsterdam.app.service.mapper.PostPollMapper;
import jakarta.persistence.EntityManager;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PostPollResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostPollResourceIT {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MULTI_CHOICE = false;
    private static final Boolean UPDATED_IS_MULTI_CHOICE = true;

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

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

    private static final String ENTITY_API_URL = "/api/post-polls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostPollRepository postPollRepository;

    @Autowired
    private PostPollMapper postPollMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostPollMockMvc;

    private PostPoll postPoll;

    private PostPoll insertedPostPoll;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostPoll createEntity(EntityManager em) {
        PostPoll postPoll = new PostPoll()
            .question(DEFAULT_QUESTION)
            .isMultiChoice(DEFAULT_IS_MULTI_CHOICE)
            .endDate(DEFAULT_END_DATE)
            .duration(DEFAULT_DURATION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .deletedDate(DEFAULT_DELETED_DATE);
        // Add required entity
        PostFeed postFeed;
        if (TestUtil.findAll(em, PostFeed.class).isEmpty()) {
            postFeed = PostFeedResourceIT.createEntity(em);
            em.persist(postFeed);
            em.flush();
        } else {
            postFeed = TestUtil.findAll(em, PostFeed.class).get(0);
        }
        postPoll.setPost(postFeed);
        return postPoll;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostPoll createUpdatedEntity(EntityManager em) {
        PostPoll updatedPostPoll = new PostPoll()
            .question(UPDATED_QUESTION)
            .isMultiChoice(UPDATED_IS_MULTI_CHOICE)
            .endDate(UPDATED_END_DATE)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        // Add required entity
        PostFeed postFeed;
        if (TestUtil.findAll(em, PostFeed.class).isEmpty()) {
            postFeed = PostFeedResourceIT.createUpdatedEntity(em);
            em.persist(postFeed);
            em.flush();
        } else {
            postFeed = TestUtil.findAll(em, PostFeed.class).get(0);
        }
        updatedPostPoll.setPost(postFeed);
        return updatedPostPoll;
    }

    @BeforeEach
    void initTest() {
        postPoll = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPostPoll != null) {
            postPollRepository.delete(insertedPostPoll);
            insertedPostPoll = null;
        }
    }

    @Test
    @Transactional
    void createPostPoll() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);
        var returnedPostPollDTO = om.readValue(
            restPostPollMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPollDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PostPollDTO.class
        );

        // Validate the PostPoll in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPostPoll = postPollMapper.toEntity(returnedPostPollDTO);
        assertPostPollUpdatableFieldsEquals(returnedPostPoll, getPersistedPostPoll(returnedPostPoll));

        insertedPostPoll = returnedPostPoll;
    }

    @Test
    @Transactional
    void createPostPollWithExistingId() throws Exception {
        // Create the PostPoll with an existing ID
        postPoll.setId(1L);
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPollDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostPoll in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsMultiChoiceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postPoll.setIsMultiChoice(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPollDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postPoll.setEndDate(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPollDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postPoll.setDuration(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPollDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postPoll.setCreatedDate(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        restPostPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPollDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostPolls() throws Exception {
        // Initialize the database
        insertedPostPoll = postPollRepository.saveAndFlush(postPoll);

        // Get all the postPollList
        restPostPollMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postPoll.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].isMultiChoice").value(hasItem(DEFAULT_IS_MULTI_CHOICE)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPostPoll() throws Exception {
        // Initialize the database
        insertedPostPoll = postPollRepository.saveAndFlush(postPoll);

        // Get the postPoll
        restPostPollMockMvc
            .perform(get(ENTITY_API_URL_ID, postPoll.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postPoll.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION))
            .andExpect(jsonPath("$.isMultiChoice").value(DEFAULT_IS_MULTI_CHOICE))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPostPoll() throws Exception {
        // Get the postPoll
        restPostPollMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostPoll() throws Exception {
        // Initialize the database
        insertedPostPoll = postPollRepository.saveAndFlush(postPoll);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postPoll
        PostPoll updatedPostPoll = postPollRepository.findById(postPoll.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostPoll are not directly saved in db
        em.detach(updatedPostPoll);
        updatedPostPoll
            .question(UPDATED_QUESTION)
            .isMultiChoice(UPDATED_IS_MULTI_CHOICE)
            .endDate(UPDATED_END_DATE)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        PostPollDTO postPollDTO = postPollMapper.toDto(updatedPostPoll);

        restPostPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postPollDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postPollDTO))
            )
            .andExpect(status().isOk());

        // Validate the PostPoll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPostPollToMatchAllProperties(updatedPostPoll);
    }

    @Test
    @Transactional
    void putNonExistingPostPoll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postPollDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPoll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostPoll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPoll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostPoll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPollDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostPoll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostPollWithPatch() throws Exception {
        // Initialize the database
        insertedPostPoll = postPollRepository.saveAndFlush(postPoll);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postPoll using partial update
        PostPoll partialUpdatedPostPoll = new PostPoll();
        partialUpdatedPostPoll.setId(postPoll.getId());

        partialUpdatedPostPoll
            .isMultiChoice(UPDATED_IS_MULTI_CHOICE)
            .endDate(UPDATED_END_DATE)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPostPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostPoll.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostPoll))
            )
            .andExpect(status().isOk());

        // Validate the PostPoll in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostPollUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPostPoll, postPoll), getPersistedPostPoll(postPoll));
    }

    @Test
    @Transactional
    void fullUpdatePostPollWithPatch() throws Exception {
        // Initialize the database
        insertedPostPoll = postPollRepository.saveAndFlush(postPoll);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postPoll using partial update
        PostPoll partialUpdatedPostPoll = new PostPoll();
        partialUpdatedPostPoll.setId(postPoll.getId());

        partialUpdatedPostPoll
            .question(UPDATED_QUESTION)
            .isMultiChoice(UPDATED_IS_MULTI_CHOICE)
            .endDate(UPDATED_END_DATE)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPostPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostPoll.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostPoll))
            )
            .andExpect(status().isOk());

        // Validate the PostPoll in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostPollUpdatableFieldsEquals(partialUpdatedPostPoll, getPersistedPostPoll(partialUpdatedPostPoll));
    }

    @Test
    @Transactional
    void patchNonExistingPostPoll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postPollDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPoll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostPoll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPoll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostPoll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPollMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(postPollDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostPoll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostPoll() throws Exception {
        // Initialize the database
        insertedPostPoll = postPollRepository.saveAndFlush(postPoll);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the postPoll
        restPostPollMockMvc
            .perform(delete(ENTITY_API_URL_ID, postPoll.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return postPollRepository.count();
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

    protected PostPoll getPersistedPostPoll(PostPoll postPoll) {
        return postPollRepository.findById(postPoll.getId()).orElseThrow();
    }

    protected void assertPersistedPostPollToMatchAllProperties(PostPoll expectedPostPoll) {
        assertPostPollAllPropertiesEquals(expectedPostPoll, getPersistedPostPoll(expectedPostPoll));
    }

    protected void assertPersistedPostPollToMatchUpdatableProperties(PostPoll expectedPostPoll) {
        assertPostPollAllUpdatablePropertiesEquals(expectedPostPoll, getPersistedPostPoll(expectedPostPoll));
    }
}
