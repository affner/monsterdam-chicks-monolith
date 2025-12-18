package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.HashTagAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.HashTag;
import com.monsterdam.app.domain.enumeration.HashtagType;
import com.monsterdam.app.repository.HashTagRepository;
import com.monsterdam.app.service.dto.HashTagDTO;
import com.monsterdam.app.service.mapper.HashTagMapper;
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
 * Integration tests for the {@link HashTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HashTagResourceIT {

    private static final String DEFAULT_TAG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TAG_NAME = "BBBBBBBBBB";

    private static final HashtagType DEFAULT_HASHTAG_TYPE = HashtagType.USER;
    private static final HashtagType UPDATED_HASHTAG_TYPE = HashtagType.POST;

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

    private static final String ENTITY_API_URL = "/api/hash-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private HashTagMapper hashTagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHashTagMockMvc;

    private HashTag hashTag;

    private HashTag insertedHashTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HashTag createEntity() {
        return new HashTag()
            .tagName(DEFAULT_TAG_NAME)
            .hashtagType(DEFAULT_HASHTAG_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .deletedDate(DEFAULT_DELETED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HashTag createUpdatedEntity() {
        return new HashTag()
            .tagName(UPDATED_TAG_NAME)
            .hashtagType(UPDATED_HASHTAG_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        hashTag = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHashTag != null) {
            hashTagRepository.delete(insertedHashTag);
            insertedHashTag = null;
        }
    }

    @Test
    @Transactional
    void createHashTag() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);
        var returnedHashTagDTO = om.readValue(
            restHashTagMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashTagDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HashTagDTO.class
        );

        // Validate the HashTag in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHashTag = hashTagMapper.toEntity(returnedHashTagDTO);
        assertHashTagUpdatableFieldsEquals(returnedHashTag, getPersistedHashTag(returnedHashTag));

        insertedHashTag = returnedHashTag;
    }

    @Test
    @Transactional
    void createHashTagWithExistingId() throws Exception {
        // Create the HashTag with an existing ID
        hashTag.setId(1L);
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHashTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashTagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HashTag in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTagNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hashTag.setTagName(null);

        // Create the HashTag, which fails.
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        restHashTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHashtagTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hashTag.setHashtagType(null);

        // Create the HashTag, which fails.
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        restHashTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hashTag.setCreatedDate(null);

        // Create the HashTag, which fails.
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        restHashTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHashTags() throws Exception {
        // Initialize the database
        insertedHashTag = hashTagRepository.saveAndFlush(hashTag);

        // Get all the hashTagList
        restHashTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hashTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME)))
            .andExpect(jsonPath("$.[*].hashtagType").value(hasItem(DEFAULT_HASHTAG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getHashTag() throws Exception {
        // Initialize the database
        insertedHashTag = hashTagRepository.saveAndFlush(hashTag);

        // Get the hashTag
        restHashTagMockMvc
            .perform(get(ENTITY_API_URL_ID, hashTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hashTag.getId().intValue()))
            .andExpect(jsonPath("$.tagName").value(DEFAULT_TAG_NAME))
            .andExpect(jsonPath("$.hashtagType").value(DEFAULT_HASHTAG_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHashTag() throws Exception {
        // Get the hashTag
        restHashTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHashTag() throws Exception {
        // Initialize the database
        insertedHashTag = hashTagRepository.saveAndFlush(hashTag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hashTag
        HashTag updatedHashTag = hashTagRepository.findById(hashTag.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHashTag are not directly saved in db
        em.detach(updatedHashTag);
        updatedHashTag
            .tagName(UPDATED_TAG_NAME)
            .hashtagType(UPDATED_HASHTAG_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        HashTagDTO hashTagDTO = hashTagMapper.toDto(updatedHashTag);

        restHashTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hashTagDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashTagDTO))
            )
            .andExpect(status().isOk());

        // Validate the HashTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHashTagToMatchAllProperties(updatedHashTag);
    }

    @Test
    @Transactional
    void putNonExistingHashTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hashTagDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHashTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hashTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHashTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hashTagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HashTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHashTagWithPatch() throws Exception {
        // Initialize the database
        insertedHashTag = hashTagRepository.saveAndFlush(hashTag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hashTag using partial update
        HashTag partialUpdatedHashTag = new HashTag();
        partialUpdatedHashTag.setId(hashTag.getId());

        partialUpdatedHashTag.lastModifiedDate(UPDATED_LAST_MODIFIED_DATE).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restHashTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHashTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHashTag))
            )
            .andExpect(status().isOk());

        // Validate the HashTag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHashTagUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHashTag, hashTag), getPersistedHashTag(hashTag));
    }

    @Test
    @Transactional
    void fullUpdateHashTagWithPatch() throws Exception {
        // Initialize the database
        insertedHashTag = hashTagRepository.saveAndFlush(hashTag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hashTag using partial update
        HashTag partialUpdatedHashTag = new HashTag();
        partialUpdatedHashTag.setId(hashTag.getId());

        partialUpdatedHashTag
            .tagName(UPDATED_TAG_NAME)
            .hashtagType(UPDATED_HASHTAG_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restHashTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHashTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHashTag))
            )
            .andExpect(status().isOk());

        // Validate the HashTag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHashTagUpdatableFieldsEquals(partialUpdatedHashTag, getPersistedHashTag(partialUpdatedHashTag));
    }

    @Test
    @Transactional
    void patchNonExistingHashTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hashTagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hashTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHashTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hashTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HashTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHashTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHashTagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hashTagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HashTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHashTag() throws Exception {
        // Initialize the database
        insertedHashTag = hashTagRepository.saveAndFlush(hashTag);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hashTag
        restHashTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, hashTag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hashTagRepository.count();
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

    protected HashTag getPersistedHashTag(HashTag hashTag) {
        return hashTagRepository.findById(hashTag.getId()).orElseThrow();
    }

    protected void assertPersistedHashTagToMatchAllProperties(HashTag expectedHashTag) {
        assertHashTagAllPropertiesEquals(expectedHashTag, getPersistedHashTag(expectedHashTag));
    }

    protected void assertPersistedHashTagToMatchUpdatableProperties(HashTag expectedHashTag) {
        assertHashTagAllUpdatablePropertiesEquals(expectedHashTag, getPersistedHashTag(expectedHashTag));
    }
}
