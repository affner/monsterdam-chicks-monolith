package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.VideoStoryAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.VideoStory;
import com.monsterdam.app.repository.VideoStoryRepository;
import com.monsterdam.app.service.dto.VideoStoryDTO;
import com.monsterdam.app.service.mapper.VideoStoryMapper;
import jakarta.persistence.EntityManager;
import java.time.Duration;
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
 * Integration tests for the {@link VideoStoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VideoStoryResourceIT {

    private static final String DEFAULT_THUMBNAIL_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_S_3_KEY = "BBBBBBBBBB";

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

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

    private static final Instant DEFAULT_DELETED_DATE = null;
    private static final Instant UPDATED_DELETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/video-stories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VideoStoryRepository videoStoryRepository;

    @Autowired
    private VideoStoryMapper videoStoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVideoStoryMockMvc;

    private VideoStory videoStory;

    private VideoStory insertedVideoStory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoStory createEntity(EntityManager em) {
        VideoStory videoStory = new VideoStory()
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .contentS3Key(DEFAULT_CONTENT_S_3_KEY)
            .duration(DEFAULT_DURATION)
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
        videoStory.setCreator(userLite);
        return videoStory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoStory createUpdatedEntity(EntityManager em) {
        VideoStory updatedVideoStory = new VideoStory()
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
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
        updatedVideoStory.setCreator(userLite);
        return updatedVideoStory;
    }

    @BeforeEach
    void initTest() {
        videoStory = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedVideoStory != null) {
            videoStoryRepository.delete(insertedVideoStory);
            insertedVideoStory = null;
        }
    }

    @Test
    @Transactional
    void createVideoStory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);
        var returnedVideoStoryDTO = om.readValue(
            restVideoStoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(videoStoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VideoStoryDTO.class
        );

        // Validate the VideoStory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVideoStory = videoStoryMapper.toEntity(returnedVideoStoryDTO);
        assertVideoStoryUpdatableFieldsEquals(returnedVideoStory, getPersistedVideoStory(returnedVideoStory));

        insertedVideoStory = returnedVideoStory;
    }

    @Test
    @Transactional
    void createVideoStoryWithExistingId() throws Exception {
        // Create the VideoStory with an existing ID
        videoStory.setId(1L);
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(videoStoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VideoStory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkThumbnailS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        videoStory.setThumbnailS3Key(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        restVideoStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(videoStoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        videoStory.setContentS3Key(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        restVideoStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(videoStoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        videoStory.setCreatedDate(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        restVideoStoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(videoStoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVideoStories() throws Exception {
        // Initialize the database
        insertedVideoStory = videoStoryRepository.saveAndFlush(videoStory);

        // Get all the videoStoryList
        restVideoStoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoStory.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].contentS3Key").value(hasItem(DEFAULT_CONTENT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getVideoStory() throws Exception {
        // Initialize the database
        insertedVideoStory = videoStoryRepository.saveAndFlush(videoStory);

        // Get the videoStory
        restVideoStoryMockMvc
            .perform(get(ENTITY_API_URL_ID, videoStory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(videoStory.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailS3Key").value(DEFAULT_THUMBNAIL_S_3_KEY))
            .andExpect(jsonPath("$.contentS3Key").value(DEFAULT_CONTENT_S_3_KEY))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.likeCount").value(DEFAULT_LIKE_COUNT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVideoStory() throws Exception {
        // Get the videoStory
        restVideoStoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVideoStory() throws Exception {
        // Initialize the database
        insertedVideoStory = videoStoryRepository.saveAndFlush(videoStory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the videoStory
        VideoStory updatedVideoStory = videoStoryRepository.findById(videoStory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVideoStory are not directly saved in db
        em.detach(updatedVideoStory);
        updatedVideoStory
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(updatedVideoStory);

        restVideoStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, videoStoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(videoStoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the VideoStory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVideoStoryToMatchAllProperties(updatedVideoStory);
    }

    @Test
    @Transactional
    void putNonExistingVideoStory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, videoStoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(videoStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoStory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVideoStory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(videoStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoStory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVideoStory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(videoStoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VideoStory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVideoStoryWithPatch() throws Exception {
        // Initialize the database
        insertedVideoStory = videoStoryRepository.saveAndFlush(videoStory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the videoStory using partial update
        VideoStory partialUpdatedVideoStory = new VideoStory();
        partialUpdatedVideoStory.setId(videoStory.getId());

        partialUpdatedVideoStory
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restVideoStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideoStory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVideoStory))
            )
            .andExpect(status().isOk());

        // Validate the VideoStory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVideoStoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVideoStory, videoStory),
            getPersistedVideoStory(videoStory)
        );
    }

    @Test
    @Transactional
    void fullUpdateVideoStoryWithPatch() throws Exception {
        // Initialize the database
        insertedVideoStory = videoStoryRepository.saveAndFlush(videoStory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the videoStory using partial update
        VideoStory partialUpdatedVideoStory = new VideoStory();
        partialUpdatedVideoStory.setId(videoStory.getId());

        partialUpdatedVideoStory
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restVideoStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideoStory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVideoStory))
            )
            .andExpect(status().isOk());

        // Validate the VideoStory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVideoStoryUpdatableFieldsEquals(partialUpdatedVideoStory, getPersistedVideoStory(partialUpdatedVideoStory));
    }

    @Test
    @Transactional
    void patchNonExistingVideoStory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, videoStoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(videoStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoStory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVideoStory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(videoStoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VideoStory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVideoStory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideoStoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(videoStoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VideoStory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVideoStory() throws Exception {
        // Initialize the database
        insertedVideoStory = videoStoryRepository.saveAndFlush(videoStory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the videoStory
        restVideoStoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, videoStory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return videoStoryRepository.count();
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

    protected VideoStory getPersistedVideoStory(VideoStory videoStory) {
        return videoStoryRepository.findById(videoStory.getId()).orElseThrow();
    }

    protected void assertPersistedVideoStoryToMatchAllProperties(VideoStory expectedVideoStory) {
        assertVideoStoryAllPropertiesEquals(expectedVideoStory, getPersistedVideoStory(expectedVideoStory));
    }

    protected void assertPersistedVideoStoryToMatchUpdatableProperties(VideoStory expectedVideoStory) {
        assertVideoStoryAllUpdatablePropertiesEquals(expectedVideoStory, getPersistedVideoStory(expectedVideoStory));
    }
}
