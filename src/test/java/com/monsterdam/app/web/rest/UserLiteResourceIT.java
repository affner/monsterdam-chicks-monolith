package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.UserLiteAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.enumeration.UserGender;
import com.monsterdam.app.repository.UserLiteRepository;
import com.monsterdam.app.service.dto.UserLiteDTO;
import com.monsterdam.app.service.mapper.UserLiteMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link UserLiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserLiteResourceIT {

    private static final String DEFAULT_THUMBNAIL_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_S_3_KEY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final UserGender DEFAULT_GENDER = UserGender.MALE;
    private static final UserGender UPDATED_GENDER = UserGender.FEMALE;

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

    private static final String DEFAULT_NICK_NAME = "s52eh79awbyc";
    private static final String UPDATED_NICK_NAME = "lrb8nonf";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-lites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserLiteRepository userLiteRepository;

    @Autowired
    private UserLiteMapper userLiteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserLiteMockMvc;

    private UserLite userLite;

    private UserLite insertedUserLite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserLite createEntity() {
        return new UserLite()
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .birthDate(DEFAULT_BIRTH_DATE)
            .gender(DEFAULT_GENDER)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .deletedDate(DEFAULT_DELETED_DATE)
            .nickName(DEFAULT_NICK_NAME)
            .fullName(DEFAULT_FULL_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserLite createUpdatedEntity() {
        return new UserLite()
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .nickName(UPDATED_NICK_NAME)
            .fullName(UPDATED_FULL_NAME);
    }

    @BeforeEach
    void initTest() {
        userLite = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUserLite != null) {
            userLiteRepository.delete(insertedUserLite);
            insertedUserLite = null;
        }
    }

    @Test
    @Transactional
    void createUserLite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);
        var returnedUserLiteDTO = om.readValue(
            restUserLiteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userLiteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserLiteDTO.class
        );

        // Validate the UserLite in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserLite = userLiteMapper.toEntity(returnedUserLiteDTO);
        assertUserLiteUpdatableFieldsEquals(returnedUserLite, getPersistedUserLite(returnedUserLite));

        insertedUserLite = returnedUserLite;
    }

    @Test
    @Transactional
    void createUserLiteWithExistingId() throws Exception {
        // Create the UserLite with an existing ID
        userLite.setId(1L);
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserLite in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBirthDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userLite.setBirthDate(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userLite.setGender(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userLite.setCreatedDate(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNickNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userLite.setNickName(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userLite.setFullName(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserLites() throws Exception {
        // Initialize the database
        insertedUserLite = userLiteRepository.saveAndFlush(userLite);

        // Get all the userLiteList
        restUserLiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userLite.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)));
    }

    @Test
    @Transactional
    void getUserLite() throws Exception {
        // Initialize the database
        insertedUserLite = userLiteRepository.saveAndFlush(userLite);

        // Get the userLite
        restUserLiteMockMvc
            .perform(get(ENTITY_API_URL_ID, userLite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userLite.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailS3Key").value(DEFAULT_THUMBNAIL_S_3_KEY))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()))
            .andExpect(jsonPath("$.nickName").value(DEFAULT_NICK_NAME))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME));
    }

    @Test
    @Transactional
    void getNonExistingUserLite() throws Exception {
        // Get the userLite
        restUserLiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserLite() throws Exception {
        // Initialize the database
        insertedUserLite = userLiteRepository.saveAndFlush(userLite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userLite
        UserLite updatedUserLite = userLiteRepository.findById(userLite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserLite are not directly saved in db
        em.detach(updatedUserLite);
        updatedUserLite
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .nickName(UPDATED_NICK_NAME)
            .fullName(UPDATED_FULL_NAME);
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(updatedUserLite);

        restUserLiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userLiteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userLiteDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserLite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserLiteToMatchAllProperties(updatedUserLite);
    }

    @Test
    @Transactional
    void putNonExistingUserLite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userLiteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userLiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserLite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userLiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserLite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userLiteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserLite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserLiteWithPatch() throws Exception {
        // Initialize the database
        insertedUserLite = userLiteRepository.saveAndFlush(userLite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userLite using partial update
        UserLite partialUpdatedUserLite = new UserLite();
        partialUpdatedUserLite.setId(userLite.getId());

        partialUpdatedUserLite
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .deletedDate(UPDATED_DELETED_DATE)
            .nickName(UPDATED_NICK_NAME);

        restUserLiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserLite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserLite))
            )
            .andExpect(status().isOk());

        // Validate the UserLite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserLiteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUserLite, userLite), getPersistedUserLite(userLite));
    }

    @Test
    @Transactional
    void fullUpdateUserLiteWithPatch() throws Exception {
        // Initialize the database
        insertedUserLite = userLiteRepository.saveAndFlush(userLite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userLite using partial update
        UserLite partialUpdatedUserLite = new UserLite();
        partialUpdatedUserLite.setId(userLite.getId());

        partialUpdatedUserLite
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .nickName(UPDATED_NICK_NAME)
            .fullName(UPDATED_FULL_NAME);

        restUserLiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserLite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserLite))
            )
            .andExpect(status().isOk());

        // Validate the UserLite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserLiteUpdatableFieldsEquals(partialUpdatedUserLite, getPersistedUserLite(partialUpdatedUserLite));
    }

    @Test
    @Transactional
    void patchNonExistingUserLite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userLiteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userLiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserLite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userLiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserLite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userLiteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserLite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserLite() throws Exception {
        // Initialize the database
        insertedUserLite = userLiteRepository.saveAndFlush(userLite);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userLite
        restUserLiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, userLite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userLiteRepository.count();
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

    protected UserLite getPersistedUserLite(UserLite userLite) {
        return userLiteRepository.findById(userLite.getId()).orElseThrow();
    }

    protected void assertPersistedUserLiteToMatchAllProperties(UserLite expectedUserLite) {
        assertUserLiteAllPropertiesEquals(expectedUserLite, getPersistedUserLite(expectedUserLite));
    }

    protected void assertPersistedUserLiteToMatchUpdatableProperties(UserLite expectedUserLite) {
        assertUserLiteAllUpdatablePropertiesEquals(expectedUserLite, getPersistedUserLite(expectedUserLite));
    }
}
