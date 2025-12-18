package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.SocialNetworkAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.SocialNetwork;
import com.monsterdam.app.repository.SocialNetworkRepository;
import com.monsterdam.app.service.dto.SocialNetworkDTO;
import com.monsterdam.app.service.mapper.SocialNetworkMapper;
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
 * Integration tests for the {@link SocialNetworkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SocialNetworkResourceIT {

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLETE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPLETE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_LINK = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_LINK = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/social-networks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SocialNetworkRepository socialNetworkRepository;

    @Autowired
    private SocialNetworkMapper socialNetworkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocialNetworkMockMvc;

    private SocialNetwork socialNetwork;

    private SocialNetwork insertedSocialNetwork;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialNetwork createEntity() {
        return new SocialNetwork()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .name(DEFAULT_NAME)
            .completeName(DEFAULT_COMPLETE_NAME)
            .mainLink(DEFAULT_MAIN_LINK)
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
    public static SocialNetwork createUpdatedEntity() {
        return new SocialNetwork()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .completeName(UPDATED_COMPLETE_NAME)
            .mainLink(UPDATED_MAIN_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        socialNetwork = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSocialNetwork != null) {
            socialNetworkRepository.delete(insertedSocialNetwork);
            insertedSocialNetwork = null;
        }
    }

    @Test
    @Transactional
    void createSocialNetwork() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);
        var returnedSocialNetworkDTO = om.readValue(
            restSocialNetworkMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialNetworkDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SocialNetworkDTO.class
        );

        // Validate the SocialNetwork in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSocialNetwork = socialNetworkMapper.toEntity(returnedSocialNetworkDTO);
        assertSocialNetworkUpdatableFieldsEquals(returnedSocialNetwork, getPersistedSocialNetwork(returnedSocialNetwork));

        insertedSocialNetwork = returnedSocialNetwork;
    }

    @Test
    @Transactional
    void createSocialNetworkWithExistingId() throws Exception {
        // Create the SocialNetwork with an existing ID
        socialNetwork.setId(1L);
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialNetworkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialNetworkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SocialNetwork in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        socialNetwork.setName(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        restSocialNetworkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialNetworkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompleteNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        socialNetwork.setCompleteName(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        restSocialNetworkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialNetworkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMainLinkIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        socialNetwork.setMainLink(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        restSocialNetworkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialNetworkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        socialNetwork.setCreatedDate(null);

        // Create the SocialNetwork, which fails.
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        restSocialNetworkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialNetworkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSocialNetworks() throws Exception {
        // Initialize the database
        insertedSocialNetwork = socialNetworkRepository.saveAndFlush(socialNetwork);

        // Get all the socialNetworkList
        restSocialNetworkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialNetwork.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].completeName").value(hasItem(DEFAULT_COMPLETE_NAME)))
            .andExpect(jsonPath("$.[*].mainLink").value(hasItem(DEFAULT_MAIN_LINK)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSocialNetwork() throws Exception {
        // Initialize the database
        insertedSocialNetwork = socialNetworkRepository.saveAndFlush(socialNetwork);

        // Get the socialNetwork
        restSocialNetworkMockMvc
            .perform(get(ENTITY_API_URL_ID, socialNetwork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socialNetwork.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.completeName").value(DEFAULT_COMPLETE_NAME))
            .andExpect(jsonPath("$.mainLink").value(DEFAULT_MAIN_LINK))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSocialNetwork() throws Exception {
        // Get the socialNetwork
        restSocialNetworkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSocialNetwork() throws Exception {
        // Initialize the database
        insertedSocialNetwork = socialNetworkRepository.saveAndFlush(socialNetwork);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialNetwork
        SocialNetwork updatedSocialNetwork = socialNetworkRepository.findById(socialNetwork.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSocialNetwork are not directly saved in db
        em.detach(updatedSocialNetwork);
        updatedSocialNetwork
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .completeName(UPDATED_COMPLETE_NAME)
            .mainLink(UPDATED_MAIN_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(updatedSocialNetwork);

        restSocialNetworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialNetworkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(socialNetworkDTO))
            )
            .andExpect(status().isOk());

        // Validate the SocialNetwork in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSocialNetworkToMatchAllProperties(updatedSocialNetwork);
    }

    @Test
    @Transactional
    void putNonExistingSocialNetwork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialNetworkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetwork in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSocialNetwork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetwork in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSocialNetwork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialNetworkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialNetwork in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocialNetworkWithPatch() throws Exception {
        // Initialize the database
        insertedSocialNetwork = socialNetworkRepository.saveAndFlush(socialNetwork);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialNetwork using partial update
        SocialNetwork partialUpdatedSocialNetwork = new SocialNetwork();
        partialUpdatedSocialNetwork.setId(socialNetwork.getId());

        partialUpdatedSocialNetwork
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restSocialNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialNetwork.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSocialNetwork))
            )
            .andExpect(status().isOk());

        // Validate the SocialNetwork in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSocialNetworkUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSocialNetwork, socialNetwork),
            getPersistedSocialNetwork(socialNetwork)
        );
    }

    @Test
    @Transactional
    void fullUpdateSocialNetworkWithPatch() throws Exception {
        // Initialize the database
        insertedSocialNetwork = socialNetworkRepository.saveAndFlush(socialNetwork);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialNetwork using partial update
        SocialNetwork partialUpdatedSocialNetwork = new SocialNetwork();
        partialUpdatedSocialNetwork.setId(socialNetwork.getId());

        partialUpdatedSocialNetwork
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .completeName(UPDATED_COMPLETE_NAME)
            .mainLink(UPDATED_MAIN_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restSocialNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialNetwork.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSocialNetwork))
            )
            .andExpect(status().isOk());

        // Validate the SocialNetwork in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSocialNetworkUpdatableFieldsEquals(partialUpdatedSocialNetwork, getPersistedSocialNetwork(partialUpdatedSocialNetwork));
    }

    @Test
    @Transactional
    void patchNonExistingSocialNetwork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, socialNetworkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetwork in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSocialNetwork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(socialNetworkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialNetwork in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSocialNetwork() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialNetwork.setId(longCount.incrementAndGet());

        // Create the SocialNetwork
        SocialNetworkDTO socialNetworkDTO = socialNetworkMapper.toDto(socialNetwork);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialNetworkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(socialNetworkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialNetwork in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSocialNetwork() throws Exception {
        // Initialize the database
        insertedSocialNetwork = socialNetworkRepository.saveAndFlush(socialNetwork);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the socialNetwork
        restSocialNetworkMockMvc
            .perform(delete(ENTITY_API_URL_ID, socialNetwork.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return socialNetworkRepository.count();
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

    protected SocialNetwork getPersistedSocialNetwork(SocialNetwork socialNetwork) {
        return socialNetworkRepository.findById(socialNetwork.getId()).orElseThrow();
    }

    protected void assertPersistedSocialNetworkToMatchAllProperties(SocialNetwork expectedSocialNetwork) {
        assertSocialNetworkAllPropertiesEquals(expectedSocialNetwork, getPersistedSocialNetwork(expectedSocialNetwork));
    }

    protected void assertPersistedSocialNetworkToMatchUpdatableProperties(SocialNetwork expectedSocialNetwork) {
        assertSocialNetworkAllUpdatablePropertiesEquals(expectedSocialNetwork, getPersistedSocialNetwork(expectedSocialNetwork));
    }
}
