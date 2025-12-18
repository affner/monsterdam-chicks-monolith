package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.TaxInfoAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.TaxInfo;
import com.monsterdam.app.domain.enumeration.TaxType;
import com.monsterdam.app.repository.TaxInfoRepository;
import com.monsterdam.app.service.dto.TaxInfoDTO;
import com.monsterdam.app.service.mapper.TaxInfoMapper;
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
 * Integration tests for the {@link TaxInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaxInfoResourceIT {

    private static final Float DEFAULT_RATE_PERCENTAGE = 1F;
    private static final Float UPDATED_RATE_PERCENTAGE = 2F;

    private static final TaxType DEFAULT_TAX_TYPE = TaxType.VAT;
    private static final TaxType UPDATED_TAX_TYPE = TaxType.WITHHOLDING;

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

    private static final String ENTITY_API_URL = "/api/tax-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaxInfoRepository taxInfoRepository;

    @Autowired
    private TaxInfoMapper taxInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxInfoMockMvc;

    private TaxInfo taxInfo;

    private TaxInfo insertedTaxInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxInfo createEntity() {
        return new TaxInfo()
            .ratePercentage(DEFAULT_RATE_PERCENTAGE)
            .taxType(DEFAULT_TAX_TYPE)
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
    public static TaxInfo createUpdatedEntity() {
        return new TaxInfo()
            .ratePercentage(UPDATED_RATE_PERCENTAGE)
            .taxType(UPDATED_TAX_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        taxInfo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTaxInfo != null) {
            taxInfoRepository.delete(insertedTaxInfo);
            insertedTaxInfo = null;
        }
    }

    @Test
    @Transactional
    void createTaxInfo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);
        var returnedTaxInfoDTO = om.readValue(
            restTaxInfoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxInfoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TaxInfoDTO.class
        );

        // Validate the TaxInfo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTaxInfo = taxInfoMapper.toEntity(returnedTaxInfoDTO);
        assertTaxInfoUpdatableFieldsEquals(returnedTaxInfo, getPersistedTaxInfo(returnedTaxInfo));

        insertedTaxInfo = returnedTaxInfo;
    }

    @Test
    @Transactional
    void createTaxInfoWithExistingId() throws Exception {
        // Create the TaxInfo with an existing ID
        taxInfo.setId(1L);
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTaxTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        taxInfo.setTaxType(null);

        // Create the TaxInfo, which fails.
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        restTaxInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        taxInfo.setCreatedDate(null);

        // Create the TaxInfo, which fails.
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        restTaxInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaxInfos() throws Exception {
        // Initialize the database
        insertedTaxInfo = taxInfoRepository.saveAndFlush(taxInfo);

        // Get all the taxInfoList
        restTaxInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].ratePercentage").value(hasItem(DEFAULT_RATE_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxType").value(hasItem(DEFAULT_TAX_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTaxInfo() throws Exception {
        // Initialize the database
        insertedTaxInfo = taxInfoRepository.saveAndFlush(taxInfo);

        // Get the taxInfo
        restTaxInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, taxInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxInfo.getId().intValue()))
            .andExpect(jsonPath("$.ratePercentage").value(DEFAULT_RATE_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.taxType").value(DEFAULT_TAX_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTaxInfo() throws Exception {
        // Get the taxInfo
        restTaxInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaxInfo() throws Exception {
        // Initialize the database
        insertedTaxInfo = taxInfoRepository.saveAndFlush(taxInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxInfo
        TaxInfo updatedTaxInfo = taxInfoRepository.findById(taxInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTaxInfo are not directly saved in db
        em.detach(updatedTaxInfo);
        updatedTaxInfo
            .ratePercentage(UPDATED_RATE_PERCENTAGE)
            .taxType(UPDATED_TAX_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(updatedTaxInfo);

        restTaxInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxInfoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaxInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaxInfoToMatchAllProperties(updatedTaxInfo);
    }

    @Test
    @Transactional
    void putNonExistingTaxInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxInfoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taxInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taxInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaxInfoWithPatch() throws Exception {
        // Initialize the database
        insertedTaxInfo = taxInfoRepository.saveAndFlush(taxInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxInfo using partial update
        TaxInfo partialUpdatedTaxInfo = new TaxInfo();
        partialUpdatedTaxInfo.setId(taxInfo.getId());

        partialUpdatedTaxInfo
            .taxType(UPDATED_TAX_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restTaxInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxInfo))
            )
            .andExpect(status().isOk());

        // Validate the TaxInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxInfoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTaxInfo, taxInfo), getPersistedTaxInfo(taxInfo));
    }

    @Test
    @Transactional
    void fullUpdateTaxInfoWithPatch() throws Exception {
        // Initialize the database
        insertedTaxInfo = taxInfoRepository.saveAndFlush(taxInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taxInfo using partial update
        TaxInfo partialUpdatedTaxInfo = new TaxInfo();
        partialUpdatedTaxInfo.setId(taxInfo.getId());

        partialUpdatedTaxInfo
            .ratePercentage(UPDATED_RATE_PERCENTAGE)
            .taxType(UPDATED_TAX_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restTaxInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaxInfo))
            )
            .andExpect(status().isOk());

        // Validate the TaxInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaxInfoUpdatableFieldsEquals(partialUpdatedTaxInfo, getPersistedTaxInfo(partialUpdatedTaxInfo));
    }

    @Test
    @Transactional
    void patchNonExistingTaxInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taxInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taxInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taxInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaxInfo() throws Exception {
        // Initialize the database
        insertedTaxInfo = taxInfoRepository.saveAndFlush(taxInfo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the taxInfo
        restTaxInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taxInfoRepository.count();
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

    protected TaxInfo getPersistedTaxInfo(TaxInfo taxInfo) {
        return taxInfoRepository.findById(taxInfo.getId()).orElseThrow();
    }

    protected void assertPersistedTaxInfoToMatchAllProperties(TaxInfo expectedTaxInfo) {
        assertTaxInfoAllPropertiesEquals(expectedTaxInfo, getPersistedTaxInfo(expectedTaxInfo));
    }

    protected void assertPersistedTaxInfoToMatchUpdatableProperties(TaxInfo expectedTaxInfo) {
        assertTaxInfoAllUpdatablePropertiesEquals(expectedTaxInfo, getPersistedTaxInfo(expectedTaxInfo));
    }
}
