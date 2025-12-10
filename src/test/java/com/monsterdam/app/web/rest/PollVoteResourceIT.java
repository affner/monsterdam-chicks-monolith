package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.PollVoteAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PollVote;
import com.monsterdam.app.repository.PollVoteRepository;
import com.monsterdam.app.service.dto.PollVoteDTO;
import com.monsterdam.app.service.mapper.PollVoteMapper;
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
 * Integration tests for the {@link PollVoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PollVoteResourceIT {

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_VOTING_USER_ID = 1L;
    private static final Long UPDATED_VOTING_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/poll-votes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PollVoteRepository pollVoteRepository;

    @Autowired
    private PollVoteMapper pollVoteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPollVoteMockMvc;

    private PollVote pollVote;

    private PollVote insertedPollVote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PollVote createEntity() {
        return new PollVote().createdDate(DEFAULT_CREATED_DATE).votingUserId(DEFAULT_VOTING_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PollVote createUpdatedEntity() {
        return new PollVote().createdDate(UPDATED_CREATED_DATE).votingUserId(UPDATED_VOTING_USER_ID);
    }

    @BeforeEach
    void initTest() {
        pollVote = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPollVote != null) {
            pollVoteRepository.delete(insertedPollVote);
            insertedPollVote = null;
        }
    }

    @Test
    @Transactional
    void createPollVote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);
        var returnedPollVoteDTO = om.readValue(
            restPollVoteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pollVoteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PollVoteDTO.class
        );

        // Validate the PollVote in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPollVote = pollVoteMapper.toEntity(returnedPollVoteDTO);
        assertPollVoteUpdatableFieldsEquals(returnedPollVote, getPersistedPollVote(returnedPollVote));

        insertedPollVote = returnedPollVote;
    }

    @Test
    @Transactional
    void createPollVoteWithExistingId() throws Exception {
        // Create the PollVote with an existing ID
        pollVote.setId(1L);
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPollVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pollVoteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PollVote in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pollVote.setCreatedDate(null);

        // Create the PollVote, which fails.
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        restPollVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pollVoteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVotingUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pollVote.setVotingUserId(null);

        // Create the PollVote, which fails.
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        restPollVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pollVoteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPollVotes() throws Exception {
        // Initialize the database
        insertedPollVote = pollVoteRepository.saveAndFlush(pollVote);

        // Get all the pollVoteList
        restPollVoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pollVote.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].votingUserId").value(hasItem(DEFAULT_VOTING_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getPollVote() throws Exception {
        // Initialize the database
        insertedPollVote = pollVoteRepository.saveAndFlush(pollVote);

        // Get the pollVote
        restPollVoteMockMvc
            .perform(get(ENTITY_API_URL_ID, pollVote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pollVote.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.votingUserId").value(DEFAULT_VOTING_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPollVote() throws Exception {
        // Get the pollVote
        restPollVoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPollVote() throws Exception {
        // Initialize the database
        insertedPollVote = pollVoteRepository.saveAndFlush(pollVote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pollVote
        PollVote updatedPollVote = pollVoteRepository.findById(pollVote.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPollVote are not directly saved in db
        em.detach(updatedPollVote);
        updatedPollVote.createdDate(UPDATED_CREATED_DATE).votingUserId(UPDATED_VOTING_USER_ID);
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(updatedPollVote);

        restPollVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollVoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pollVoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the PollVote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPollVoteToMatchAllProperties(updatedPollVote);
    }

    @Test
    @Transactional
    void putNonExistingPollVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pollVoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pollVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollVote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPollVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pollVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollVote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPollVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pollVoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PollVote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePollVoteWithPatch() throws Exception {
        // Initialize the database
        insertedPollVote = pollVoteRepository.saveAndFlush(pollVote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pollVote using partial update
        PollVote partialUpdatedPollVote = new PollVote();
        partialUpdatedPollVote.setId(pollVote.getId());

        partialUpdatedPollVote.votingUserId(UPDATED_VOTING_USER_ID);

        restPollVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPollVote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPollVote))
            )
            .andExpect(status().isOk());

        // Validate the PollVote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPollVoteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPollVote, pollVote), getPersistedPollVote(pollVote));
    }

    @Test
    @Transactional
    void fullUpdatePollVoteWithPatch() throws Exception {
        // Initialize the database
        insertedPollVote = pollVoteRepository.saveAndFlush(pollVote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pollVote using partial update
        PollVote partialUpdatedPollVote = new PollVote();
        partialUpdatedPollVote.setId(pollVote.getId());

        partialUpdatedPollVote.createdDate(UPDATED_CREATED_DATE).votingUserId(UPDATED_VOTING_USER_ID);

        restPollVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPollVote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPollVote))
            )
            .andExpect(status().isOk());

        // Validate the PollVote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPollVoteUpdatableFieldsEquals(partialUpdatedPollVote, getPersistedPollVote(partialUpdatedPollVote));
    }

    @Test
    @Transactional
    void patchNonExistingPollVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pollVoteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pollVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollVote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPollVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pollVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PollVote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPollVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pollVote.setId(longCount.incrementAndGet());

        // Create the PollVote
        PollVoteDTO pollVoteDTO = pollVoteMapper.toDto(pollVote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPollVoteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pollVoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PollVote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePollVote() throws Exception {
        // Initialize the database
        insertedPollVote = pollVoteRepository.saveAndFlush(pollVote);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pollVote
        restPollVoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, pollVote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pollVoteRepository.count();
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

    protected PollVote getPersistedPollVote(PollVote pollVote) {
        return pollVoteRepository.findById(pollVote.getId()).orElseThrow();
    }

    protected void assertPersistedPollVoteToMatchAllProperties(PollVote expectedPollVote) {
        assertPollVoteAllPropertiesEquals(expectedPollVote, getPersistedPollVote(expectedPollVote));
    }

    protected void assertPersistedPollVoteToMatchUpdatableProperties(PollVote expectedPollVote) {
        assertPollVoteAllUpdatablePropertiesEquals(expectedPollVote, getPersistedPollVote(expectedPollVote));
    }
}
