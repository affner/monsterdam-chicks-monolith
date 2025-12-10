package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.SpecialAwardAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.SpecialAward;
import com.monsterdam.app.repository.SpecialAwardRepository;
import com.monsterdam.app.service.dto.SpecialAwardDTO;
import com.monsterdam.app.service.mapper.SpecialAwardMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SpecialAwardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialAwardResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_SPECIAL_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ALT_SPECIAL_TITLE = "BBBBBBBBBB";

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

    private static final Long DEFAULT_SPECIAL_TITLE_ID = 1L;
    private static final Long UPDATED_SPECIAL_TITLE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/special-awards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpecialAwardRepository specialAwardRepository;

    @Autowired
    private SpecialAwardMapper specialAwardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialAwardMockMvc;

    private SpecialAward specialAward;

    private SpecialAward insertedSpecialAward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialAward createEntity() {
        return new SpecialAward()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .reason(DEFAULT_REASON)
            .altSpecialTitle(DEFAULT_ALT_SPECIAL_TITLE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .specialTitleId(DEFAULT_SPECIAL_TITLE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialAward createUpdatedEntity() {
        return new SpecialAward()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .altSpecialTitle(UPDATED_ALT_SPECIAL_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .specialTitleId(UPDATED_SPECIAL_TITLE_ID);
    }

    @BeforeEach
    void initTest() {
        specialAward = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSpecialAward != null) {
            specialAwardRepository.delete(insertedSpecialAward);
            insertedSpecialAward = null;
        }
    }

    @Test
    @Transactional
    void createSpecialAward() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);
        var returnedSpecialAwardDTO = om.readValue(
            restSpecialAwardMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialAwardDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SpecialAwardDTO.class
        );

        // Validate the SpecialAward in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSpecialAward = specialAwardMapper.toEntity(returnedSpecialAwardDTO);
        assertSpecialAwardUpdatableFieldsEquals(returnedSpecialAward, getPersistedSpecialAward(returnedSpecialAward));

        insertedSpecialAward = returnedSpecialAward;
    }

    @Test
    @Transactional
    void createSpecialAwardWithExistingId() throws Exception {
        // Create the SpecialAward with an existing ID
        specialAward.setId(1L);
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialAwardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialAwardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SpecialAward in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialAward.setStartDate(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        restSpecialAwardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialAwardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialAward.setEndDate(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        restSpecialAwardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialAwardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialAward.setCreatedDate(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        restSpecialAwardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialAwardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialAward.setIsDeleted(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        restSpecialAwardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialAwardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpecialTitleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialAward.setSpecialTitleId(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        restSpecialAwardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialAwardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialAwards() throws Exception {
        // Initialize the database
        insertedSpecialAward = specialAwardRepository.saveAndFlush(specialAward);

        // Get all the specialAwardList
        restSpecialAwardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialAward.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].altSpecialTitle").value(hasItem(DEFAULT_ALT_SPECIAL_TITLE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)))
            .andExpect(jsonPath("$.[*].specialTitleId").value(hasItem(DEFAULT_SPECIAL_TITLE_ID.intValue())));
    }

    @Test
    @Transactional
    void getSpecialAward() throws Exception {
        // Initialize the database
        insertedSpecialAward = specialAwardRepository.saveAndFlush(specialAward);

        // Get the specialAward
        restSpecialAwardMockMvc
            .perform(get(ENTITY_API_URL_ID, specialAward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialAward.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.altSpecialTitle").value(DEFAULT_ALT_SPECIAL_TITLE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED))
            .andExpect(jsonPath("$.specialTitleId").value(DEFAULT_SPECIAL_TITLE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSpecialAward() throws Exception {
        // Get the specialAward
        restSpecialAwardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialAward() throws Exception {
        // Initialize the database
        insertedSpecialAward = specialAwardRepository.saveAndFlush(specialAward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialAward
        SpecialAward updatedSpecialAward = specialAwardRepository.findById(specialAward.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialAward are not directly saved in db
        em.detach(updatedSpecialAward);
        updatedSpecialAward
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .altSpecialTitle(UPDATED_ALT_SPECIAL_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .specialTitleId(UPDATED_SPECIAL_TITLE_ID);
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(updatedSpecialAward);

        restSpecialAwardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialAwardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialAwardDTO))
            )
            .andExpect(status().isOk());

        // Validate the SpecialAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSpecialAwardToMatchAllProperties(updatedSpecialAward);
    }

    @Test
    @Transactional
    void putNonExistingSpecialAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialAwardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialAwardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialAwardWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialAward = specialAwardRepository.saveAndFlush(specialAward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialAward using partial update
        SpecialAward partialUpdatedSpecialAward = new SpecialAward();
        partialUpdatedSpecialAward.setId(specialAward.getId());

        partialUpdatedSpecialAward
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restSpecialAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialAward.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialAward))
            )
            .andExpect(status().isOk());

        // Validate the SpecialAward in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialAwardUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSpecialAward, specialAward),
            getPersistedSpecialAward(specialAward)
        );
    }

    @Test
    @Transactional
    void fullUpdateSpecialAwardWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialAward = specialAwardRepository.saveAndFlush(specialAward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialAward using partial update
        SpecialAward partialUpdatedSpecialAward = new SpecialAward();
        partialUpdatedSpecialAward.setId(specialAward.getId());

        partialUpdatedSpecialAward
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .altSpecialTitle(UPDATED_ALT_SPECIAL_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .specialTitleId(UPDATED_SPECIAL_TITLE_ID);

        restSpecialAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialAward.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialAward))
            )
            .andExpect(status().isOk());

        // Validate the SpecialAward in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialAwardUpdatableFieldsEquals(partialUpdatedSpecialAward, getPersistedSpecialAward(partialUpdatedSpecialAward));
    }

    @Test
    @Transactional
    void patchNonExistingSpecialAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialAwardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(specialAwardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialAward() throws Exception {
        // Initialize the database
        insertedSpecialAward = specialAwardRepository.saveAndFlush(specialAward);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the specialAward
        restSpecialAwardMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialAward.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return specialAwardRepository.count();
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

    protected SpecialAward getPersistedSpecialAward(SpecialAward specialAward) {
        return specialAwardRepository.findById(specialAward.getId()).orElseThrow();
    }

    protected void assertPersistedSpecialAwardToMatchAllProperties(SpecialAward expectedSpecialAward) {
        assertSpecialAwardAllPropertiesEquals(expectedSpecialAward, getPersistedSpecialAward(expectedSpecialAward));
    }

    protected void assertPersistedSpecialAwardToMatchUpdatableProperties(SpecialAward expectedSpecialAward) {
        assertSpecialAwardAllUpdatablePropertiesEquals(expectedSpecialAward, getPersistedSpecialAward(expectedSpecialAward));
    }
}
