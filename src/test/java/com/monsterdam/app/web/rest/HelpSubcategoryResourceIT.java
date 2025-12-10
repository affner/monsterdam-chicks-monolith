package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.HelpSubcategoryAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.HelpSubcategory;
import com.monsterdam.app.repository.HelpSubcategoryRepository;
import com.monsterdam.app.service.dto.HelpSubcategoryDTO;
import com.monsterdam.app.service.mapper.HelpSubcategoryMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link HelpSubcategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HelpSubcategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/help-subcategories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HelpSubcategoryRepository helpSubcategoryRepository;

    @Autowired
    private HelpSubcategoryMapper helpSubcategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHelpSubcategoryMockMvc;

    private HelpSubcategory helpSubcategory;

    private HelpSubcategory insertedHelpSubcategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpSubcategory createEntity() {
        return new HelpSubcategory().name(DEFAULT_NAME).isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpSubcategory createUpdatedEntity() {
        return new HelpSubcategory().name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    void initTest() {
        helpSubcategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHelpSubcategory != null) {
            helpSubcategoryRepository.delete(insertedHelpSubcategory);
            insertedHelpSubcategory = null;
        }
    }

    @Test
    @Transactional
    void createHelpSubcategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);
        var returnedHelpSubcategoryDTO = om.readValue(
            restHelpSubcategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpSubcategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HelpSubcategoryDTO.class
        );

        // Validate the HelpSubcategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHelpSubcategory = helpSubcategoryMapper.toEntity(returnedHelpSubcategoryDTO);
        assertHelpSubcategoryUpdatableFieldsEquals(returnedHelpSubcategory, getPersistedHelpSubcategory(returnedHelpSubcategory));

        insertedHelpSubcategory = returnedHelpSubcategory;
    }

    @Test
    @Transactional
    void createHelpSubcategoryWithExistingId() throws Exception {
        // Create the HelpSubcategory with an existing ID
        helpSubcategory.setId(1L);
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHelpSubcategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpSubcategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HelpSubcategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        helpSubcategory.setName(null);

        // Create the HelpSubcategory, which fails.
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        restHelpSubcategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpSubcategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        helpSubcategory.setIsDeleted(null);

        // Create the HelpSubcategory, which fails.
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        restHelpSubcategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpSubcategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHelpSubcategories() throws Exception {
        // Initialize the database
        insertedHelpSubcategory = helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        // Get all the helpSubcategoryList
        restHelpSubcategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpSubcategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)));
    }

    @Test
    @Transactional
    void getHelpSubcategory() throws Exception {
        // Initialize the database
        insertedHelpSubcategory = helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        // Get the helpSubcategory
        restHelpSubcategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, helpSubcategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(helpSubcategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED));
    }

    @Test
    @Transactional
    void getNonExistingHelpSubcategory() throws Exception {
        // Get the helpSubcategory
        restHelpSubcategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHelpSubcategory() throws Exception {
        // Initialize the database
        insertedHelpSubcategory = helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpSubcategory
        HelpSubcategory updatedHelpSubcategory = helpSubcategoryRepository.findById(helpSubcategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHelpSubcategory are not directly saved in db
        em.detach(updatedHelpSubcategory);
        updatedHelpSubcategory.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(updatedHelpSubcategory);

        restHelpSubcategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpSubcategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the HelpSubcategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHelpSubcategoryToMatchAllProperties(updatedHelpSubcategory);
    }

    @Test
    @Transactional
    void putNonExistingHelpSubcategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpSubcategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpSubcategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHelpSubcategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpSubcategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHelpSubcategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpSubcategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpSubcategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHelpSubcategoryWithPatch() throws Exception {
        // Initialize the database
        insertedHelpSubcategory = helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpSubcategory using partial update
        HelpSubcategory partialUpdatedHelpSubcategory = new HelpSubcategory();
        partialUpdatedHelpSubcategory.setId(helpSubcategory.getId());

        partialUpdatedHelpSubcategory.name(UPDATED_NAME);

        restHelpSubcategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpSubcategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHelpSubcategory))
            )
            .andExpect(status().isOk());

        // Validate the HelpSubcategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHelpSubcategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHelpSubcategory, helpSubcategory),
            getPersistedHelpSubcategory(helpSubcategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateHelpSubcategoryWithPatch() throws Exception {
        // Initialize the database
        insertedHelpSubcategory = helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpSubcategory using partial update
        HelpSubcategory partialUpdatedHelpSubcategory = new HelpSubcategory();
        partialUpdatedHelpSubcategory.setId(helpSubcategory.getId());

        partialUpdatedHelpSubcategory.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);

        restHelpSubcategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpSubcategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHelpSubcategory))
            )
            .andExpect(status().isOk());

        // Validate the HelpSubcategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHelpSubcategoryUpdatableFieldsEquals(
            partialUpdatedHelpSubcategory,
            getPersistedHelpSubcategory(partialUpdatedHelpSubcategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHelpSubcategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, helpSubcategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpSubcategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHelpSubcategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpSubcategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHelpSubcategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(helpSubcategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpSubcategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHelpSubcategory() throws Exception {
        // Initialize the database
        insertedHelpSubcategory = helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the helpSubcategory
        restHelpSubcategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, helpSubcategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return helpSubcategoryRepository.count();
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

    protected HelpSubcategory getPersistedHelpSubcategory(HelpSubcategory helpSubcategory) {
        return helpSubcategoryRepository.findById(helpSubcategory.getId()).orElseThrow();
    }

    protected void assertPersistedHelpSubcategoryToMatchAllProperties(HelpSubcategory expectedHelpSubcategory) {
        assertHelpSubcategoryAllPropertiesEquals(expectedHelpSubcategory, getPersistedHelpSubcategory(expectedHelpSubcategory));
    }

    protected void assertPersistedHelpSubcategoryToMatchUpdatableProperties(HelpSubcategory expectedHelpSubcategory) {
        assertHelpSubcategoryAllUpdatablePropertiesEquals(expectedHelpSubcategory, getPersistedHelpSubcategory(expectedHelpSubcategory));
    }
}
