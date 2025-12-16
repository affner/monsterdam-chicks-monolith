package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.PaymentAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.monsterdam.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.Payment;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.enumeration.GenericStatus;
import com.monsterdam.app.repository.PaymentRepository;
import com.monsterdam.app.service.dto.PaymentDTO;
import com.monsterdam.app.service.mapper.PaymentMapper;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final GenericStatus DEFAULT_PAYMENT_STATUS = GenericStatus.PENDING;
    private static final GenericStatus UPDATED_PAYMENT_STATUS = GenericStatus.COMPLETED;

    private static final String DEFAULT_PAYMENT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_CLOUD_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLOUD_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER_PAYMENT_INTENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_PAYMENT_INTENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER_CHARGE_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_CHARGE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER_PAYMENT_METHOD_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_PAYMENT_METHOD_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER_EVENT_LAST_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_EVENT_LAST_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_CODE = "AA";
    private static final String UPDATED_COUNTRY_CODE = "BB";

    private static final BigDecimal DEFAULT_PROVIDER_FEE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PROVIDER_FEE_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PLATFORM_FEE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PLATFORM_FEE_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CREATOR_NET_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREATOR_NET_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAX_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_AUTHORIZED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_AUTHORIZED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CAPTURED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CAPTURED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    private Payment insertedPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .paymentReference(DEFAULT_PAYMENT_REFERENCE)
            .cloudTransactionId(DEFAULT_CLOUD_TRANSACTION_ID)
            .providerPaymentIntentId(DEFAULT_PROVIDER_PAYMENT_INTENT_ID)
            .providerChargeId(DEFAULT_PROVIDER_CHARGE_ID)
            .providerCustomerId(DEFAULT_PROVIDER_CUSTOMER_ID)
            .providerPaymentMethodId(DEFAULT_PROVIDER_PAYMENT_METHOD_ID)
            .providerEventLastId(DEFAULT_PROVIDER_EVENT_LAST_ID)
            .countryCode(DEFAULT_COUNTRY_CODE)
            .providerFeeAmount(DEFAULT_PROVIDER_FEE_AMOUNT)
            .platformFeeAmount(DEFAULT_PLATFORM_FEE_AMOUNT)
            .creatorNetAmount(DEFAULT_CREATOR_NET_AMOUNT)
            .taxAmount(DEFAULT_TAX_AMOUNT)
            .authorizedDate(DEFAULT_AUTHORIZED_DATE)
            .capturedDate(DEFAULT_CAPTURED_DATE);
        // Add required entity
        UserLite userLite;
        if (TestUtil.findAll(em, UserLite.class).isEmpty()) {
            userLite = UserLiteResourceIT.createEntity();
            em.persist(userLite);
            em.flush();
        } else {
            userLite = TestUtil.findAll(em, UserLite.class).get(0);
        }
        payment.setViewer(userLite);
        return payment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment updatedPayment = new Payment()
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .cloudTransactionId(UPDATED_CLOUD_TRANSACTION_ID)
            .providerPaymentIntentId(UPDATED_PROVIDER_PAYMENT_INTENT_ID)
            .providerChargeId(UPDATED_PROVIDER_CHARGE_ID)
            .providerCustomerId(UPDATED_PROVIDER_CUSTOMER_ID)
            .providerPaymentMethodId(UPDATED_PROVIDER_PAYMENT_METHOD_ID)
            .providerEventLastId(UPDATED_PROVIDER_EVENT_LAST_ID)
            .countryCode(UPDATED_COUNTRY_CODE)
            .providerFeeAmount(UPDATED_PROVIDER_FEE_AMOUNT)
            .platformFeeAmount(UPDATED_PLATFORM_FEE_AMOUNT)
            .creatorNetAmount(UPDATED_CREATOR_NET_AMOUNT)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .authorizedDate(UPDATED_AUTHORIZED_DATE)
            .capturedDate(UPDATED_CAPTURED_DATE);
        // Add required entity
        UserLite userLite;
        if (TestUtil.findAll(em, UserLite.class).isEmpty()) {
            userLite = UserLiteResourceIT.createUpdatedEntity();
            em.persist(userLite);
            em.flush();
        } else {
            userLite = TestUtil.findAll(em, UserLite.class).get(0);
        }
        updatedPayment.setViewer(userLite);
        return updatedPayment;
    }

    @BeforeEach
    void initTest() {
        payment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPayment != null) {
            paymentRepository.delete(insertedPayment);
            insertedPayment = null;
        }
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        var returnedPaymentDTO = om.readValue(
            restPaymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentDTO.class
        );

        // Validate the Payment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPayment = paymentMapper.toEntity(returnedPaymentDTO);
        assertPaymentUpdatableFieldsEquals(returnedPayment, getPersistedPayment(returnedPayment));

        insertedPayment = returnedPayment;
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setAmount(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setCurrency(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setPaymentDate(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setPaymentStatus(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlatformFeeAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setPlatformFeeAmount(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatorNetAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setCreatorNetAmount(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentReference").value(hasItem(DEFAULT_PAYMENT_REFERENCE)))
            .andExpect(jsonPath("$.[*].cloudTransactionId").value(hasItem(DEFAULT_CLOUD_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].providerPaymentIntentId").value(hasItem(DEFAULT_PROVIDER_PAYMENT_INTENT_ID)))
            .andExpect(jsonPath("$.[*].providerChargeId").value(hasItem(DEFAULT_PROVIDER_CHARGE_ID)))
            .andExpect(jsonPath("$.[*].providerCustomerId").value(hasItem(DEFAULT_PROVIDER_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].providerPaymentMethodId").value(hasItem(DEFAULT_PROVIDER_PAYMENT_METHOD_ID)))
            .andExpect(jsonPath("$.[*].providerEventLastId").value(hasItem(DEFAULT_PROVIDER_EVENT_LAST_ID)))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].providerFeeAmount").value(hasItem(sameNumber(DEFAULT_PROVIDER_FEE_AMOUNT))))
            .andExpect(jsonPath("$.[*].platformFeeAmount").value(hasItem(sameNumber(DEFAULT_PLATFORM_FEE_AMOUNT))))
            .andExpect(jsonPath("$.[*].creatorNetAmount").value(hasItem(sameNumber(DEFAULT_CREATOR_NET_AMOUNT))))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(sameNumber(DEFAULT_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].authorizedDate").value(hasItem(DEFAULT_AUTHORIZED_DATE.toString())))
            .andExpect(jsonPath("$.[*].capturedDate").value(hasItem(DEFAULT_CAPTURED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentReference").value(DEFAULT_PAYMENT_REFERENCE))
            .andExpect(jsonPath("$.cloudTransactionId").value(DEFAULT_CLOUD_TRANSACTION_ID))
            .andExpect(jsonPath("$.providerPaymentIntentId").value(DEFAULT_PROVIDER_PAYMENT_INTENT_ID))
            .andExpect(jsonPath("$.providerChargeId").value(DEFAULT_PROVIDER_CHARGE_ID))
            .andExpect(jsonPath("$.providerCustomerId").value(DEFAULT_PROVIDER_CUSTOMER_ID))
            .andExpect(jsonPath("$.providerPaymentMethodId").value(DEFAULT_PROVIDER_PAYMENT_METHOD_ID))
            .andExpect(jsonPath("$.providerEventLastId").value(DEFAULT_PROVIDER_EVENT_LAST_ID))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE))
            .andExpect(jsonPath("$.providerFeeAmount").value(sameNumber(DEFAULT_PROVIDER_FEE_AMOUNT)))
            .andExpect(jsonPath("$.platformFeeAmount").value(sameNumber(DEFAULT_PLATFORM_FEE_AMOUNT)))
            .andExpect(jsonPath("$.creatorNetAmount").value(sameNumber(DEFAULT_CREATOR_NET_AMOUNT)))
            .andExpect(jsonPath("$.taxAmount").value(sameNumber(DEFAULT_TAX_AMOUNT)))
            .andExpect(jsonPath("$.authorizedDate").value(DEFAULT_AUTHORIZED_DATE.toString()))
            .andExpect(jsonPath("$.capturedDate").value(DEFAULT_CAPTURED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .cloudTransactionId(UPDATED_CLOUD_TRANSACTION_ID)
            .providerPaymentIntentId(UPDATED_PROVIDER_PAYMENT_INTENT_ID)
            .providerChargeId(UPDATED_PROVIDER_CHARGE_ID)
            .providerCustomerId(UPDATED_PROVIDER_CUSTOMER_ID)
            .providerPaymentMethodId(UPDATED_PROVIDER_PAYMENT_METHOD_ID)
            .providerEventLastId(UPDATED_PROVIDER_EVENT_LAST_ID)
            .countryCode(UPDATED_COUNTRY_CODE)
            .providerFeeAmount(UPDATED_PROVIDER_FEE_AMOUNT)
            .platformFeeAmount(UPDATED_PLATFORM_FEE_AMOUNT)
            .creatorNetAmount(UPDATED_CREATOR_NET_AMOUNT)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .authorizedDate(UPDATED_AUTHORIZED_DATE)
            .capturedDate(UPDATED_CAPTURED_DATE);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentToMatchAllProperties(updatedPayment);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .providerPaymentIntentId(UPDATED_PROVIDER_PAYMENT_INTENT_ID)
            .providerPaymentMethodId(UPDATED_PROVIDER_PAYMENT_METHOD_ID)
            .countryCode(UPDATED_COUNTRY_CODE)
            .platformFeeAmount(UPDATED_PLATFORM_FEE_AMOUNT)
            .authorizedDate(UPDATED_AUTHORIZED_DATE);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPayment, payment), getPersistedPayment(payment));
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .cloudTransactionId(UPDATED_CLOUD_TRANSACTION_ID)
            .providerPaymentIntentId(UPDATED_PROVIDER_PAYMENT_INTENT_ID)
            .providerChargeId(UPDATED_PROVIDER_CHARGE_ID)
            .providerCustomerId(UPDATED_PROVIDER_CUSTOMER_ID)
            .providerPaymentMethodId(UPDATED_PROVIDER_PAYMENT_METHOD_ID)
            .providerEventLastId(UPDATED_PROVIDER_EVENT_LAST_ID)
            .countryCode(UPDATED_COUNTRY_CODE)
            .providerFeeAmount(UPDATED_PROVIDER_FEE_AMOUNT)
            .platformFeeAmount(UPDATED_PLATFORM_FEE_AMOUNT)
            .creatorNetAmount(UPDATED_CREATOR_NET_AMOUNT)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .authorizedDate(UPDATED_AUTHORIZED_DATE)
            .capturedDate(UPDATED_CAPTURED_DATE);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(partialUpdatedPayment, getPersistedPayment(partialUpdatedPayment));
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentRepository.count();
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

    protected Payment getPersistedPayment(Payment payment) {
        return paymentRepository.findById(payment.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentToMatchAllProperties(Payment expectedPayment) {
        assertPaymentAllPropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }

    protected void assertPersistedPaymentToMatchUpdatableProperties(Payment expectedPayment) {
        assertPaymentAllUpdatablePropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }
}
