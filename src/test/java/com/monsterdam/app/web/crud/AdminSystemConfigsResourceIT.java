package com.monsterdam.app.web.crud;

import static com.monsterdam.app.domain.AdminSystemConfigsAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.AdminSystemConfigs;
import com.monsterdam.app.domain.enumeration.ConfigurationCategory;
import com.monsterdam.app.domain.enumeration.ConfigurationValueType;
import com.monsterdam.app.repository.AdminSystemConfigsRepository;
import com.monsterdam.app.service.dto.AdminSystemConfigsDTO;
import com.monsterdam.app.service.mapper.AdminSystemConfigsMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link AdminSystemConfigsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdminSystemConfigsResourceIT {

    private static final String DEFAULT_CONFIG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ConfigurationValueType DEFAULT_CONFIG_VALUE_TYPE = ConfigurationValueType.STRING;
    private static final ConfigurationValueType UPDATED_CONFIG_VALUE_TYPE = ConfigurationValueType.INTEGER;

    private static final ConfigurationCategory DEFAULT_CONFIG_CATEGORY = ConfigurationCategory.GENERAL;
    private static final ConfigurationCategory UPDATED_CONFIG_CATEGORY = ConfigurationCategory.SECURITY;

    private static final byte[] DEFAULT_CONFIG_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONFIG_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONFIG_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONFIG_FILE_CONTENT_TYPE = "image/png";

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

    private static final String ENTITY_API_URL = "/api/crud/admin-system-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdminSystemConfigsRepository adminSystemConfigsRepository;

    @Autowired
    private AdminSystemConfigsMapper adminSystemConfigsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminSystemConfigsMockMvc;

    private AdminSystemConfigs adminSystemConfigs;

    private AdminSystemConfigs insertedAdminSystemConfigs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminSystemConfigs createEntity() {
        return new AdminSystemConfigs()
            .configKey(DEFAULT_CONFIG_KEY)
            .configValue(DEFAULT_CONFIG_VALUE)
            .description(DEFAULT_DESCRIPTION)
            .configValueType(DEFAULT_CONFIG_VALUE_TYPE)
            .configCategory(DEFAULT_CONFIG_CATEGORY)
            .configFile(DEFAULT_CONFIG_FILE)
            .configFileContentType(DEFAULT_CONFIG_FILE_CONTENT_TYPE)
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
    public static AdminSystemConfigs createUpdatedEntity() {
        return new AdminSystemConfigs()
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .configValueType(UPDATED_CONFIG_VALUE_TYPE)
            .configCategory(UPDATED_CONFIG_CATEGORY)
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        adminSystemConfigs = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAdminSystemConfigs != null) {
            adminSystemConfigsRepository.delete(insertedAdminSystemConfigs);
            insertedAdminSystemConfigs = null;
        }
    }

    @Test
    @Transactional
    void createAdminSystemConfigs() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);
        var returnedAdminSystemConfigsDTO = om.readValue(
            restAdminSystemConfigsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminSystemConfigsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdminSystemConfigsDTO.class
        );

        // Validate the AdminSystemConfigs in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdminSystemConfigs = adminSystemConfigsMapper.toEntity(returnedAdminSystemConfigsDTO);
        assertAdminSystemConfigsUpdatableFieldsEquals(
            returnedAdminSystemConfigs,
            getPersistedAdminSystemConfigs(returnedAdminSystemConfigs)
        );

        insertedAdminSystemConfigs = returnedAdminSystemConfigs;
    }

    @Test
    @Transactional
    void createAdminSystemConfigsWithExistingId() throws Exception {
        // Create the AdminSystemConfigs with an existing ID
        adminSystemConfigs.setId(1L);
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminSystemConfigsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminSystemConfigsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdminSystemConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConfigKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminSystemConfigs.setConfigKey(null);

        // Create the AdminSystemConfigs, which fails.
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        restAdminSystemConfigsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminSystemConfigsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConfigValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminSystemConfigs.setConfigValue(null);

        // Create the AdminSystemConfigs, which fails.
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        restAdminSystemConfigsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminSystemConfigsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminSystemConfigs.setCreatedDate(null);

        // Create the AdminSystemConfigs, which fails.
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        restAdminSystemConfigsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminSystemConfigsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdminSystemConfigs() throws Exception {
        // Initialize the database
        insertedAdminSystemConfigs = adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        // Get all the adminSystemConfigsList
        restAdminSystemConfigsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminSystemConfigs.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].configValueType").value(hasItem(DEFAULT_CONFIG_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].configCategory").value(hasItem(DEFAULT_CONFIG_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].configFileContentType").value(hasItem(DEFAULT_CONFIG_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].configFile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONFIG_FILE))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAdminSystemConfigs() throws Exception {
        // Initialize the database
        insertedAdminSystemConfigs = adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        // Get the adminSystemConfigs
        restAdminSystemConfigsMockMvc
            .perform(get(ENTITY_API_URL_ID, adminSystemConfigs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adminSystemConfigs.getId().intValue()))
            .andExpect(jsonPath("$.configKey").value(DEFAULT_CONFIG_KEY))
            .andExpect(jsonPath("$.configValue").value(DEFAULT_CONFIG_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.configValueType").value(DEFAULT_CONFIG_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.configCategory").value(DEFAULT_CONFIG_CATEGORY.toString()))
            .andExpect(jsonPath("$.configFileContentType").value(DEFAULT_CONFIG_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.configFile").value(Base64.getEncoder().encodeToString(DEFAULT_CONFIG_FILE)))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAdminSystemConfigs() throws Exception {
        // Get the adminSystemConfigs
        restAdminSystemConfigsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdminSystemConfigs() throws Exception {
        // Initialize the database
        insertedAdminSystemConfigs = adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adminSystemConfigs
        AdminSystemConfigs updatedAdminSystemConfigs = adminSystemConfigsRepository.findById(adminSystemConfigs.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdminSystemConfigs are not directly saved in db
        em.detach(updatedAdminSystemConfigs);
        updatedAdminSystemConfigs
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .configValueType(UPDATED_CONFIG_VALUE_TYPE)
            .configCategory(UPDATED_CONFIG_CATEGORY)
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(updatedAdminSystemConfigs);

        restAdminSystemConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminSystemConfigsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isOk());

        // Validate the AdminSystemConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdminSystemConfigsToMatchAllProperties(updatedAdminSystemConfigs);
    }

    @Test
    @Transactional
    void putNonExistingAdminSystemConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminSystemConfigsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminSystemConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdminSystemConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminSystemConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdminSystemConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminSystemConfigsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminSystemConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdminSystemConfigsWithPatch() throws Exception {
        // Initialize the database
        insertedAdminSystemConfigs = adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adminSystemConfigs using partial update
        AdminSystemConfigs partialUpdatedAdminSystemConfigs = new AdminSystemConfigs();
        partialUpdatedAdminSystemConfigs.setId(adminSystemConfigs.getId());

        partialUpdatedAdminSystemConfigs
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restAdminSystemConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminSystemConfigs.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdminSystemConfigs))
            )
            .andExpect(status().isOk());

        // Validate the AdminSystemConfigs in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdminSystemConfigsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdminSystemConfigs, adminSystemConfigs),
            getPersistedAdminSystemConfigs(adminSystemConfigs)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdminSystemConfigsWithPatch() throws Exception {
        // Initialize the database
        insertedAdminSystemConfigs = adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adminSystemConfigs using partial update
        AdminSystemConfigs partialUpdatedAdminSystemConfigs = new AdminSystemConfigs();
        partialUpdatedAdminSystemConfigs.setId(adminSystemConfigs.getId());

        partialUpdatedAdminSystemConfigs
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .configValueType(UPDATED_CONFIG_VALUE_TYPE)
            .configCategory(UPDATED_CONFIG_CATEGORY)
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restAdminSystemConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminSystemConfigs.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdminSystemConfigs))
            )
            .andExpect(status().isOk());

        // Validate the AdminSystemConfigs in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdminSystemConfigsUpdatableFieldsEquals(
            partialUpdatedAdminSystemConfigs,
            getPersistedAdminSystemConfigs(partialUpdatedAdminSystemConfigs)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAdminSystemConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adminSystemConfigsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminSystemConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdminSystemConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminSystemConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdminSystemConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(adminSystemConfigsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminSystemConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdminSystemConfigs() throws Exception {
        // Initialize the database
        insertedAdminSystemConfigs = adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the adminSystemConfigs
        restAdminSystemConfigsMockMvc
            .perform(delete(ENTITY_API_URL_ID, adminSystemConfigs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return adminSystemConfigsRepository.count();
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

    protected AdminSystemConfigs getPersistedAdminSystemConfigs(AdminSystemConfigs adminSystemConfigs) {
        return adminSystemConfigsRepository.findById(adminSystemConfigs.getId()).orElseThrow();
    }

    protected void assertPersistedAdminSystemConfigsToMatchAllProperties(AdminSystemConfigs expectedAdminSystemConfigs) {
        assertAdminSystemConfigsAllPropertiesEquals(expectedAdminSystemConfigs, getPersistedAdminSystemConfigs(expectedAdminSystemConfigs));
    }

    protected void assertPersistedAdminSystemConfigsToMatchUpdatableProperties(AdminSystemConfigs expectedAdminSystemConfigs) {
        assertAdminSystemConfigsAllUpdatablePropertiesEquals(
            expectedAdminSystemConfigs,
            getPersistedAdminSystemConfigs(expectedAdminSystemConfigs)
        );
    }
}
