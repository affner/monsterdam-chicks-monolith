package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.SingleAudioAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.SingleAudio;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.repository.SingleAudioRepository;
import com.monsterdam.app.service.dto.SingleAudioDTO;
import com.monsterdam.app.service.mapper.SingleAudioMapper;
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
 * Integration tests for the {@link SingleAudioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SingleAudioResourceIT {

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

    private static final String ENTITY_API_URL = "/api/single-audios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SingleAudioRepository singleAudioRepository;

    @Autowired
    private SingleAudioMapper singleAudioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSingleAudioMockMvc;

    private SingleAudio singleAudio;

    private SingleAudio insertedSingleAudio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleAudio createEntity(EntityManager em) {
        SingleAudio singleAudio = new SingleAudio()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .contentS3Key(DEFAULT_CONTENT_S_3_KEY)
            .duration(DEFAULT_DURATION)
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
        singleAudio.setCreator(userLite);
        return singleAudio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleAudio createUpdatedEntity(EntityManager em) {
        SingleAudio updatedSingleAudio = new SingleAudio()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
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
        updatedSingleAudio.setCreator(userLite);
        return updatedSingleAudio;
    }

    @BeforeEach
    void initTest() {
        singleAudio = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSingleAudio != null) {
            singleAudioRepository.delete(insertedSingleAudio);
            insertedSingleAudio = null;
        }
    }

    @Test
    @Transactional
    void createSingleAudio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);
        var returnedSingleAudioDTO = om.readValue(
            restSingleAudioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleAudioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SingleAudioDTO.class
        );

        // Validate the SingleAudio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSingleAudio = singleAudioMapper.toEntity(returnedSingleAudioDTO);
        assertSingleAudioUpdatableFieldsEquals(returnedSingleAudio, getPersistedSingleAudio(returnedSingleAudio));

        insertedSingleAudio = returnedSingleAudio;
    }

    @Test
    @Transactional
    void createSingleAudioWithExistingId() throws Exception {
        // Create the SingleAudio with an existing ID
        singleAudio.setId(1L);
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSingleAudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleAudioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SingleAudio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkThumbnailS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        singleAudio.setThumbnailS3Key(null);

        // Create the SingleAudio, which fails.
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        restSingleAudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleAudioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        singleAudio.setContentS3Key(null);

        // Create the SingleAudio, which fails.
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        restSingleAudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleAudioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        singleAudio.setCreatedDate(null);

        // Create the SingleAudio, which fails.
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        restSingleAudioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleAudioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSingleAudios() throws Exception {
        // Initialize the database
        insertedSingleAudio = singleAudioRepository.saveAndFlush(singleAudio);

        // Get all the singleAudioList
        restSingleAudioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(singleAudio.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].contentS3Key").value(hasItem(DEFAULT_CONTENT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSingleAudio() throws Exception {
        // Initialize the database
        insertedSingleAudio = singleAudioRepository.saveAndFlush(singleAudio);

        // Get the singleAudio
        restSingleAudioMockMvc
            .perform(get(ENTITY_API_URL_ID, singleAudio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(singleAudio.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.thumbnailS3Key").value(DEFAULT_THUMBNAIL_S_3_KEY))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64.getEncoder().encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.contentS3Key").value(DEFAULT_CONTENT_S_3_KEY))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSingleAudio() throws Exception {
        // Get the singleAudio
        restSingleAudioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSingleAudio() throws Exception {
        // Initialize the database
        insertedSingleAudio = singleAudioRepository.saveAndFlush(singleAudio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the singleAudio
        SingleAudio updatedSingleAudio = singleAudioRepository.findById(singleAudio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSingleAudio are not directly saved in db
        em.detach(updatedSingleAudio);
        updatedSingleAudio
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(updatedSingleAudio);

        restSingleAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleAudioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(singleAudioDTO))
            )
            .andExpect(status().isOk());

        // Validate the SingleAudio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSingleAudioToMatchAllProperties(updatedSingleAudio);
    }

    @Test
    @Transactional
    void putNonExistingSingleAudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, singleAudioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleAudio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSingleAudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleAudio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSingleAudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(singleAudioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleAudio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSingleAudioWithPatch() throws Exception {
        // Initialize the database
        insertedSingleAudio = singleAudioRepository.saveAndFlush(singleAudio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the singleAudio using partial update
        SingleAudio partialUpdatedSingleAudio = new SingleAudio();
        partialUpdatedSingleAudio.setId(singleAudio.getId());

        partialUpdatedSingleAudio
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restSingleAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleAudio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSingleAudio))
            )
            .andExpect(status().isOk());

        // Validate the SingleAudio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSingleAudioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSingleAudio, singleAudio),
            getPersistedSingleAudio(singleAudio)
        );
    }

    @Test
    @Transactional
    void fullUpdateSingleAudioWithPatch() throws Exception {
        // Initialize the database
        insertedSingleAudio = singleAudioRepository.saveAndFlush(singleAudio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the singleAudio using partial update
        SingleAudio partialUpdatedSingleAudio = new SingleAudio();
        partialUpdatedSingleAudio.setId(singleAudio.getId());

        partialUpdatedSingleAudio
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restSingleAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSingleAudio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSingleAudio))
            )
            .andExpect(status().isOk());

        // Validate the SingleAudio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSingleAudioUpdatableFieldsEquals(partialUpdatedSingleAudio, getPersistedSingleAudio(partialUpdatedSingleAudio));
    }

    @Test
    @Transactional
    void patchNonExistingSingleAudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, singleAudioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleAudio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSingleAudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(singleAudioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SingleAudio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSingleAudio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSingleAudioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(singleAudioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SingleAudio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSingleAudio() throws Exception {
        // Initialize the database
        insertedSingleAudio = singleAudioRepository.saveAndFlush(singleAudio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the singleAudio
        restSingleAudioMockMvc
            .perform(delete(ENTITY_API_URL_ID, singleAudio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return singleAudioRepository.count();
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

    protected SingleAudio getPersistedSingleAudio(SingleAudio singleAudio) {
        return singleAudioRepository.findById(singleAudio.getId()).orElseThrow();
    }

    protected void assertPersistedSingleAudioToMatchAllProperties(SingleAudio expectedSingleAudio) {
        assertSingleAudioAllPropertiesEquals(expectedSingleAudio, getPersistedSingleAudio(expectedSingleAudio));
    }

    protected void assertPersistedSingleAudioToMatchUpdatableProperties(SingleAudio expectedSingleAudio) {
        assertSingleAudioAllUpdatablePropertiesEquals(expectedSingleAudio, getPersistedSingleAudio(expectedSingleAudio));
    }
}
