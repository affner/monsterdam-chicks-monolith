package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.PurchasedSubscriptionAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PurchasedSubscription;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.enumeration.PurchasedSubscriptionStatus;
import com.monsterdam.app.repository.PurchasedSubscriptionRepository;
import com.monsterdam.app.service.dto.PurchasedSubscriptionDTO;
import com.monsterdam.app.service.mapper.PurchasedSubscriptionMapper;
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
 * Integration tests for the {@link PurchasedSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchasedSubscriptionResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PurchasedSubscriptionStatus DEFAULT_SUBSCRIPTION_STATUS = PurchasedSubscriptionStatus.PURCHASED;
    private static final PurchasedSubscriptionStatus UPDATED_SUBSCRIPTION_STATUS = PurchasedSubscriptionStatus.RENEWED;

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

    private static final String ENTITY_API_URL = "/api/purchased-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PurchasedSubscriptionRepository purchasedSubscriptionRepository;

    @Autowired
    private PurchasedSubscriptionMapper purchasedSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchasedSubscriptionMockMvc;

    private PurchasedSubscription purchasedSubscription;

    private PurchasedSubscription insertedPurchasedSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedSubscription createEntity(EntityManager em) {
        PurchasedSubscription purchasedSubscription = new PurchasedSubscription()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .subscriptionStatus(DEFAULT_SUBSCRIPTION_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .deletedDate(DEFAULT_DELETED_DATE);
        // Add required entity
        UserLite userLite;
        if (TestUtil.findAll(em, UserLite.class).isEmpty()) {
            userLite = UserLiteResourceIT.createEntity();
            em.persist(userLite);
            em.flush();
        } else {
            userLite = TestUtil.findAll(em, UserLite.class).get(0);
        }
        purchasedSubscription.setViewer(userLite);
        // Add required entity
        purchasedSubscription.setCreator(userLite);
        return purchasedSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedSubscription createUpdatedEntity(EntityManager em) {
        PurchasedSubscription updatedPurchasedSubscription = new PurchasedSubscription()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        // Add required entity
        UserLite userLite;
        if (TestUtil.findAll(em, UserLite.class).isEmpty()) {
            userLite = UserLiteResourceIT.createUpdatedEntity();
            em.persist(userLite);
            em.flush();
        } else {
            userLite = TestUtil.findAll(em, UserLite.class).get(0);
        }
        updatedPurchasedSubscription.setViewer(userLite);
        // Add required entity
        updatedPurchasedSubscription.setCreator(userLite);
        return updatedPurchasedSubscription;
    }

    @BeforeEach
    void initTest() {
        purchasedSubscription = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPurchasedSubscription != null) {
            purchasedSubscriptionRepository.delete(insertedPurchasedSubscription);
            insertedPurchasedSubscription = null;
        }
    }

    @Test
    @Transactional
    void createPurchasedSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);
        var returnedPurchasedSubscriptionDTO = om.readValue(
            restPurchasedSubscriptionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedSubscriptionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PurchasedSubscriptionDTO.class
        );

        // Validate the PurchasedSubscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPurchasedSubscription = purchasedSubscriptionMapper.toEntity(returnedPurchasedSubscriptionDTO);
        assertPurchasedSubscriptionUpdatableFieldsEquals(
            returnedPurchasedSubscription,
            getPersistedPurchasedSubscription(returnedPurchasedSubscription)
        );

        insertedPurchasedSubscription = returnedPurchasedSubscription;
    }

    @Test
    @Transactional
    void createPurchasedSubscriptionWithExistingId() throws Exception {
        // Create the PurchasedSubscription with an existing ID
        purchasedSubscription.setId(1L);
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchasedSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchasedSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchasedSubscription.setStartDate(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchasedSubscription.setEndDate(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchasedSubscription.setSubscriptionStatus(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchasedSubscription.setCreatedDate(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchasedSubscriptions() throws Exception {
        // Initialize the database
        insertedPurchasedSubscription = purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        // Get all the purchasedSubscriptionList
        restPurchasedSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasedSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionStatus").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPurchasedSubscription() throws Exception {
        // Initialize the database
        insertedPurchasedSubscription = purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        // Get the purchasedSubscription
        restPurchasedSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, purchasedSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchasedSubscription.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.subscriptionStatus").value(DEFAULT_SUBSCRIPTION_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPurchasedSubscription() throws Exception {
        // Get the purchasedSubscription
        restPurchasedSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchasedSubscription() throws Exception {
        // Initialize the database
        insertedPurchasedSubscription = purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedSubscription
        PurchasedSubscription updatedPurchasedSubscription = purchasedSubscriptionRepository
            .findById(purchasedSubscription.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPurchasedSubscription are not directly saved in db
        em.detach(updatedPurchasedSubscription);
        updatedPurchasedSubscription
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(updatedPurchasedSubscription);

        restPurchasedSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPurchasedSubscriptionToMatchAllProperties(updatedPurchasedSubscription);
    }

    @Test
    @Transactional
    void putNonExistingPurchasedSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchasedSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchasedSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedSubscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchasedSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedPurchasedSubscription = purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedSubscription using partial update
        PurchasedSubscription partialUpdatedPurchasedSubscription = new PurchasedSubscription();
        partialUpdatedPurchasedSubscription.setId(purchasedSubscription.getId());

        partialUpdatedPurchasedSubscription.endDate(UPDATED_END_DATE).createdBy(UPDATED_CREATED_BY);

        restPurchasedSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchasedSubscription))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchasedSubscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPurchasedSubscription, purchasedSubscription),
            getPersistedPurchasedSubscription(purchasedSubscription)
        );
    }

    @Test
    @Transactional
    void fullUpdatePurchasedSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedPurchasedSubscription = purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedSubscription using partial update
        PurchasedSubscription partialUpdatedPurchasedSubscription = new PurchasedSubscription();
        partialUpdatedPurchasedSubscription.setId(purchasedSubscription.getId());

        partialUpdatedPurchasedSubscription
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPurchasedSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchasedSubscription))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchasedSubscriptionUpdatableFieldsEquals(
            partialUpdatedPurchasedSubscription,
            getPersistedPurchasedSubscription(partialUpdatedPurchasedSubscription)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPurchasedSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchasedSubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchasedSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchasedSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(purchasedSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchasedSubscription() throws Exception {
        // Initialize the database
        insertedPurchasedSubscription = purchasedSubscriptionRepository.saveAndFlush(purchasedSubscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the purchasedSubscription
        restPurchasedSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchasedSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return purchasedSubscriptionRepository.count();
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

    protected PurchasedSubscription getPersistedPurchasedSubscription(PurchasedSubscription purchasedSubscription) {
        return purchasedSubscriptionRepository.findById(purchasedSubscription.getId()).orElseThrow();
    }

    protected void assertPersistedPurchasedSubscriptionToMatchAllProperties(PurchasedSubscription expectedPurchasedSubscription) {
        assertPurchasedSubscriptionAllPropertiesEquals(
            expectedPurchasedSubscription,
            getPersistedPurchasedSubscription(expectedPurchasedSubscription)
        );
    }

    protected void assertPersistedPurchasedSubscriptionToMatchUpdatableProperties(PurchasedSubscription expectedPurchasedSubscription) {
        assertPurchasedSubscriptionAllUpdatablePropertiesEquals(
            expectedPurchasedSubscription,
            getPersistedPurchasedSubscription(expectedPurchasedSubscription)
        );
    }
}
