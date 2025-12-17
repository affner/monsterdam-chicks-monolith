package com.monsterdam.app.web.crud;

import static com.monsterdam.app.domain.IdentityDocumentReviewAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.IdentityDocumentReview;
import com.monsterdam.app.domain.enumeration.DocumentStatus;
import com.monsterdam.app.domain.enumeration.ReviewStatus;
import com.monsterdam.app.repository.IdentityDocumentReviewRepository;
import com.monsterdam.app.service.dto.IdentityDocumentReviewDTO;
import com.monsterdam.app.service.mapper.IdentityDocumentReviewMapper;
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
 * Integration tests for the {@link IdentityDocumentReviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IdentityDocumentReviewResourceIT {

    private static final DocumentStatus DEFAULT_DOCUMENT_STATUS = DocumentStatus.PENDING;
    private static final DocumentStatus UPDATED_DOCUMENT_STATUS = DocumentStatus.APPROVED;

    private static final Instant DEFAULT_RESOLUTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESOLUTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ReviewStatus DEFAULT_REVIEW_STATUS = ReviewStatus.REVIEWING;
    private static final ReviewStatus UPDATED_REVIEW_STATUS = ReviewStatus.APPROVED;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_TICKET_ID = 1L;
    private static final Long UPDATED_TICKET_ID = 2L;

    private static final String ENTITY_API_URL = "/api/crud/identity-document-reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IdentityDocumentReviewRepository identityDocumentReviewRepository;

    @Autowired
    private IdentityDocumentReviewMapper identityDocumentReviewMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIdentityDocumentReviewMockMvc;

    private IdentityDocumentReview identityDocumentReview;

    private IdentityDocumentReview insertedIdentityDocumentReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentityDocumentReview createEntity() {
        return new IdentityDocumentReview()
            .documentStatus(DEFAULT_DOCUMENT_STATUS)
            .resolutionDate(DEFAULT_RESOLUTION_DATE)
            .reviewStatus(DEFAULT_REVIEW_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .ticketId(DEFAULT_TICKET_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentityDocumentReview createUpdatedEntity() {
        return new IdentityDocumentReview()
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .resolutionDate(UPDATED_RESOLUTION_DATE)
            .reviewStatus(UPDATED_REVIEW_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .ticketId(UPDATED_TICKET_ID);
    }

    @BeforeEach
    void initTest() {
        identityDocumentReview = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIdentityDocumentReview != null) {
            identityDocumentReviewRepository.delete(insertedIdentityDocumentReview);
            insertedIdentityDocumentReview = null;
        }
    }

    @Test
    @Transactional
    void createIdentityDocumentReview() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);
        var returnedIdentityDocumentReviewDTO = om.readValue(
            restIdentityDocumentReviewMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentReviewDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IdentityDocumentReviewDTO.class
        );

        // Validate the IdentityDocumentReview in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIdentityDocumentReview = identityDocumentReviewMapper.toEntity(returnedIdentityDocumentReviewDTO);
        assertIdentityDocumentReviewUpdatableFieldsEquals(
            returnedIdentityDocumentReview,
            getPersistedIdentityDocumentReview(returnedIdentityDocumentReview)
        );

        insertedIdentityDocumentReview = returnedIdentityDocumentReview;
    }

    @Test
    @Transactional
    void createIdentityDocumentReviewWithExistingId() throws Exception {
        // Create the IdentityDocumentReview with an existing ID
        identityDocumentReview.setId(1L);
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdentityDocumentReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentReviewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocumentReview in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        identityDocumentReview.setCreatedDate(null);

        // Create the IdentityDocumentReview, which fails.
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        restIdentityDocumentReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentReviewDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentReviews() throws Exception {
        // Initialize the database
        insertedIdentityDocumentReview = identityDocumentReviewRepository.saveAndFlush(identityDocumentReview);

        // Get all the identityDocumentReviewList
        restIdentityDocumentReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(identityDocumentReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentStatus").value(hasItem(DEFAULT_DOCUMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].resolutionDate").value(hasItem(DEFAULT_RESOLUTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].reviewStatus").value(hasItem(DEFAULT_REVIEW_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].ticketId").value(hasItem(DEFAULT_TICKET_ID.intValue())));
    }

    @Test
    @Transactional
    void getIdentityDocumentReview() throws Exception {
        // Initialize the database
        insertedIdentityDocumentReview = identityDocumentReviewRepository.saveAndFlush(identityDocumentReview);

        // Get the identityDocumentReview
        restIdentityDocumentReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, identityDocumentReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(identityDocumentReview.getId().intValue()))
            .andExpect(jsonPath("$.documentStatus").value(DEFAULT_DOCUMENT_STATUS.toString()))
            .andExpect(jsonPath("$.resolutionDate").value(DEFAULT_RESOLUTION_DATE.toString()))
            .andExpect(jsonPath("$.reviewStatus").value(DEFAULT_REVIEW_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.ticketId").value(DEFAULT_TICKET_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingIdentityDocumentReview() throws Exception {
        // Get the identityDocumentReview
        restIdentityDocumentReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIdentityDocumentReview() throws Exception {
        // Initialize the database
        insertedIdentityDocumentReview = identityDocumentReviewRepository.saveAndFlush(identityDocumentReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the identityDocumentReview
        IdentityDocumentReview updatedIdentityDocumentReview = identityDocumentReviewRepository
            .findById(identityDocumentReview.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedIdentityDocumentReview are not directly saved in db
        em.detach(updatedIdentityDocumentReview);
        updatedIdentityDocumentReview
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .resolutionDate(UPDATED_RESOLUTION_DATE)
            .reviewStatus(UPDATED_REVIEW_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .ticketId(UPDATED_TICKET_ID);
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(updatedIdentityDocumentReview);

        restIdentityDocumentReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, identityDocumentReviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(identityDocumentReviewDTO))
            )
            .andExpect(status().isOk());

        // Validate the IdentityDocumentReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIdentityDocumentReviewToMatchAllProperties(updatedIdentityDocumentReview);
    }

    @Test
    @Transactional
    void putNonExistingIdentityDocumentReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdentityDocumentReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, identityDocumentReviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(identityDocumentReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocumentReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIdentityDocumentReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(identityDocumentReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocumentReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIdentityDocumentReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentReviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdentityDocumentReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIdentityDocumentReviewWithPatch() throws Exception {
        // Initialize the database
        insertedIdentityDocumentReview = identityDocumentReviewRepository.saveAndFlush(identityDocumentReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the identityDocumentReview using partial update
        IdentityDocumentReview partialUpdatedIdentityDocumentReview = new IdentityDocumentReview();
        partialUpdatedIdentityDocumentReview.setId(identityDocumentReview.getId());

        partialUpdatedIdentityDocumentReview
            .reviewStatus(UPDATED_REVIEW_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restIdentityDocumentReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdentityDocumentReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdentityDocumentReview))
            )
            .andExpect(status().isOk());

        // Validate the IdentityDocumentReview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdentityDocumentReviewUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIdentityDocumentReview, identityDocumentReview),
            getPersistedIdentityDocumentReview(identityDocumentReview)
        );
    }

    @Test
    @Transactional
    void fullUpdateIdentityDocumentReviewWithPatch() throws Exception {
        // Initialize the database
        insertedIdentityDocumentReview = identityDocumentReviewRepository.saveAndFlush(identityDocumentReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the identityDocumentReview using partial update
        IdentityDocumentReview partialUpdatedIdentityDocumentReview = new IdentityDocumentReview();
        partialUpdatedIdentityDocumentReview.setId(identityDocumentReview.getId());

        partialUpdatedIdentityDocumentReview
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .resolutionDate(UPDATED_RESOLUTION_DATE)
            .reviewStatus(UPDATED_REVIEW_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .ticketId(UPDATED_TICKET_ID);

        restIdentityDocumentReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdentityDocumentReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdentityDocumentReview))
            )
            .andExpect(status().isOk());

        // Validate the IdentityDocumentReview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdentityDocumentReviewUpdatableFieldsEquals(
            partialUpdatedIdentityDocumentReview,
            getPersistedIdentityDocumentReview(partialUpdatedIdentityDocumentReview)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIdentityDocumentReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdentityDocumentReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, identityDocumentReviewDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(identityDocumentReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocumentReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIdentityDocumentReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(identityDocumentReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocumentReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIdentityDocumentReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentReviewMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(identityDocumentReviewDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdentityDocumentReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIdentityDocumentReview() throws Exception {
        // Initialize the database
        insertedIdentityDocumentReview = identityDocumentReviewRepository.saveAndFlush(identityDocumentReview);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the identityDocumentReview
        restIdentityDocumentReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, identityDocumentReview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return identityDocumentReviewRepository.count();
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

    protected IdentityDocumentReview getPersistedIdentityDocumentReview(IdentityDocumentReview identityDocumentReview) {
        return identityDocumentReviewRepository.findById(identityDocumentReview.getId()).orElseThrow();
    }

    protected void assertPersistedIdentityDocumentReviewToMatchAllProperties(IdentityDocumentReview expectedIdentityDocumentReview) {
        assertIdentityDocumentReviewAllPropertiesEquals(
            expectedIdentityDocumentReview,
            getPersistedIdentityDocumentReview(expectedIdentityDocumentReview)
        );
    }

    protected void assertPersistedIdentityDocumentReviewToMatchUpdatableProperties(IdentityDocumentReview expectedIdentityDocumentReview) {
        assertIdentityDocumentReviewAllUpdatablePropertiesEquals(
            expectedIdentityDocumentReview,
            getPersistedIdentityDocumentReview(expectedIdentityDocumentReview)
        );
    }
}
