package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.MoneyWithdrawAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.monsterdam.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.MoneyWithdraw;
import com.monsterdam.app.domain.enumeration.MoneyWithdrawStatus;
import com.monsterdam.app.repository.MoneyWithdrawRepository;
import com.monsterdam.app.service.dto.MoneyWithdrawDTO;
import com.monsterdam.app.service.mapper.MoneyWithdrawMapper;
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
 * Integration tests for the {@link MoneyWithdrawResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MoneyWithdrawResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final String DEFAULT_PAYOUT_PROVIDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PAYOUT_PROVIDER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PAYOUT_REFERENCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_PAYOUT_REFERENCE_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_PROCESSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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

    private static final MoneyWithdrawStatus DEFAULT_WITHDRAW_STATUS = MoneyWithdrawStatus.WITHDRAW_PENDING;
    private static final MoneyWithdrawStatus UPDATED_WITHDRAW_STATUS = MoneyWithdrawStatus.WITHDRAW_PROCESSED;

    private static final String ENTITY_API_URL = "/api/money-withdraws";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MoneyWithdrawRepository moneyWithdrawRepository;

    @Autowired
    private MoneyWithdrawMapper moneyWithdrawMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoneyWithdrawMockMvc;

    private MoneyWithdraw moneyWithdraw;

    private MoneyWithdraw insertedMoneyWithdraw;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyWithdraw createEntity() {
        return new MoneyWithdraw()
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .payoutProviderName(DEFAULT_PAYOUT_PROVIDER_NAME)
            .payoutReferenceId(DEFAULT_PAYOUT_REFERENCE_ID)
            .processedAt(DEFAULT_PROCESSED_AT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .deletedDate(DEFAULT_DELETED_DATE)
            .withdrawStatus(DEFAULT_WITHDRAW_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyWithdraw createUpdatedEntity() {
        return new MoneyWithdraw()
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .payoutProviderName(UPDATED_PAYOUT_PROVIDER_NAME)
            .payoutReferenceId(UPDATED_PAYOUT_REFERENCE_ID)
            .processedAt(UPDATED_PROCESSED_AT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS);
    }

    @BeforeEach
    void initTest() {
        moneyWithdraw = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMoneyWithdraw != null) {
            moneyWithdrawRepository.delete(insertedMoneyWithdraw);
            insertedMoneyWithdraw = null;
        }
    }

    @Test
    @Transactional
    void createMoneyWithdraw() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MoneyWithdraw
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);
        var returnedMoneyWithdrawDTO = om.readValue(
            restMoneyWithdrawMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyWithdrawDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MoneyWithdrawDTO.class
        );

        // Validate the MoneyWithdraw in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMoneyWithdraw = moneyWithdrawMapper.toEntity(returnedMoneyWithdrawDTO);
        assertMoneyWithdrawUpdatableFieldsEquals(returnedMoneyWithdraw, getPersistedMoneyWithdraw(returnedMoneyWithdraw));

        insertedMoneyWithdraw = returnedMoneyWithdraw;
    }

    @Test
    @Transactional
    void createMoneyWithdrawWithExistingId() throws Exception {
        // Create the MoneyWithdraw with an existing ID
        moneyWithdraw.setId(1L);
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoneyWithdrawMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyWithdrawDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MoneyWithdraw in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyWithdraw.setAmount(null);

        // Create the MoneyWithdraw, which fails.
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);

        restMoneyWithdrawMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyWithdrawDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyWithdraw.setCurrency(null);

        // Create the MoneyWithdraw, which fails.
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);

        restMoneyWithdrawMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyWithdrawDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyWithdraw.setCreatedDate(null);

        // Create the MoneyWithdraw, which fails.
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);

        restMoneyWithdrawMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyWithdrawDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWithdrawStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyWithdraw.setWithdrawStatus(null);

        // Create the MoneyWithdraw, which fails.
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);

        restMoneyWithdrawMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyWithdrawDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMoneyWithdraws() throws Exception {
        // Initialize the database
        insertedMoneyWithdraw = moneyWithdrawRepository.saveAndFlush(moneyWithdraw);

        // Get all the moneyWithdrawList
        restMoneyWithdrawMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyWithdraw.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].payoutProviderName").value(hasItem(DEFAULT_PAYOUT_PROVIDER_NAME)))
            .andExpect(jsonPath("$.[*].payoutReferenceId").value(hasItem(DEFAULT_PAYOUT_REFERENCE_ID)))
            .andExpect(jsonPath("$.[*].processedAt").value(hasItem(DEFAULT_PROCESSED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].withdrawStatus").value(hasItem(DEFAULT_WITHDRAW_STATUS.toString())));
    }

    @Test
    @Transactional
    void getMoneyWithdraw() throws Exception {
        // Initialize the database
        insertedMoneyWithdraw = moneyWithdrawRepository.saveAndFlush(moneyWithdraw);

        // Get the moneyWithdraw
        restMoneyWithdrawMockMvc
            .perform(get(ENTITY_API_URL_ID, moneyWithdraw.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moneyWithdraw.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.payoutProviderName").value(DEFAULT_PAYOUT_PROVIDER_NAME))
            .andExpect(jsonPath("$.payoutReferenceId").value(DEFAULT_PAYOUT_REFERENCE_ID))
            .andExpect(jsonPath("$.processedAt").value(DEFAULT_PROCESSED_AT.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()))
            .andExpect(jsonPath("$.withdrawStatus").value(DEFAULT_WITHDRAW_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMoneyWithdraw() throws Exception {
        // Get the moneyWithdraw
        restMoneyWithdrawMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoneyWithdraw() throws Exception {
        // Initialize the database
        insertedMoneyWithdraw = moneyWithdrawRepository.saveAndFlush(moneyWithdraw);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyWithdraw
        MoneyWithdraw updatedMoneyWithdraw = moneyWithdrawRepository.findById(moneyWithdraw.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMoneyWithdraw are not directly saved in db
        em.detach(updatedMoneyWithdraw);
        updatedMoneyWithdraw
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .payoutProviderName(UPDATED_PAYOUT_PROVIDER_NAME)
            .payoutReferenceId(UPDATED_PAYOUT_REFERENCE_ID)
            .processedAt(UPDATED_PROCESSED_AT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS);
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(updatedMoneyWithdraw);

        restMoneyWithdrawMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyWithdrawDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyWithdrawDTO))
            )
            .andExpect(status().isOk());

        // Validate the MoneyWithdraw in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMoneyWithdrawToMatchAllProperties(updatedMoneyWithdraw);
    }

    @Test
    @Transactional
    void putNonExistingMoneyWithdraw() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyWithdraw.setId(longCount.incrementAndGet());

        // Create the MoneyWithdraw
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyWithdrawMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyWithdrawDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyWithdrawDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyWithdraw in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoneyWithdraw() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyWithdraw.setId(longCount.incrementAndGet());

        // Create the MoneyWithdraw
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyWithdrawMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyWithdrawDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyWithdraw in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoneyWithdraw() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyWithdraw.setId(longCount.incrementAndGet());

        // Create the MoneyWithdraw
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyWithdrawMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyWithdrawDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyWithdraw in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMoneyWithdrawWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyWithdraw = moneyWithdrawRepository.saveAndFlush(moneyWithdraw);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyWithdraw using partial update
        MoneyWithdraw partialUpdatedMoneyWithdraw = new MoneyWithdraw();
        partialUpdatedMoneyWithdraw.setId(moneyWithdraw.getId());

        partialUpdatedMoneyWithdraw
            .payoutProviderName(UPDATED_PAYOUT_PROVIDER_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS);

        restMoneyWithdrawMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyWithdraw.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyWithdraw))
            )
            .andExpect(status().isOk());

        // Validate the MoneyWithdraw in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyWithdrawUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMoneyWithdraw, moneyWithdraw),
            getPersistedMoneyWithdraw(moneyWithdraw)
        );
    }

    @Test
    @Transactional
    void fullUpdateMoneyWithdrawWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyWithdraw = moneyWithdrawRepository.saveAndFlush(moneyWithdraw);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyWithdraw using partial update
        MoneyWithdraw partialUpdatedMoneyWithdraw = new MoneyWithdraw();
        partialUpdatedMoneyWithdraw.setId(moneyWithdraw.getId());

        partialUpdatedMoneyWithdraw
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .payoutProviderName(UPDATED_PAYOUT_PROVIDER_NAME)
            .payoutReferenceId(UPDATED_PAYOUT_REFERENCE_ID)
            .processedAt(UPDATED_PROCESSED_AT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS);

        restMoneyWithdrawMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyWithdraw.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyWithdraw))
            )
            .andExpect(status().isOk());

        // Validate the MoneyWithdraw in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyWithdrawUpdatableFieldsEquals(partialUpdatedMoneyWithdraw, getPersistedMoneyWithdraw(partialUpdatedMoneyWithdraw));
    }

    @Test
    @Transactional
    void patchNonExistingMoneyWithdraw() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyWithdraw.setId(longCount.incrementAndGet());

        // Create the MoneyWithdraw
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyWithdrawMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moneyWithdrawDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyWithdrawDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyWithdraw in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoneyWithdraw() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyWithdraw.setId(longCount.incrementAndGet());

        // Create the MoneyWithdraw
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyWithdrawMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyWithdrawDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyWithdraw in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoneyWithdraw() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyWithdraw.setId(longCount.incrementAndGet());

        // Create the MoneyWithdraw
        MoneyWithdrawDTO moneyWithdrawDTO = moneyWithdrawMapper.toDto(moneyWithdraw);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyWithdrawMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moneyWithdrawDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyWithdraw in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMoneyWithdraw() throws Exception {
        // Initialize the database
        insertedMoneyWithdraw = moneyWithdrawRepository.saveAndFlush(moneyWithdraw);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the moneyWithdraw
        restMoneyWithdrawMockMvc
            .perform(delete(ENTITY_API_URL_ID, moneyWithdraw.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return moneyWithdrawRepository.count();
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

    protected MoneyWithdraw getPersistedMoneyWithdraw(MoneyWithdraw moneyWithdraw) {
        return moneyWithdrawRepository.findById(moneyWithdraw.getId()).orElseThrow();
    }

    protected void assertPersistedMoneyWithdrawToMatchAllProperties(MoneyWithdraw expectedMoneyWithdraw) {
        assertMoneyWithdrawAllPropertiesEquals(expectedMoneyWithdraw, getPersistedMoneyWithdraw(expectedMoneyWithdraw));
    }

    protected void assertPersistedMoneyWithdrawToMatchUpdatableProperties(MoneyWithdraw expectedMoneyWithdraw) {
        assertMoneyWithdrawAllUpdatablePropertiesEquals(expectedMoneyWithdraw, getPersistedMoneyWithdraw(expectedMoneyWithdraw));
    }
}
