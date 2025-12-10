package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.MoneyEarningAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.monsterdam.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.MoneyEarning;
import com.monsterdam.app.domain.enumeration.MoneyEarningType;
import com.monsterdam.app.repository.MoneyEarningRepository;
import com.monsterdam.app.service.dto.MoneyEarningDTO;
import com.monsterdam.app.service.mapper.MoneyEarningMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link MoneyEarningResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MoneyEarningResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

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

    private static final MoneyEarningType DEFAULT_TRANSACTION_TYPE = MoneyEarningType.TIP_EARNING;
    private static final MoneyEarningType UPDATED_TRANSACTION_TYPE = MoneyEarningType.SUBSCRIPTION_EARNING;

    private static final String ENTITY_API_URL = "/api/money-earnings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MoneyEarningRepository moneyEarningRepository;

    @Autowired
    private MoneyEarningMapper moneyEarningMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoneyEarningMockMvc;

    private MoneyEarning moneyEarning;

    private MoneyEarning insertedMoneyEarning;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyEarning createEntity() {
        return new MoneyEarning()
            .amount(DEFAULT_AMOUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .transactionType(DEFAULT_TRANSACTION_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyEarning createUpdatedEntity() {
        return new MoneyEarning()
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .transactionType(UPDATED_TRANSACTION_TYPE);
    }

    @BeforeEach
    void initTest() {
        moneyEarning = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMoneyEarning != null) {
            moneyEarningRepository.delete(insertedMoneyEarning);
            insertedMoneyEarning = null;
        }
    }

    @Test
    @Transactional
    void createMoneyEarning() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MoneyEarning
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);
        var returnedMoneyEarningDTO = om.readValue(
            restMoneyEarningMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyEarningDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MoneyEarningDTO.class
        );

        // Validate the MoneyEarning in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMoneyEarning = moneyEarningMapper.toEntity(returnedMoneyEarningDTO);
        assertMoneyEarningUpdatableFieldsEquals(returnedMoneyEarning, getPersistedMoneyEarning(returnedMoneyEarning));

        insertedMoneyEarning = returnedMoneyEarning;
    }

    @Test
    @Transactional
    void createMoneyEarningWithExistingId() throws Exception {
        // Create the MoneyEarning with an existing ID
        moneyEarning.setId(1L);
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoneyEarningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyEarningDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MoneyEarning in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyEarning.setAmount(null);

        // Create the MoneyEarning, which fails.
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);

        restMoneyEarningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyEarningDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyEarning.setCreatedDate(null);

        // Create the MoneyEarning, which fails.
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);

        restMoneyEarningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyEarningDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyEarning.setIsDeleted(null);

        // Create the MoneyEarning, which fails.
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);

        restMoneyEarningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyEarningDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyEarning.setTransactionType(null);

        // Create the MoneyEarning, which fails.
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);

        restMoneyEarningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyEarningDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMoneyEarnings() throws Exception {
        // Initialize the database
        insertedMoneyEarning = moneyEarningRepository.saveAndFlush(moneyEarning);

        // Get all the moneyEarningList
        restMoneyEarningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyEarning.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())));
    }

    @Test
    @Transactional
    void getMoneyEarning() throws Exception {
        // Initialize the database
        insertedMoneyEarning = moneyEarningRepository.saveAndFlush(moneyEarning);

        // Get the moneyEarning
        restMoneyEarningMockMvc
            .perform(get(ENTITY_API_URL_ID, moneyEarning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moneyEarning.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMoneyEarning() throws Exception {
        // Get the moneyEarning
        restMoneyEarningMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoneyEarning() throws Exception {
        // Initialize the database
        insertedMoneyEarning = moneyEarningRepository.saveAndFlush(moneyEarning);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyEarning
        MoneyEarning updatedMoneyEarning = moneyEarningRepository.findById(moneyEarning.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMoneyEarning are not directly saved in db
        em.detach(updatedMoneyEarning);
        updatedMoneyEarning
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .transactionType(UPDATED_TRANSACTION_TYPE);
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(updatedMoneyEarning);

        restMoneyEarningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyEarningDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyEarningDTO))
            )
            .andExpect(status().isOk());

        // Validate the MoneyEarning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMoneyEarningToMatchAllProperties(updatedMoneyEarning);
    }

    @Test
    @Transactional
    void putNonExistingMoneyEarning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyEarning.setId(longCount.incrementAndGet());

        // Create the MoneyEarning
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyEarningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyEarningDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyEarningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyEarning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoneyEarning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyEarning.setId(longCount.incrementAndGet());

        // Create the MoneyEarning
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyEarningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyEarningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyEarning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoneyEarning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyEarning.setId(longCount.incrementAndGet());

        // Create the MoneyEarning
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyEarningMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyEarningDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyEarning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMoneyEarningWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyEarning = moneyEarningRepository.saveAndFlush(moneyEarning);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyEarning using partial update
        MoneyEarning partialUpdatedMoneyEarning = new MoneyEarning();
        partialUpdatedMoneyEarning.setId(moneyEarning.getId());

        partialUpdatedMoneyEarning
            .amount(UPDATED_AMOUNT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restMoneyEarningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyEarning.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyEarning))
            )
            .andExpect(status().isOk());

        // Validate the MoneyEarning in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyEarningUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMoneyEarning, moneyEarning),
            getPersistedMoneyEarning(moneyEarning)
        );
    }

    @Test
    @Transactional
    void fullUpdateMoneyEarningWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyEarning = moneyEarningRepository.saveAndFlush(moneyEarning);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyEarning using partial update
        MoneyEarning partialUpdatedMoneyEarning = new MoneyEarning();
        partialUpdatedMoneyEarning.setId(moneyEarning.getId());

        partialUpdatedMoneyEarning
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .transactionType(UPDATED_TRANSACTION_TYPE);

        restMoneyEarningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyEarning.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyEarning))
            )
            .andExpect(status().isOk());

        // Validate the MoneyEarning in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyEarningUpdatableFieldsEquals(partialUpdatedMoneyEarning, getPersistedMoneyEarning(partialUpdatedMoneyEarning));
    }

    @Test
    @Transactional
    void patchNonExistingMoneyEarning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyEarning.setId(longCount.incrementAndGet());

        // Create the MoneyEarning
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyEarningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moneyEarningDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyEarningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyEarning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoneyEarning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyEarning.setId(longCount.incrementAndGet());

        // Create the MoneyEarning
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyEarningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyEarningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyEarning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoneyEarning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyEarning.setId(longCount.incrementAndGet());

        // Create the MoneyEarning
        MoneyEarningDTO moneyEarningDTO = moneyEarningMapper.toDto(moneyEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyEarningMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moneyEarningDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyEarning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMoneyEarning() throws Exception {
        // Initialize the database
        insertedMoneyEarning = moneyEarningRepository.saveAndFlush(moneyEarning);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the moneyEarning
        restMoneyEarningMockMvc
            .perform(delete(ENTITY_API_URL_ID, moneyEarning.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return moneyEarningRepository.count();
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

    protected MoneyEarning getPersistedMoneyEarning(MoneyEarning moneyEarning) {
        return moneyEarningRepository.findById(moneyEarning.getId()).orElseThrow();
    }

    protected void assertPersistedMoneyEarningToMatchAllProperties(MoneyEarning expectedMoneyEarning) {
        assertMoneyEarningAllPropertiesEquals(expectedMoneyEarning, getPersistedMoneyEarning(expectedMoneyEarning));
    }

    protected void assertPersistedMoneyEarningToMatchUpdatableProperties(MoneyEarning expectedMoneyEarning) {
        assertMoneyEarningAllUpdatablePropertiesEquals(expectedMoneyEarning, getPersistedMoneyEarning(expectedMoneyEarning));
    }
}
