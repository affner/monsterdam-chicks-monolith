package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.AdminEmailConfigsAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.AdminEmailConfigs;
import com.monsterdam.app.domain.enumeration.EmailTemplateType;
import com.monsterdam.app.repository.AdminEmailConfigsRepository;
import com.monsterdam.app.service.dto.AdminEmailConfigsDTO;
import com.monsterdam.app.service.mapper.AdminEmailConfigsMapper;
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
 * Integration tests for the {@link AdminEmailConfigsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdminEmailConfigsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final EmailTemplateType DEFAULT_MAIL_TEMPLATE_TYPE = EmailTemplateType.NOTIFICATION;
    private static final EmailTemplateType UPDATED_MAIL_TEMPLATE_TYPE = EmailTemplateType.NEWSLETTER;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/admin-email-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdminEmailConfigsRepository adminEmailConfigsRepository;

    @Autowired
    private AdminEmailConfigsMapper adminEmailConfigsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminEmailConfigsMockMvc;

    private AdminEmailConfigs adminEmailConfigs;

    private AdminEmailConfigs insertedAdminEmailConfigs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminEmailConfigs createEntity() {
        return new AdminEmailConfigs()
            .title(DEFAULT_TITLE)
            .subject(DEFAULT_SUBJECT)
            .content(DEFAULT_CONTENT)
            .mailTemplateType(DEFAULT_MAIL_TEMPLATE_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminEmailConfigs createUpdatedEntity() {
        return new AdminEmailConfigs()
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .mailTemplateType(UPDATED_MAIL_TEMPLATE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        adminEmailConfigs = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAdminEmailConfigs != null) {
            adminEmailConfigsRepository.delete(insertedAdminEmailConfigs);
            insertedAdminEmailConfigs = null;
        }
    }

    @Test
    @Transactional
    void createAdminEmailConfigs() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);
        var returnedAdminEmailConfigsDTO = om.readValue(
            restAdminEmailConfigsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminEmailConfigsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdminEmailConfigsDTO.class
        );

        // Validate the AdminEmailConfigs in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdminEmailConfigs = adminEmailConfigsMapper.toEntity(returnedAdminEmailConfigsDTO);
        assertAdminEmailConfigsUpdatableFieldsEquals(returnedAdminEmailConfigs, getPersistedAdminEmailConfigs(returnedAdminEmailConfigs));

        insertedAdminEmailConfigs = returnedAdminEmailConfigs;
    }

    @Test
    @Transactional
    void createAdminEmailConfigsWithExistingId() throws Exception {
        // Create the AdminEmailConfigs with an existing ID
        adminEmailConfigs.setId(1L);
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminEmailConfigsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminEmailConfigsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdminEmailConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminEmailConfigs.setTitle(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminEmailConfigsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubjectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminEmailConfigs.setSubject(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminEmailConfigsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminEmailConfigs.setContent(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminEmailConfigsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMailTemplateTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminEmailConfigs.setMailTemplateType(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminEmailConfigsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminEmailConfigs.setCreatedDate(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminEmailConfigsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminEmailConfigs.setIsActive(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminEmailConfigsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdminEmailConfigs() throws Exception {
        // Initialize the database
        insertedAdminEmailConfigs = adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        // Get all the adminEmailConfigsList
        restAdminEmailConfigsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminEmailConfigs.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].mailTemplateType").value(hasItem(DEFAULT_MAIL_TEMPLATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getAdminEmailConfigs() throws Exception {
        // Initialize the database
        insertedAdminEmailConfigs = adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        // Get the adminEmailConfigs
        restAdminEmailConfigsMockMvc
            .perform(get(ENTITY_API_URL_ID, adminEmailConfigs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adminEmailConfigs.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.mailTemplateType").value(DEFAULT_MAIL_TEMPLATE_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingAdminEmailConfigs() throws Exception {
        // Get the adminEmailConfigs
        restAdminEmailConfigsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdminEmailConfigs() throws Exception {
        // Initialize the database
        insertedAdminEmailConfigs = adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adminEmailConfigs
        AdminEmailConfigs updatedAdminEmailConfigs = adminEmailConfigsRepository.findById(adminEmailConfigs.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdminEmailConfigs are not directly saved in db
        em.detach(updatedAdminEmailConfigs);
        updatedAdminEmailConfigs
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .mailTemplateType(UPDATED_MAIL_TEMPLATE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(updatedAdminEmailConfigs);

        restAdminEmailConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminEmailConfigsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isOk());

        // Validate the AdminEmailConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdminEmailConfigsToMatchAllProperties(updatedAdminEmailConfigs);
    }

    @Test
    @Transactional
    void putNonExistingAdminEmailConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminEmailConfigsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminEmailConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdminEmailConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminEmailConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdminEmailConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminEmailConfigsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminEmailConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdminEmailConfigsWithPatch() throws Exception {
        // Initialize the database
        insertedAdminEmailConfigs = adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adminEmailConfigs using partial update
        AdminEmailConfigs partialUpdatedAdminEmailConfigs = new AdminEmailConfigs();
        partialUpdatedAdminEmailConfigs.setId(adminEmailConfigs.getId());

        partialUpdatedAdminEmailConfigs.content(UPDATED_CONTENT).createdBy(UPDATED_CREATED_BY);

        restAdminEmailConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminEmailConfigs.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdminEmailConfigs))
            )
            .andExpect(status().isOk());

        // Validate the AdminEmailConfigs in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdminEmailConfigsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdminEmailConfigs, adminEmailConfigs),
            getPersistedAdminEmailConfigs(adminEmailConfigs)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdminEmailConfigsWithPatch() throws Exception {
        // Initialize the database
        insertedAdminEmailConfigs = adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adminEmailConfigs using partial update
        AdminEmailConfigs partialUpdatedAdminEmailConfigs = new AdminEmailConfigs();
        partialUpdatedAdminEmailConfigs.setId(adminEmailConfigs.getId());

        partialUpdatedAdminEmailConfigs
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .mailTemplateType(UPDATED_MAIL_TEMPLATE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);

        restAdminEmailConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminEmailConfigs.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdminEmailConfigs))
            )
            .andExpect(status().isOk());

        // Validate the AdminEmailConfigs in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdminEmailConfigsUpdatableFieldsEquals(
            partialUpdatedAdminEmailConfigs,
            getPersistedAdminEmailConfigs(partialUpdatedAdminEmailConfigs)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAdminEmailConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adminEmailConfigsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminEmailConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdminEmailConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adminEmailConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminEmailConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdminEmailConfigs() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminEmailConfigsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(adminEmailConfigsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminEmailConfigs in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdminEmailConfigs() throws Exception {
        // Initialize the database
        insertedAdminEmailConfigs = adminEmailConfigsRepository.saveAndFlush(adminEmailConfigs);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the adminEmailConfigs
        restAdminEmailConfigsMockMvc
            .perform(delete(ENTITY_API_URL_ID, adminEmailConfigs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return adminEmailConfigsRepository.count();
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

    protected AdminEmailConfigs getPersistedAdminEmailConfigs(AdminEmailConfigs adminEmailConfigs) {
        return adminEmailConfigsRepository.findById(adminEmailConfigs.getId()).orElseThrow();
    }

    protected void assertPersistedAdminEmailConfigsToMatchAllProperties(AdminEmailConfigs expectedAdminEmailConfigs) {
        assertAdminEmailConfigsAllPropertiesEquals(expectedAdminEmailConfigs, getPersistedAdminEmailConfigs(expectedAdminEmailConfigs));
    }

    protected void assertPersistedAdminEmailConfigsToMatchUpdatableProperties(AdminEmailConfigs expectedAdminEmailConfigs) {
        assertAdminEmailConfigsAllUpdatablePropertiesEquals(
            expectedAdminEmailConfigs,
            getPersistedAdminEmailConfigs(expectedAdminEmailConfigs)
        );
    }
}
