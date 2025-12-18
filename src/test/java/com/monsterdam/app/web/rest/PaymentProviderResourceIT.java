package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.PaymentProviderAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PaymentProvider;
import com.monsterdam.app.repository.PaymentProviderRepository;
import com.monsterdam.app.service.dto.PaymentProviderDTO;
import com.monsterdam.app.service.mapper.PaymentProviderMapper;
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
 * Integration tests for the {@link PaymentProviderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentProviderResourceIT {

    private static final String DEFAULT_PROVIDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_API_KEY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_API_KEY_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_API_SECRET_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_API_SECRET_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_ENDPOINT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ENDPOINT_TEXT = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/payment-providers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentProviderRepository paymentProviderRepository;

    @Autowired
    private PaymentProviderMapper paymentProviderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentProviderMockMvc;

    private PaymentProvider paymentProvider;

    private PaymentProvider insertedPaymentProvider;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentProvider createEntity() {
        return new PaymentProvider()
            .providerName(DEFAULT_PROVIDER_NAME)
            .description(DEFAULT_DESCRIPTION)
            .apiKeyText(DEFAULT_API_KEY_TEXT)
            .apiSecretText(DEFAULT_API_SECRET_TEXT)
            .endpointText(DEFAULT_ENDPOINT_TEXT)
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
    public static PaymentProvider createUpdatedEntity() {
        return new PaymentProvider()
            .providerName(UPDATED_PROVIDER_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiKeyText(UPDATED_API_KEY_TEXT)
            .apiSecretText(UPDATED_API_SECRET_TEXT)
            .endpointText(UPDATED_ENDPOINT_TEXT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        paymentProvider = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentProvider != null) {
            paymentProviderRepository.delete(insertedPaymentProvider);
            insertedPaymentProvider = null;
        }
    }

    @Test
    @Transactional
    void createPaymentProvider() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);
        var returnedPaymentProviderDTO = om.readValue(
            restPaymentProviderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentProviderDTO.class
        );

        // Validate the PaymentProvider in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentProvider = paymentProviderMapper.toEntity(returnedPaymentProviderDTO);
        assertPaymentProviderUpdatableFieldsEquals(returnedPaymentProvider, getPersistedPaymentProvider(returnedPaymentProvider));

        insertedPaymentProvider = returnedPaymentProvider;
    }

    @Test
    @Transactional
    void createPaymentProviderWithExistingId() throws Exception {
        // Create the PaymentProvider with an existing ID
        paymentProvider.setId(1L);
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProviderNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentProvider.setProviderName(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        restPaymentProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApiKeyTextIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentProvider.setApiKeyText(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        restPaymentProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApiSecretTextIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentProvider.setApiSecretText(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        restPaymentProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndpointTextIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentProvider.setEndpointText(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        restPaymentProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentProvider.setCreatedDate(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        restPaymentProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentProviders() throws Exception {
        // Initialize the database
        insertedPaymentProvider = paymentProviderRepository.saveAndFlush(paymentProvider);

        // Get all the paymentProviderList
        restPaymentProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].providerName").value(hasItem(DEFAULT_PROVIDER_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].apiKeyText").value(hasItem(DEFAULT_API_KEY_TEXT)))
            .andExpect(jsonPath("$.[*].apiSecretText").value(hasItem(DEFAULT_API_SECRET_TEXT)))
            .andExpect(jsonPath("$.[*].endpointText").value(hasItem(DEFAULT_ENDPOINT_TEXT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPaymentProvider() throws Exception {
        // Initialize the database
        insertedPaymentProvider = paymentProviderRepository.saveAndFlush(paymentProvider);

        // Get the paymentProvider
        restPaymentProviderMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentProvider.getId().intValue()))
            .andExpect(jsonPath("$.providerName").value(DEFAULT_PROVIDER_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.apiKeyText").value(DEFAULT_API_KEY_TEXT))
            .andExpect(jsonPath("$.apiSecretText").value(DEFAULT_API_SECRET_TEXT))
            .andExpect(jsonPath("$.endpointText").value(DEFAULT_ENDPOINT_TEXT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPaymentProvider() throws Exception {
        // Get the paymentProvider
        restPaymentProviderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentProvider() throws Exception {
        // Initialize the database
        insertedPaymentProvider = paymentProviderRepository.saveAndFlush(paymentProvider);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentProvider
        PaymentProvider updatedPaymentProvider = paymentProviderRepository.findById(paymentProvider.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentProvider are not directly saved in db
        em.detach(updatedPaymentProvider);
        updatedPaymentProvider
            .providerName(UPDATED_PROVIDER_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiKeyText(UPDATED_API_KEY_TEXT)
            .apiSecretText(UPDATED_API_SECRET_TEXT)
            .endpointText(UPDATED_ENDPOINT_TEXT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(updatedPaymentProvider);

        restPaymentProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentProviderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentProviderDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentProviderToMatchAllProperties(updatedPaymentProvider);
    }

    @Test
    @Transactional
    void putNonExistingPaymentProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentProviderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentProviderWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentProvider = paymentProviderRepository.saveAndFlush(paymentProvider);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentProvider using partial update
        PaymentProvider partialUpdatedPaymentProvider = new PaymentProvider();
        partialUpdatedPaymentProvider.setId(paymentProvider.getId());

        partialUpdatedPaymentProvider
            .apiKeyText(UPDATED_API_KEY_TEXT)
            .apiSecretText(UPDATED_API_SECRET_TEXT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPaymentProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentProvider))
            )
            .andExpect(status().isOk());

        // Validate the PaymentProvider in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentProviderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentProvider, paymentProvider),
            getPersistedPaymentProvider(paymentProvider)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentProviderWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentProvider = paymentProviderRepository.saveAndFlush(paymentProvider);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentProvider using partial update
        PaymentProvider partialUpdatedPaymentProvider = new PaymentProvider();
        partialUpdatedPaymentProvider.setId(paymentProvider.getId());

        partialUpdatedPaymentProvider
            .providerName(UPDATED_PROVIDER_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiKeyText(UPDATED_API_KEY_TEXT)
            .apiSecretText(UPDATED_API_SECRET_TEXT)
            .endpointText(UPDATED_ENDPOINT_TEXT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPaymentProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentProvider))
            )
            .andExpect(status().isOk());

        // Validate the PaymentProvider in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentProviderUpdatableFieldsEquals(
            partialUpdatedPaymentProvider,
            getPersistedPaymentProvider(partialUpdatedPaymentProvider)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPaymentProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentProviderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentProviderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentProvider() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentProviderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentProvider in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentProvider() throws Exception {
        // Initialize the database
        insertedPaymentProvider = paymentProviderRepository.saveAndFlush(paymentProvider);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentProvider
        restPaymentProviderMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentProvider.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentProviderRepository.count();
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

    protected PaymentProvider getPersistedPaymentProvider(PaymentProvider paymentProvider) {
        return paymentProviderRepository.findById(paymentProvider.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentProviderToMatchAllProperties(PaymentProvider expectedPaymentProvider) {
        assertPaymentProviderAllPropertiesEquals(expectedPaymentProvider, getPersistedPaymentProvider(expectedPaymentProvider));
    }

    protected void assertPersistedPaymentProviderToMatchUpdatableProperties(PaymentProvider expectedPaymentProvider) {
        assertPaymentProviderAllUpdatablePropertiesEquals(expectedPaymentProvider, getPersistedPaymentProvider(expectedPaymentProvider));
    }
}
