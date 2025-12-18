package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.PlatformAdminUserAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PlatformAdminUser;
import com.monsterdam.app.domain.enumeration.AdminGender;
import com.monsterdam.app.repository.PlatformAdminUserRepository;
import com.monsterdam.app.service.dto.PlatformAdminUserDTO;
import com.monsterdam.app.service.mapper.PlatformAdminUserMapper;
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
 * Integration tests for the {@link PlatformAdminUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlatformAdminUserResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_NICK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NICK_NAME = "BBBBBBBBBB";

    private static final AdminGender DEFAULT_GENDER = AdminGender.MALE;
    private static final AdminGender UPDATED_GENDER = AdminGender.FEMALE;

    private static final String DEFAULT_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_LOGIN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_LOGIN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

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

    private static final String ENTITY_API_URL = "/api/platform-admin-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlatformAdminUserRepository platformAdminUserRepository;

    @Autowired
    private PlatformAdminUserMapper platformAdminUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlatformAdminUserMockMvc;

    private PlatformAdminUser platformAdminUser;

    private PlatformAdminUser insertedPlatformAdminUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlatformAdminUser createEntity() {
        return new PlatformAdminUser()
            .fullName(DEFAULT_FULL_NAME)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .nickName(DEFAULT_NICK_NAME)
            .gender(DEFAULT_GENDER)
            .mobilePhone(DEFAULT_MOBILE_PHONE)
            .lastLoginDate(DEFAULT_LAST_LOGIN_DATE)
            .birthDate(DEFAULT_BIRTH_DATE)
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
    public static PlatformAdminUser createUpdatedEntity() {
        return new PlatformAdminUser()
            .fullName(UPDATED_FULL_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .nickName(UPDATED_NICK_NAME)
            .gender(UPDATED_GENDER)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        platformAdminUser = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPlatformAdminUser != null) {
            platformAdminUserRepository.delete(insertedPlatformAdminUser);
            insertedPlatformAdminUser = null;
        }
    }

    @Test
    @Transactional
    void createPlatformAdminUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlatformAdminUser
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);
        var returnedPlatformAdminUserDTO = om.readValue(
            restPlatformAdminUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(platformAdminUserDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlatformAdminUserDTO.class
        );

        // Validate the PlatformAdminUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlatformAdminUser = platformAdminUserMapper.toEntity(returnedPlatformAdminUserDTO);
        assertPlatformAdminUserUpdatableFieldsEquals(returnedPlatformAdminUser, getPersistedPlatformAdminUser(returnedPlatformAdminUser));

        insertedPlatformAdminUser = returnedPlatformAdminUser;
    }

    @Test
    @Transactional
    void createPlatformAdminUserWithExistingId() throws Exception {
        // Create the PlatformAdminUser with an existing ID
        platformAdminUser.setId(1L);
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlatformAdminUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(platformAdminUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlatformAdminUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        platformAdminUser.setFullName(null);

        // Create the PlatformAdminUser, which fails.
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        restPlatformAdminUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(platformAdminUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        platformAdminUser.setEmailAddress(null);

        // Create the PlatformAdminUser, which fails.
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        restPlatformAdminUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(platformAdminUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        platformAdminUser.setGender(null);

        // Create the PlatformAdminUser, which fails.
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        restPlatformAdminUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(platformAdminUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        platformAdminUser.setBirthDate(null);

        // Create the PlatformAdminUser, which fails.
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        restPlatformAdminUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(platformAdminUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        platformAdminUser.setCreatedDate(null);

        // Create the PlatformAdminUser, which fails.
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        restPlatformAdminUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(platformAdminUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlatformAdminUsers() throws Exception {
        // Initialize the database
        insertedPlatformAdminUser = platformAdminUserRepository.saveAndFlush(platformAdminUser);

        // Get all the platformAdminUserList
        restPlatformAdminUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(platformAdminUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)))
            .andExpect(jsonPath("$.[*].lastLoginDate").value(hasItem(DEFAULT_LAST_LOGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPlatformAdminUser() throws Exception {
        // Initialize the database
        insertedPlatformAdminUser = platformAdminUserRepository.saveAndFlush(platformAdminUser);

        // Get the platformAdminUser
        restPlatformAdminUserMockMvc
            .perform(get(ENTITY_API_URL_ID, platformAdminUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(platformAdminUser.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.nickName").value(DEFAULT_NICK_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE))
            .andExpect(jsonPath("$.lastLoginDate").value(DEFAULT_LAST_LOGIN_DATE.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlatformAdminUser() throws Exception {
        // Get the platformAdminUser
        restPlatformAdminUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlatformAdminUser() throws Exception {
        // Initialize the database
        insertedPlatformAdminUser = platformAdminUserRepository.saveAndFlush(platformAdminUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the platformAdminUser
        PlatformAdminUser updatedPlatformAdminUser = platformAdminUserRepository.findById(platformAdminUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlatformAdminUser are not directly saved in db
        em.detach(updatedPlatformAdminUser);
        updatedPlatformAdminUser
            .fullName(UPDATED_FULL_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .nickName(UPDATED_NICK_NAME)
            .gender(UPDATED_GENDER)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(updatedPlatformAdminUser);

        restPlatformAdminUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, platformAdminUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(platformAdminUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlatformAdminUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlatformAdminUserToMatchAllProperties(updatedPlatformAdminUser);
    }

    @Test
    @Transactional
    void putNonExistingPlatformAdminUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        platformAdminUser.setId(longCount.incrementAndGet());

        // Create the PlatformAdminUser
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlatformAdminUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, platformAdminUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(platformAdminUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlatformAdminUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlatformAdminUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        platformAdminUser.setId(longCount.incrementAndGet());

        // Create the PlatformAdminUser
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlatformAdminUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(platformAdminUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlatformAdminUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlatformAdminUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        platformAdminUser.setId(longCount.incrementAndGet());

        // Create the PlatformAdminUser
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlatformAdminUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(platformAdminUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlatformAdminUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlatformAdminUserWithPatch() throws Exception {
        // Initialize the database
        insertedPlatformAdminUser = platformAdminUserRepository.saveAndFlush(platformAdminUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the platformAdminUser using partial update
        PlatformAdminUser partialUpdatedPlatformAdminUser = new PlatformAdminUser();
        partialUpdatedPlatformAdminUser.setId(platformAdminUser.getId());

        partialUpdatedPlatformAdminUser
            .fullName(UPDATED_FULL_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restPlatformAdminUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlatformAdminUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlatformAdminUser))
            )
            .andExpect(status().isOk());

        // Validate the PlatformAdminUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlatformAdminUserUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlatformAdminUser, platformAdminUser),
            getPersistedPlatformAdminUser(platformAdminUser)
        );
    }

    @Test
    @Transactional
    void fullUpdatePlatformAdminUserWithPatch() throws Exception {
        // Initialize the database
        insertedPlatformAdminUser = platformAdminUserRepository.saveAndFlush(platformAdminUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the platformAdminUser using partial update
        PlatformAdminUser partialUpdatedPlatformAdminUser = new PlatformAdminUser();
        partialUpdatedPlatformAdminUser.setId(platformAdminUser.getId());

        partialUpdatedPlatformAdminUser
            .fullName(UPDATED_FULL_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .nickName(UPDATED_NICK_NAME)
            .gender(UPDATED_GENDER)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPlatformAdminUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlatformAdminUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlatformAdminUser))
            )
            .andExpect(status().isOk());

        // Validate the PlatformAdminUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlatformAdminUserUpdatableFieldsEquals(
            partialUpdatedPlatformAdminUser,
            getPersistedPlatformAdminUser(partialUpdatedPlatformAdminUser)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPlatformAdminUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        platformAdminUser.setId(longCount.incrementAndGet());

        // Create the PlatformAdminUser
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlatformAdminUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, platformAdminUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(platformAdminUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlatformAdminUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlatformAdminUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        platformAdminUser.setId(longCount.incrementAndGet());

        // Create the PlatformAdminUser
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlatformAdminUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(platformAdminUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlatformAdminUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlatformAdminUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        platformAdminUser.setId(longCount.incrementAndGet());

        // Create the PlatformAdminUser
        PlatformAdminUserDTO platformAdminUserDTO = platformAdminUserMapper.toDto(platformAdminUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlatformAdminUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(platformAdminUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlatformAdminUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlatformAdminUser() throws Exception {
        // Initialize the database
        insertedPlatformAdminUser = platformAdminUserRepository.saveAndFlush(platformAdminUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the platformAdminUser
        restPlatformAdminUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, platformAdminUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return platformAdminUserRepository.count();
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

    protected PlatformAdminUser getPersistedPlatformAdminUser(PlatformAdminUser platformAdminUser) {
        return platformAdminUserRepository.findById(platformAdminUser.getId()).orElseThrow();
    }

    protected void assertPersistedPlatformAdminUserToMatchAllProperties(PlatformAdminUser expectedPlatformAdminUser) {
        assertPlatformAdminUserAllPropertiesEquals(expectedPlatformAdminUser, getPersistedPlatformAdminUser(expectedPlatformAdminUser));
    }

    protected void assertPersistedPlatformAdminUserToMatchUpdatableProperties(PlatformAdminUser expectedPlatformAdminUser) {
        assertPlatformAdminUserAllUpdatablePropertiesEquals(
            expectedPlatformAdminUser,
            getPersistedPlatformAdminUser(expectedPlatformAdminUser)
        );
    }
}
