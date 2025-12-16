package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.SubscriptionBundleAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.monsterdam.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.SubscriptionBundle;
import com.monsterdam.app.repository.SubscriptionBundleRepository;
import com.monsterdam.app.service.dto.SubscriptionBundleDTO;
import com.monsterdam.app.service.mapper.SubscriptionBundleMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Duration;
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
 * Integration tests for the {@link SubscriptionBundleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionBundleResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

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

    private static final String ENTITY_API_URL = "/api/subscription-bundles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubscriptionBundleRepository subscriptionBundleRepository;

    @Autowired
    private SubscriptionBundleMapper subscriptionBundleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionBundleMockMvc;

    private SubscriptionBundle subscriptionBundle;

    private SubscriptionBundle insertedSubscriptionBundle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionBundle createEntity() {
        return new SubscriptionBundle()
            .amount(DEFAULT_AMOUNT)
            .duration(DEFAULT_DURATION)
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
    public static SubscriptionBundle createUpdatedEntity() {
        return new SubscriptionBundle()
            .amount(UPDATED_AMOUNT)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        subscriptionBundle = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSubscriptionBundle != null) {
            subscriptionBundleRepository.delete(insertedSubscriptionBundle);
            insertedSubscriptionBundle = null;
        }
    }

    @Test
    @Transactional
    void createSubscriptionBundle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);
        var returnedSubscriptionBundleDTO = om.readValue(
            restSubscriptionBundleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionBundleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubscriptionBundleDTO.class
        );

        // Validate the SubscriptionBundle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubscriptionBundle = subscriptionBundleMapper.toEntity(returnedSubscriptionBundleDTO);
        assertSubscriptionBundleUpdatableFieldsEquals(
            returnedSubscriptionBundle,
            getPersistedSubscriptionBundle(returnedSubscriptionBundle)
        );

        insertedSubscriptionBundle = returnedSubscriptionBundle;
    }

    @Test
    @Transactional
    void createSubscriptionBundleWithExistingId() throws Exception {
        // Create the SubscriptionBundle with an existing ID
        subscriptionBundle.setId(1L);
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionBundleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionBundleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionBundle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionBundle.setAmount(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        restSubscriptionBundleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionBundleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionBundle.setDuration(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        restSubscriptionBundleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionBundleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionBundle.setCreatedDate(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        restSubscriptionBundleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionBundleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptionBundles() throws Exception {
        // Initialize the database
        insertedSubscriptionBundle = subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        // Get all the subscriptionBundleList
        restSubscriptionBundleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionBundle.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSubscriptionBundle() throws Exception {
        // Initialize the database
        insertedSubscriptionBundle = subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        // Get the subscriptionBundle
        restSubscriptionBundleMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptionBundle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionBundle.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptionBundle() throws Exception {
        // Get the subscriptionBundle
        restSubscriptionBundleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscriptionBundle() throws Exception {
        // Initialize the database
        insertedSubscriptionBundle = subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionBundle
        SubscriptionBundle updatedSubscriptionBundle = subscriptionBundleRepository.findById(subscriptionBundle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubscriptionBundle are not directly saved in db
        em.detach(updatedSubscriptionBundle);
        updatedSubscriptionBundle
            .amount(UPDATED_AMOUNT)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(updatedSubscriptionBundle);

        restSubscriptionBundleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionBundleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionBundle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubscriptionBundleToMatchAllProperties(updatedSubscriptionBundle);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptionBundle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionBundleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionBundle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptionBundle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionBundle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptionBundle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionBundleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionBundle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionBundleWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionBundle = subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionBundle using partial update
        SubscriptionBundle partialUpdatedSubscriptionBundle = new SubscriptionBundle();
        partialUpdatedSubscriptionBundle.setId(subscriptionBundle.getId());

        partialUpdatedSubscriptionBundle.lastModifiedBy(UPDATED_LAST_MODIFIED_BY).deletedDate(UPDATED_DELETED_DATE);

        restSubscriptionBundleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionBundle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionBundle))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionBundle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionBundleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubscriptionBundle, subscriptionBundle),
            getPersistedSubscriptionBundle(subscriptionBundle)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionBundleWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionBundle = subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionBundle using partial update
        SubscriptionBundle partialUpdatedSubscriptionBundle = new SubscriptionBundle();
        partialUpdatedSubscriptionBundle.setId(subscriptionBundle.getId());

        partialUpdatedSubscriptionBundle
            .amount(UPDATED_AMOUNT)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restSubscriptionBundleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionBundle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionBundle))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionBundle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionBundleUpdatableFieldsEquals(
            partialUpdatedSubscriptionBundle,
            getPersistedSubscriptionBundle(partialUpdatedSubscriptionBundle)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptionBundle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptionBundleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionBundle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptionBundle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionBundleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionBundle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptionBundle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionBundleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subscriptionBundleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionBundle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptionBundle() throws Exception {
        // Initialize the database
        insertedSubscriptionBundle = subscriptionBundleRepository.saveAndFlush(subscriptionBundle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subscriptionBundle
        restSubscriptionBundleMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptionBundle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subscriptionBundleRepository.count();
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

    protected SubscriptionBundle getPersistedSubscriptionBundle(SubscriptionBundle subscriptionBundle) {
        return subscriptionBundleRepository.findById(subscriptionBundle.getId()).orElseThrow();
    }

    protected void assertPersistedSubscriptionBundleToMatchAllProperties(SubscriptionBundle expectedSubscriptionBundle) {
        assertSubscriptionBundleAllPropertiesEquals(expectedSubscriptionBundle, getPersistedSubscriptionBundle(expectedSubscriptionBundle));
    }

    protected void assertPersistedSubscriptionBundleToMatchUpdatableProperties(SubscriptionBundle expectedSubscriptionBundle) {
        assertSubscriptionBundleAllUpdatablePropertiesEquals(
            expectedSubscriptionBundle,
            getPersistedSubscriptionBundle(expectedSubscriptionBundle)
        );
    }
}
