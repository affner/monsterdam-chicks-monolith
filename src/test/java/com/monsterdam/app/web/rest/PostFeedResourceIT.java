package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.PostFeedAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.repository.PostFeedRepository;
import com.monsterdam.app.service.dto.PostFeedDTO;
import com.monsterdam.app.service.mapper.PostFeedMapper;
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
 * Integration tests for the {@link PostFeedResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostFeedResourceIT {

    private static final String DEFAULT_POST_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_POST_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_HIDDEN = false;
    private static final Boolean UPDATED_IS_HIDDEN = true;

    private static final Boolean DEFAULT_PINNED_POST = false;
    private static final Boolean UPDATED_PINNED_POST = true;

    private static final Integer DEFAULT_LIKE_COUNT = 1;
    private static final Integer UPDATED_LIKE_COUNT = 2;

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

    private static final String ENTITY_API_URL = "/api/post-feeds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostFeedRepository postFeedRepository;

    @Autowired
    private PostFeedMapper postFeedMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostFeedMockMvc;

    private PostFeed postFeed;

    private PostFeed insertedPostFeed;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostFeed createEntity(EntityManager em) {
        PostFeed postFeed = new PostFeed()
            .postContent(DEFAULT_POST_CONTENT)
            .isHidden(DEFAULT_IS_HIDDEN)
            .pinnedPost(DEFAULT_PINNED_POST)
            .likeCount(DEFAULT_LIKE_COUNT)
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
        postFeed.setCreator(userLite);
        return postFeed;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostFeed createUpdatedEntity(EntityManager em) {
        PostFeed updatedPostFeed = new PostFeed()
            .postContent(UPDATED_POST_CONTENT)
            .isHidden(UPDATED_IS_HIDDEN)
            .pinnedPost(UPDATED_PINNED_POST)
            .likeCount(UPDATED_LIKE_COUNT)
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
        updatedPostFeed.setCreator(userLite);
        return updatedPostFeed;
    }

    @BeforeEach
    void initTest() {
        postFeed = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPostFeed != null) {
            postFeedRepository.delete(insertedPostFeed);
            insertedPostFeed = null;
        }
    }

    @Test
    @Transactional
    void createPostFeed() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);
        var returnedPostFeedDTO = om.readValue(
            restPostFeedMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postFeedDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PostFeedDTO.class
        );

        // Validate the PostFeed in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPostFeed = postFeedMapper.toEntity(returnedPostFeedDTO);
        assertPostFeedUpdatableFieldsEquals(returnedPostFeed, getPersistedPostFeed(returnedPostFeed));

        insertedPostFeed = returnedPostFeed;
    }

    @Test
    @Transactional
    void createPostFeedWithExistingId() throws Exception {
        // Create the PostFeed with an existing ID
        postFeed.setId(1L);
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostFeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postFeedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostFeed in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postFeed.setCreatedDate(null);

        // Create the PostFeed, which fails.
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        restPostFeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postFeedDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostFeeds() throws Exception {
        // Initialize the database
        insertedPostFeed = postFeedRepository.saveAndFlush(postFeed);

        // Get all the postFeedList
        restPostFeedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postFeed.getId().intValue())))
            .andExpect(jsonPath("$.[*].postContent").value(hasItem(DEFAULT_POST_CONTENT)))
            .andExpect(jsonPath("$.[*].isHidden").value(hasItem(DEFAULT_IS_HIDDEN)))
            .andExpect(jsonPath("$.[*].pinnedPost").value(hasItem(DEFAULT_PINNED_POST)))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPostFeed() throws Exception {
        // Initialize the database
        insertedPostFeed = postFeedRepository.saveAndFlush(postFeed);

        // Get the postFeed
        restPostFeedMockMvc
            .perform(get(ENTITY_API_URL_ID, postFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postFeed.getId().intValue()))
            .andExpect(jsonPath("$.postContent").value(DEFAULT_POST_CONTENT))
            .andExpect(jsonPath("$.isHidden").value(DEFAULT_IS_HIDDEN))
            .andExpect(jsonPath("$.pinnedPost").value(DEFAULT_PINNED_POST))
            .andExpect(jsonPath("$.likeCount").value(DEFAULT_LIKE_COUNT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPostFeed() throws Exception {
        // Get the postFeed
        restPostFeedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostFeed() throws Exception {
        // Initialize the database
        insertedPostFeed = postFeedRepository.saveAndFlush(postFeed);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postFeed
        PostFeed updatedPostFeed = postFeedRepository.findById(postFeed.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostFeed are not directly saved in db
        em.detach(updatedPostFeed);
        updatedPostFeed
            .postContent(UPDATED_POST_CONTENT)
            .isHidden(UPDATED_IS_HIDDEN)
            .pinnedPost(UPDATED_PINNED_POST)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(updatedPostFeed);

        restPostFeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postFeedDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postFeedDTO))
            )
            .andExpect(status().isOk());

        // Validate the PostFeed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPostFeedToMatchAllProperties(updatedPostFeed);
    }

    @Test
    @Transactional
    void putNonExistingPostFeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postFeedDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postFeedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostFeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postFeedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostFeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postFeedDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostFeed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostFeedWithPatch() throws Exception {
        // Initialize the database
        insertedPostFeed = postFeedRepository.saveAndFlush(postFeed);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postFeed using partial update
        PostFeed partialUpdatedPostFeed = new PostFeed();
        partialUpdatedPostFeed.setId(postFeed.getId());

        partialUpdatedPostFeed
            .postContent(UPDATED_POST_CONTENT)
            .pinnedPost(UPDATED_PINNED_POST)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restPostFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostFeed.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostFeed))
            )
            .andExpect(status().isOk());

        // Validate the PostFeed in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostFeedUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPostFeed, postFeed), getPersistedPostFeed(postFeed));
    }

    @Test
    @Transactional
    void fullUpdatePostFeedWithPatch() throws Exception {
        // Initialize the database
        insertedPostFeed = postFeedRepository.saveAndFlush(postFeed);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postFeed using partial update
        PostFeed partialUpdatedPostFeed = new PostFeed();
        partialUpdatedPostFeed.setId(postFeed.getId());

        partialUpdatedPostFeed
            .postContent(UPDATED_POST_CONTENT)
            .isHidden(UPDATED_IS_HIDDEN)
            .pinnedPost(UPDATED_PINNED_POST)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPostFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostFeed.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostFeed))
            )
            .andExpect(status().isOk());

        // Validate the PostFeed in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostFeedUpdatableFieldsEquals(partialUpdatedPostFeed, getPersistedPostFeed(partialUpdatedPostFeed));
    }

    @Test
    @Transactional
    void patchNonExistingPostFeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postFeedDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postFeedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostFeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postFeedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostFeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(postFeedDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostFeed in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostFeed() throws Exception {
        // Initialize the database
        insertedPostFeed = postFeedRepository.saveAndFlush(postFeed);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the postFeed
        restPostFeedMockMvc
            .perform(delete(ENTITY_API_URL_ID, postFeed.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return postFeedRepository.count();
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

    protected PostFeed getPersistedPostFeed(PostFeed postFeed) {
        return postFeedRepository.findById(postFeed.getId()).orElseThrow();
    }

    protected void assertPersistedPostFeedToMatchAllProperties(PostFeed expectedPostFeed) {
        assertPostFeedAllPropertiesEquals(expectedPostFeed, getPersistedPostFeed(expectedPostFeed));
    }

    protected void assertPersistedPostFeedToMatchUpdatableProperties(PostFeed expectedPostFeed) {
        assertPostFeedAllUpdatablePropertiesEquals(expectedPostFeed, getPersistedPostFeed(expectedPostFeed));
    }
}
