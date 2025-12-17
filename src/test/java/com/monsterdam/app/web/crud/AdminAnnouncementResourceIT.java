package com.monsterdam.app.web.crud;

import static com.monsterdam.app.domain.AdminAnnouncementAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.AdminAnnouncement;
import com.monsterdam.app.domain.enumeration.AdminAnnouncementType;
import com.monsterdam.app.repository.AdminAnnouncementRepository;
import com.monsterdam.app.service.dto.AdminAnnouncementDTO;
import com.monsterdam.app.service.mapper.AdminAnnouncementMapper;
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
 * Integration tests for the {@link AdminAnnouncementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdminAnnouncementResourceIT {

    private static final AdminAnnouncementType DEFAULT_ANNOUNCEMENT_TYPE = AdminAnnouncementType.BANNER;
    private static final AdminAnnouncementType UPDATED_ANNOUNCEMENT_TYPE = AdminAnnouncementType.SYSTEM_UPDATE;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_ANNOUNCER_ID = 1L;
    private static final Long UPDATED_ANNOUNCER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/crud/admin-announcements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdminAnnouncementRepository adminAnnouncementRepository;

    @Autowired
    private AdminAnnouncementMapper adminAnnouncementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminAnnouncementMockMvc;

    private AdminAnnouncement adminAnnouncement;

    private AdminAnnouncement insertedAdminAnnouncement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminAnnouncement createEntity() {
        return new AdminAnnouncement()
            .announcementType(DEFAULT_ANNOUNCEMENT_TYPE)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .announcerId(DEFAULT_ANNOUNCER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminAnnouncement createUpdatedEntity() {
        return new AdminAnnouncement()
            .announcementType(UPDATED_ANNOUNCEMENT_TYPE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .announcerId(UPDATED_ANNOUNCER_ID);
    }

    @BeforeEach
    void initTest() {
        adminAnnouncement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAdminAnnouncement != null) {
            adminAnnouncementRepository.delete(insertedAdminAnnouncement);
            insertedAdminAnnouncement = null;
        }
    }

    @Test
    @Transactional
    void createAdminAnnouncement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);
        var returnedAdminAnnouncementDTO = om.readValue(
            restAdminAnnouncementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminAnnouncementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdminAnnouncementDTO.class
        );

        // Validate the AdminAnnouncement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdminAnnouncement = adminAnnouncementMapper.toEntity(returnedAdminAnnouncementDTO);
        assertAdminAnnouncementUpdatableFieldsEquals(returnedAdminAnnouncement, getPersistedAdminAnnouncement(returnedAdminAnnouncement));

        insertedAdminAnnouncement = returnedAdminAnnouncement;
    }

    @Test
    @Transactional
    void createAdminAnnouncementWithExistingId() throws Exception {
        // Create the AdminAnnouncement with an existing ID
        adminAnnouncement.setId(1L);
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminAnnouncementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminAnnouncementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdminAnnouncement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnnouncementTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminAnnouncement.setAnnouncementType(null);

        // Create the AdminAnnouncement, which fails.
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        restAdminAnnouncementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminAnnouncementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminAnnouncement.setTitle(null);

        // Create the AdminAnnouncement, which fails.
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        restAdminAnnouncementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminAnnouncementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminAnnouncement.setContent(null);

        // Create the AdminAnnouncement, which fails.
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        restAdminAnnouncementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminAnnouncementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminAnnouncement.setCreatedDate(null);

        // Create the AdminAnnouncement, which fails.
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        restAdminAnnouncementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminAnnouncementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnnouncerIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adminAnnouncement.setAnnouncerId(null);

        // Create the AdminAnnouncement, which fails.
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        restAdminAnnouncementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminAnnouncementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdminAnnouncements() throws Exception {
        // Initialize the database
        insertedAdminAnnouncement = adminAnnouncementRepository.saveAndFlush(adminAnnouncement);

        // Get all the adminAnnouncementList
        restAdminAnnouncementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminAnnouncement.getId().intValue())))
            .andExpect(jsonPath("$.[*].announcementType").value(hasItem(DEFAULT_ANNOUNCEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].announcerId").value(hasItem(DEFAULT_ANNOUNCER_ID.intValue())));
    }

    @Test
    @Transactional
    void getAdminAnnouncement() throws Exception {
        // Initialize the database
        insertedAdminAnnouncement = adminAnnouncementRepository.saveAndFlush(adminAnnouncement);

        // Get the adminAnnouncement
        restAdminAnnouncementMockMvc
            .perform(get(ENTITY_API_URL_ID, adminAnnouncement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adminAnnouncement.getId().intValue()))
            .andExpect(jsonPath("$.announcementType").value(DEFAULT_ANNOUNCEMENT_TYPE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.announcerId").value(DEFAULT_ANNOUNCER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAdminAnnouncement() throws Exception {
        // Get the adminAnnouncement
        restAdminAnnouncementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdminAnnouncement() throws Exception {
        // Initialize the database
        insertedAdminAnnouncement = adminAnnouncementRepository.saveAndFlush(adminAnnouncement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adminAnnouncement
        AdminAnnouncement updatedAdminAnnouncement = adminAnnouncementRepository.findById(adminAnnouncement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdminAnnouncement are not directly saved in db
        em.detach(updatedAdminAnnouncement);
        updatedAdminAnnouncement
            .announcementType(UPDATED_ANNOUNCEMENT_TYPE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .announcerId(UPDATED_ANNOUNCER_ID);
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(updatedAdminAnnouncement);

        restAdminAnnouncementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminAnnouncementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adminAnnouncementDTO))
            )
            .andExpect(status().isOk());

        // Validate the AdminAnnouncement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdminAnnouncementToMatchAllProperties(updatedAdminAnnouncement);
    }

    @Test
    @Transactional
    void putNonExistingAdminAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminAnnouncementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminAnnouncementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adminAnnouncementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminAnnouncement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdminAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminAnnouncementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adminAnnouncementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminAnnouncement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdminAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminAnnouncementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adminAnnouncementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminAnnouncement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdminAnnouncementWithPatch() throws Exception {
        // Initialize the database
        insertedAdminAnnouncement = adminAnnouncementRepository.saveAndFlush(adminAnnouncement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adminAnnouncement using partial update
        AdminAnnouncement partialUpdatedAdminAnnouncement = new AdminAnnouncement();
        partialUpdatedAdminAnnouncement.setId(adminAnnouncement.getId());

        partialUpdatedAdminAnnouncement
            .announcementType(UPDATED_ANNOUNCEMENT_TYPE)
            .title(UPDATED_TITLE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .announcerId(UPDATED_ANNOUNCER_ID);

        restAdminAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminAnnouncement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdminAnnouncement))
            )
            .andExpect(status().isOk());

        // Validate the AdminAnnouncement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdminAnnouncementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdminAnnouncement, adminAnnouncement),
            getPersistedAdminAnnouncement(adminAnnouncement)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdminAnnouncementWithPatch() throws Exception {
        // Initialize the database
        insertedAdminAnnouncement = adminAnnouncementRepository.saveAndFlush(adminAnnouncement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adminAnnouncement using partial update
        AdminAnnouncement partialUpdatedAdminAnnouncement = new AdminAnnouncement();
        partialUpdatedAdminAnnouncement.setId(adminAnnouncement.getId());

        partialUpdatedAdminAnnouncement
            .announcementType(UPDATED_ANNOUNCEMENT_TYPE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .announcerId(UPDATED_ANNOUNCER_ID);

        restAdminAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminAnnouncement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdminAnnouncement))
            )
            .andExpect(status().isOk());

        // Validate the AdminAnnouncement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdminAnnouncementUpdatableFieldsEquals(
            partialUpdatedAdminAnnouncement,
            getPersistedAdminAnnouncement(partialUpdatedAdminAnnouncement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAdminAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adminAnnouncementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adminAnnouncementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminAnnouncement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdminAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adminAnnouncementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminAnnouncement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdminAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminAnnouncementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(adminAnnouncementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminAnnouncement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdminAnnouncement() throws Exception {
        // Initialize the database
        insertedAdminAnnouncement = adminAnnouncementRepository.saveAndFlush(adminAnnouncement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the adminAnnouncement
        restAdminAnnouncementMockMvc
            .perform(delete(ENTITY_API_URL_ID, adminAnnouncement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return adminAnnouncementRepository.count();
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

    protected AdminAnnouncement getPersistedAdminAnnouncement(AdminAnnouncement adminAnnouncement) {
        return adminAnnouncementRepository.findById(adminAnnouncement.getId()).orElseThrow();
    }

    protected void assertPersistedAdminAnnouncementToMatchAllProperties(AdminAnnouncement expectedAdminAnnouncement) {
        assertAdminAnnouncementAllPropertiesEquals(expectedAdminAnnouncement, getPersistedAdminAnnouncement(expectedAdminAnnouncement));
    }

    protected void assertPersistedAdminAnnouncementToMatchUpdatableProperties(AdminAnnouncement expectedAdminAnnouncement) {
        assertAdminAnnouncementAllUpdatablePropertiesEquals(
            expectedAdminAnnouncement,
            getPersistedAdminAnnouncement(expectedAdminAnnouncement)
        );
    }
}
