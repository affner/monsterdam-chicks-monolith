package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.SubscriptionPlanOfferAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.SubscriptionPlanOffer;
import com.monsterdam.app.domain.enumeration.OfferPromotionType;
import com.monsterdam.app.repository.SubscriptionPlanOfferRepository;
import com.monsterdam.app.service.dto.SubscriptionPlanOfferDTO;
import com.monsterdam.app.service.mapper.SubscriptionPlanOfferMapper;
import jakarta.persistence.EntityManager;
import java.time.Duration;
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
 * Integration tests for the {@link SubscriptionPlanOfferResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionPlanOfferResourceIT {

    private static final Duration DEFAULT_FREE_DAYS_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_FREE_DAYS_DURATION = Duration.ofHours(12);

    private static final Float DEFAULT_DISCOUNT_PERCENTAGE = 0F;
    private static final Float UPDATED_DISCOUNT_PERCENTAGE = 1F;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_SUBSCRIPTIONS_LIMIT = 1;
    private static final Integer UPDATED_SUBSCRIPTIONS_LIMIT = 2;

    private static final OfferPromotionType DEFAULT_PROMOTION_TYPE = OfferPromotionType.DISCOUNT;
    private static final OfferPromotionType UPDATED_PROMOTION_TYPE = OfferPromotionType.FREE_DAYS;

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

    private static final String ENTITY_API_URL = "/api/subscription-plan-offers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubscriptionPlanOfferRepository subscriptionPlanOfferRepository;

    @Autowired
    private SubscriptionPlanOfferMapper subscriptionPlanOfferMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionPlanOfferMockMvc;

    private SubscriptionPlanOffer subscriptionPlanOffer;

    private SubscriptionPlanOffer insertedSubscriptionPlanOffer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionPlanOffer createEntity() {
        return new SubscriptionPlanOffer()
            .freeDaysDuration(DEFAULT_FREE_DAYS_DURATION)
            .discountPercentage(DEFAULT_DISCOUNT_PERCENTAGE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .subscriptionsLimit(DEFAULT_SUBSCRIPTIONS_LIMIT)
            .promotionType(DEFAULT_PROMOTION_TYPE)
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
    public static SubscriptionPlanOffer createUpdatedEntity() {
        return new SubscriptionPlanOffer()
            .freeDaysDuration(UPDATED_FREE_DAYS_DURATION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionsLimit(UPDATED_SUBSCRIPTIONS_LIMIT)
            .promotionType(UPDATED_PROMOTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        subscriptionPlanOffer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSubscriptionPlanOffer != null) {
            subscriptionPlanOfferRepository.delete(insertedSubscriptionPlanOffer);
            insertedSubscriptionPlanOffer = null;
        }
    }

    @Test
    @Transactional
    void createSubscriptionPlanOffer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubscriptionPlanOffer
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);
        var returnedSubscriptionPlanOfferDTO = om.readValue(
            restSubscriptionPlanOfferMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionPlanOfferDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubscriptionPlanOfferDTO.class
        );

        // Validate the SubscriptionPlanOffer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubscriptionPlanOffer = subscriptionPlanOfferMapper.toEntity(returnedSubscriptionPlanOfferDTO);
        assertSubscriptionPlanOfferUpdatableFieldsEquals(
            returnedSubscriptionPlanOffer,
            getPersistedSubscriptionPlanOffer(returnedSubscriptionPlanOffer)
        );

        insertedSubscriptionPlanOffer = returnedSubscriptionPlanOffer;
    }

    @Test
    @Transactional
    void createSubscriptionPlanOfferWithExistingId() throws Exception {
        // Create the SubscriptionPlanOffer with an existing ID
        subscriptionPlanOffer.setId(1L);
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionPlanOfferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionPlanOfferDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionPlanOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionPlanOffer.setStartDate(null);

        // Create the SubscriptionPlanOffer, which fails.
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);

        restSubscriptionPlanOfferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionPlanOfferDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionPlanOffer.setEndDate(null);

        // Create the SubscriptionPlanOffer, which fails.
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);

        restSubscriptionPlanOfferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionPlanOfferDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPromotionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionPlanOffer.setPromotionType(null);

        // Create the SubscriptionPlanOffer, which fails.
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);

        restSubscriptionPlanOfferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionPlanOfferDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptionPlanOffer.setCreatedDate(null);

        // Create the SubscriptionPlanOffer, which fails.
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);

        restSubscriptionPlanOfferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionPlanOfferDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptionPlanOffers() throws Exception {
        // Initialize the database
        insertedSubscriptionPlanOffer = subscriptionPlanOfferRepository.saveAndFlush(subscriptionPlanOffer);

        // Get all the subscriptionPlanOfferList
        restSubscriptionPlanOfferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionPlanOffer.getId().intValue())))
            .andExpect(jsonPath("$.[*].freeDaysDuration").value(hasItem(DEFAULT_FREE_DAYS_DURATION.toString())))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionsLimit").value(hasItem(DEFAULT_SUBSCRIPTIONS_LIMIT)))
            .andExpect(jsonPath("$.[*].promotionType").value(hasItem(DEFAULT_PROMOTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSubscriptionPlanOffer() throws Exception {
        // Initialize the database
        insertedSubscriptionPlanOffer = subscriptionPlanOfferRepository.saveAndFlush(subscriptionPlanOffer);

        // Get the subscriptionPlanOffer
        restSubscriptionPlanOfferMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptionPlanOffer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionPlanOffer.getId().intValue()))
            .andExpect(jsonPath("$.freeDaysDuration").value(DEFAULT_FREE_DAYS_DURATION.toString()))
            .andExpect(jsonPath("$.discountPercentage").value(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.subscriptionsLimit").value(DEFAULT_SUBSCRIPTIONS_LIMIT))
            .andExpect(jsonPath("$.promotionType").value(DEFAULT_PROMOTION_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptionPlanOffer() throws Exception {
        // Get the subscriptionPlanOffer
        restSubscriptionPlanOfferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscriptionPlanOffer() throws Exception {
        // Initialize the database
        insertedSubscriptionPlanOffer = subscriptionPlanOfferRepository.saveAndFlush(subscriptionPlanOffer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionPlanOffer
        SubscriptionPlanOffer updatedSubscriptionPlanOffer = subscriptionPlanOfferRepository
            .findById(subscriptionPlanOffer.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSubscriptionPlanOffer are not directly saved in db
        em.detach(updatedSubscriptionPlanOffer);
        updatedSubscriptionPlanOffer
            .freeDaysDuration(UPDATED_FREE_DAYS_DURATION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionsLimit(UPDATED_SUBSCRIPTIONS_LIMIT)
            .promotionType(UPDATED_PROMOTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(updatedSubscriptionPlanOffer);

        restSubscriptionPlanOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionPlanOfferDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionPlanOfferDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionPlanOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubscriptionPlanOfferToMatchAllProperties(updatedSubscriptionPlanOffer);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptionPlanOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionPlanOffer.setId(longCount.incrementAndGet());

        // Create the SubscriptionPlanOffer
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionPlanOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionPlanOfferDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionPlanOfferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionPlanOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptionPlanOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionPlanOffer.setId(longCount.incrementAndGet());

        // Create the SubscriptionPlanOffer
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionPlanOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptionPlanOfferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionPlanOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptionPlanOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionPlanOffer.setId(longCount.incrementAndGet());

        // Create the SubscriptionPlanOffer
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionPlanOfferMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptionPlanOfferDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionPlanOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionPlanOfferWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionPlanOffer = subscriptionPlanOfferRepository.saveAndFlush(subscriptionPlanOffer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionPlanOffer using partial update
        SubscriptionPlanOffer partialUpdatedSubscriptionPlanOffer = new SubscriptionPlanOffer();
        partialUpdatedSubscriptionPlanOffer.setId(subscriptionPlanOffer.getId());

        partialUpdatedSubscriptionPlanOffer
            .startDate(UPDATED_START_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restSubscriptionPlanOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionPlanOffer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionPlanOffer))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionPlanOffer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionPlanOfferUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubscriptionPlanOffer, subscriptionPlanOffer),
            getPersistedSubscriptionPlanOffer(subscriptionPlanOffer)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionPlanOfferWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptionPlanOffer = subscriptionPlanOfferRepository.saveAndFlush(subscriptionPlanOffer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptionPlanOffer using partial update
        SubscriptionPlanOffer partialUpdatedSubscriptionPlanOffer = new SubscriptionPlanOffer();
        partialUpdatedSubscriptionPlanOffer.setId(subscriptionPlanOffer.getId());

        partialUpdatedSubscriptionPlanOffer
            .freeDaysDuration(UPDATED_FREE_DAYS_DURATION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionsLimit(UPDATED_SUBSCRIPTIONS_LIMIT)
            .promotionType(UPDATED_PROMOTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restSubscriptionPlanOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionPlanOffer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptionPlanOffer))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionPlanOffer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionPlanOfferUpdatableFieldsEquals(
            partialUpdatedSubscriptionPlanOffer,
            getPersistedSubscriptionPlanOffer(partialUpdatedSubscriptionPlanOffer)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptionPlanOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionPlanOffer.setId(longCount.incrementAndGet());

        // Create the SubscriptionPlanOffer
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionPlanOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptionPlanOfferDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionPlanOfferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionPlanOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptionPlanOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionPlanOffer.setId(longCount.incrementAndGet());

        // Create the SubscriptionPlanOffer
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionPlanOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptionPlanOfferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionPlanOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptionPlanOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptionPlanOffer.setId(longCount.incrementAndGet());

        // Create the SubscriptionPlanOffer
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionPlanOfferMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subscriptionPlanOfferDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionPlanOffer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptionPlanOffer() throws Exception {
        // Initialize the database
        insertedSubscriptionPlanOffer = subscriptionPlanOfferRepository.saveAndFlush(subscriptionPlanOffer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subscriptionPlanOffer
        restSubscriptionPlanOfferMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptionPlanOffer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subscriptionPlanOfferRepository.count();
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

    protected SubscriptionPlanOffer getPersistedSubscriptionPlanOffer(SubscriptionPlanOffer subscriptionPlanOffer) {
        return subscriptionPlanOfferRepository.findById(subscriptionPlanOffer.getId()).orElseThrow();
    }

    protected void assertPersistedSubscriptionPlanOfferToMatchAllProperties(SubscriptionPlanOffer expectedSubscriptionPlanOffer) {
        assertSubscriptionPlanOfferAllPropertiesEquals(
            expectedSubscriptionPlanOffer,
            getPersistedSubscriptionPlanOffer(expectedSubscriptionPlanOffer)
        );
    }

    protected void assertPersistedSubscriptionPlanOfferToMatchUpdatableProperties(SubscriptionPlanOffer expectedSubscriptionPlanOffer) {
        assertSubscriptionPlanOfferAllUpdatablePropertiesEquals(
            expectedSubscriptionPlanOffer,
            getPersistedSubscriptionPlanOffer(expectedSubscriptionPlanOffer)
        );
    }
}
