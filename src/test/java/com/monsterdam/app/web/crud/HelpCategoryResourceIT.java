package com.monsterdam.app.web.crud;

import static com.monsterdam.app.domain.HelpCategoryAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.HelpCategory;
import com.monsterdam.app.repository.HelpCategoryRepository;
import com.monsterdam.app.service.dto.HelpCategoryDTO;
import com.monsterdam.app.service.mapper.HelpCategoryMapper;
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
 * Integration tests for the {@link HelpCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HelpCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/crud/help-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HelpCategoryRepository helpCategoryRepository;

    @Autowired
    private HelpCategoryMapper helpCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHelpCategoryMockMvc;

    private HelpCategory helpCategory;

    private HelpCategory insertedHelpCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpCategory createEntity() {
        return new HelpCategory()
            .name(DEFAULT_NAME)
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
    public static HelpCategory createUpdatedEntity() {
        return new HelpCategory()
            .name(UPDATED_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        helpCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHelpCategory != null) {
            helpCategoryRepository.delete(insertedHelpCategory);
            insertedHelpCategory = null;
        }
    }

    @Test
    @Transactional
    void createHelpCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);
        var returnedHelpCategoryDTO = om.readValue(
            restHelpCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HelpCategoryDTO.class
        );

        // Validate the HelpCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHelpCategory = helpCategoryMapper.toEntity(returnedHelpCategoryDTO);
        assertHelpCategoryUpdatableFieldsEquals(returnedHelpCategory, getPersistedHelpCategory(returnedHelpCategory));

        insertedHelpCategory = returnedHelpCategory;
    }

    @Test
    @Transactional
    void createHelpCategoryWithExistingId() throws Exception {
        // Create the HelpCategory with an existing ID
        helpCategory.setId(1L);
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHelpCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HelpCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        helpCategory.setName(null);

        // Create the HelpCategory, which fails.
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        restHelpCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        helpCategory.setCreatedDate(null);

        // Create the HelpCategory, which fails.
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        restHelpCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHelpCategories() throws Exception {
        // Initialize the database
        insertedHelpCategory = helpCategoryRepository.saveAndFlush(helpCategory);

        // Get all the helpCategoryList
        restHelpCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getHelpCategory() throws Exception {
        // Initialize the database
        insertedHelpCategory = helpCategoryRepository.saveAndFlush(helpCategory);

        // Get the helpCategory
        restHelpCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, helpCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(helpCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHelpCategory() throws Exception {
        // Get the helpCategory
        restHelpCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHelpCategory() throws Exception {
        // Initialize the database
        insertedHelpCategory = helpCategoryRepository.saveAndFlush(helpCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpCategory
        HelpCategory updatedHelpCategory = helpCategoryRepository.findById(helpCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHelpCategory are not directly saved in db
        em.detach(updatedHelpCategory);
        updatedHelpCategory
            .name(UPDATED_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(updatedHelpCategory);

        restHelpCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the HelpCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHelpCategoryToMatchAllProperties(updatedHelpCategory);
    }

    @Test
    @Transactional
    void putNonExistingHelpCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHelpCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHelpCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHelpCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedHelpCategory = helpCategoryRepository.saveAndFlush(helpCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpCategory using partial update
        HelpCategory partialUpdatedHelpCategory = new HelpCategory();
        partialUpdatedHelpCategory.setId(helpCategory.getId());

        partialUpdatedHelpCategory.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restHelpCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHelpCategory))
            )
            .andExpect(status().isOk());

        // Validate the HelpCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHelpCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHelpCategory, helpCategory),
            getPersistedHelpCategory(helpCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateHelpCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedHelpCategory = helpCategoryRepository.saveAndFlush(helpCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpCategory using partial update
        HelpCategory partialUpdatedHelpCategory = new HelpCategory();
        partialUpdatedHelpCategory.setId(helpCategory.getId());

        partialUpdatedHelpCategory
            .name(UPDATED_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restHelpCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHelpCategory))
            )
            .andExpect(status().isOk());

        // Validate the HelpCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHelpCategoryUpdatableFieldsEquals(partialUpdatedHelpCategory, getPersistedHelpCategory(partialUpdatedHelpCategory));
    }

    @Test
    @Transactional
    void patchNonExistingHelpCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, helpCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(helpCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHelpCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(helpCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHelpCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(helpCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHelpCategory() throws Exception {
        // Initialize the database
        insertedHelpCategory = helpCategoryRepository.saveAndFlush(helpCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the helpCategory
        restHelpCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, helpCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return helpCategoryRepository.count();
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

    protected HelpCategory getPersistedHelpCategory(HelpCategory helpCategory) {
        return helpCategoryRepository.findById(helpCategory.getId()).orElseThrow();
    }

    protected void assertPersistedHelpCategoryToMatchAllProperties(HelpCategory expectedHelpCategory) {
        assertHelpCategoryAllPropertiesEquals(expectedHelpCategory, getPersistedHelpCategory(expectedHelpCategory));
    }

    protected void assertPersistedHelpCategoryToMatchUpdatableProperties(HelpCategory expectedHelpCategory) {
        assertHelpCategoryAllUpdatablePropertiesEquals(expectedHelpCategory, getPersistedHelpCategory(expectedHelpCategory));
    }
}
