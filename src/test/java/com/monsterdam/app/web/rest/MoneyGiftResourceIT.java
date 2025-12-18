package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.MoneyGiftAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.monsterdam.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.MoneyGift;
import com.monsterdam.app.repository.MoneyGiftRepository;
import com.monsterdam.app.service.dto.MoneyGiftDTO;
import com.monsterdam.app.service.mapper.MoneyGiftMapper;
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
 * Integration tests for the {@link MoneyGiftResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MoneyGiftResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

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

    private static final Long DEFAULT_MESSAGE_ID = 1L;
    private static final Long UPDATED_MESSAGE_ID = 2L;

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final String ENTITY_API_URL = "/api/money-gifts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MoneyGiftRepository moneyGiftRepository;

    @Autowired
    private MoneyGiftMapper moneyGiftMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoneyGiftMockMvc;

    private MoneyGift moneyGift;

    private MoneyGift insertedMoneyGift;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyGift createEntity() {
        return new MoneyGift()
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .deletedDate(DEFAULT_DELETED_DATE)
            .messageId(DEFAULT_MESSAGE_ID)
            .postId(DEFAULT_POST_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyGift createUpdatedEntity() {
        return new MoneyGift()
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID);
    }

    @BeforeEach
    void initTest() {
        moneyGift = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMoneyGift != null) {
            moneyGiftRepository.delete(insertedMoneyGift);
            insertedMoneyGift = null;
        }
    }

    @Test
    @Transactional
    void createMoneyGift() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MoneyGift
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(moneyGift);
        var returnedMoneyGiftDTO = om.readValue(
            restMoneyGiftMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyGiftDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MoneyGiftDTO.class
        );

        // Validate the MoneyGift in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMoneyGift = moneyGiftMapper.toEntity(returnedMoneyGiftDTO);
        assertMoneyGiftUpdatableFieldsEquals(returnedMoneyGift, getPersistedMoneyGift(returnedMoneyGift));

        insertedMoneyGift = returnedMoneyGift;
    }

    @Test
    @Transactional
    void createMoneyGiftWithExistingId() throws Exception {
        // Create the MoneyGift with an existing ID
        moneyGift.setId(1L);
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(moneyGift);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoneyGiftMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyGiftDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MoneyGift in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyGift.setAmount(null);

        // Create the MoneyGift, which fails.
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(moneyGift);

        restMoneyGiftMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyGiftDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyGift.setCurrency(null);

        // Create the MoneyGift, which fails.
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(moneyGift);

        restMoneyGiftMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyGiftDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyGift.setCreatedDate(null);

        // Create the MoneyGift, which fails.
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(moneyGift);

        restMoneyGiftMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyGiftDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMoneyGifts() throws Exception {
        // Initialize the database
        insertedMoneyGift = moneyGiftRepository.saveAndFlush(moneyGift);

        // Get all the moneyGiftList
        restMoneyGiftMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyGift.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())));
    }

    @Test
    @Transactional
    void getMoneyGift() throws Exception {
        // Initialize the database
        insertedMoneyGift = moneyGiftRepository.saveAndFlush(moneyGift);

        // Get the moneyGift
        restMoneyGiftMockMvc
            .perform(get(ENTITY_API_URL_ID, moneyGift.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moneyGift.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMoneyGift() throws Exception {
        // Get the moneyGift
        restMoneyGiftMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoneyGift() throws Exception {
        // Initialize the database
        insertedMoneyGift = moneyGiftRepository.saveAndFlush(moneyGift);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyGift
        MoneyGift updatedMoneyGift = moneyGiftRepository.findById(moneyGift.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMoneyGift are not directly saved in db
        em.detach(updatedMoneyGift);
        updatedMoneyGift
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID);
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(updatedMoneyGift);

        restMoneyGiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyGiftDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyGiftDTO))
            )
            .andExpect(status().isOk());

        // Validate the MoneyGift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMoneyGiftToMatchAllProperties(updatedMoneyGift);
    }

    @Test
    @Transactional
    void putNonExistingMoneyGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyGift.setId(longCount.incrementAndGet());

        // Create the MoneyGift
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(moneyGift);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyGiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyGiftDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyGiftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyGift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoneyGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyGift.setId(longCount.incrementAndGet());

        // Create the MoneyGift
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(moneyGift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyGiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyGiftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyGift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoneyGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyGift.setId(longCount.incrementAndGet());

        // Create the MoneyGift
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(moneyGift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyGiftMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyGiftDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyGift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMoneyGiftWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyGift = moneyGiftRepository.saveAndFlush(moneyGift);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyGift using partial update
        MoneyGift partialUpdatedMoneyGift = new MoneyGift();
        partialUpdatedMoneyGift.setId(moneyGift.getId());

        partialUpdatedMoneyGift
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .messageId(UPDATED_MESSAGE_ID);

        restMoneyGiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyGift.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyGift))
            )
            .andExpect(status().isOk());

        // Validate the MoneyGift in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyGiftUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMoneyGift, moneyGift),
            getPersistedMoneyGift(moneyGift)
        );
    }

    @Test
    @Transactional
    void fullUpdateMoneyGiftWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyGift = moneyGiftRepository.saveAndFlush(moneyGift);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyGift using partial update
        MoneyGift partialUpdatedMoneyGift = new MoneyGift();
        partialUpdatedMoneyGift.setId(moneyGift.getId());

        partialUpdatedMoneyGift
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID);

        restMoneyGiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyGift.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyGift))
            )
            .andExpect(status().isOk());

        // Validate the MoneyGift in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyGiftUpdatableFieldsEquals(partialUpdatedMoneyGift, getPersistedMoneyGift(partialUpdatedMoneyGift));
    }

    @Test
    @Transactional
    void patchNonExistingMoneyGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyGift.setId(longCount.incrementAndGet());

        // Create the MoneyGift
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(moneyGift);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyGiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moneyGiftDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyGiftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyGift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoneyGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyGift.setId(longCount.incrementAndGet());

        // Create the MoneyGift
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(moneyGift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyGiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyGiftDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyGift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoneyGift() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyGift.setId(longCount.incrementAndGet());

        // Create the MoneyGift
        MoneyGiftDTO moneyGiftDTO = moneyGiftMapper.toDto(moneyGift);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyGiftMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moneyGiftDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyGift in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMoneyGift() throws Exception {
        // Initialize the database
        insertedMoneyGift = moneyGiftRepository.saveAndFlush(moneyGift);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the moneyGift
        restMoneyGiftMockMvc
            .perform(delete(ENTITY_API_URL_ID, moneyGift.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return moneyGiftRepository.count();
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

    protected MoneyGift getPersistedMoneyGift(MoneyGift moneyGift) {
        return moneyGiftRepository.findById(moneyGift.getId()).orElseThrow();
    }

    protected void assertPersistedMoneyGiftToMatchAllProperties(MoneyGift expectedMoneyGift) {
        assertMoneyGiftAllPropertiesEquals(expectedMoneyGift, getPersistedMoneyGift(expectedMoneyGift));
    }

    protected void assertPersistedMoneyGiftToMatchUpdatableProperties(MoneyGift expectedMoneyGift) {
        assertMoneyGiftAllUpdatablePropertiesEquals(expectedMoneyGift, getPersistedMoneyGift(expectedMoneyGift));
    }
}
