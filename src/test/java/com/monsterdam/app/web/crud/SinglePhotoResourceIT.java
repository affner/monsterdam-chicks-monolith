package com.monsterdam.app.web.crud;

import static com.monsterdam.app.domain.SinglePhotoAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.SinglePhoto;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.repository.SinglePhotoRepository;
import com.monsterdam.app.service.dto.SinglePhotoDTO;
import com.monsterdam.app.service.mapper.SinglePhotoMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SinglePhotoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SinglePhotoResourceIT {

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

    private static final Instant DEFAULT_DELETED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/crud/single-photos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SinglePhotoRepository singlePhotoRepository;

    @Autowired
    private SinglePhotoMapper singlePhotoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSinglePhotoMockMvc;

    private SinglePhoto singlePhoto;

    private SinglePhoto insertedSinglePhoto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SinglePhoto createEntity(EntityManager em) {
        SinglePhoto singlePhoto = new SinglePhoto()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .contentS3Key(DEFAULT_CONTENT_S_3_KEY)
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
        singlePhoto.setCreator(userLite);
        return singlePhoto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SinglePhoto createUpdatedEntity(EntityManager em) {
        SinglePhoto updatedSinglePhoto = new SinglePhoto()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
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
        updatedSinglePhoto.setCreator(userLite);
        return updatedSinglePhoto;
    }

    @BeforeEach
    void initTest() {
        singlePhoto = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSinglePhoto != null) {
            singlePhotoRepository.delete(insertedSinglePhoto);
            insertedSinglePhoto = null;
        }
    }

    @Test
    @Transactional
    void createSinglePhoto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);
        var returnedSinglePhotoDTO = om.readValue(
            restSinglePhotoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singlePhotoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SinglePhotoDTO.class
        );

        // Validate the SinglePhoto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSinglePhoto = singlePhotoMapper.toEntity(returnedSinglePhotoDTO);
        assertSinglePhotoUpdatableFieldsEquals(returnedSinglePhoto, getPersistedSinglePhoto(returnedSinglePhoto));

        insertedSinglePhoto = returnedSinglePhoto;
    }

    @Test
    @Transactional
    void createSinglePhotoWithExistingId() throws Exception {
        // Create the SinglePhoto with an existing ID
        singlePhoto.setId(1L);
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSinglePhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singlePhotoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SinglePhoto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkThumbnailS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        singlePhoto.setThumbnailS3Key(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        restSinglePhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singlePhotoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        singlePhoto.setContentS3Key(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        restSinglePhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singlePhotoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPreviewIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        singlePhoto.setIsPreview(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        restSinglePhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singlePhotoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        singlePhoto.setCreatedDate(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        restSinglePhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singlePhotoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSinglePhotos() throws Exception {
        // Initialize the database
        insertedSinglePhoto = singlePhotoRepository.saveAndFlush(singlePhoto);

        // Get all the singlePhotoList
        restSinglePhotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(singlePhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].contentS3Key").value(hasItem(DEFAULT_CONTENT_S_3_KEY)))
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
    void getSinglePhoto() throws Exception {
        // Initialize the database
        insertedSinglePhoto = singlePhotoRepository.saveAndFlush(singlePhoto);

        // Get the singlePhoto
        restSinglePhotoMockMvc
            .perform(get(ENTITY_API_URL_ID, singlePhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(singlePhoto.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.thumbnailS3Key").value(DEFAULT_THUMBNAIL_S_3_KEY))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64.getEncoder().encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.contentS3Key").value(DEFAULT_CONTENT_S_3_KEY))
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
    void getNonExistingSinglePhoto() throws Exception {
        // Get the singlePhoto
        restSinglePhotoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSinglePhoto() throws Exception {
        // Initialize the database
        insertedSinglePhoto = singlePhotoRepository.saveAndFlush(singlePhoto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the singlePhoto
        SinglePhoto updatedSinglePhoto = singlePhotoRepository.findById(singlePhoto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSinglePhoto are not directly saved in db
        em.detach(updatedSinglePhoto);
        updatedSinglePhoto
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(updatedSinglePhoto);

        restSinglePhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singlePhotoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(singlePhotoDTO))
            )
            .andExpect(status().isOk());

        // Validate the SinglePhoto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSinglePhotoToMatchAllProperties(updatedSinglePhoto);
    }

    @Test
    @Transactional
    void putNonExistingSinglePhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singlePhotoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SinglePhoto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSinglePhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SinglePhoto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSinglePhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singlePhotoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SinglePhoto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSinglePhotoWithPatch() throws Exception {
        // Initialize the database
        insertedSinglePhoto = singlePhotoRepository.saveAndFlush(singlePhoto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the singlePhoto using partial update
        SinglePhoto partialUpdatedSinglePhoto = new SinglePhoto();
        partialUpdatedSinglePhoto.setId(singlePhoto.getId());

        partialUpdatedSinglePhoto
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .likeCount(UPDATED_LIKE_COUNT)
            .isPreview(UPDATED_IS_PREVIEW)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .deletedDate(UPDATED_DELETED_DATE);

        restSinglePhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSinglePhoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSinglePhoto))
            )
            .andExpect(status().isOk());

        // Validate the SinglePhoto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSinglePhotoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSinglePhoto, singlePhoto),
            getPersistedSinglePhoto(singlePhoto)
        );
    }

    @Test
    @Transactional
    void fullUpdateSinglePhotoWithPatch() throws Exception {
        // Initialize the database
        insertedSinglePhoto = singlePhotoRepository.saveAndFlush(singlePhoto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the singlePhoto using partial update
        SinglePhoto partialUpdatedSinglePhoto = new SinglePhoto();
        partialUpdatedSinglePhoto.setId(singlePhoto.getId());

        partialUpdatedSinglePhoto
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .likeCount(UPDATED_LIKE_COUNT)
            .isPreview(UPDATED_IS_PREVIEW)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restSinglePhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSinglePhoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSinglePhoto))
            )
            .andExpect(status().isOk());

        // Validate the SinglePhoto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSinglePhotoUpdatableFieldsEquals(partialUpdatedSinglePhoto, getPersistedSinglePhoto(partialUpdatedSinglePhoto));
    }

    @Test
    @Transactional
    void patchNonExistingSinglePhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, singlePhotoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SinglePhoto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSinglePhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(singlePhotoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SinglePhoto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSinglePhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinglePhotoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(singlePhotoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SinglePhoto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSinglePhoto() throws Exception {
        // Initialize the database
        insertedSinglePhoto = singlePhotoRepository.saveAndFlush(singlePhoto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the singlePhoto
        restSinglePhotoMockMvc
            .perform(delete(ENTITY_API_URL_ID, singlePhoto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return singlePhotoRepository.count();
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

    protected SinglePhoto getPersistedSinglePhoto(SinglePhoto singlePhoto) {
        return singlePhotoRepository.findById(singlePhoto.getId()).orElseThrow();
    }

    protected void assertPersistedSinglePhotoToMatchAllProperties(SinglePhoto expectedSinglePhoto) {
        assertSinglePhotoAllPropertiesEquals(expectedSinglePhoto, getPersistedSinglePhoto(expectedSinglePhoto));
    }

    protected void assertPersistedSinglePhotoToMatchUpdatableProperties(SinglePhoto expectedSinglePhoto) {
        assertSinglePhotoAllUpdatablePropertiesEquals(expectedSinglePhoto, getPersistedSinglePhoto(expectedSinglePhoto));
    }
}
