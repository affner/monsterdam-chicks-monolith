package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.TrialLinkAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.TrialLink;
import com.monsterdam.app.repository.TrialLinkRepository;
import com.monsterdam.app.service.dto.TrialLinkDTO;
import com.monsterdam.app.service.mapper.TrialLinkMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link TrialLinkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrialLinkResourceIT {

    private static final String DEFAULT_LINK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LINK_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_FREE_DAYS = 1;
    private static final Integer UPDATED_FREE_DAYS = 2;

    private static final Boolean DEFAULT_IS_USED = false;
    private static final Boolean UPDATED_IS_USED = true;

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

    private static final String ENTITY_API_URL = "/api/trial-links";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrialLinkRepository trialLinkRepository;

    @Autowired
    private TrialLinkMapper trialLinkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrialLinkMockMvc;

    private TrialLink trialLink;

    private TrialLink insertedTrialLink;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrialLink createEntity() {
        return new TrialLink()
            .linkCode(DEFAULT_LINK_CODE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .freeDays(DEFAULT_FREE_DAYS)
            .isUsed(DEFAULT_IS_USED)
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
    public static TrialLink createUpdatedEntity() {
        return new TrialLink()
            .linkCode(UPDATED_LINK_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .freeDays(UPDATED_FREE_DAYS)
            .isUsed(UPDATED_IS_USED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        trialLink = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrialLink != null) {
            trialLinkRepository.delete(insertedTrialLink);
            insertedTrialLink = null;
        }
    }

    @Test
    @Transactional
    void createTrialLink() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrialLink
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);
        var returnedTrialLinkDTO = om.readValue(
            restTrialLinkMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trialLinkDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrialLinkDTO.class
        );

        // Validate the TrialLink in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrialLink = trialLinkMapper.toEntity(returnedTrialLinkDTO);
        assertTrialLinkUpdatableFieldsEquals(returnedTrialLink, getPersistedTrialLink(returnedTrialLink));

        insertedTrialLink = returnedTrialLink;
    }

    @Test
    @Transactional
    void createTrialLinkWithExistingId() throws Exception {
        // Create the TrialLink with an existing ID
        trialLink.setId(1L);
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrialLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trialLinkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLinkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trialLink.setLinkCode(null);

        // Create the TrialLink, which fails.
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        restTrialLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trialLinkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trialLink.setStartDate(null);

        // Create the TrialLink, which fails.
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        restTrialLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trialLinkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trialLink.setEndDate(null);

        // Create the TrialLink, which fails.
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        restTrialLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trialLinkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsUsedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trialLink.setIsUsed(null);

        // Create the TrialLink, which fails.
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        restTrialLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trialLinkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trialLink.setCreatedDate(null);

        // Create the TrialLink, which fails.
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        restTrialLinkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trialLinkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrialLinks() throws Exception {
        // Initialize the database
        insertedTrialLink = trialLinkRepository.saveAndFlush(trialLink);

        // Get all the trialLinkList
        restTrialLinkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trialLink.getId().intValue())))
            .andExpect(jsonPath("$.[*].linkCode").value(hasItem(DEFAULT_LINK_CODE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].freeDays").value(hasItem(DEFAULT_FREE_DAYS)))
            .andExpect(jsonPath("$.[*].isUsed").value(hasItem(DEFAULT_IS_USED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTrialLink() throws Exception {
        // Initialize the database
        insertedTrialLink = trialLinkRepository.saveAndFlush(trialLink);

        // Get the trialLink
        restTrialLinkMockMvc
            .perform(get(ENTITY_API_URL_ID, trialLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trialLink.getId().intValue()))
            .andExpect(jsonPath("$.linkCode").value(DEFAULT_LINK_CODE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.freeDays").value(DEFAULT_FREE_DAYS))
            .andExpect(jsonPath("$.isUsed").value(DEFAULT_IS_USED))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTrialLink() throws Exception {
        // Get the trialLink
        restTrialLinkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrialLink() throws Exception {
        // Initialize the database
        insertedTrialLink = trialLinkRepository.saveAndFlush(trialLink);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trialLink
        TrialLink updatedTrialLink = trialLinkRepository.findById(trialLink.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrialLink are not directly saved in db
        em.detach(updatedTrialLink);
        updatedTrialLink
            .linkCode(UPDATED_LINK_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .freeDays(UPDATED_FREE_DAYS)
            .isUsed(UPDATED_IS_USED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(updatedTrialLink);

        restTrialLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trialLinkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trialLinkDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrialLinkToMatchAllProperties(updatedTrialLink);
    }

    @Test
    @Transactional
    void putNonExistingTrialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trialLink.setId(longCount.incrementAndGet());

        // Create the TrialLink
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrialLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trialLinkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trialLinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trialLink.setId(longCount.incrementAndGet());

        // Create the TrialLink
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrialLinkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trialLinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trialLink.setId(longCount.incrementAndGet());

        // Create the TrialLink
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrialLinkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trialLinkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrialLinkWithPatch() throws Exception {
        // Initialize the database
        insertedTrialLink = trialLinkRepository.saveAndFlush(trialLink);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trialLink using partial update
        TrialLink partialUpdatedTrialLink = new TrialLink();
        partialUpdatedTrialLink.setId(trialLink.getId());

        partialUpdatedTrialLink
            .startDate(UPDATED_START_DATE)
            .freeDays(UPDATED_FREE_DAYS)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restTrialLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrialLink.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrialLink))
            )
            .andExpect(status().isOk());

        // Validate the TrialLink in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrialLinkUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrialLink, trialLink),
            getPersistedTrialLink(trialLink)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrialLinkWithPatch() throws Exception {
        // Initialize the database
        insertedTrialLink = trialLinkRepository.saveAndFlush(trialLink);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trialLink using partial update
        TrialLink partialUpdatedTrialLink = new TrialLink();
        partialUpdatedTrialLink.setId(trialLink.getId());

        partialUpdatedTrialLink
            .linkCode(UPDATED_LINK_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .freeDays(UPDATED_FREE_DAYS)
            .isUsed(UPDATED_IS_USED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restTrialLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrialLink.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrialLink))
            )
            .andExpect(status().isOk());

        // Validate the TrialLink in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrialLinkUpdatableFieldsEquals(partialUpdatedTrialLink, getPersistedTrialLink(partialUpdatedTrialLink));
    }

    @Test
    @Transactional
    void patchNonExistingTrialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trialLink.setId(longCount.incrementAndGet());

        // Create the TrialLink
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrialLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trialLinkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trialLinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trialLink.setId(longCount.incrementAndGet());

        // Create the TrialLink
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrialLinkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trialLinkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrialLink() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trialLink.setId(longCount.incrementAndGet());

        // Create the TrialLink
        TrialLinkDTO trialLinkDTO = trialLinkMapper.toDto(trialLink);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrialLinkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trialLinkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrialLink in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrialLink() throws Exception {
        // Initialize the database
        insertedTrialLink = trialLinkRepository.saveAndFlush(trialLink);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trialLink
        restTrialLinkMockMvc
            .perform(delete(ENTITY_API_URL_ID, trialLink.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trialLinkRepository.count();
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

    protected TrialLink getPersistedTrialLink(TrialLink trialLink) {
        return trialLinkRepository.findById(trialLink.getId()).orElseThrow();
    }

    protected void assertPersistedTrialLinkToMatchAllProperties(TrialLink expectedTrialLink) {
        assertTrialLinkAllPropertiesEquals(expectedTrialLink, getPersistedTrialLink(expectedTrialLink));
    }

    protected void assertPersistedTrialLinkToMatchUpdatableProperties(TrialLink expectedTrialLink) {
        assertTrialLinkAllUpdatablePropertiesEquals(expectedTrialLink, getPersistedTrialLink(expectedTrialLink));
    }
}
