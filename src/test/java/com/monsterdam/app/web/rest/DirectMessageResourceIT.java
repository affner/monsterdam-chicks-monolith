package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.DirectMessageAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.DirectMessage;
import com.monsterdam.app.repository.DirectMessageRepository;
import com.monsterdam.app.service.dto.DirectMessageDTO;
import com.monsterdam.app.service.mapper.DirectMessageMapper;
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
 * Integration tests for the {@link DirectMessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DirectMessageResourceIT {

    private static final String DEFAULT_MESSAGE_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_READ_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_READ_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_LIKE_COUNT = 1;
    private static final Integer UPDATED_LIKE_COUNT = 2;

    private static final Boolean DEFAULT_IS_HIDDEN = false;
    private static final Boolean UPDATED_IS_HIDDEN = true;

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

    private static final Long DEFAULT_REPLIED_STORY_ID = 1L;
    private static final Long UPDATED_REPLIED_STORY_ID = 2L;

    private static final Long DEFAULT_SENDER_ID = 1L;
    private static final Long UPDATED_SENDER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/direct-messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DirectMessageRepository directMessageRepository;

    @Autowired
    private DirectMessageMapper directMessageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDirectMessageMockMvc;

    private DirectMessage directMessage;

    private DirectMessage insertedDirectMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DirectMessage createEntity() {
        return new DirectMessage()
            .messageContent(DEFAULT_MESSAGE_CONTENT)
            .readDate(DEFAULT_READ_DATE)
            .likeCount(DEFAULT_LIKE_COUNT)
            .isHidden(DEFAULT_IS_HIDDEN)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .repliedStoryId(DEFAULT_REPLIED_STORY_ID)
            .senderId(DEFAULT_SENDER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DirectMessage createUpdatedEntity() {
        return new DirectMessage()
            .messageContent(UPDATED_MESSAGE_CONTENT)
            .readDate(UPDATED_READ_DATE)
            .likeCount(UPDATED_LIKE_COUNT)
            .isHidden(UPDATED_IS_HIDDEN)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .repliedStoryId(UPDATED_REPLIED_STORY_ID)
            .senderId(UPDATED_SENDER_ID);
    }

    @BeforeEach
    void initTest() {
        directMessage = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDirectMessage != null) {
            directMessageRepository.delete(insertedDirectMessage);
            insertedDirectMessage = null;
        }
    }

    @Test
    @Transactional
    void createDirectMessage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);
        var returnedDirectMessageDTO = om.readValue(
            restDirectMessageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directMessageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DirectMessageDTO.class
        );

        // Validate the DirectMessage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDirectMessage = directMessageMapper.toEntity(returnedDirectMessageDTO);
        assertDirectMessageUpdatableFieldsEquals(returnedDirectMessage, getPersistedDirectMessage(returnedDirectMessage));

        insertedDirectMessage = returnedDirectMessage;
    }

    @Test
    @Transactional
    void createDirectMessageWithExistingId() throws Exception {
        // Create the DirectMessage with an existing ID
        directMessage.setId(1L);
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        directMessage.setCreatedDate(null);

        // Create the DirectMessage, which fails.
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        restDirectMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        directMessage.setIsDeleted(null);

        // Create the DirectMessage, which fails.
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        restDirectMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSenderIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        directMessage.setSenderId(null);

        // Create the DirectMessage, which fails.
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        restDirectMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directMessageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDirectMessages() throws Exception {
        // Initialize the database
        insertedDirectMessage = directMessageRepository.saveAndFlush(directMessage);

        // Get all the directMessageList
        restDirectMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].messageContent").value(hasItem(DEFAULT_MESSAGE_CONTENT)))
            .andExpect(jsonPath("$.[*].readDate").value(hasItem(DEFAULT_READ_DATE.toString())))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].isHidden").value(hasItem(DEFAULT_IS_HIDDEN)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)))
            .andExpect(jsonPath("$.[*].repliedStoryId").value(hasItem(DEFAULT_REPLIED_STORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].senderId").value(hasItem(DEFAULT_SENDER_ID.intValue())));
    }

    @Test
    @Transactional
    void getDirectMessage() throws Exception {
        // Initialize the database
        insertedDirectMessage = directMessageRepository.saveAndFlush(directMessage);

        // Get the directMessage
        restDirectMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, directMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(directMessage.getId().intValue()))
            .andExpect(jsonPath("$.messageContent").value(DEFAULT_MESSAGE_CONTENT))
            .andExpect(jsonPath("$.readDate").value(DEFAULT_READ_DATE.toString()))
            .andExpect(jsonPath("$.likeCount").value(DEFAULT_LIKE_COUNT))
            .andExpect(jsonPath("$.isHidden").value(DEFAULT_IS_HIDDEN))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED))
            .andExpect(jsonPath("$.repliedStoryId").value(DEFAULT_REPLIED_STORY_ID.intValue()))
            .andExpect(jsonPath("$.senderId").value(DEFAULT_SENDER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDirectMessage() throws Exception {
        // Get the directMessage
        restDirectMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDirectMessage() throws Exception {
        // Initialize the database
        insertedDirectMessage = directMessageRepository.saveAndFlush(directMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the directMessage
        DirectMessage updatedDirectMessage = directMessageRepository.findById(directMessage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDirectMessage are not directly saved in db
        em.detach(updatedDirectMessage);
        updatedDirectMessage
            .messageContent(UPDATED_MESSAGE_CONTENT)
            .readDate(UPDATED_READ_DATE)
            .likeCount(UPDATED_LIKE_COUNT)
            .isHidden(UPDATED_IS_HIDDEN)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .repliedStoryId(UPDATED_REPLIED_STORY_ID)
            .senderId(UPDATED_SENDER_ID);
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(updatedDirectMessage);

        restDirectMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(directMessageDTO))
            )
            .andExpect(status().isOk());

        // Validate the DirectMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDirectMessageToMatchAllProperties(updatedDirectMessage);
    }

    @Test
    @Transactional
    void putNonExistingDirectMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDirectMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDirectMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directMessageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DirectMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDirectMessageWithPatch() throws Exception {
        // Initialize the database
        insertedDirectMessage = directMessageRepository.saveAndFlush(directMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the directMessage using partial update
        DirectMessage partialUpdatedDirectMessage = new DirectMessage();
        partialUpdatedDirectMessage.setId(directMessage.getId());

        partialUpdatedDirectMessage
            .readDate(UPDATED_READ_DATE)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restDirectMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDirectMessage))
            )
            .andExpect(status().isOk());

        // Validate the DirectMessage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDirectMessageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDirectMessage, directMessage),
            getPersistedDirectMessage(directMessage)
        );
    }

    @Test
    @Transactional
    void fullUpdateDirectMessageWithPatch() throws Exception {
        // Initialize the database
        insertedDirectMessage = directMessageRepository.saveAndFlush(directMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the directMessage using partial update
        DirectMessage partialUpdatedDirectMessage = new DirectMessage();
        partialUpdatedDirectMessage.setId(directMessage.getId());

        partialUpdatedDirectMessage
            .messageContent(UPDATED_MESSAGE_CONTENT)
            .readDate(UPDATED_READ_DATE)
            .likeCount(UPDATED_LIKE_COUNT)
            .isHidden(UPDATED_IS_HIDDEN)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .repliedStoryId(UPDATED_REPLIED_STORY_ID)
            .senderId(UPDATED_SENDER_ID);

        restDirectMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDirectMessage))
            )
            .andExpect(status().isOk());

        // Validate the DirectMessage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDirectMessageUpdatableFieldsEquals(partialUpdatedDirectMessage, getPersistedDirectMessage(partialUpdatedDirectMessage));
    }

    @Test
    @Transactional
    void patchNonExistingDirectMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, directMessageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDirectMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(directMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDirectMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directMessage.setId(longCount.incrementAndGet());

        // Create the DirectMessage
        DirectMessageDTO directMessageDTO = directMessageMapper.toDto(directMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectMessageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(directMessageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DirectMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDirectMessage() throws Exception {
        // Initialize the database
        insertedDirectMessage = directMessageRepository.saveAndFlush(directMessage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the directMessage
        restDirectMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, directMessage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return directMessageRepository.count();
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

    protected DirectMessage getPersistedDirectMessage(DirectMessage directMessage) {
        return directMessageRepository.findById(directMessage.getId()).orElseThrow();
    }

    protected void assertPersistedDirectMessageToMatchAllProperties(DirectMessage expectedDirectMessage) {
        assertDirectMessageAllPropertiesEquals(expectedDirectMessage, getPersistedDirectMessage(expectedDirectMessage));
    }

    protected void assertPersistedDirectMessageToMatchUpdatableProperties(DirectMessage expectedDirectMessage) {
        assertDirectMessageAllUpdatablePropertiesEquals(expectedDirectMessage, getPersistedDirectMessage(expectedDirectMessage));
    }
}
