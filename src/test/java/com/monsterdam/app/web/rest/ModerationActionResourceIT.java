package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.ModerationActionAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.ModerationAction;
import com.monsterdam.app.domain.enumeration.ModerationActionType;
import com.monsterdam.app.repository.ModerationActionRepository;
import com.monsterdam.app.service.dto.ModerationActionDTO;
import com.monsterdam.app.service.mapper.ModerationActionMapper;
import jakarta.persistence.EntityManager;
import java.time.Duration;
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
 * Integration tests for the {@link ModerationActionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ModerationActionResourceIT {

    private static final ModerationActionType DEFAULT_ACTION_TYPE = ModerationActionType.WARNING;
    private static final ModerationActionType UPDATED_ACTION_TYPE = ModerationActionType.TEMPORARY_BAN;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Duration DEFAULT_DURATION_DAYS = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION_DAYS = Duration.ofHours(12);

    private static final Long DEFAULT_TICKET_ID = 1L;
    private static final Long UPDATED_TICKET_ID = 2L;

    private static final String ENTITY_API_URL = "/api/moderation-actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModerationActionRepository moderationActionRepository;

    @Autowired
    private ModerationActionMapper moderationActionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModerationActionMockMvc;

    private ModerationAction moderationAction;

    private ModerationAction insertedModerationAction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModerationAction createEntity() {
        return new ModerationAction()
            .actionType(DEFAULT_ACTION_TYPE)
            .reason(DEFAULT_REASON)
            .actionDate(DEFAULT_ACTION_DATE)
            .durationDays(DEFAULT_DURATION_DAYS)
            .ticketId(DEFAULT_TICKET_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModerationAction createUpdatedEntity() {
        return new ModerationAction()
            .actionType(UPDATED_ACTION_TYPE)
            .reason(UPDATED_REASON)
            .actionDate(UPDATED_ACTION_DATE)
            .durationDays(UPDATED_DURATION_DAYS)
            .ticketId(UPDATED_TICKET_ID);
    }

    @BeforeEach
    void initTest() {
        moderationAction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedModerationAction != null) {
            moderationActionRepository.delete(insertedModerationAction);
            insertedModerationAction = null;
        }
    }

    @Test
    @Transactional
    void createModerationAction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);
        var returnedModerationActionDTO = om.readValue(
            restModerationActionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationActionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ModerationActionDTO.class
        );

        // Validate the ModerationAction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedModerationAction = moderationActionMapper.toEntity(returnedModerationActionDTO);
        assertModerationActionUpdatableFieldsEquals(returnedModerationAction, getPersistedModerationAction(returnedModerationAction));

        insertedModerationAction = returnedModerationAction;
    }

    @Test
    @Transactional
    void createModerationActionWithExistingId() throws Exception {
        // Create the ModerationAction with an existing ID
        moderationAction.setId(1L);
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModerationActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationActionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moderationAction.setActionType(null);

        // Create the ModerationAction, which fails.
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        restModerationActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationActionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllModerationActions() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        // Get all the moderationActionList
        restModerationActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moderationAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(DEFAULT_ACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].durationDays").value(hasItem(DEFAULT_DURATION_DAYS.toString())))
            .andExpect(jsonPath("$.[*].ticketId").value(hasItem(DEFAULT_TICKET_ID.intValue())));
    }

    @Test
    @Transactional
    void getModerationAction() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        // Get the moderationAction
        restModerationActionMockMvc
            .perform(get(ENTITY_API_URL_ID, moderationAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moderationAction.getId().intValue()))
            .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.actionDate").value(DEFAULT_ACTION_DATE.toString()))
            .andExpect(jsonPath("$.durationDays").value(DEFAULT_DURATION_DAYS.toString()))
            .andExpect(jsonPath("$.ticketId").value(DEFAULT_TICKET_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingModerationAction() throws Exception {
        // Get the moderationAction
        restModerationActionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingModerationAction() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moderationAction
        ModerationAction updatedModerationAction = moderationActionRepository.findById(moderationAction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedModerationAction are not directly saved in db
        em.detach(updatedModerationAction);
        updatedModerationAction
            .actionType(UPDATED_ACTION_TYPE)
            .reason(UPDATED_REASON)
            .actionDate(UPDATED_ACTION_DATE)
            .durationDays(UPDATED_DURATION_DAYS)
            .ticketId(UPDATED_TICKET_ID);
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(updatedModerationAction);

        restModerationActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moderationActionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moderationActionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedModerationActionToMatchAllProperties(updatedModerationAction);
    }

    @Test
    @Transactional
    void putNonExistingModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moderationActionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moderationActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moderationActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationActionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModerationActionWithPatch() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moderationAction using partial update
        ModerationAction partialUpdatedModerationAction = new ModerationAction();
        partialUpdatedModerationAction.setId(moderationAction.getId());

        partialUpdatedModerationAction.reason(UPDATED_REASON).actionDate(UPDATED_ACTION_DATE);

        restModerationActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModerationAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModerationAction))
            )
            .andExpect(status().isOk());

        // Validate the ModerationAction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModerationActionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedModerationAction, moderationAction),
            getPersistedModerationAction(moderationAction)
        );
    }

    @Test
    @Transactional
    void fullUpdateModerationActionWithPatch() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moderationAction using partial update
        ModerationAction partialUpdatedModerationAction = new ModerationAction();
        partialUpdatedModerationAction.setId(moderationAction.getId());

        partialUpdatedModerationAction
            .actionType(UPDATED_ACTION_TYPE)
            .reason(UPDATED_REASON)
            .actionDate(UPDATED_ACTION_DATE)
            .durationDays(UPDATED_DURATION_DAYS)
            .ticketId(UPDATED_TICKET_ID);

        restModerationActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModerationAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModerationAction))
            )
            .andExpect(status().isOk());

        // Validate the ModerationAction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModerationActionUpdatableFieldsEquals(
            partialUpdatedModerationAction,
            getPersistedModerationAction(partialUpdatedModerationAction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moderationActionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moderationActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moderationActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moderationActionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModerationAction() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the moderationAction
        restModerationActionMockMvc
            .perform(delete(ENTITY_API_URL_ID, moderationAction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return moderationActionRepository.count();
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

    protected ModerationAction getPersistedModerationAction(ModerationAction moderationAction) {
        return moderationActionRepository.findById(moderationAction.getId()).orElseThrow();
    }

    protected void assertPersistedModerationActionToMatchAllProperties(ModerationAction expectedModerationAction) {
        assertModerationActionAllPropertiesEquals(expectedModerationAction, getPersistedModerationAction(expectedModerationAction));
    }

    protected void assertPersistedModerationActionToMatchUpdatableProperties(ModerationAction expectedModerationAction) {
        assertModerationActionAllUpdatablePropertiesEquals(
            expectedModerationAction,
            getPersistedModerationAction(expectedModerationAction)
        );
    }
}
