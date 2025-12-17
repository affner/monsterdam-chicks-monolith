package com.monsterdam.app.web.crud;

import static com.monsterdam.app.domain.PaymentProviderEventAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PaymentProviderEvent;
import com.monsterdam.app.domain.enumeration.GenericStatus;
import com.monsterdam.app.repository.PaymentProviderEventRepository;
import com.monsterdam.app.service.dto.PaymentProviderEventDTO;
import com.monsterdam.app.service.mapper.PaymentProviderEventMapper;
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
 * Integration tests for the {@link PaymentProviderEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentProviderEventResourceIT {

    private static final String DEFAULT_PROVIDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PAYLOAD_JSON = "AAAAAAAAAA";
    private static final String UPDATED_PAYLOAD_JSON = "BBBBBBBBBB";

    private static final Instant DEFAULT_RECEIVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PROCESSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final GenericStatus DEFAULT_PROCESSING_STATUS = GenericStatus.PENDING;
    private static final GenericStatus UPDATED_PROCESSING_STATUS = GenericStatus.COMPLETED;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELETED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/crud/payment-provider-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentProviderEventRepository paymentProviderEventRepository;

    @Autowired
    private PaymentProviderEventMapper paymentProviderEventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentProviderEventMockMvc;

    private PaymentProviderEvent paymentProviderEvent;

    private PaymentProviderEvent insertedPaymentProviderEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentProviderEvent createEntity() {
        return new PaymentProviderEvent()
            .providerName(DEFAULT_PROVIDER_NAME)
            .eventType(DEFAULT_EVENT_TYPE)
            .eventId(DEFAULT_EVENT_ID)
            .payloadJson(DEFAULT_PAYLOAD_JSON)
            .receivedAt(DEFAULT_RECEIVED_AT)
            .processedAt(DEFAULT_PROCESSED_AT)
            .processingStatus(DEFAULT_PROCESSING_STATUS)
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
    public static PaymentProviderEvent createUpdatedEntity() {
        return new PaymentProviderEvent()
            .providerName(UPDATED_PROVIDER_NAME)
            .eventType(UPDATED_EVENT_TYPE)
            .eventId(UPDATED_EVENT_ID)
            .payloadJson(UPDATED_PAYLOAD_JSON)
            .receivedAt(UPDATED_RECEIVED_AT)
            .processedAt(UPDATED_PROCESSED_AT)
            .processingStatus(UPDATED_PROCESSING_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        paymentProviderEvent = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentProviderEvent != null) {
            paymentProviderEventRepository.delete(insertedPaymentProviderEvent);
            insertedPaymentProviderEvent = null;
        }
    }

    @Test
    @Transactional
    void createPaymentProviderEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentProviderEvent
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);
        var returnedPaymentProviderEventDTO = om.readValue(
            restPaymentProviderEventMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderEventDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentProviderEventDTO.class
        );

        // Validate the PaymentProviderEvent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentProviderEvent = paymentProviderEventMapper.toEntity(returnedPaymentProviderEventDTO);
        assertPaymentProviderEventUpdatableFieldsEquals(
            returnedPaymentProviderEvent,
            getPersistedPaymentProviderEvent(returnedPaymentProviderEvent)
        );

        insertedPaymentProviderEvent = returnedPaymentProviderEvent;
    }

    @Test
    @Transactional
    void createPaymentProviderEventWithExistingId() throws Exception {
        // Create the PaymentProviderEvent with an existing ID
        paymentProviderEvent.setId(1L);
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentProviderEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentProviderEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProviderNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentProviderEvent.setProviderName(null);

        // Create the PaymentProviderEvent, which fails.
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        restPaymentProviderEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentProviderEvent.setEventType(null);

        // Create the PaymentProviderEvent, which fails.
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        restPaymentProviderEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentProviderEvent.setEventId(null);

        // Create the PaymentProviderEvent, which fails.
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        restPaymentProviderEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceivedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentProviderEvent.setReceivedAt(null);

        // Create the PaymentProviderEvent, which fails.
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        restPaymentProviderEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProcessingStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentProviderEvent.setProcessingStatus(null);

        // Create the PaymentProviderEvent, which fails.
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        restPaymentProviderEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentProviderEvents() throws Exception {
        // Initialize the database
        insertedPaymentProviderEvent = paymentProviderEventRepository.saveAndFlush(paymentProviderEvent);

        // Get all the paymentProviderEventList
        restPaymentProviderEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentProviderEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].providerName").value(hasItem(DEFAULT_PROVIDER_NAME)))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID)))
            .andExpect(jsonPath("$.[*].payloadJson").value(hasItem(DEFAULT_PAYLOAD_JSON)))
            .andExpect(jsonPath("$.[*].receivedAt").value(hasItem(DEFAULT_RECEIVED_AT.toString())))
            .andExpect(jsonPath("$.[*].processedAt").value(hasItem(DEFAULT_PROCESSED_AT.toString())))
            .andExpect(jsonPath("$.[*].processingStatus").value(hasItem(DEFAULT_PROCESSING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPaymentProviderEvent() throws Exception {
        // Initialize the database
        insertedPaymentProviderEvent = paymentProviderEventRepository.saveAndFlush(paymentProviderEvent);

        // Get the paymentProviderEvent
        restPaymentProviderEventMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentProviderEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentProviderEvent.getId().intValue()))
            .andExpect(jsonPath("$.providerName").value(DEFAULT_PROVIDER_NAME))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID))
            .andExpect(jsonPath("$.payloadJson").value(DEFAULT_PAYLOAD_JSON))
            .andExpect(jsonPath("$.receivedAt").value(DEFAULT_RECEIVED_AT.toString()))
            .andExpect(jsonPath("$.processedAt").value(DEFAULT_PROCESSED_AT.toString()))
            .andExpect(jsonPath("$.processingStatus").value(DEFAULT_PROCESSING_STATUS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPaymentProviderEvent() throws Exception {
        // Get the paymentProviderEvent
        restPaymentProviderEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentProviderEvent() throws Exception {
        // Initialize the database
        insertedPaymentProviderEvent = paymentProviderEventRepository.saveAndFlush(paymentProviderEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentProviderEvent
        PaymentProviderEvent updatedPaymentProviderEvent = paymentProviderEventRepository
            .findById(paymentProviderEvent.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentProviderEvent are not directly saved in db
        em.detach(updatedPaymentProviderEvent);
        updatedPaymentProviderEvent
            .providerName(UPDATED_PROVIDER_NAME)
            .eventType(UPDATED_EVENT_TYPE)
            .eventId(UPDATED_EVENT_ID)
            .payloadJson(UPDATED_PAYLOAD_JSON)
            .receivedAt(UPDATED_RECEIVED_AT)
            .processedAt(UPDATED_PROCESSED_AT)
            .processingStatus(UPDATED_PROCESSING_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(updatedPaymentProviderEvent);

        restPaymentProviderEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentProviderEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentProviderEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentProviderEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentProviderEventToMatchAllProperties(updatedPaymentProviderEvent);
    }

    @Test
    @Transactional
    void putNonExistingPaymentProviderEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProviderEvent.setId(longCount.incrementAndGet());

        // Create the PaymentProviderEvent
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentProviderEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentProviderEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentProviderEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProviderEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentProviderEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProviderEvent.setId(longCount.incrementAndGet());

        // Create the PaymentProviderEvent
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentProviderEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProviderEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentProviderEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProviderEvent.setId(longCount.incrementAndGet());

        // Create the PaymentProviderEvent
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentProviderEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentProviderEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentProviderEventWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentProviderEvent = paymentProviderEventRepository.saveAndFlush(paymentProviderEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentProviderEvent using partial update
        PaymentProviderEvent partialUpdatedPaymentProviderEvent = new PaymentProviderEvent();
        partialUpdatedPaymentProviderEvent.setId(paymentProviderEvent.getId());

        partialUpdatedPaymentProviderEvent
            .providerName(UPDATED_PROVIDER_NAME)
            .eventType(UPDATED_EVENT_TYPE)
            .payloadJson(UPDATED_PAYLOAD_JSON)
            .receivedAt(UPDATED_RECEIVED_AT)
            .processingStatus(UPDATED_PROCESSING_STATUS)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPaymentProviderEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentProviderEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentProviderEvent))
            )
            .andExpect(status().isOk());

        // Validate the PaymentProviderEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentProviderEventUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentProviderEvent, paymentProviderEvent),
            getPersistedPaymentProviderEvent(paymentProviderEvent)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentProviderEventWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentProviderEvent = paymentProviderEventRepository.saveAndFlush(paymentProviderEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentProviderEvent using partial update
        PaymentProviderEvent partialUpdatedPaymentProviderEvent = new PaymentProviderEvent();
        partialUpdatedPaymentProviderEvent.setId(paymentProviderEvent.getId());

        partialUpdatedPaymentProviderEvent
            .providerName(UPDATED_PROVIDER_NAME)
            .eventType(UPDATED_EVENT_TYPE)
            .eventId(UPDATED_EVENT_ID)
            .payloadJson(UPDATED_PAYLOAD_JSON)
            .receivedAt(UPDATED_RECEIVED_AT)
            .processedAt(UPDATED_PROCESSED_AT)
            .processingStatus(UPDATED_PROCESSING_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPaymentProviderEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentProviderEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentProviderEvent))
            )
            .andExpect(status().isOk());

        // Validate the PaymentProviderEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentProviderEventUpdatableFieldsEquals(
            partialUpdatedPaymentProviderEvent,
            getPersistedPaymentProviderEvent(partialUpdatedPaymentProviderEvent)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPaymentProviderEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProviderEvent.setId(longCount.incrementAndGet());

        // Create the PaymentProviderEvent
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentProviderEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentProviderEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentProviderEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProviderEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentProviderEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProviderEvent.setId(longCount.incrementAndGet());

        // Create the PaymentProviderEvent
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentProviderEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentProviderEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentProviderEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentProviderEvent.setId(longCount.incrementAndGet());

        // Create the PaymentProviderEvent
        PaymentProviderEventDTO paymentProviderEventDTO = paymentProviderEventMapper.toDto(paymentProviderEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentProviderEventMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentProviderEventDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentProviderEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentProviderEvent() throws Exception {
        // Initialize the database
        insertedPaymentProviderEvent = paymentProviderEventRepository.saveAndFlush(paymentProviderEvent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentProviderEvent
        restPaymentProviderEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentProviderEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentProviderEventRepository.count();
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

    protected PaymentProviderEvent getPersistedPaymentProviderEvent(PaymentProviderEvent paymentProviderEvent) {
        return paymentProviderEventRepository.findById(paymentProviderEvent.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentProviderEventToMatchAllProperties(PaymentProviderEvent expectedPaymentProviderEvent) {
        assertPaymentProviderEventAllPropertiesEquals(
            expectedPaymentProviderEvent,
            getPersistedPaymentProviderEvent(expectedPaymentProviderEvent)
        );
    }

    protected void assertPersistedPaymentProviderEventToMatchUpdatableProperties(PaymentProviderEvent expectedPaymentProviderEvent) {
        assertPaymentProviderEventAllUpdatablePropertiesEquals(
            expectedPaymentProviderEvent,
            getPersistedPaymentProviderEvent(expectedPaymentProviderEvent)
        );
    }
}
