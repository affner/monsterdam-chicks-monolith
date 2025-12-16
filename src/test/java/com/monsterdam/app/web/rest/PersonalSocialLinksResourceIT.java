package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.PersonalSocialLinksAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PersonalSocialLinks;
import com.monsterdam.app.repository.PersonalSocialLinksRepository;
import com.monsterdam.app.service.dto.PersonalSocialLinksDTO;
import com.monsterdam.app.service.mapper.PersonalSocialLinksMapper;
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
 * Integration tests for the {@link PersonalSocialLinksResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalSocialLinksResourceIT {

    private static final String DEFAULT_NORMAL_IMAGE_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_NORMAL_IMAGE_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL_ICON_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_ICON_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_SOCIAL_LINK = "AAAAAAAAAA";
    private static final String UPDATED_SOCIAL_LINK = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/personal-social-links";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PersonalSocialLinksRepository personalSocialLinksRepository;

    @Autowired
    private PersonalSocialLinksMapper personalSocialLinksMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalSocialLinksMockMvc;

    private PersonalSocialLinks personalSocialLinks;

    private PersonalSocialLinks insertedPersonalSocialLinks;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalSocialLinks createEntity() {
        return new PersonalSocialLinks()
            .normalImageS3Key(DEFAULT_NORMAL_IMAGE_S_3_KEY)
            .thumbnailIconS3Key(DEFAULT_THUMBNAIL_ICON_S_3_KEY)
            .socialLink(DEFAULT_SOCIAL_LINK)
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
    public static PersonalSocialLinks createUpdatedEntity() {
        return new PersonalSocialLinks()
            .normalImageS3Key(UPDATED_NORMAL_IMAGE_S_3_KEY)
            .thumbnailIconS3Key(UPDATED_THUMBNAIL_ICON_S_3_KEY)
            .socialLink(UPDATED_SOCIAL_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        personalSocialLinks = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPersonalSocialLinks != null) {
            personalSocialLinksRepository.delete(insertedPersonalSocialLinks);
            insertedPersonalSocialLinks = null;
        }
    }

    @Test
    @Transactional
    void createPersonalSocialLinks() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);
        var returnedPersonalSocialLinksDTO = om.readValue(
            restPersonalSocialLinksMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalSocialLinksDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PersonalSocialLinksDTO.class
        );

        // Validate the PersonalSocialLinks in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPersonalSocialLinks = personalSocialLinksMapper.toEntity(returnedPersonalSocialLinksDTO);
        assertPersonalSocialLinksUpdatableFieldsEquals(
            returnedPersonalSocialLinks,
            getPersistedPersonalSocialLinks(returnedPersonalSocialLinks)
        );

        insertedPersonalSocialLinks = returnedPersonalSocialLinks;
    }

    @Test
    @Transactional
    void createPersonalSocialLinksWithExistingId() throws Exception {
        // Create the PersonalSocialLinks with an existing ID
        personalSocialLinks.setId(1L);
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalSocialLinksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalSocialLinksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PersonalSocialLinks in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSocialLinkIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        personalSocialLinks.setSocialLink(null);

        // Create the PersonalSocialLinks, which fails.
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        restPersonalSocialLinksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalSocialLinksDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        personalSocialLinks.setCreatedDate(null);

        // Create the PersonalSocialLinks, which fails.
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        restPersonalSocialLinksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalSocialLinksDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonalSocialLinks() throws Exception {
        // Initialize the database
        insertedPersonalSocialLinks = personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        // Get all the personalSocialLinksList
        restPersonalSocialLinksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalSocialLinks.getId().intValue())))
            .andExpect(jsonPath("$.[*].normalImageS3Key").value(hasItem(DEFAULT_NORMAL_IMAGE_S_3_KEY)))
            .andExpect(jsonPath("$.[*].thumbnailIconS3Key").value(hasItem(DEFAULT_THUMBNAIL_ICON_S_3_KEY)))
            .andExpect(jsonPath("$.[*].socialLink").value(hasItem(DEFAULT_SOCIAL_LINK)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPersonalSocialLinks() throws Exception {
        // Initialize the database
        insertedPersonalSocialLinks = personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        // Get the personalSocialLinks
        restPersonalSocialLinksMockMvc
            .perform(get(ENTITY_API_URL_ID, personalSocialLinks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalSocialLinks.getId().intValue()))
            .andExpect(jsonPath("$.normalImageS3Key").value(DEFAULT_NORMAL_IMAGE_S_3_KEY))
            .andExpect(jsonPath("$.thumbnailIconS3Key").value(DEFAULT_THUMBNAIL_ICON_S_3_KEY))
            .andExpect(jsonPath("$.socialLink").value(DEFAULT_SOCIAL_LINK))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPersonalSocialLinks() throws Exception {
        // Get the personalSocialLinks
        restPersonalSocialLinksMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonalSocialLinks() throws Exception {
        // Initialize the database
        insertedPersonalSocialLinks = personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personalSocialLinks
        PersonalSocialLinks updatedPersonalSocialLinks = personalSocialLinksRepository.findById(personalSocialLinks.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersonalSocialLinks are not directly saved in db
        em.detach(updatedPersonalSocialLinks);
        updatedPersonalSocialLinks
            .normalImageS3Key(UPDATED_NORMAL_IMAGE_S_3_KEY)
            .thumbnailIconS3Key(UPDATED_THUMBNAIL_ICON_S_3_KEY)
            .socialLink(UPDATED_SOCIAL_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(updatedPersonalSocialLinks);

        restPersonalSocialLinksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalSocialLinksDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isOk());

        // Validate the PersonalSocialLinks in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPersonalSocialLinksToMatchAllProperties(updatedPersonalSocialLinks);
    }

    @Test
    @Transactional
    void putNonExistingPersonalSocialLinks() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalSocialLinksDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalSocialLinks in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalSocialLinks() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalSocialLinks in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalSocialLinks() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personalSocialLinksDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalSocialLinks in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalSocialLinksWithPatch() throws Exception {
        // Initialize the database
        insertedPersonalSocialLinks = personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personalSocialLinks using partial update
        PersonalSocialLinks partialUpdatedPersonalSocialLinks = new PersonalSocialLinks();
        partialUpdatedPersonalSocialLinks.setId(personalSocialLinks.getId());

        partialUpdatedPersonalSocialLinks.thumbnailIconS3Key(UPDATED_THUMBNAIL_ICON_S_3_KEY).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restPersonalSocialLinksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalSocialLinks.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonalSocialLinks))
            )
            .andExpect(status().isOk());

        // Validate the PersonalSocialLinks in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonalSocialLinksUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPersonalSocialLinks, personalSocialLinks),
            getPersistedPersonalSocialLinks(personalSocialLinks)
        );
    }

    @Test
    @Transactional
    void fullUpdatePersonalSocialLinksWithPatch() throws Exception {
        // Initialize the database
        insertedPersonalSocialLinks = personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the personalSocialLinks using partial update
        PersonalSocialLinks partialUpdatedPersonalSocialLinks = new PersonalSocialLinks();
        partialUpdatedPersonalSocialLinks.setId(personalSocialLinks.getId());

        partialUpdatedPersonalSocialLinks
            .normalImageS3Key(UPDATED_NORMAL_IMAGE_S_3_KEY)
            .thumbnailIconS3Key(UPDATED_THUMBNAIL_ICON_S_3_KEY)
            .socialLink(UPDATED_SOCIAL_LINK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPersonalSocialLinksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalSocialLinks.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersonalSocialLinks))
            )
            .andExpect(status().isOk());

        // Validate the PersonalSocialLinks in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonalSocialLinksUpdatableFieldsEquals(
            partialUpdatedPersonalSocialLinks,
            getPersistedPersonalSocialLinks(partialUpdatedPersonalSocialLinks)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPersonalSocialLinks() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalSocialLinksDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalSocialLinks in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalSocialLinks() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalSocialLinks in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalSocialLinks() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        personalSocialLinks.setId(longCount.incrementAndGet());

        // Create the PersonalSocialLinks
        PersonalSocialLinksDTO personalSocialLinksDTO = personalSocialLinksMapper.toDto(personalSocialLinks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalSocialLinksMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(personalSocialLinksDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalSocialLinks in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalSocialLinks() throws Exception {
        // Initialize the database
        insertedPersonalSocialLinks = personalSocialLinksRepository.saveAndFlush(personalSocialLinks);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the personalSocialLinks
        restPersonalSocialLinksMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalSocialLinks.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return personalSocialLinksRepository.count();
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

    protected PersonalSocialLinks getPersistedPersonalSocialLinks(PersonalSocialLinks personalSocialLinks) {
        return personalSocialLinksRepository.findById(personalSocialLinks.getId()).orElseThrow();
    }

    protected void assertPersistedPersonalSocialLinksToMatchAllProperties(PersonalSocialLinks expectedPersonalSocialLinks) {
        assertPersonalSocialLinksAllPropertiesEquals(
            expectedPersonalSocialLinks,
            getPersistedPersonalSocialLinks(expectedPersonalSocialLinks)
        );
    }

    protected void assertPersistedPersonalSocialLinksToMatchUpdatableProperties(PersonalSocialLinks expectedPersonalSocialLinks) {
        assertPersonalSocialLinksAllUpdatablePropertiesEquals(
            expectedPersonalSocialLinks,
            getPersistedPersonalSocialLinks(expectedPersonalSocialLinks)
        );
    }
}
