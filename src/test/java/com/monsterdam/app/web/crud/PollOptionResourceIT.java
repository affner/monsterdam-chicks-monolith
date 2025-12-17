package com.monsterdam.app.web.crud;

import static com.monsterdam.app.domain.PollOptionAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PollOption;
import com.monsterdam.app.repository.PollOptionRepository;
import com.monsterdam.app.service.dto.PollOptionDTO;
import com.monsterdam.app.service.mapper.PollOptionMapper;
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
 * Integration tests for the {@link PollOptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PollOptionResourceIT {

    private static final String DEFAULT_OPTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crud/poll-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private PollOptionMapper pollOptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPollOptionMockMvc;

    private PollOption pollOption;

    private PollOption insertedPollOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PollOption createEntity() {
        return new PollOption().optionDescription(DEFAULT_OPTION_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PollOption createUpdatedEntity() {
        return new PollOption().optionDescription(UPDATED_OPTION_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        pollOption = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPollOption != null) {
            pollOptionRepository.delete(insertedPollOption);
            insertedPollOption = null;
        }
    }

    @Test
    @Transactional
    void createPollOption() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);
        var returnedPollOptionDTO = om.readValue(
            restPollOptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pollOptionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PollOptionDTO.class
        );

        // Validate the PollOption in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPollOption = pollOptionMapper.toEntity(returnedPollOptionDTO);
        assertPollOptionUpdatableFieldsEquals(returnedPollOption, getPersistedPollOption(returnedPollOption));

        insertedPollOption = returnedPollOption;
    }

    @Test
    @Transactional
    void createPollOptionWithExistingId() throws Exception {
        // Create the PollOption with an existing ID
        pollOption.setId(1L);
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPollOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pollOptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PollOption in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPollOptions() throws Exception {
        // Initialize the database
        insertedPollOption = pollOptionRepository.saveAndFlush(pollOption);

        // Get all the pollOptionList
        restPollOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pollOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].optionDescription").value(hasItem(DEFAULT_OPTION_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPollOption() throws Exception {
        // Initialize the database
        insertedPollOption = pollOptionRepository.saveAndFlush(pollOption);

        // Get the pollOption
        restPollOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, pollOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pollOption.getId().intValue()))
            .andExpect(jsonPath("$.optionDescription").value(DEFAULT_OPTION_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPollOption() throws Exception {
        // Get the pollOption
        restPollOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPollOption() throws Exception {
        // Initialize the database
        insertedPollOption = pollOptionRepository.saveAndFlush(pollOption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pollOption
        PollOption updatedPollOption = pollOptionRepository.findById(pollOption.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPollOption are not directly saved in db
        em.detach(updatedPollOption);
        updatedPollOption.optionDescription(UPDATED_OPTION_DESCRIPTION);
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(updatedPollOption);

        restPollOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pollOptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PollOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPollOptionToMatchAllProperties(updatedPollOption);
    }

    @Test
    @Transactional
    void putNonExistingPollOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPollOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPollOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pollOptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PollOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePollOptionWithPatch() throws Exception {
        // Initialize the database
        insertedPollOption = pollOptionRepository.saveAndFlush(pollOption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pollOption using partial update
        PollOption partialUpdatedPollOption = new PollOption();
        partialUpdatedPollOption.setId(pollOption.getId());

        partialUpdatedPollOption.optionDescription(UPDATED_OPTION_DESCRIPTION);

        restPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPollOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPollOption))
            )
            .andExpect(status().isOk());

        // Validate the PollOption in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPollOptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPollOption, pollOption),
            getPersistedPollOption(pollOption)
        );
    }

    @Test
    @Transactional
    void fullUpdatePollOptionWithPatch() throws Exception {
        // Initialize the database
        insertedPollOption = pollOptionRepository.saveAndFlush(pollOption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pollOption using partial update
        PollOption partialUpdatedPollOption = new PollOption();
        partialUpdatedPollOption.setId(pollOption.getId());

        partialUpdatedPollOption.optionDescription(UPDATED_OPTION_DESCRIPTION);

        restPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPollOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPollOption))
            )
            .andExpect(status().isOk());

        // Validate the PollOption in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPollOptionUpdatableFieldsEquals(partialUpdatedPollOption, getPersistedPollOption(partialUpdatedPollOption));
    }

    @Test
    @Transactional
    void patchNonExistingPollOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pollOptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPollOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPollOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollOption.setId(longCount.incrementAndGet());

        // Create the PollOption
        PollOptionDTO pollOptionDTO = pollOptionMapper.toDto(pollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollOptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pollOptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PollOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePollOption() throws Exception {
        // Initialize the database
        insertedPollOption = pollOptionRepository.saveAndFlush(pollOption);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pollOption
        restPollOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, pollOption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pollOptionRepository.count();
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

    protected PollOption getPersistedPollOption(PollOption pollOption) {
        return pollOptionRepository.findById(pollOption.getId()).orElseThrow();
    }

    protected void assertPersistedPollOptionToMatchAllProperties(PollOption expectedPollOption) {
        assertPollOptionAllPropertiesEquals(expectedPollOption, getPersistedPollOption(expectedPollOption));
    }

    protected void assertPersistedPollOptionToMatchUpdatableProperties(PollOption expectedPollOption) {
        assertPollOptionAllUpdatablePropertiesEquals(expectedPollOption, getPersistedPollOption(expectedPollOption));
    }
}
