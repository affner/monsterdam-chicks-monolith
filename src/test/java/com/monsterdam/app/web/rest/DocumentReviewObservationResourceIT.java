package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.DocumentReviewObservationAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.DocumentReviewObservation;
import com.monsterdam.app.repository.DocumentReviewObservationRepository;
import com.monsterdam.app.service.dto.DocumentReviewObservationDTO;
import com.monsterdam.app.service.mapper.DocumentReviewObservationMapper;
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
 * Integration tests for the {@link DocumentReviewObservationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentReviewObservationResourceIT {

    private static final Instant DEFAULT_COMMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-review-observations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentReviewObservationRepository documentReviewObservationRepository;

    @Autowired
    private DocumentReviewObservationMapper documentReviewObservationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentReviewObservationMockMvc;

    private DocumentReviewObservation documentReviewObservation;

    private DocumentReviewObservation insertedDocumentReviewObservation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentReviewObservation createEntity() {
        return new DocumentReviewObservation().commentDate(DEFAULT_COMMENT_DATE).comment(DEFAULT_COMMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentReviewObservation createUpdatedEntity() {
        return new DocumentReviewObservation().commentDate(UPDATED_COMMENT_DATE).comment(UPDATED_COMMENT);
    }

    @BeforeEach
    void initTest() {
        documentReviewObservation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentReviewObservation != null) {
            documentReviewObservationRepository.delete(insertedDocumentReviewObservation);
            insertedDocumentReviewObservation = null;
        }
    }

    @Test
    @Transactional
    void createDocumentReviewObservation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);
        var returnedDocumentReviewObservationDTO = om.readValue(
            restDocumentReviewObservationMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentReviewObservationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentReviewObservationDTO.class
        );

        // Validate the DocumentReviewObservation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentReviewObservation = documentReviewObservationMapper.toEntity(returnedDocumentReviewObservationDTO);
        assertDocumentReviewObservationUpdatableFieldsEquals(
            returnedDocumentReviewObservation,
            getPersistedDocumentReviewObservation(returnedDocumentReviewObservation)
        );

        insertedDocumentReviewObservation = returnedDocumentReviewObservation;
    }

    @Test
    @Transactional
    void createDocumentReviewObservationWithExistingId() throws Exception {
        // Create the DocumentReviewObservation with an existing ID
        documentReviewObservation.setId(1L);
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentReviewObservationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentReviewObservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentReviewObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCommentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentReviewObservation.setComment(null);

        // Create the DocumentReviewObservation, which fails.
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        restDocumentReviewObservationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentReviewObservationDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentReviewObservations() throws Exception {
        // Initialize the database
        insertedDocumentReviewObservation = documentReviewObservationRepository.saveAndFlush(documentReviewObservation);

        // Get all the documentReviewObservationList
        restDocumentReviewObservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentReviewObservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].commentDate").value(hasItem(DEFAULT_COMMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getDocumentReviewObservation() throws Exception {
        // Initialize the database
        insertedDocumentReviewObservation = documentReviewObservationRepository.saveAndFlush(documentReviewObservation);

        // Get the documentReviewObservation
        restDocumentReviewObservationMockMvc
            .perform(get(ENTITY_API_URL_ID, documentReviewObservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentReviewObservation.getId().intValue()))
            .andExpect(jsonPath("$.commentDate").value(DEFAULT_COMMENT_DATE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingDocumentReviewObservation() throws Exception {
        // Get the documentReviewObservation
        restDocumentReviewObservationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentReviewObservation() throws Exception {
        // Initialize the database
        insertedDocumentReviewObservation = documentReviewObservationRepository.saveAndFlush(documentReviewObservation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentReviewObservation
        DocumentReviewObservation updatedDocumentReviewObservation = documentReviewObservationRepository
            .findById(documentReviewObservation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentReviewObservation are not directly saved in db
        em.detach(updatedDocumentReviewObservation);
        updatedDocumentReviewObservation.commentDate(UPDATED_COMMENT_DATE).comment(UPDATED_COMMENT);
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(updatedDocumentReviewObservation);

        restDocumentReviewObservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentReviewObservationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentReviewObservationDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentReviewObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentReviewObservationToMatchAllProperties(updatedDocumentReviewObservation);
    }

    @Test
    @Transactional
    void putNonExistingDocumentReviewObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentReviewObservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentReviewObservationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentReviewObservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentReviewObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentReviewObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentReviewObservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentReviewObservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentReviewObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentReviewObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentReviewObservationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentReviewObservationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentReviewObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentReviewObservationWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentReviewObservation = documentReviewObservationRepository.saveAndFlush(documentReviewObservation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentReviewObservation using partial update
        DocumentReviewObservation partialUpdatedDocumentReviewObservation = new DocumentReviewObservation();
        partialUpdatedDocumentReviewObservation.setId(documentReviewObservation.getId());

        partialUpdatedDocumentReviewObservation.commentDate(UPDATED_COMMENT_DATE).comment(UPDATED_COMMENT);

        restDocumentReviewObservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentReviewObservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentReviewObservation))
            )
            .andExpect(status().isOk());

        // Validate the DocumentReviewObservation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentReviewObservationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentReviewObservation, documentReviewObservation),
            getPersistedDocumentReviewObservation(documentReviewObservation)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentReviewObservationWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentReviewObservation = documentReviewObservationRepository.saveAndFlush(documentReviewObservation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentReviewObservation using partial update
        DocumentReviewObservation partialUpdatedDocumentReviewObservation = new DocumentReviewObservation();
        partialUpdatedDocumentReviewObservation.setId(documentReviewObservation.getId());

        partialUpdatedDocumentReviewObservation.commentDate(UPDATED_COMMENT_DATE).comment(UPDATED_COMMENT);

        restDocumentReviewObservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentReviewObservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentReviewObservation))
            )
            .andExpect(status().isOk());

        // Validate the DocumentReviewObservation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentReviewObservationUpdatableFieldsEquals(
            partialUpdatedDocumentReviewObservation,
            getPersistedDocumentReviewObservation(partialUpdatedDocumentReviewObservation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentReviewObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentReviewObservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentReviewObservationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentReviewObservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentReviewObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentReviewObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentReviewObservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentReviewObservationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentReviewObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentReviewObservation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentReviewObservationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentReviewObservationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentReviewObservation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentReviewObservation() throws Exception {
        // Initialize the database
        insertedDocumentReviewObservation = documentReviewObservationRepository.saveAndFlush(documentReviewObservation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentReviewObservation
        restDocumentReviewObservationMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentReviewObservation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentReviewObservationRepository.count();
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

    protected DocumentReviewObservation getPersistedDocumentReviewObservation(DocumentReviewObservation documentReviewObservation) {
        return documentReviewObservationRepository.findById(documentReviewObservation.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentReviewObservationToMatchAllProperties(
        DocumentReviewObservation expectedDocumentReviewObservation
    ) {
        assertDocumentReviewObservationAllPropertiesEquals(
            expectedDocumentReviewObservation,
            getPersistedDocumentReviewObservation(expectedDocumentReviewObservation)
        );
    }

    protected void assertPersistedDocumentReviewObservationToMatchUpdatableProperties(
        DocumentReviewObservation expectedDocumentReviewObservation
    ) {
        assertDocumentReviewObservationAllUpdatablePropertiesEquals(
            expectedDocumentReviewObservation,
            getPersistedDocumentReviewObservation(expectedDocumentReviewObservation)
        );
    }
}
