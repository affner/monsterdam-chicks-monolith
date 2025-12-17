package com.monsterdam.app.web.crud;

import static com.monsterdam.app.domain.RefundTransactionAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.monsterdam.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.RefundTransaction;
import com.monsterdam.app.domain.enumeration.GenericStatus;
import com.monsterdam.app.repository.RefundTransactionRepository;
import com.monsterdam.app.service.dto.RefundTransactionDTO;
import com.monsterdam.app.service.mapper.RefundTransactionMapper;
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
 * Integration tests for the {@link RefundTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RefundTransactionResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER_CHARGE_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_CHARGE_ID = "BBBBBBBBBB";

    private static final GenericStatus DEFAULT_STATUS = GenericStatus.PENDING;
    private static final GenericStatus UPDATED_STATUS = GenericStatus.COMPLETED;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PROCESSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELETED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/crud/refund-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RefundTransactionRepository refundTransactionRepository;

    @Autowired
    private RefundTransactionMapper refundTransactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRefundTransactionMockMvc;

    private RefundTransaction refundTransaction;

    private RefundTransaction insertedRefundTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RefundTransaction createEntity() {
        return new RefundTransaction()
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .reason(DEFAULT_REASON)
            .paymentReference(DEFAULT_PAYMENT_REFERENCE)
            .providerChargeId(DEFAULT_PROVIDER_CHARGE_ID)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .processedAt(DEFAULT_PROCESSED_AT)
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
    public static RefundTransaction createUpdatedEntity() {
        return new RefundTransaction()
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .reason(UPDATED_REASON)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .providerChargeId(UPDATED_PROVIDER_CHARGE_ID)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .processedAt(UPDATED_PROCESSED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        refundTransaction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRefundTransaction != null) {
            refundTransactionRepository.delete(insertedRefundTransaction);
            insertedRefundTransaction = null;
        }
    }

    @Test
    @Transactional
    void createRefundTransaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RefundTransaction
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);
        var returnedRefundTransactionDTO = om.readValue(
            restRefundTransactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundTransactionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RefundTransactionDTO.class
        );

        // Validate the RefundTransaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRefundTransaction = refundTransactionMapper.toEntity(returnedRefundTransactionDTO);
        assertRefundTransactionUpdatableFieldsEquals(returnedRefundTransaction, getPersistedRefundTransaction(returnedRefundTransaction));

        insertedRefundTransaction = returnedRefundTransaction;
    }

    @Test
    @Transactional
    void createRefundTransactionWithExistingId() throws Exception {
        // Create the RefundTransaction with an existing ID
        refundTransaction.setId(1L);
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRefundTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RefundTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        refundTransaction.setAmount(null);

        // Create the RefundTransaction, which fails.
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);

        restRefundTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        refundTransaction.setCurrency(null);

        // Create the RefundTransaction, which fails.
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);

        restRefundTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        refundTransaction.setStatus(null);

        // Create the RefundTransaction, which fails.
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);

        restRefundTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        refundTransaction.setCreatedDate(null);

        // Create the RefundTransaction, which fails.
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);

        restRefundTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRefundTransactions() throws Exception {
        // Initialize the database
        insertedRefundTransaction = refundTransactionRepository.saveAndFlush(refundTransaction);

        // Get all the refundTransactionList
        restRefundTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(refundTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].paymentReference").value(hasItem(DEFAULT_PAYMENT_REFERENCE)))
            .andExpect(jsonPath("$.[*].providerChargeId").value(hasItem(DEFAULT_PROVIDER_CHARGE_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].processedAt").value(hasItem(DEFAULT_PROCESSED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getRefundTransaction() throws Exception {
        // Initialize the database
        insertedRefundTransaction = refundTransactionRepository.saveAndFlush(refundTransaction);

        // Get the refundTransaction
        restRefundTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, refundTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(refundTransaction.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.paymentReference").value(DEFAULT_PAYMENT_REFERENCE))
            .andExpect(jsonPath("$.providerChargeId").value(DEFAULT_PROVIDER_CHARGE_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.processedAt").value(DEFAULT_PROCESSED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRefundTransaction() throws Exception {
        // Get the refundTransaction
        restRefundTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRefundTransaction() throws Exception {
        // Initialize the database
        insertedRefundTransaction = refundTransactionRepository.saveAndFlush(refundTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the refundTransaction
        RefundTransaction updatedRefundTransaction = refundTransactionRepository.findById(refundTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRefundTransaction are not directly saved in db
        em.detach(updatedRefundTransaction);
        updatedRefundTransaction
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .reason(UPDATED_REASON)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .providerChargeId(UPDATED_PROVIDER_CHARGE_ID)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .processedAt(UPDATED_PROCESSED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(updatedRefundTransaction);

        restRefundTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refundTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(refundTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the RefundTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRefundTransactionToMatchAllProperties(updatedRefundTransaction);
    }

    @Test
    @Transactional
    void putNonExistingRefundTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refundTransaction.setId(longCount.incrementAndGet());

        // Create the RefundTransaction
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefundTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refundTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(refundTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RefundTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRefundTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refundTransaction.setId(longCount.incrementAndGet());

        // Create the RefundTransaction
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(refundTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RefundTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRefundTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refundTransaction.setId(longCount.incrementAndGet());

        // Create the RefundTransaction
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundTransactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RefundTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRefundTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedRefundTransaction = refundTransactionRepository.saveAndFlush(refundTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the refundTransaction using partial update
        RefundTransaction partialUpdatedRefundTransaction = new RefundTransaction();
        partialUpdatedRefundTransaction.setId(refundTransaction.getId());

        partialUpdatedRefundTransaction
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .providerChargeId(UPDATED_PROVIDER_CHARGE_ID)
            .status(UPDATED_STATUS)
            .processedAt(UPDATED_PROCESSED_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restRefundTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefundTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRefundTransaction))
            )
            .andExpect(status().isOk());

        // Validate the RefundTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRefundTransactionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRefundTransaction, refundTransaction),
            getPersistedRefundTransaction(refundTransaction)
        );
    }

    @Test
    @Transactional
    void fullUpdateRefundTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedRefundTransaction = refundTransactionRepository.saveAndFlush(refundTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the refundTransaction using partial update
        RefundTransaction partialUpdatedRefundTransaction = new RefundTransaction();
        partialUpdatedRefundTransaction.setId(refundTransaction.getId());

        partialUpdatedRefundTransaction
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .reason(UPDATED_REASON)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .providerChargeId(UPDATED_PROVIDER_CHARGE_ID)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .processedAt(UPDATED_PROCESSED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restRefundTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefundTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRefundTransaction))
            )
            .andExpect(status().isOk());

        // Validate the RefundTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRefundTransactionUpdatableFieldsEquals(
            partialUpdatedRefundTransaction,
            getPersistedRefundTransaction(partialUpdatedRefundTransaction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRefundTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refundTransaction.setId(longCount.incrementAndGet());

        // Create the RefundTransaction
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefundTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, refundTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(refundTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RefundTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRefundTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refundTransaction.setId(longCount.incrementAndGet());

        // Create the RefundTransaction
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(refundTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RefundTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRefundTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refundTransaction.setId(longCount.incrementAndGet());

        // Create the RefundTransaction
        RefundTransactionDTO refundTransactionDTO = refundTransactionMapper.toDto(refundTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundTransactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(refundTransactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RefundTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRefundTransaction() throws Exception {
        // Initialize the database
        insertedRefundTransaction = refundTransactionRepository.saveAndFlush(refundTransaction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the refundTransaction
        restRefundTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, refundTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return refundTransactionRepository.count();
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

    protected RefundTransaction getPersistedRefundTransaction(RefundTransaction refundTransaction) {
        return refundTransactionRepository.findById(refundTransaction.getId()).orElseThrow();
    }

    protected void assertPersistedRefundTransactionToMatchAllProperties(RefundTransaction expectedRefundTransaction) {
        assertRefundTransactionAllPropertiesEquals(expectedRefundTransaction, getPersistedRefundTransaction(expectedRefundTransaction));
    }

    protected void assertPersistedRefundTransactionToMatchUpdatableProperties(RefundTransaction expectedRefundTransaction) {
        assertRefundTransactionAllUpdatablePropertiesEquals(
            expectedRefundTransaction,
            getPersistedRefundTransaction(expectedRefundTransaction)
        );
    }
}
