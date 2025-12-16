package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.IdentityDocumentAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.IdentityDocument;
import com.monsterdam.app.domain.enumeration.DocumentStatus;
import com.monsterdam.app.domain.enumeration.DocumentType;
import com.monsterdam.app.repository.IdentityDocumentRepository;
import com.monsterdam.app.service.dto.IdentityDocumentDTO;
import com.monsterdam.app.service.mapper.IdentityDocumentMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link IdentityDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IdentityDocumentResourceIT {

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_DESCRIPTION = "BBBBBBBBBB";

    private static final DocumentStatus DEFAULT_DOCUMENT_STATUS = DocumentStatus.PENDING;
    private static final DocumentStatus UPDATED_DOCUMENT_STATUS = DocumentStatus.APPROVED;

    private static final DocumentType DEFAULT_DOCUMENT_TYPE = DocumentType.ID_VERIFICATION;
    private static final DocumentType UPDATED_DOCUMENT_TYPE = DocumentType.CONTRACT;

    private static final byte[] DEFAULT_FILE_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_DOCUMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_DOCUMENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_DOCUMENT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_FILE_DOCUMENT_S_3_KEY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/identity-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IdentityDocumentRepository identityDocumentRepository;

    @Autowired
    private IdentityDocumentMapper identityDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIdentityDocumentMockMvc;

    private IdentityDocument identityDocument;

    private IdentityDocument insertedIdentityDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentityDocument createEntity() {
        return new IdentityDocument()
            .documentName(DEFAULT_DOCUMENT_NAME)
            .documentDescription(DEFAULT_DOCUMENT_DESCRIPTION)
            .documentStatus(DEFAULT_DOCUMENT_STATUS)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .fileDocument(DEFAULT_FILE_DOCUMENT)
            .fileDocumentContentType(DEFAULT_FILE_DOCUMENT_CONTENT_TYPE)
            .fileDocumentS3Key(DEFAULT_FILE_DOCUMENT_S_3_KEY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentityDocument createUpdatedEntity() {
        return new IdentityDocument()
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentDescription(UPDATED_DOCUMENT_DESCRIPTION)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileDocument(UPDATED_FILE_DOCUMENT)
            .fileDocumentContentType(UPDATED_FILE_DOCUMENT_CONTENT_TYPE)
            .fileDocumentS3Key(UPDATED_FILE_DOCUMENT_S_3_KEY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
    }

    @BeforeEach
    void initTest() {
        identityDocument = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIdentityDocument != null) {
            identityDocumentRepository.delete(insertedIdentityDocument);
            insertedIdentityDocument = null;
        }
    }

    @Test
    @Transactional
    void createIdentityDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);
        var returnedIdentityDocumentDTO = om.readValue(
            restIdentityDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IdentityDocumentDTO.class
        );

        // Validate the IdentityDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIdentityDocument = identityDocumentMapper.toEntity(returnedIdentityDocumentDTO);
        assertIdentityDocumentUpdatableFieldsEquals(returnedIdentityDocument, getPersistedIdentityDocument(returnedIdentityDocument));

        insertedIdentityDocument = returnedIdentityDocument;
    }

    @Test
    @Transactional
    void createIdentityDocumentWithExistingId() throws Exception {
        // Create the IdentityDocument with an existing ID
        identityDocument.setId(1L);
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdentityDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        identityDocument.setDocumentName(null);

        // Create the IdentityDocument, which fails.
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        restIdentityDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileDocumentS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        identityDocument.setFileDocumentS3Key(null);

        // Create the IdentityDocument, which fails.
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        restIdentityDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        identityDocument.setCreatedDate(null);

        // Create the IdentityDocument, which fails.
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        restIdentityDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIdentityDocuments() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList
        restIdentityDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(identityDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME)))
            .andExpect(jsonPath("$.[*].documentDescription").value(hasItem(DEFAULT_DOCUMENT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].documentStatus").value(hasItem(DEFAULT_DOCUMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileDocumentContentType").value(hasItem(DEFAULT_FILE_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileDocument").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE_DOCUMENT))))
            .andExpect(jsonPath("$.[*].fileDocumentS3Key").value(hasItem(DEFAULT_FILE_DOCUMENT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getIdentityDocument() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get the identityDocument
        restIdentityDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, identityDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(identityDocument.getId().intValue()))
            .andExpect(jsonPath("$.documentName").value(DEFAULT_DOCUMENT_NAME))
            .andExpect(jsonPath("$.documentDescription").value(DEFAULT_DOCUMENT_DESCRIPTION))
            .andExpect(jsonPath("$.documentStatus").value(DEFAULT_DOCUMENT_STATUS.toString()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE.toString()))
            .andExpect(jsonPath("$.fileDocumentContentType").value(DEFAULT_FILE_DOCUMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileDocument").value(Base64.getEncoder().encodeToString(DEFAULT_FILE_DOCUMENT)))
            .andExpect(jsonPath("$.fileDocumentS3Key").value(DEFAULT_FILE_DOCUMENT_S_3_KEY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getNonExistingIdentityDocument() throws Exception {
        // Get the identityDocument
        restIdentityDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIdentityDocument() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the identityDocument
        IdentityDocument updatedIdentityDocument = identityDocumentRepository.findById(identityDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIdentityDocument are not directly saved in db
        em.detach(updatedIdentityDocument);
        updatedIdentityDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentDescription(UPDATED_DOCUMENT_DESCRIPTION)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileDocument(UPDATED_FILE_DOCUMENT)
            .fileDocumentContentType(UPDATED_FILE_DOCUMENT_CONTENT_TYPE)
            .fileDocumentS3Key(UPDATED_FILE_DOCUMENT_S_3_KEY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(updatedIdentityDocument);

        restIdentityDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, identityDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(identityDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIdentityDocumentToMatchAllProperties(updatedIdentityDocument);
    }

    @Test
    @Transactional
    void putNonExistingIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, identityDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(identityDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(identityDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIdentityDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the identityDocument using partial update
        IdentityDocument partialUpdatedIdentityDocument = new IdentityDocument();
        partialUpdatedIdentityDocument.setId(identityDocument.getId());

        partialUpdatedIdentityDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileDocument(UPDATED_FILE_DOCUMENT)
            .fileDocumentContentType(UPDATED_FILE_DOCUMENT_CONTENT_TYPE)
            .fileDocumentS3Key(UPDATED_FILE_DOCUMENT_S_3_KEY)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdentityDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdentityDocument))
            )
            .andExpect(status().isOk());

        // Validate the IdentityDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdentityDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIdentityDocument, identityDocument),
            getPersistedIdentityDocument(identityDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateIdentityDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the identityDocument using partial update
        IdentityDocument partialUpdatedIdentityDocument = new IdentityDocument();
        partialUpdatedIdentityDocument.setId(identityDocument.getId());

        partialUpdatedIdentityDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentDescription(UPDATED_DOCUMENT_DESCRIPTION)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileDocument(UPDATED_FILE_DOCUMENT)
            .fileDocumentContentType(UPDATED_FILE_DOCUMENT_CONTENT_TYPE)
            .fileDocumentS3Key(UPDATED_FILE_DOCUMENT_S_3_KEY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdentityDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdentityDocument))
            )
            .andExpect(status().isOk());

        // Validate the IdentityDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdentityDocumentUpdatableFieldsEquals(
            partialUpdatedIdentityDocument,
            getPersistedIdentityDocument(partialUpdatedIdentityDocument)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, identityDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(identityDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(identityDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIdentityDocument() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the identityDocument
        restIdentityDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, identityDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return identityDocumentRepository.count();
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

    protected IdentityDocument getPersistedIdentityDocument(IdentityDocument identityDocument) {
        return identityDocumentRepository.findById(identityDocument.getId()).orElseThrow();
    }

    protected void assertPersistedIdentityDocumentToMatchAllProperties(IdentityDocument expectedIdentityDocument) {
        assertIdentityDocumentAllPropertiesEquals(expectedIdentityDocument, getPersistedIdentityDocument(expectedIdentityDocument));
    }

    protected void assertPersistedIdentityDocumentToMatchUpdatableProperties(IdentityDocument expectedIdentityDocument) {
        assertIdentityDocumentAllUpdatablePropertiesEquals(
            expectedIdentityDocument,
            getPersistedIdentityDocument(expectedIdentityDocument)
        );
    }
}
