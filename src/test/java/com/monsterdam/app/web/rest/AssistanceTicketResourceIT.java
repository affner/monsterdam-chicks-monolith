package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.AssistanceTicketAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.AssistanceTicket;
import com.monsterdam.app.domain.enumeration.TicketStatus;
import com.monsterdam.app.domain.enumeration.TicketType;
import com.monsterdam.app.repository.AssistanceTicketRepository;
import com.monsterdam.app.service.dto.AssistanceTicketDTO;
import com.monsterdam.app.service.mapper.AssistanceTicketMapper;
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
 * Integration tests for the {@link AssistanceTicketResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssistanceTicketResourceIT {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TicketStatus DEFAULT_STATUS = TicketStatus.OPEN;
    private static final TicketStatus UPDATED_STATUS = TicketStatus.ASSIGNED;

    private static final TicketType DEFAULT_TYPE = TicketType.ACCESS_ISSUE;
    private static final TicketType UPDATED_TYPE = TicketType.DOCUMENT_VERIFICATION;

    private static final Instant DEFAULT_OPENED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OPENED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/assistance-tickets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AssistanceTicketRepository assistanceTicketRepository;

    @Autowired
    private AssistanceTicketMapper assistanceTicketMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssistanceTicketMockMvc;

    private AssistanceTicket assistanceTicket;

    private AssistanceTicket insertedAssistanceTicket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssistanceTicket createEntity() {
        return new AssistanceTicket()
            .subject(DEFAULT_SUBJECT)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .type(DEFAULT_TYPE)
            .openedAt(DEFAULT_OPENED_AT)
            .closedAt(DEFAULT_CLOSED_AT)
            .comments(DEFAULT_COMMENTS)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .userId(DEFAULT_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssistanceTicket createUpdatedEntity() {
        return new AssistanceTicket()
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .openedAt(UPDATED_OPENED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .comments(UPDATED_COMMENTS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .userId(UPDATED_USER_ID);
    }

    @BeforeEach
    void initTest() {
        assistanceTicket = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAssistanceTicket != null) {
            assistanceTicketRepository.delete(insertedAssistanceTicket);
            insertedAssistanceTicket = null;
        }
    }

    @Test
    @Transactional
    void createAssistanceTicket() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);
        var returnedAssistanceTicketDTO = om.readValue(
            restAssistanceTicketMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assistanceTicketDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AssistanceTicketDTO.class
        );

        // Validate the AssistanceTicket in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAssistanceTicket = assistanceTicketMapper.toEntity(returnedAssistanceTicketDTO);
        assertAssistanceTicketUpdatableFieldsEquals(returnedAssistanceTicket, getPersistedAssistanceTicket(returnedAssistanceTicket));

        insertedAssistanceTicket = returnedAssistanceTicket;
    }

    @Test
    @Transactional
    void createAssistanceTicketWithExistingId() throws Exception {
        // Create the AssistanceTicket with an existing ID
        assistanceTicket.setId(1L);
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssistanceTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assistanceTicketDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssistanceTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubjectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assistanceTicket.setSubject(null);

        // Create the AssistanceTicket, which fails.
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        restAssistanceTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assistanceTicketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assistanceTicket.setDescription(null);

        // Create the AssistanceTicket, which fails.
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        restAssistanceTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assistanceTicketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assistanceTicket.setStatus(null);

        // Create the AssistanceTicket, which fails.
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        restAssistanceTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assistanceTicketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assistanceTicket.setType(null);

        // Create the AssistanceTicket, which fails.
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        restAssistanceTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assistanceTicketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assistanceTicket.setCreatedDate(null);

        // Create the AssistanceTicket, which fails.
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        restAssistanceTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assistanceTicketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assistanceTicket.setUserId(null);

        // Create the AssistanceTicket, which fails.
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        restAssistanceTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assistanceTicketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssistanceTickets() throws Exception {
        // Initialize the database
        insertedAssistanceTicket = assistanceTicketRepository.saveAndFlush(assistanceTicket);

        // Get all the assistanceTicketList
        restAssistanceTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assistanceTicket.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].openedAt").value(hasItem(DEFAULT_OPENED_AT.toString())))
            .andExpect(jsonPath("$.[*].closedAt").value(hasItem(DEFAULT_CLOSED_AT.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getAssistanceTicket() throws Exception {
        // Initialize the database
        insertedAssistanceTicket = assistanceTicketRepository.saveAndFlush(assistanceTicket);

        // Get the assistanceTicket
        restAssistanceTicketMockMvc
            .perform(get(ENTITY_API_URL_ID, assistanceTicket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assistanceTicket.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.openedAt").value(DEFAULT_OPENED_AT.toString()))
            .andExpect(jsonPath("$.closedAt").value(DEFAULT_CLOSED_AT.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAssistanceTicket() throws Exception {
        // Get the assistanceTicket
        restAssistanceTicketMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssistanceTicket() throws Exception {
        // Initialize the database
        insertedAssistanceTicket = assistanceTicketRepository.saveAndFlush(assistanceTicket);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assistanceTicket
        AssistanceTicket updatedAssistanceTicket = assistanceTicketRepository.findById(assistanceTicket.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAssistanceTicket are not directly saved in db
        em.detach(updatedAssistanceTicket);
        updatedAssistanceTicket
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .openedAt(UPDATED_OPENED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .comments(UPDATED_COMMENTS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .userId(UPDATED_USER_ID);
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(updatedAssistanceTicket);

        restAssistanceTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assistanceTicketDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assistanceTicketDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssistanceTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAssistanceTicketToMatchAllProperties(updatedAssistanceTicket);
    }

    @Test
    @Transactional
    void putNonExistingAssistanceTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssistanceTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assistanceTicketDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assistanceTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssistanceTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssistanceTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssistanceTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assistanceTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssistanceTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssistanceTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssistanceTicketMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assistanceTicketDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssistanceTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssistanceTicketWithPatch() throws Exception {
        // Initialize the database
        insertedAssistanceTicket = assistanceTicketRepository.saveAndFlush(assistanceTicket);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assistanceTicket using partial update
        AssistanceTicket partialUpdatedAssistanceTicket = new AssistanceTicket();
        partialUpdatedAssistanceTicket.setId(assistanceTicket.getId());

        partialUpdatedAssistanceTicket
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .closedAt(UPDATED_CLOSED_AT)
            .comments(UPDATED_COMMENTS)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .userId(UPDATED_USER_ID);

        restAssistanceTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssistanceTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssistanceTicket))
            )
            .andExpect(status().isOk());

        // Validate the AssistanceTicket in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssistanceTicketUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAssistanceTicket, assistanceTicket),
            getPersistedAssistanceTicket(assistanceTicket)
        );
    }

    @Test
    @Transactional
    void fullUpdateAssistanceTicketWithPatch() throws Exception {
        // Initialize the database
        insertedAssistanceTicket = assistanceTicketRepository.saveAndFlush(assistanceTicket);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assistanceTicket using partial update
        AssistanceTicket partialUpdatedAssistanceTicket = new AssistanceTicket();
        partialUpdatedAssistanceTicket.setId(assistanceTicket.getId());

        partialUpdatedAssistanceTicket
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .openedAt(UPDATED_OPENED_AT)
            .closedAt(UPDATED_CLOSED_AT)
            .comments(UPDATED_COMMENTS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .userId(UPDATED_USER_ID);

        restAssistanceTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssistanceTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssistanceTicket))
            )
            .andExpect(status().isOk());

        // Validate the AssistanceTicket in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssistanceTicketUpdatableFieldsEquals(
            partialUpdatedAssistanceTicket,
            getPersistedAssistanceTicket(partialUpdatedAssistanceTicket)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAssistanceTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssistanceTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assistanceTicketDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assistanceTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssistanceTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssistanceTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssistanceTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assistanceTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssistanceTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssistanceTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assistanceTicket.setId(longCount.incrementAndGet());

        // Create the AssistanceTicket
        AssistanceTicketDTO assistanceTicketDTO = assistanceTicketMapper.toDto(assistanceTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssistanceTicketMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(assistanceTicketDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssistanceTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssistanceTicket() throws Exception {
        // Initialize the database
        insertedAssistanceTicket = assistanceTicketRepository.saveAndFlush(assistanceTicket);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the assistanceTicket
        restAssistanceTicketMockMvc
            .perform(delete(ENTITY_API_URL_ID, assistanceTicket.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return assistanceTicketRepository.count();
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

    protected AssistanceTicket getPersistedAssistanceTicket(AssistanceTicket assistanceTicket) {
        return assistanceTicketRepository.findById(assistanceTicket.getId()).orElseThrow();
    }

    protected void assertPersistedAssistanceTicketToMatchAllProperties(AssistanceTicket expectedAssistanceTicket) {
        assertAssistanceTicketAllPropertiesEquals(expectedAssistanceTicket, getPersistedAssistanceTicket(expectedAssistanceTicket));
    }

    protected void assertPersistedAssistanceTicketToMatchUpdatableProperties(AssistanceTicket expectedAssistanceTicket) {
        assertAssistanceTicketAllUpdatablePropertiesEquals(
            expectedAssistanceTicket,
            getPersistedAssistanceTicket(expectedAssistanceTicket)
        );
    }
}
