package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.ViewerWalletAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.monsterdam.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.ViewerWallet;
import com.monsterdam.app.domain.enumeration.ViewerWalletTransactionType;
import com.monsterdam.app.repository.ViewerWalletRepository;
import com.monsterdam.app.service.dto.ViewerWalletDTO;
import com.monsterdam.app.service.mapper.ViewerWalletMapper;
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
 * Integration tests for the {@link ViewerWalletResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ViewerWalletResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final ViewerWalletTransactionType DEFAULT_TRANSACTION_TYPE = ViewerWalletTransactionType.TOP_UP;
    private static final ViewerWalletTransactionType UPDATED_TRANSACTION_TYPE = ViewerWalletTransactionType.WITHDRAW;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Long DEFAULT_VIEWER_ID = 1L;
    private static final Long UPDATED_VIEWER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/viewer-wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ViewerWalletRepository viewerWalletRepository;

    @Autowired
    private ViewerWalletMapper viewerWalletMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restViewerWalletMockMvc;

    private ViewerWallet viewerWallet;

    private ViewerWallet insertedViewerWallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewerWallet createEntity() {
        return new ViewerWallet()
            .amount(DEFAULT_AMOUNT)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .viewerId(DEFAULT_VIEWER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewerWallet createUpdatedEntity() {
        return new ViewerWallet()
            .amount(UPDATED_AMOUNT)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID);
    }

    @BeforeEach
    void initTest() {
        viewerWallet = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedViewerWallet != null) {
            viewerWalletRepository.delete(insertedViewerWallet);
            insertedViewerWallet = null;
        }
    }

    @Test
    @Transactional
    void createViewerWallet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ViewerWallet
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);
        var returnedViewerWalletDTO = om.readValue(
            restViewerWalletMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewerWalletDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ViewerWalletDTO.class
        );

        // Validate the ViewerWallet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedViewerWallet = viewerWalletMapper.toEntity(returnedViewerWalletDTO);
        assertViewerWalletUpdatableFieldsEquals(returnedViewerWallet, getPersistedViewerWallet(returnedViewerWallet));

        insertedViewerWallet = returnedViewerWallet;
    }

    @Test
    @Transactional
    void createViewerWalletWithExistingId() throws Exception {
        // Create the ViewerWallet with an existing ID
        viewerWallet.setId(1L);
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restViewerWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewerWalletDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ViewerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        viewerWallet.setAmount(null);

        // Create the ViewerWallet, which fails.
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        restViewerWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewerWalletDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        viewerWallet.setTransactionType(null);

        // Create the ViewerWallet, which fails.
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        restViewerWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewerWalletDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        viewerWallet.setCreatedDate(null);

        // Create the ViewerWallet, which fails.
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        restViewerWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewerWalletDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        viewerWallet.setIsDeleted(null);

        // Create the ViewerWallet, which fails.
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        restViewerWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewerWalletDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkViewerIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        viewerWallet.setViewerId(null);

        // Create the ViewerWallet, which fails.
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        restViewerWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewerWalletDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllViewerWallets() throws Exception {
        // Initialize the database
        insertedViewerWallet = viewerWalletRepository.saveAndFlush(viewerWallet);

        // Get all the viewerWalletList
        restViewerWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viewerWallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)))
            .andExpect(jsonPath("$.[*].viewerId").value(hasItem(DEFAULT_VIEWER_ID.intValue())));
    }

    @Test
    @Transactional
    void getViewerWallet() throws Exception {
        // Initialize the database
        insertedViewerWallet = viewerWalletRepository.saveAndFlush(viewerWallet);

        // Get the viewerWallet
        restViewerWalletMockMvc
            .perform(get(ENTITY_API_URL_ID, viewerWallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(viewerWallet.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED))
            .andExpect(jsonPath("$.viewerId").value(DEFAULT_VIEWER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingViewerWallet() throws Exception {
        // Get the viewerWallet
        restViewerWalletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingViewerWallet() throws Exception {
        // Initialize the database
        insertedViewerWallet = viewerWalletRepository.saveAndFlush(viewerWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the viewerWallet
        ViewerWallet updatedViewerWallet = viewerWalletRepository.findById(viewerWallet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedViewerWallet are not directly saved in db
        em.detach(updatedViewerWallet);
        updatedViewerWallet
            .amount(UPDATED_AMOUNT)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID);
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(updatedViewerWallet);

        restViewerWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viewerWalletDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(viewerWalletDTO))
            )
            .andExpect(status().isOk());

        // Validate the ViewerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedViewerWalletToMatchAllProperties(updatedViewerWallet);
    }

    @Test
    @Transactional
    void putNonExistingViewerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewerWallet.setId(longCount.incrementAndGet());

        // Create the ViewerWallet
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewerWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viewerWalletDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(viewerWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchViewerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewerWallet.setId(longCount.incrementAndGet());

        // Create the ViewerWallet
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewerWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(viewerWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamViewerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewerWallet.setId(longCount.incrementAndGet());

        // Create the ViewerWallet
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewerWalletMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewerWalletDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateViewerWalletWithPatch() throws Exception {
        // Initialize the database
        insertedViewerWallet = viewerWalletRepository.saveAndFlush(viewerWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the viewerWallet using partial update
        ViewerWallet partialUpdatedViewerWallet = new ViewerWallet();
        partialUpdatedViewerWallet.setId(viewerWallet.getId());

        partialUpdatedViewerWallet.transactionType(UPDATED_TRANSACTION_TYPE).isDeleted(UPDATED_IS_DELETED).viewerId(UPDATED_VIEWER_ID);

        restViewerWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViewerWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedViewerWallet))
            )
            .andExpect(status().isOk());

        // Validate the ViewerWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertViewerWalletUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedViewerWallet, viewerWallet),
            getPersistedViewerWallet(viewerWallet)
        );
    }

    @Test
    @Transactional
    void fullUpdateViewerWalletWithPatch() throws Exception {
        // Initialize the database
        insertedViewerWallet = viewerWalletRepository.saveAndFlush(viewerWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the viewerWallet using partial update
        ViewerWallet partialUpdatedViewerWallet = new ViewerWallet();
        partialUpdatedViewerWallet.setId(viewerWallet.getId());

        partialUpdatedViewerWallet
            .amount(UPDATED_AMOUNT)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID);

        restViewerWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViewerWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedViewerWallet))
            )
            .andExpect(status().isOk());

        // Validate the ViewerWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertViewerWalletUpdatableFieldsEquals(partialUpdatedViewerWallet, getPersistedViewerWallet(partialUpdatedViewerWallet));
    }

    @Test
    @Transactional
    void patchNonExistingViewerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewerWallet.setId(longCount.incrementAndGet());

        // Create the ViewerWallet
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewerWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, viewerWalletDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(viewerWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchViewerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewerWallet.setId(longCount.incrementAndGet());

        // Create the ViewerWallet
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewerWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(viewerWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamViewerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewerWallet.setId(longCount.incrementAndGet());

        // Create the ViewerWallet
        ViewerWalletDTO viewerWalletDTO = viewerWalletMapper.toDto(viewerWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewerWalletMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(viewerWalletDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteViewerWallet() throws Exception {
        // Initialize the database
        insertedViewerWallet = viewerWalletRepository.saveAndFlush(viewerWallet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the viewerWallet
        restViewerWalletMockMvc
            .perform(delete(ENTITY_API_URL_ID, viewerWallet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return viewerWalletRepository.count();
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

    protected ViewerWallet getPersistedViewerWallet(ViewerWallet viewerWallet) {
        return viewerWalletRepository.findById(viewerWallet.getId()).orElseThrow();
    }

    protected void assertPersistedViewerWalletToMatchAllProperties(ViewerWallet expectedViewerWallet) {
        assertViewerWalletAllPropertiesEquals(expectedViewerWallet, getPersistedViewerWallet(expectedViewerWallet));
    }

    protected void assertPersistedViewerWalletToMatchUpdatableProperties(ViewerWallet expectedViewerWallet) {
        assertViewerWalletAllUpdatablePropertiesEquals(expectedViewerWallet, getPersistedViewerWallet(expectedViewerWallet));
    }
}
