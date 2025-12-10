package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.ChatRoomAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.ChatRoom;
import com.monsterdam.app.repository.ChatRoomRepository;
import com.monsterdam.app.service.dto.ChatRoomDTO;
import com.monsterdam.app.service.mapper.ChatRoomMapper;
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
 * Integration tests for the {@link ChatRoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChatRoomResourceIT {

    private static final String DEFAULT_LAST_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_LAST_ACTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_CONNECTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_CONNECTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_MUTED = false;
    private static final Boolean UPDATED_MUTED = true;

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

    private static final Long DEFAULT_PARTICIPANT_ID = 1L;
    private static final Long UPDATED_PARTICIPANT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/chat-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMapper chatRoomMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChatRoomMockMvc;

    private ChatRoom chatRoom;

    private ChatRoom insertedChatRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatRoom createEntity() {
        return new ChatRoom()
            .lastAction(DEFAULT_LAST_ACTION)
            .lastConnectionDate(DEFAULT_LAST_CONNECTION_DATE)
            .muted(DEFAULT_MUTED)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .participantId(DEFAULT_PARTICIPANT_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatRoom createUpdatedEntity() {
        return new ChatRoom()
            .lastAction(UPDATED_LAST_ACTION)
            .lastConnectionDate(UPDATED_LAST_CONNECTION_DATE)
            .muted(UPDATED_MUTED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .participantId(UPDATED_PARTICIPANT_ID);
    }

    @BeforeEach
    void initTest() {
        chatRoom = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedChatRoom != null) {
            chatRoomRepository.delete(insertedChatRoom);
            insertedChatRoom = null;
        }
    }

    @Test
    @Transactional
    void createChatRoom() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);
        var returnedChatRoomDTO = om.readValue(
            restChatRoomMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatRoomDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChatRoomDTO.class
        );

        // Validate the ChatRoom in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChatRoom = chatRoomMapper.toEntity(returnedChatRoomDTO);
        assertChatRoomUpdatableFieldsEquals(returnedChatRoom, getPersistedChatRoom(returnedChatRoom));

        insertedChatRoom = returnedChatRoom;
    }

    @Test
    @Transactional
    void createChatRoomWithExistingId() throws Exception {
        // Create the ChatRoom with an existing ID
        chatRoom.setId(1L);
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatRoomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chatRoom.setCreatedDate(null);

        // Create the ChatRoom, which fails.
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        restChatRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatRoomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chatRoom.setIsDeleted(null);

        // Create the ChatRoom, which fails.
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        restChatRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatRoomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChatRooms() throws Exception {
        // Initialize the database
        insertedChatRoom = chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList
        restChatRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastAction").value(hasItem(DEFAULT_LAST_ACTION)))
            .andExpect(jsonPath("$.[*].lastConnectionDate").value(hasItem(DEFAULT_LAST_CONNECTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].muted").value(hasItem(DEFAULT_MUTED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)))
            .andExpect(jsonPath("$.[*].participantId").value(hasItem(DEFAULT_PARTICIPANT_ID.intValue())));
    }

    @Test
    @Transactional
    void getChatRoom() throws Exception {
        // Initialize the database
        insertedChatRoom = chatRoomRepository.saveAndFlush(chatRoom);

        // Get the chatRoom
        restChatRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, chatRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatRoom.getId().intValue()))
            .andExpect(jsonPath("$.lastAction").value(DEFAULT_LAST_ACTION))
            .andExpect(jsonPath("$.lastConnectionDate").value(DEFAULT_LAST_CONNECTION_DATE.toString()))
            .andExpect(jsonPath("$.muted").value(DEFAULT_MUTED))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED))
            .andExpect(jsonPath("$.participantId").value(DEFAULT_PARTICIPANT_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingChatRoom() throws Exception {
        // Get the chatRoom
        restChatRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChatRoom() throws Exception {
        // Initialize the database
        insertedChatRoom = chatRoomRepository.saveAndFlush(chatRoom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatRoom
        ChatRoom updatedChatRoom = chatRoomRepository.findById(chatRoom.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChatRoom are not directly saved in db
        em.detach(updatedChatRoom);
        updatedChatRoom
            .lastAction(UPDATED_LAST_ACTION)
            .lastConnectionDate(UPDATED_LAST_CONNECTION_DATE)
            .muted(UPDATED_MUTED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .participantId(UPDATED_PARTICIPANT_ID);
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(updatedChatRoom);

        restChatRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatRoomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatRoomDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChatRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChatRoomToMatchAllProperties(updatedChatRoom);
    }

    @Test
    @Transactional
    void putNonExistingChatRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatRoomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChatRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChatRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatRoomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChatRoomWithPatch() throws Exception {
        // Initialize the database
        insertedChatRoom = chatRoomRepository.saveAndFlush(chatRoom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatRoom using partial update
        ChatRoom partialUpdatedChatRoom = new ChatRoom();
        partialUpdatedChatRoom.setId(chatRoom.getId());

        partialUpdatedChatRoom
            .lastAction(UPDATED_LAST_ACTION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .participantId(UPDATED_PARTICIPANT_ID);

        restChatRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatRoom))
            )
            .andExpect(status().isOk());

        // Validate the ChatRoom in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatRoomUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedChatRoom, chatRoom), getPersistedChatRoom(chatRoom));
    }

    @Test
    @Transactional
    void fullUpdateChatRoomWithPatch() throws Exception {
        // Initialize the database
        insertedChatRoom = chatRoomRepository.saveAndFlush(chatRoom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatRoom using partial update
        ChatRoom partialUpdatedChatRoom = new ChatRoom();
        partialUpdatedChatRoom.setId(chatRoom.getId());

        partialUpdatedChatRoom
            .lastAction(UPDATED_LAST_ACTION)
            .lastConnectionDate(UPDATED_LAST_CONNECTION_DATE)
            .muted(UPDATED_MUTED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .participantId(UPDATED_PARTICIPANT_ID);

        restChatRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatRoom))
            )
            .andExpect(status().isOk());

        // Validate the ChatRoom in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatRoomUpdatableFieldsEquals(partialUpdatedChatRoom, getPersistedChatRoom(partialUpdatedChatRoom));
    }

    @Test
    @Transactional
    void patchNonExistingChatRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chatRoomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChatRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChatRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatRoom.setId(longCount.incrementAndGet());

        // Create the ChatRoom
        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDto(chatRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatRoomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(chatRoomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChatRoom() throws Exception {
        // Initialize the database
        insertedChatRoom = chatRoomRepository.saveAndFlush(chatRoom);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the chatRoom
        restChatRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, chatRoom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return chatRoomRepository.count();
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

    protected ChatRoom getPersistedChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.findById(chatRoom.getId()).orElseThrow();
    }

    protected void assertPersistedChatRoomToMatchAllProperties(ChatRoom expectedChatRoom) {
        assertChatRoomAllPropertiesEquals(expectedChatRoom, getPersistedChatRoom(expectedChatRoom));
    }

    protected void assertPersistedChatRoomToMatchUpdatableProperties(ChatRoom expectedChatRoom) {
        assertChatRoomAllUpdatablePropertiesEquals(expectedChatRoom, getPersistedChatRoom(expectedChatRoom));
    }
}
