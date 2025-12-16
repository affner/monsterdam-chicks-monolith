package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.LedgerEntryAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.monsterdam.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.LedgerEntry;
import com.monsterdam.app.domain.enumeration.LedgerAccountType;
import com.monsterdam.app.domain.enumeration.LedgerEntryType;
import com.monsterdam.app.domain.enumeration.LedgerReason;
import com.monsterdam.app.repository.LedgerEntryRepository;
import com.monsterdam.app.service.dto.LedgerEntryDTO;
import com.monsterdam.app.service.mapper.LedgerEntryMapper;
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
 * Integration tests for the {@link LedgerEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LedgerEntryResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final LedgerEntryType DEFAULT_ENTRY_TYPE = LedgerEntryType.CREDIT;
    private static final LedgerEntryType UPDATED_ENTRY_TYPE = LedgerEntryType.DEBIT;

    private static final LedgerAccountType DEFAULT_ACCOUNT_TYPE = LedgerAccountType.PLATFORM;
    private static final LedgerAccountType UPDATED_ACCOUNT_TYPE = LedgerAccountType.CREATOR;

    private static final Long DEFAULT_ACCOUNT_OWNER_ID = 1L;
    private static final Long UPDATED_ACCOUNT_OWNER_ID = 2L;

    private static final LedgerReason DEFAULT_REASON = LedgerReason.PURCHASE;
    private static final LedgerReason UPDATED_REASON = LedgerReason.COMMISSION;

    private static final String DEFAULT_REFERENCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_REFERENCE_ID = 1L;
    private static final Long UPDATED_REFERENCE_ID = 2L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELETED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ledger-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    @Autowired
    private LedgerEntryMapper ledgerEntryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLedgerEntryMockMvc;

    private LedgerEntry ledgerEntry;

    private LedgerEntry insertedLedgerEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LedgerEntry createEntity() {
        return new LedgerEntry()
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .entryType(DEFAULT_ENTRY_TYPE)
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .accountOwnerId(DEFAULT_ACCOUNT_OWNER_ID)
            .reason(DEFAULT_REASON)
            .referenceType(DEFAULT_REFERENCE_TYPE)
            .referenceId(DEFAULT_REFERENCE_ID)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .deletedDate(DEFAULT_DELETED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LedgerEntry createUpdatedEntity() {
        return new LedgerEntry()
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .entryType(UPDATED_ENTRY_TYPE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountOwnerId(UPDATED_ACCOUNT_OWNER_ID)
            .reason(UPDATED_REASON)
            .referenceType(UPDATED_REFERENCE_TYPE)
            .referenceId(UPDATED_REFERENCE_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        ledgerEntry = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLedgerEntry != null) {
            ledgerEntryRepository.delete(insertedLedgerEntry);
            insertedLedgerEntry = null;
        }
    }

    @Test
    @Transactional
    void createLedgerEntry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LedgerEntry
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);
        var returnedLedgerEntryDTO = om.readValue(
            restLedgerEntryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerEntryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LedgerEntryDTO.class
        );

        // Validate the LedgerEntry in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLedgerEntry = ledgerEntryMapper.toEntity(returnedLedgerEntryDTO);
        assertLedgerEntryUpdatableFieldsEquals(returnedLedgerEntry, getPersistedLedgerEntry(returnedLedgerEntry));

        insertedLedgerEntry = returnedLedgerEntry;
    }

    @Test
    @Transactional
    void createLedgerEntryWithExistingId() throws Exception {
        // Create the LedgerEntry with an existing ID
        ledgerEntry.setId(1L);
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLedgerEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LedgerEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ledgerEntry.setAmount(null);

        // Create the LedgerEntry, which fails.
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        restLedgerEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ledgerEntry.setCurrency(null);

        // Create the LedgerEntry, which fails.
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        restLedgerEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntryTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ledgerEntry.setEntryType(null);

        // Create the LedgerEntry, which fails.
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        restLedgerEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ledgerEntry.setAccountType(null);

        // Create the LedgerEntry, which fails.
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        restLedgerEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReasonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ledgerEntry.setReason(null);

        // Create the LedgerEntry, which fails.
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        restLedgerEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReferenceTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ledgerEntry.setReferenceType(null);

        // Create the LedgerEntry, which fails.
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        restLedgerEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReferenceIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ledgerEntry.setReferenceId(null);

        // Create the LedgerEntry, which fails.
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        restLedgerEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ledgerEntry.setCreatedDate(null);

        // Create the LedgerEntry, which fails.
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        restLedgerEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLedgerEntries() throws Exception {
        // Initialize the database
        insertedLedgerEntry = ledgerEntryRepository.saveAndFlush(ledgerEntry);

        // Get all the ledgerEntryList
        restLedgerEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ledgerEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].entryType").value(hasItem(DEFAULT_ENTRY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountOwnerId").value(hasItem(DEFAULT_ACCOUNT_OWNER_ID.intValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
            .andExpect(jsonPath("$.[*].referenceType").value(hasItem(DEFAULT_REFERENCE_TYPE)))
            .andExpect(jsonPath("$.[*].referenceId").value(hasItem(DEFAULT_REFERENCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getLedgerEntry() throws Exception {
        // Initialize the database
        insertedLedgerEntry = ledgerEntryRepository.saveAndFlush(ledgerEntry);

        // Get the ledgerEntry
        restLedgerEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, ledgerEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ledgerEntry.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.entryType").value(DEFAULT_ENTRY_TYPE.toString()))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.accountOwnerId").value(DEFAULT_ACCOUNT_OWNER_ID.intValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.referenceType").value(DEFAULT_REFERENCE_TYPE))
            .andExpect(jsonPath("$.referenceId").value(DEFAULT_REFERENCE_ID.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLedgerEntry() throws Exception {
        // Get the ledgerEntry
        restLedgerEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLedgerEntry() throws Exception {
        // Initialize the database
        insertedLedgerEntry = ledgerEntryRepository.saveAndFlush(ledgerEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ledgerEntry
        LedgerEntry updatedLedgerEntry = ledgerEntryRepository.findById(ledgerEntry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLedgerEntry are not directly saved in db
        em.detach(updatedLedgerEntry);
        updatedLedgerEntry
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .entryType(UPDATED_ENTRY_TYPE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountOwnerId(UPDATED_ACCOUNT_OWNER_ID)
            .reason(UPDATED_REASON)
            .referenceType(UPDATED_REFERENCE_TYPE)
            .referenceId(UPDATED_REFERENCE_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(updatedLedgerEntry);

        restLedgerEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ledgerEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ledgerEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the LedgerEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLedgerEntryToMatchAllProperties(updatedLedgerEntry);
    }

    @Test
    @Transactional
    void putNonExistingLedgerEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledgerEntry.setId(longCount.incrementAndGet());

        // Create the LedgerEntry
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ledgerEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ledgerEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLedgerEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledgerEntry.setId(longCount.incrementAndGet());

        // Create the LedgerEntry
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ledgerEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLedgerEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledgerEntry.setId(longCount.incrementAndGet());

        // Create the LedgerEntry
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ledgerEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LedgerEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLedgerEntryWithPatch() throws Exception {
        // Initialize the database
        insertedLedgerEntry = ledgerEntryRepository.saveAndFlush(ledgerEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ledgerEntry using partial update
        LedgerEntry partialUpdatedLedgerEntry = new LedgerEntry();
        partialUpdatedLedgerEntry.setId(ledgerEntry.getId());

        partialUpdatedLedgerEntry
            .currency(UPDATED_CURRENCY)
            .entryType(UPDATED_ENTRY_TYPE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .reason(UPDATED_REASON)
            .referenceId(UPDATED_REFERENCE_ID)
            .createdBy(UPDATED_CREATED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restLedgerEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLedgerEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLedgerEntry))
            )
            .andExpect(status().isOk());

        // Validate the LedgerEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLedgerEntryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLedgerEntry, ledgerEntry),
            getPersistedLedgerEntry(ledgerEntry)
        );
    }

    @Test
    @Transactional
    void fullUpdateLedgerEntryWithPatch() throws Exception {
        // Initialize the database
        insertedLedgerEntry = ledgerEntryRepository.saveAndFlush(ledgerEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ledgerEntry using partial update
        LedgerEntry partialUpdatedLedgerEntry = new LedgerEntry();
        partialUpdatedLedgerEntry.setId(ledgerEntry.getId());

        partialUpdatedLedgerEntry
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .entryType(UPDATED_ENTRY_TYPE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountOwnerId(UPDATED_ACCOUNT_OWNER_ID)
            .reason(UPDATED_REASON)
            .referenceType(UPDATED_REFERENCE_TYPE)
            .referenceId(UPDATED_REFERENCE_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restLedgerEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLedgerEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLedgerEntry))
            )
            .andExpect(status().isOk());

        // Validate the LedgerEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLedgerEntryUpdatableFieldsEquals(partialUpdatedLedgerEntry, getPersistedLedgerEntry(partialUpdatedLedgerEntry));
    }

    @Test
    @Transactional
    void patchNonExistingLedgerEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledgerEntry.setId(longCount.incrementAndGet());

        // Create the LedgerEntry
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ledgerEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ledgerEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLedgerEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledgerEntry.setId(longCount.incrementAndGet());

        // Create the LedgerEntry
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ledgerEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLedgerEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ledgerEntry.setId(longCount.incrementAndGet());

        // Create the LedgerEntry
        LedgerEntryDTO ledgerEntryDTO = ledgerEntryMapper.toDto(ledgerEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerEntryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ledgerEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LedgerEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLedgerEntry() throws Exception {
        // Initialize the database
        insertedLedgerEntry = ledgerEntryRepository.saveAndFlush(ledgerEntry);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ledgerEntry
        restLedgerEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, ledgerEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ledgerEntryRepository.count();
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

    protected LedgerEntry getPersistedLedgerEntry(LedgerEntry ledgerEntry) {
        return ledgerEntryRepository.findById(ledgerEntry.getId()).orElseThrow();
    }

    protected void assertPersistedLedgerEntryToMatchAllProperties(LedgerEntry expectedLedgerEntry) {
        assertLedgerEntryAllPropertiesEquals(expectedLedgerEntry, getPersistedLedgerEntry(expectedLedgerEntry));
    }

    protected void assertPersistedLedgerEntryToMatchUpdatableProperties(LedgerEntry expectedLedgerEntry) {
        assertLedgerEntryAllUpdatablePropertiesEquals(expectedLedgerEntry, getPersistedLedgerEntry(expectedLedgerEntry));
    }
}
