package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.SingleVideoAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.SingleVideo;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.repository.SingleVideoRepository;
import com.monsterdam.app.service.dto.SingleVideoDTO;
import com.monsterdam.app.service.mapper.SingleVideoMapper;
import jakarta.persistence.EntityManager;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link SingleVideoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SingleVideoResourceIT {

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THUMBNAIL_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_S_3_KEY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CONTENT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_S_3_KEY = "BBBBBBBBBB";

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final Integer DEFAULT_LIKE_COUNT = 1;
    private static final Integer UPDATED_LIKE_COUNT = 2;

    private static final Boolean DEFAULT_IS_PREVIEW = false;
    private static final Boolean UPDATED_IS_PREVIEW = true;

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

    private static final String ENTITY_API_URL = "/api/single-videos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SingleVideoRepository singleVideoRepository;

    @Autowired
    private SingleVideoMapper singleVideoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSingleVideoMockMvc;

    private SingleVideo singleVideo;

    private SingleVideo insertedSingleVideo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleVideo createEntity(EntityManager em) {
        SingleVideo singleVideo = new SingleVideo()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .contentS3Key(DEFAULT_CONTENT_S_3_KEY)
            .duration(DEFAULT_DURATION)
            .likeCount(DEFAULT_LIKE_COUNT)
            .isPreview(DEFAULT_IS_PREVIEW)
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
        singleVideo.setCreator(userLite);
        return singleVideo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleVideo createUpdatedEntity(EntityManager em) {
        SingleVideo updatedSingleVideo = new SingleVideo()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .likeCount(UPDATED_LIKE_COUNT)
            .isPreview(UPDATED_IS_PREVIEW)
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
        updatedSingleVideo.setCreator(userLite);
        return updatedSingleVideo;
    }

    @BeforeEach
    void initTest() {
        singleVideo = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSingleVideo != null) {
            singleVideoRepository.delete(insertedSingleVideo);
            insertedSingleVideo = null;
        }
    }

    @Test
    @Transactional
    void createSingleVideo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);
        var returnedSingleVideoDTO = om.readValue(
            restSingleVideoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleVideoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SingleVideoDTO.class
        );

        // Validate the SingleVideo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSingleVideo = singleVideoMapper.toEntity(returnedSingleVideoDTO);
        assertSingleVideoUpdatableFieldsEquals(returnedSingleVideo, getPersistedSingleVideo(returnedSingleVideo));

        insertedSingleVideo = returnedSingleVideo;
    }

    @Test
    @Transactional
    void createSingleVideoWithExistingId() throws Exception {
        // Create the SingleVideo with an existing ID
        singleVideo.setId(1L);
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSingleVideoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleVideoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SingleVideo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkThumbnailS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        singleVideo.setThumbnailS3Key(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        restSingleVideoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleVideoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        singleVideo.setContentS3Key(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        restSingleVideoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleVideoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPreviewIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        singleVideo.setIsPreview(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        restSingleVideoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleVideoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        singleVideo.setCreatedDate(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        restSingleVideoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleVideoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSingleVideos() throws Exception {
        // Initialize the database
        insertedSingleVideo = singleVideoRepository.saveAndFlush(singleVideo);

        // Get all the singleVideoList
        restSingleVideoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(singleVideo.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].contentS3Key").value(hasItem(DEFAULT_CONTENT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].isPreview").value(hasItem(DEFAULT_IS_PREVIEW.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSingleVideo() throws Exception {
        // Initialize the database
        insertedSingleVideo = singleVideoRepository.saveAndFlush(singleVideo);

        // Get the singleVideo
        restSingleVideoMockMvc
            .perform(get(ENTITY_API_URL_ID, singleVideo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(singleVideo.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.thumbnailS3Key").value(DEFAULT_THUMBNAIL_S_3_KEY))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64.getEncoder().encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.contentS3Key").value(DEFAULT_CONTENT_S_3_KEY))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.likeCount").value(DEFAULT_LIKE_COUNT))
            .andExpect(jsonPath("$.isPreview").value(DEFAULT_IS_PREVIEW.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSingleVideo() throws Exception {
        // Get the singleVideo
        restSingleVideoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSingleVideo() throws Exception {
        // Initialize the database
        insertedSingleVideo = singleVideoRepository.saveAndFlush(singleVideo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the singleVideo
        SingleVideo updatedSingleVideo = singleVideoRepository.findById(singleVideo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSingleVideo are not directly saved in db
        em.detach(updatedSingleVideo);
        updatedSingleVideo
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(updatedSingleVideo);

        restSingleVideoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleVideoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(singleVideoDTO))
            )
            .andExpect(status().isOk());

        // Validate the SingleVideo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSingleVideoToMatchAllProperties(updatedSingleVideo);
    }

    @Test
    @Transactional
    void putNonExistingSingleVideo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleVideoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleVideo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSingleVideo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleVideo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSingleVideo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleVideoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleVideo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSingleVideoWithPatch() throws Exception {
        // Initialize the database
        insertedSingleVideo = singleVideoRepository.saveAndFlush(singleVideo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the singleVideo using partial update
        SingleVideo partialUpdatedSingleVideo = new SingleVideo();
        partialUpdatedSingleVideo.setId(singleVideo.getId());

        partialUpdatedSingleVideo
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .duration(UPDATED_DURATION)
            .isPreview(UPDATED_IS_PREVIEW)
            .createdDate(UPDATED_CREATED_DATE);

        restSingleVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleVideo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSingleVideo))
            )
            .andExpect(status().isOk());

        // Validate the SingleVideo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSingleVideoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSingleVideo, singleVideo),
            getPersistedSingleVideo(singleVideo)
        );
    }

    @Test
    @Transactional
    void fullUpdateSingleVideoWithPatch() throws Exception {
        // Initialize the database
        insertedSingleVideo = singleVideoRepository.saveAndFlush(singleVideo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the singleVideo using partial update
        SingleVideo partialUpdatedSingleVideo = new SingleVideo();
        partialUpdatedSingleVideo.setId(singleVideo.getId());

        partialUpdatedSingleVideo
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .likeCount(UPDATED_LIKE_COUNT)
            .isPreview(UPDATED_IS_PREVIEW)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restSingleVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleVideo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSingleVideo))
            )
            .andExpect(status().isOk());

        // Validate the SingleVideo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSingleVideoUpdatableFieldsEquals(partialUpdatedSingleVideo, getPersistedSingleVideo(partialUpdatedSingleVideo));
    }

    @Test
    @Transactional
    void patchNonExistingSingleVideo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, singleVideoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleVideo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSingleVideo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(singleVideoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleVideo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSingleVideo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleVideoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(singleVideoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleVideo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSingleVideo() throws Exception {
        // Initialize the database
        insertedSingleVideo = singleVideoRepository.saveAndFlush(singleVideo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the singleVideo
        restSingleVideoMockMvc
            .perform(delete(ENTITY_API_URL_ID, singleVideo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return singleVideoRepository.count();
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

    protected SingleVideo getPersistedSingleVideo(SingleVideo singleVideo) {
        return singleVideoRepository.findById(singleVideo.getId()).orElseThrow();
    }

    protected void assertPersistedSingleVideoToMatchAllProperties(SingleVideo expectedSingleVideo) {
        assertSingleVideoAllPropertiesEquals(expectedSingleVideo, getPersistedSingleVideo(expectedSingleVideo));
    }

    protected void assertPersistedSingleVideoToMatchUpdatableProperties(SingleVideo expectedSingleVideo) {
        assertSingleVideoAllUpdatablePropertiesEquals(expectedSingleVideo, getPersistedSingleVideo(expectedSingleVideo));
    }
}
