package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.UserAssociationAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.UserAssociation;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.enumeration.AssociationStatus;
import com.monsterdam.app.repository.UserAssociationRepository;
import com.monsterdam.app.service.dto.UserAssociationDTO;
import com.monsterdam.app.service.mapper.UserAssociationMapper;
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
 * Integration tests for the {@link UserAssociationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAssociationResourceIT {

    private static final Instant DEFAULT_REQUESTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUESTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AssociationStatus DEFAULT_STATUS = AssociationStatus.REQUESTED;
    private static final AssociationStatus UPDATED_STATUS = AssociationStatus.APPROVED;

    private static final String DEFAULT_ASSOCIATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_ASSOCIATION_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELETED_DATE = null;
    private static final Instant UPDATED_DELETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-associations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAssociationRepository userAssociationRepository;

    @Autowired
    private UserAssociationMapper userAssociationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAssociationMockMvc;

    private UserAssociation userAssociation;

    private UserAssociation insertedUserAssociation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAssociation createEntity(EntityManager em) {
        UserAssociation userAssociation = new UserAssociation()
            .requestedDate(DEFAULT_REQUESTED_DATE)
            .status(DEFAULT_STATUS)
            .associationToken(DEFAULT_ASSOCIATION_TOKEN)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .deletedDate(DEFAULT_DELETED_DATE);
        // Add required entity
        UserLite userLite;
        if (TestUtil.findAll(em, UserLite.class).isEmpty()) {
            userLite = UserLiteResourceIT.createEntity();
            em.persist(userLite);
            em.flush();
        } else {
            userLite = TestUtil.findAll(em, UserLite.class).get(0);
        }
        userAssociation.setOwner(userLite);
        return userAssociation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAssociation createUpdatedEntity(EntityManager em) {
        UserAssociation updatedUserAssociation = new UserAssociation()
            .requestedDate(UPDATED_REQUESTED_DATE)
            .status(UPDATED_STATUS)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        // Add required entity
        UserLite userLite;
        if (TestUtil.findAll(em, UserLite.class).isEmpty()) {
            userLite = UserLiteResourceIT.createUpdatedEntity();
            em.persist(userLite);
            em.flush();
        } else {
            userLite = TestUtil.findAll(em, UserLite.class).get(0);
        }
        updatedUserAssociation.setOwner(userLite);
        return updatedUserAssociation;
    }

    @BeforeEach
    void initTest() {
        userAssociation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserAssociation != null) {
            userAssociationRepository.delete(insertedUserAssociation);
            insertedUserAssociation = null;
        }
    }

    @Test
    @Transactional
    void createUserAssociation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);
        var returnedUserAssociationDTO = om.readValue(
            restUserAssociationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAssociationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAssociationDTO.class
        );

        // Validate the UserAssociation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserAssociation = userAssociationMapper.toEntity(returnedUserAssociationDTO);
        assertUserAssociationUpdatableFieldsEquals(returnedUserAssociation, getPersistedUserAssociation(returnedUserAssociation));

        insertedUserAssociation = returnedUserAssociation;
    }

    @Test
    @Transactional
    void createUserAssociationWithExistingId() throws Exception {
        // Create the UserAssociation with an existing ID
        userAssociation.setId(1L);
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAssociationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAssociationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserAssociation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRequestedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAssociation.setRequestedDate(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        restUserAssociationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAssociationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssociationTokenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAssociation.setAssociationToken(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        restUserAssociationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAssociationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiryDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAssociation.setExpiryDate(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        restUserAssociationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAssociationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAssociation.setCreatedDate(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        restUserAssociationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAssociationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserAssociations() throws Exception {
        // Initialize the database
        insertedUserAssociation = userAssociationRepository.saveAndFlush(userAssociation);

        // Get all the userAssociationList
        restUserAssociationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAssociation.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestedDate").value(hasItem(DEFAULT_REQUESTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].associationToken").value(hasItem(DEFAULT_ASSOCIATION_TOKEN)))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserAssociation() throws Exception {
        // Initialize the database
        insertedUserAssociation = userAssociationRepository.saveAndFlush(userAssociation);

        // Get the userAssociation
        restUserAssociationMockMvc
            .perform(get(ENTITY_API_URL_ID, userAssociation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAssociation.getId().intValue()))
            .andExpect(jsonPath("$.requestedDate").value(DEFAULT_REQUESTED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.associationToken").value(DEFAULT_ASSOCIATION_TOKEN))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserAssociation() throws Exception {
        // Get the userAssociation
        restUserAssociationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAssociation() throws Exception {
        // Initialize the database
        insertedUserAssociation = userAssociationRepository.saveAndFlush(userAssociation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAssociation
        UserAssociation updatedUserAssociation = userAssociationRepository.findById(userAssociation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAssociation are not directly saved in db
        em.detach(updatedUserAssociation);
        updatedUserAssociation
            .requestedDate(UPDATED_REQUESTED_DATE)
            .status(UPDATED_STATUS)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(updatedUserAssociation);

        restUserAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAssociationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAssociationDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAssociation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAssociationToMatchAllProperties(updatedUserAssociation);
    }

    @Test
    @Transactional
    void putNonExistingUserAssociation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAssociationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAssociation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAssociation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAssociation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAssociation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAssociationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAssociation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAssociationWithPatch() throws Exception {
        // Initialize the database
        insertedUserAssociation = userAssociationRepository.saveAndFlush(userAssociation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAssociation using partial update
        UserAssociation partialUpdatedUserAssociation = new UserAssociation();
        partialUpdatedUserAssociation.setId(userAssociation.getId());

        partialUpdatedUserAssociation
            .status(UPDATED_STATUS)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restUserAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAssociation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAssociation))
            )
            .andExpect(status().isOk());

        // Validate the UserAssociation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAssociationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAssociation, userAssociation),
            getPersistedUserAssociation(userAssociation)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserAssociationWithPatch() throws Exception {
        // Initialize the database
        insertedUserAssociation = userAssociationRepository.saveAndFlush(userAssociation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAssociation using partial update
        UserAssociation partialUpdatedUserAssociation = new UserAssociation();
        partialUpdatedUserAssociation.setId(userAssociation.getId());

        partialUpdatedUserAssociation
            .requestedDate(UPDATED_REQUESTED_DATE)
            .status(UPDATED_STATUS)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restUserAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAssociation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAssociation))
            )
            .andExpect(status().isOk());

        // Validate the UserAssociation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAssociationUpdatableFieldsEquals(
            partialUpdatedUserAssociation,
            getPersistedUserAssociation(partialUpdatedUserAssociation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserAssociation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAssociationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAssociation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAssociation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAssociation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAssociation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAssociationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userAssociationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAssociation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAssociation() throws Exception {
        // Initialize the database
        insertedUserAssociation = userAssociationRepository.saveAndFlush(userAssociation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAssociation
        restUserAssociationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAssociation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAssociationRepository.count();
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

    protected UserAssociation getPersistedUserAssociation(UserAssociation userAssociation) {
        return userAssociationRepository.findById(userAssociation.getId()).orElseThrow();
    }

    protected void assertPersistedUserAssociationToMatchAllProperties(UserAssociation expectedUserAssociation) {
        assertUserAssociationAllPropertiesEquals(expectedUserAssociation, getPersistedUserAssociation(expectedUserAssociation));
    }

    protected void assertPersistedUserAssociationToMatchUpdatableProperties(UserAssociation expectedUserAssociation) {
        assertUserAssociationAllUpdatablePropertiesEquals(expectedUserAssociation, getPersistedUserAssociation(expectedUserAssociation));
    }
}
