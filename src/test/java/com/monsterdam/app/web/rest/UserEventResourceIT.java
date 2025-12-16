package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.UserEventAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.UserEvent;
import com.monsterdam.app.domain.enumeration.UserEventStatus;
import com.monsterdam.app.repository.UserEventRepository;
import com.monsterdam.app.service.dto.UserEventDTO;
import com.monsterdam.app.service.mapper.UserEventMapper;
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
 * Integration tests for the {@link UserEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserEventResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final UserEventStatus DEFAULT_CREATOR_EVENT_STATUS = UserEventStatus.CANCELED;
    private static final UserEventStatus UPDATED_CREATOR_EVENT_STATUS = UserEventStatus.ACTIVE;

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

    private static final String ENTITY_API_URL = "/api/user-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private UserEventMapper userEventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserEventMockMvc;

    private UserEvent userEvent;

    private UserEvent insertedUserEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserEvent createEntity() {
        return new UserEvent()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .creatorEventStatus(DEFAULT_CREATOR_EVENT_STATUS)
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
    public static UserEvent createUpdatedEntity() {
        return new UserEvent()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .creatorEventStatus(UPDATED_CREATOR_EVENT_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        userEvent = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUserEvent != null) {
            userEventRepository.delete(insertedUserEvent);
            insertedUserEvent = null;
        }
    }

    @Test
    @Transactional
    void createUserEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);
        var returnedUserEventDTO = om.readValue(
            restUserEventMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userEventDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserEventDTO.class
        );

        // Validate the UserEvent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserEvent = userEventMapper.toEntity(returnedUserEventDTO);
        assertUserEventUpdatableFieldsEquals(returnedUserEvent, getPersistedUserEvent(returnedUserEvent));

        insertedUserEvent = returnedUserEvent;
    }

    @Test
    @Transactional
    void createUserEventWithExistingId() throws Exception {
        // Create the UserEvent with an existing ID
        userEvent.setId(1L);
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userEvent.setTitle(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userEvent.setStartDate(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userEvent.setEndDate(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userEvent.setCreatedDate(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserEvents() throws Exception {
        // Initialize the database
        insertedUserEvent = userEventRepository.saveAndFlush(userEvent);

        // Get all the userEventList
        restUserEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].creatorEventStatus").value(hasItem(DEFAULT_CREATOR_EVENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserEvent() throws Exception {
        // Initialize the database
        insertedUserEvent = userEventRepository.saveAndFlush(userEvent);

        // Get the userEvent
        restUserEventMockMvc
            .perform(get(ENTITY_API_URL_ID, userEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userEvent.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.creatorEventStatus").value(DEFAULT_CREATOR_EVENT_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserEvent() throws Exception {
        // Get the userEvent
        restUserEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserEvent() throws Exception {
        // Initialize the database
        insertedUserEvent = userEventRepository.saveAndFlush(userEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userEvent
        UserEvent updatedUserEvent = userEventRepository.findById(userEvent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserEvent are not directly saved in db
        em.detach(updatedUserEvent);
        updatedUserEvent
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .creatorEventStatus(UPDATED_CREATOR_EVENT_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        UserEventDTO userEventDTO = userEventMapper.toDto(updatedUserEvent);

        restUserEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserEventToMatchAllProperties(updatedUserEvent);
    }

    @Test
    @Transactional
    void putNonExistingUserEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserEventWithPatch() throws Exception {
        // Initialize the database
        insertedUserEvent = userEventRepository.saveAndFlush(userEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userEvent using partial update
        UserEvent partialUpdatedUserEvent = new UserEvent();
        partialUpdatedUserEvent.setId(userEvent.getId());

        partialUpdatedUserEvent
            .endDate(UPDATED_END_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restUserEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserEvent))
            )
            .andExpect(status().isOk());

        // Validate the UserEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserEventUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserEvent, userEvent),
            getPersistedUserEvent(userEvent)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserEventWithPatch() throws Exception {
        // Initialize the database
        insertedUserEvent = userEventRepository.saveAndFlush(userEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userEvent using partial update
        UserEvent partialUpdatedUserEvent = new UserEvent();
        partialUpdatedUserEvent.setId(userEvent.getId());

        partialUpdatedUserEvent
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .creatorEventStatus(UPDATED_CREATOR_EVENT_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restUserEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserEvent))
            )
            .andExpect(status().isOk());

        // Validate the UserEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserEventUpdatableFieldsEquals(partialUpdatedUserEvent, getPersistedUserEvent(partialUpdatedUserEvent));
    }

    @Test
    @Transactional
    void patchNonExistingUserEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserEvent() throws Exception {
        // Initialize the database
        insertedUserEvent = userEventRepository.saveAndFlush(userEvent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userEvent
        restUserEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, userEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userEventRepository.count();
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

    protected UserEvent getPersistedUserEvent(UserEvent userEvent) {
        return userEventRepository.findById(userEvent.getId()).orElseThrow();
    }

    protected void assertPersistedUserEventToMatchAllProperties(UserEvent expectedUserEvent) {
        assertUserEventAllPropertiesEquals(expectedUserEvent, getPersistedUserEvent(expectedUserEvent));
    }

    protected void assertPersistedUserEventToMatchUpdatableProperties(UserEvent expectedUserEvent) {
        assertUserEventAllUpdatablePropertiesEquals(expectedUserEvent, getPersistedUserEvent(expectedUserEvent));
    }
}
