package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.PurchasedContentAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.domain.PurchasedContent;
import com.monsterdam.app.repository.PurchasedContentRepository;
import com.monsterdam.app.service.dto.PurchasedContentDTO;
import com.monsterdam.app.service.mapper.PurchasedContentMapper;
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
 * Integration tests for the {@link PurchasedContentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchasedContentResourceIT {

    private static final Float DEFAULT_RATING = 1F;
    private static final Float UPDATED_RATING = 2F;

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

    private static final String ENTITY_API_URL = "/api/purchased-contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PurchasedContentRepository purchasedContentRepository;

    @Autowired
    private PurchasedContentMapper purchasedContentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchasedContentMockMvc;

    private PurchasedContent purchasedContent;

    private PurchasedContent insertedPurchasedContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedContent createEntity(EntityManager em) {
        PurchasedContent purchasedContent = new PurchasedContent()
            .rating(DEFAULT_RATING)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .deletedDate(DEFAULT_DELETED_DATE);
        // Add required entity
        ContentPackage contentPackage;
        if (TestUtil.findAll(em, ContentPackage.class).isEmpty()) {
            contentPackage = ContentPackageResourceIT.createEntity();
            em.persist(contentPackage);
            em.flush();
        } else {
            contentPackage = TestUtil.findAll(em, ContentPackage.class).get(0);
        }
        purchasedContent.setContentPackage(contentPackage);
        return purchasedContent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedContent createUpdatedEntity(EntityManager em) {
        PurchasedContent updatedPurchasedContent = new PurchasedContent()
            .rating(UPDATED_RATING)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        // Add required entity
        ContentPackage contentPackage;
        if (TestUtil.findAll(em, ContentPackage.class).isEmpty()) {
            contentPackage = ContentPackageResourceIT.createUpdatedEntity();
            em.persist(contentPackage);
            em.flush();
        } else {
            contentPackage = TestUtil.findAll(em, ContentPackage.class).get(0);
        }
        updatedPurchasedContent.setContentPackage(contentPackage);
        return updatedPurchasedContent;
    }

    @BeforeEach
    void initTest() {
        purchasedContent = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPurchasedContent != null) {
            purchasedContentRepository.delete(insertedPurchasedContent);
            insertedPurchasedContent = null;
        }
    }

    @Test
    @Transactional
    void createPurchasedContent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);
        var returnedPurchasedContentDTO = om.readValue(
            restPurchasedContentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedContentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PurchasedContentDTO.class
        );

        // Validate the PurchasedContent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPurchasedContent = purchasedContentMapper.toEntity(returnedPurchasedContentDTO);
        assertPurchasedContentUpdatableFieldsEquals(returnedPurchasedContent, getPersistedPurchasedContent(returnedPurchasedContent));

        insertedPurchasedContent = returnedPurchasedContent;
    }

    @Test
    @Transactional
    void createPurchasedContentWithExistingId() throws Exception {
        // Create the PurchasedContent with an existing ID
        purchasedContent.setId(1L);
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchasedContentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedContentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchasedContent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchasedContent.setCreatedDate(null);

        // Create the PurchasedContent, which fails.
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        restPurchasedContentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedContentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchasedContents() throws Exception {
        // Initialize the database
        insertedPurchasedContent = purchasedContentRepository.saveAndFlush(purchasedContent);

        // Get all the purchasedContentList
        restPurchasedContentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasedContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPurchasedContent() throws Exception {
        // Initialize the database
        insertedPurchasedContent = purchasedContentRepository.saveAndFlush(purchasedContent);

        // Get the purchasedContent
        restPurchasedContentMockMvc
            .perform(get(ENTITY_API_URL_ID, purchasedContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchasedContent.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPurchasedContent() throws Exception {
        // Get the purchasedContent
        restPurchasedContentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchasedContent() throws Exception {
        // Initialize the database
        insertedPurchasedContent = purchasedContentRepository.saveAndFlush(purchasedContent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedContent
        PurchasedContent updatedPurchasedContent = purchasedContentRepository.findById(purchasedContent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPurchasedContent are not directly saved in db
        em.detach(updatedPurchasedContent);
        updatedPurchasedContent
            .rating(UPDATED_RATING)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(updatedPurchasedContent);

        restPurchasedContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedContentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchasedContentDTO))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPurchasedContentToMatchAllProperties(updatedPurchasedContent);
    }

    @Test
    @Transactional
    void putNonExistingPurchasedContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchasedContentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchasedContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchasedContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchasedContentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchasedContentWithPatch() throws Exception {
        // Initialize the database
        insertedPurchasedContent = purchasedContentRepository.saveAndFlush(purchasedContent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedContent using partial update
        PurchasedContent partialUpdatedPurchasedContent = new PurchasedContent();
        partialUpdatedPurchasedContent.setId(purchasedContent.getId());

        partialUpdatedPurchasedContent.deletedDate(UPDATED_DELETED_DATE);

        restPurchasedContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchasedContent))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedContent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchasedContentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPurchasedContent, purchasedContent),
            getPersistedPurchasedContent(purchasedContent)
        );
    }

    @Test
    @Transactional
    void fullUpdatePurchasedContentWithPatch() throws Exception {
        // Initialize the database
        insertedPurchasedContent = purchasedContentRepository.saveAndFlush(purchasedContent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchasedContent using partial update
        PurchasedContent partialUpdatedPurchasedContent = new PurchasedContent();
        partialUpdatedPurchasedContent.setId(purchasedContent.getId());

        partialUpdatedPurchasedContent
            .rating(UPDATED_RATING)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPurchasedContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchasedContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchasedContent))
            )
            .andExpect(status().isOk());

        // Validate the PurchasedContent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchasedContentUpdatableFieldsEquals(
            partialUpdatedPurchasedContent,
            getPersistedPurchasedContent(partialUpdatedPurchasedContent)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPurchasedContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchasedContentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchasedContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchasedContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchasedContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchasedContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchasedContentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(purchasedContentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchasedContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchasedContent() throws Exception {
        // Initialize the database
        insertedPurchasedContent = purchasedContentRepository.saveAndFlush(purchasedContent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the purchasedContent
        restPurchasedContentMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchasedContent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return purchasedContentRepository.count();
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

    protected PurchasedContent getPersistedPurchasedContent(PurchasedContent purchasedContent) {
        return purchasedContentRepository.findById(purchasedContent.getId()).orElseThrow();
    }

    protected void assertPersistedPurchasedContentToMatchAllProperties(PurchasedContent expectedPurchasedContent) {
        assertPurchasedContentAllPropertiesEquals(expectedPurchasedContent, getPersistedPurchasedContent(expectedPurchasedContent));
    }

    protected void assertPersistedPurchasedContentToMatchUpdatableProperties(PurchasedContent expectedPurchasedContent) {
        assertPurchasedContentAllUpdatablePropertiesEquals(
            expectedPurchasedContent,
            getPersistedPurchasedContent(expectedPurchasedContent)
        );
    }
}
