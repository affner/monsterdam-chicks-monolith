package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.LikeMarkAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.LikeMark;
import com.monsterdam.app.domain.enumeration.LikeEntityKind;
import com.monsterdam.app.repository.LikeMarkRepository;
import com.monsterdam.app.service.dto.LikeMarkDTO;
import com.monsterdam.app.service.mapper.LikeMarkMapper;
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
 * Integration tests for the {@link LikeMarkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikeMarkResourceIT {

    private static final LikeEntityKind DEFAULT_ENTITY_TYPE = LikeEntityKind.VIDEO;
    private static final LikeEntityKind UPDATED_ENTITY_TYPE = LikeEntityKind.PHOTO;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Long DEFAULT_MULTIMEDIA_ID = 1L;
    private static final Long UPDATED_MULTIMEDIA_ID = 2L;

    private static final Long DEFAULT_MESSAGE_ID = 1L;
    private static final Long UPDATED_MESSAGE_ID = 2L;

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final Long DEFAULT_COMMENT_ID = 1L;
    private static final Long UPDATED_COMMENT_ID = 2L;

    private static final Long DEFAULT_LIKER_ID = 1L;
    private static final Long UPDATED_LIKER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/like-marks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LikeMarkRepository likeMarkRepository;

    @Autowired
    private LikeMarkMapper likeMarkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikeMarkMockMvc;

    private LikeMark likeMark;

    private LikeMark insertedLikeMark;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeMark createEntity() {
        return new LikeMark()
            .entityType(DEFAULT_ENTITY_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .multimediaId(DEFAULT_MULTIMEDIA_ID)
            .messageId(DEFAULT_MESSAGE_ID)
            .postId(DEFAULT_POST_ID)
            .commentId(DEFAULT_COMMENT_ID)
            .likerId(DEFAULT_LIKER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeMark createUpdatedEntity() {
        return new LikeMark()
            .entityType(UPDATED_ENTITY_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID)
            .likerId(UPDATED_LIKER_ID);
    }

    @BeforeEach
    void initTest() {
        likeMark = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLikeMark != null) {
            likeMarkRepository.delete(insertedLikeMark);
            insertedLikeMark = null;
        }
    }

    @Test
    @Transactional
    void createLikeMark() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);
        var returnedLikeMarkDTO = om.readValue(
            restLikeMarkMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeMarkDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LikeMarkDTO.class
        );

        // Validate the LikeMark in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLikeMark = likeMarkMapper.toEntity(returnedLikeMarkDTO);
        assertLikeMarkUpdatableFieldsEquals(returnedLikeMark, getPersistedLikeMark(returnedLikeMark));

        insertedLikeMark = returnedLikeMark;
    }

    @Test
    @Transactional
    void createLikeMarkWithExistingId() throws Exception {
        // Create the LikeMark with an existing ID
        likeMark.setId(1L);
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikeMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeMarkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LikeMark in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        likeMark.setEntityType(null);

        // Create the LikeMark, which fails.
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        restLikeMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeMarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        likeMark.setCreatedDate(null);

        // Create the LikeMark, which fails.
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        restLikeMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeMarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        likeMark.setIsDeleted(null);

        // Create the LikeMark, which fails.
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        restLikeMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeMarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLikerIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        likeMark.setLikerId(null);

        // Create the LikeMark, which fails.
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        restLikeMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeMarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLikeMarks() throws Exception {
        // Initialize the database
        insertedLikeMark = likeMarkRepository.saveAndFlush(likeMark);

        // Get all the likeMarkList
        restLikeMarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likeMark.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)))
            .andExpect(jsonPath("$.[*].multimediaId").value(hasItem(DEFAULT_MULTIMEDIA_ID.intValue())))
            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())))
            .andExpect(jsonPath("$.[*].commentId").value(hasItem(DEFAULT_COMMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].likerId").value(hasItem(DEFAULT_LIKER_ID.intValue())));
    }

    @Test
    @Transactional
    void getLikeMark() throws Exception {
        // Initialize the database
        insertedLikeMark = likeMarkRepository.saveAndFlush(likeMark);

        // Get the likeMark
        restLikeMarkMockMvc
            .perform(get(ENTITY_API_URL_ID, likeMark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likeMark.getId().intValue()))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED))
            .andExpect(jsonPath("$.multimediaId").value(DEFAULT_MULTIMEDIA_ID.intValue()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()))
            .andExpect(jsonPath("$.commentId").value(DEFAULT_COMMENT_ID.intValue()))
            .andExpect(jsonPath("$.likerId").value(DEFAULT_LIKER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingLikeMark() throws Exception {
        // Get the likeMark
        restLikeMarkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLikeMark() throws Exception {
        // Initialize the database
        insertedLikeMark = likeMarkRepository.saveAndFlush(likeMark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the likeMark
        LikeMark updatedLikeMark = likeMarkRepository.findById(likeMark.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLikeMark are not directly saved in db
        em.detach(updatedLikeMark);
        updatedLikeMark
            .entityType(UPDATED_ENTITY_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID)
            .likerId(UPDATED_LIKER_ID);
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(updatedLikeMark);

        restLikeMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeMarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(likeMarkDTO))
            )
            .andExpect(status().isOk());

        // Validate the LikeMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLikeMarkToMatchAllProperties(updatedLikeMark);
    }

    @Test
    @Transactional
    void putNonExistingLikeMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeMarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(likeMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikeMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(likeMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikeMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeMarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikeMarkWithPatch() throws Exception {
        // Initialize the database
        insertedLikeMark = likeMarkRepository.saveAndFlush(likeMark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the likeMark using partial update
        LikeMark partialUpdatedLikeMark = new LikeMark();
        partialUpdatedLikeMark.setId(likeMark.getId());

        partialUpdatedLikeMark
            .entityType(UPDATED_ENTITY_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .commentId(UPDATED_COMMENT_ID);

        restLikeMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLikeMark))
            )
            .andExpect(status().isOk());

        // Validate the LikeMark in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLikeMarkUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLikeMark, likeMark), getPersistedLikeMark(likeMark));
    }

    @Test
    @Transactional
    void fullUpdateLikeMarkWithPatch() throws Exception {
        // Initialize the database
        insertedLikeMark = likeMarkRepository.saveAndFlush(likeMark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the likeMark using partial update
        LikeMark partialUpdatedLikeMark = new LikeMark();
        partialUpdatedLikeMark.setId(likeMark.getId());

        partialUpdatedLikeMark
            .entityType(UPDATED_ENTITY_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID)
            .likerId(UPDATED_LIKER_ID);

        restLikeMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLikeMark))
            )
            .andExpect(status().isOk());

        // Validate the LikeMark in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLikeMarkUpdatableFieldsEquals(partialUpdatedLikeMark, getPersistedLikeMark(partialUpdatedLikeMark));
    }

    @Test
    @Transactional
    void patchNonExistingLikeMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likeMarkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(likeMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikeMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(likeMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikeMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(likeMarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikeMark() throws Exception {
        // Initialize the database
        insertedLikeMark = likeMarkRepository.saveAndFlush(likeMark);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the likeMark
        restLikeMarkMockMvc
            .perform(delete(ENTITY_API_URL_ID, likeMark.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return likeMarkRepository.count();
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

    protected LikeMark getPersistedLikeMark(LikeMark likeMark) {
        return likeMarkRepository.findById(likeMark.getId()).orElseThrow();
    }

    protected void assertPersistedLikeMarkToMatchAllProperties(LikeMark expectedLikeMark) {
        assertLikeMarkAllPropertiesEquals(expectedLikeMark, getPersistedLikeMark(expectedLikeMark));
    }

    protected void assertPersistedLikeMarkToMatchUpdatableProperties(LikeMark expectedLikeMark) {
        assertLikeMarkAllUpdatablePropertiesEquals(expectedLikeMark, getPersistedLikeMark(expectedLikeMark));
    }
}
