package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.StateUserRelationAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.StateUserRelation;
import com.monsterdam.app.repository.StateUserRelationRepository;
import com.monsterdam.app.service.dto.StateUserRelationDTO;
import com.monsterdam.app.service.mapper.StateUserRelationMapper;
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
 * Integration tests for the {@link StateUserRelationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StateUserRelationResourceIT {

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

    private static final String ENTITY_API_URL = "/api/state-user-relations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StateUserRelationRepository stateUserRelationRepository;

    @Autowired
    private StateUserRelationMapper stateUserRelationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStateUserRelationMockMvc;

    private StateUserRelation stateUserRelation;

    private StateUserRelation insertedStateUserRelation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateUserRelation createEntity() {
        return new StateUserRelation()
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateUserRelation createUpdatedEntity() {
        return new StateUserRelation()
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    void initTest() {
        stateUserRelation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStateUserRelation != null) {
            stateUserRelationRepository.delete(insertedStateUserRelation);
            insertedStateUserRelation = null;
        }
    }

    @Test
    @Transactional
    void createStateUserRelation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);
        var returnedStateUserRelationDTO = om.readValue(
            restStateUserRelationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateUserRelationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StateUserRelationDTO.class
        );

        // Validate the StateUserRelation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStateUserRelation = stateUserRelationMapper.toEntity(returnedStateUserRelationDTO);
        assertStateUserRelationUpdatableFieldsEquals(returnedStateUserRelation, getPersistedStateUserRelation(returnedStateUserRelation));

        insertedStateUserRelation = returnedStateUserRelation;
    }

    @Test
    @Transactional
    void createStateUserRelationWithExistingId() throws Exception {
        // Create the StateUserRelation with an existing ID
        stateUserRelation.setId(1L);
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateUserRelationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateUserRelationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StateUserRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stateUserRelation.setCreatedDate(null);

        // Create the StateUserRelation, which fails.
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        restStateUserRelationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateUserRelationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stateUserRelation.setIsDeleted(null);

        // Create the StateUserRelation, which fails.
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        restStateUserRelationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateUserRelationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStateUserRelations() throws Exception {
        // Initialize the database
        insertedStateUserRelation = stateUserRelationRepository.saveAndFlush(stateUserRelation);

        // Get all the stateUserRelationList
        restStateUserRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateUserRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)));
    }

    @Test
    @Transactional
    void getStateUserRelation() throws Exception {
        // Initialize the database
        insertedStateUserRelation = stateUserRelationRepository.saveAndFlush(stateUserRelation);

        // Get the stateUserRelation
        restStateUserRelationMockMvc
            .perform(get(ENTITY_API_URL_ID, stateUserRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stateUserRelation.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED));
    }

    @Test
    @Transactional
    void getNonExistingStateUserRelation() throws Exception {
        // Get the stateUserRelation
        restStateUserRelationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStateUserRelation() throws Exception {
        // Initialize the database
        insertedStateUserRelation = stateUserRelationRepository.saveAndFlush(stateUserRelation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stateUserRelation
        StateUserRelation updatedStateUserRelation = stateUserRelationRepository.findById(stateUserRelation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStateUserRelation are not directly saved in db
        em.detach(updatedStateUserRelation);
        updatedStateUserRelation
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(updatedStateUserRelation);

        restStateUserRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateUserRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stateUserRelationDTO))
            )
            .andExpect(status().isOk());

        // Validate the StateUserRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStateUserRelationToMatchAllProperties(updatedStateUserRelation);
    }

    @Test
    @Transactional
    void putNonExistingStateUserRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateUserRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateUserRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStateUserRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateUserRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStateUserRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateUserRelationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateUserRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStateUserRelationWithPatch() throws Exception {
        // Initialize the database
        insertedStateUserRelation = stateUserRelationRepository.saveAndFlush(stateUserRelation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stateUserRelation using partial update
        StateUserRelation partialUpdatedStateUserRelation = new StateUserRelation();
        partialUpdatedStateUserRelation.setId(stateUserRelation.getId());

        partialUpdatedStateUserRelation.createdDate(UPDATED_CREATED_DATE).isDeleted(UPDATED_IS_DELETED);

        restStateUserRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateUserRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStateUserRelation))
            )
            .andExpect(status().isOk());

        // Validate the StateUserRelation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStateUserRelationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStateUserRelation, stateUserRelation),
            getPersistedStateUserRelation(stateUserRelation)
        );
    }

    @Test
    @Transactional
    void fullUpdateStateUserRelationWithPatch() throws Exception {
        // Initialize the database
        insertedStateUserRelation = stateUserRelationRepository.saveAndFlush(stateUserRelation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stateUserRelation using partial update
        StateUserRelation partialUpdatedStateUserRelation = new StateUserRelation();
        partialUpdatedStateUserRelation.setId(stateUserRelation.getId());

        partialUpdatedStateUserRelation
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restStateUserRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateUserRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStateUserRelation))
            )
            .andExpect(status().isOk());

        // Validate the StateUserRelation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStateUserRelationUpdatableFieldsEquals(
            partialUpdatedStateUserRelation,
            getPersistedStateUserRelation(partialUpdatedStateUserRelation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStateUserRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stateUserRelationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateUserRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStateUserRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stateUserRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateUserRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStateUserRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateUserRelation.setId(longCount.incrementAndGet());

        // Create the StateUserRelation
        StateUserRelationDTO stateUserRelationDTO = stateUserRelationMapper.toDto(stateUserRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateUserRelationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stateUserRelationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateUserRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStateUserRelation() throws Exception {
        // Initialize the database
        insertedStateUserRelation = stateUserRelationRepository.saveAndFlush(stateUserRelation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stateUserRelation
        restStateUserRelationMockMvc
            .perform(delete(ENTITY_API_URL_ID, stateUserRelation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stateUserRelationRepository.count();
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

    protected StateUserRelation getPersistedStateUserRelation(StateUserRelation stateUserRelation) {
        return stateUserRelationRepository.findById(stateUserRelation.getId()).orElseThrow();
    }

    protected void assertPersistedStateUserRelationToMatchAllProperties(StateUserRelation expectedStateUserRelation) {
        assertStateUserRelationAllPropertiesEquals(expectedStateUserRelation, getPersistedStateUserRelation(expectedStateUserRelation));
    }

    protected void assertPersistedStateUserRelationToMatchUpdatableProperties(StateUserRelation expectedStateUserRelation) {
        assertStateUserRelationAllUpdatablePropertiesEquals(
            expectedStateUserRelation,
            getPersistedStateUserRelation(expectedStateUserRelation)
        );
    }
}
