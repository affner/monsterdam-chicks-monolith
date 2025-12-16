package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.UserReportAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.UserReport;
import com.monsterdam.app.domain.enumeration.ReportCategory;
import com.monsterdam.app.domain.enumeration.ReportStatus;
import com.monsterdam.app.repository.UserReportRepository;
import com.monsterdam.app.service.dto.UserReportDTO;
import com.monsterdam.app.service.mapper.UserReportMapper;
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
 * Integration tests for the {@link UserReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserReportResourceIT {

    private static final String DEFAULT_REPORT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_DESCRIPTION = "BBBBBBBBBB";

    private static final ReportStatus DEFAULT_STATUS = ReportStatus.PENDING;
    private static final ReportStatus UPDATED_STATUS = ReportStatus.REVIEWED;

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

    private static final ReportCategory DEFAULT_REPORT_CATEGORY = ReportCategory.POST_REPORT;
    private static final ReportCategory UPDATED_REPORT_CATEGORY = ReportCategory.COMMENT_REPORT;

    private static final Long DEFAULT_REPORTER_ID = 1L;
    private static final Long UPDATED_REPORTER_ID = 2L;

    private static final Long DEFAULT_REPORTED_ID = 1L;
    private static final Long UPDATED_REPORTED_ID = 2L;

    private static final Long DEFAULT_MULTIMEDIA_ID = 1L;
    private static final Long UPDATED_MULTIMEDIA_ID = 2L;

    private static final Long DEFAULT_MESSAGE_ID = 1L;
    private static final Long UPDATED_MESSAGE_ID = 2L;

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final Long DEFAULT_COMMENT_ID = 1L;
    private static final Long UPDATED_COMMENT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/user-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserReportRepository userReportRepository;

    @Autowired
    private UserReportMapper userReportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserReportMockMvc;

    private UserReport userReport;

    private UserReport insertedUserReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserReport createEntity() {
        return new UserReport()
            .reportDescription(DEFAULT_REPORT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .deletedDate(DEFAULT_DELETED_DATE)
            .reportCategory(DEFAULT_REPORT_CATEGORY)
            .reporterId(DEFAULT_REPORTER_ID)
            .reportedId(DEFAULT_REPORTED_ID)
            .multimediaId(DEFAULT_MULTIMEDIA_ID)
            .messageId(DEFAULT_MESSAGE_ID)
            .postId(DEFAULT_POST_ID)
            .commentId(DEFAULT_COMMENT_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserReport createUpdatedEntity() {
        return new UserReport()
            .reportDescription(UPDATED_REPORT_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .reportCategory(UPDATED_REPORT_CATEGORY)
            .reporterId(UPDATED_REPORTER_ID)
            .reportedId(UPDATED_REPORTED_ID)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID);
    }

    @BeforeEach
    void initTest() {
        userReport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUserReport != null) {
            userReportRepository.delete(insertedUserReport);
            insertedUserReport = null;
        }
    }

    @Test
    @Transactional
    void createUserReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);
        var returnedUserReportDTO = om.readValue(
            restUserReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserReportDTO.class
        );

        // Validate the UserReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserReport = userReportMapper.toEntity(returnedUserReportDTO);
        assertUserReportUpdatableFieldsEquals(returnedUserReport, getPersistedUserReport(returnedUserReport));

        insertedUserReport = returnedUserReport;
    }

    @Test
    @Transactional
    void createUserReportWithExistingId() throws Exception {
        // Create the UserReport with an existing ID
        userReport.setId(1L);
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userReport.setStatus(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        restUserReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userReport.setCreatedDate(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        restUserReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userReport.setReportCategory(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        restUserReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReporterIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userReport.setReporterId(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        restUserReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportedIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userReport.setReportedId(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        restUserReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserReports() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        // Get all the userReportList
        restUserReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportDescription").value(hasItem(DEFAULT_REPORT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].reportCategory").value(hasItem(DEFAULT_REPORT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].reporterId").value(hasItem(DEFAULT_REPORTER_ID.intValue())))
            .andExpect(jsonPath("$.[*].reportedId").value(hasItem(DEFAULT_REPORTED_ID.intValue())))
            .andExpect(jsonPath("$.[*].multimediaId").value(hasItem(DEFAULT_MULTIMEDIA_ID.intValue())))
            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())))
            .andExpect(jsonPath("$.[*].commentId").value(hasItem(DEFAULT_COMMENT_ID.intValue())));
    }

    @Test
    @Transactional
    void getUserReport() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        // Get the userReport
        restUserReportMockMvc
            .perform(get(ENTITY_API_URL_ID, userReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userReport.getId().intValue()))
            .andExpect(jsonPath("$.reportDescription").value(DEFAULT_REPORT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()))
            .andExpect(jsonPath("$.reportCategory").value(DEFAULT_REPORT_CATEGORY.toString()))
            .andExpect(jsonPath("$.reporterId").value(DEFAULT_REPORTER_ID.intValue()))
            .andExpect(jsonPath("$.reportedId").value(DEFAULT_REPORTED_ID.intValue()))
            .andExpect(jsonPath("$.multimediaId").value(DEFAULT_MULTIMEDIA_ID.intValue()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()))
            .andExpect(jsonPath("$.commentId").value(DEFAULT_COMMENT_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserReport() throws Exception {
        // Get the userReport
        restUserReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserReport() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userReport
        UserReport updatedUserReport = userReportRepository.findById(userReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserReport are not directly saved in db
        em.detach(updatedUserReport);
        updatedUserReport
            .reportDescription(UPDATED_REPORT_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .reportCategory(UPDATED_REPORT_CATEGORY)
            .reporterId(UPDATED_REPORTER_ID)
            .reportedId(UPDATED_REPORTED_ID)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID);
        UserReportDTO userReportDTO = userReportMapper.toDto(updatedUserReport);

        restUserReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserReportToMatchAllProperties(updatedUserReport);
    }

    @Test
    @Transactional
    void putNonExistingUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserReportWithPatch() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userReport using partial update
        UserReport partialUpdatedUserReport = new UserReport();
        partialUpdatedUserReport.setId(userReport.getId());

        partialUpdatedUserReport
            .reportDescription(UPDATED_REPORT_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .reporterId(UPDATED_REPORTER_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID);

        restUserReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserReport))
            )
            .andExpect(status().isOk());

        // Validate the UserReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserReport, userReport),
            getPersistedUserReport(userReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserReportWithPatch() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userReport using partial update
        UserReport partialUpdatedUserReport = new UserReport();
        partialUpdatedUserReport.setId(userReport.getId());

        partialUpdatedUserReport
            .reportDescription(UPDATED_REPORT_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE)
            .reportCategory(UPDATED_REPORT_CATEGORY)
            .reporterId(UPDATED_REPORTER_ID)
            .reportedId(UPDATED_REPORTED_ID)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID);

        restUserReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserReport))
            )
            .andExpect(status().isOk());

        // Validate the UserReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserReportUpdatableFieldsEquals(partialUpdatedUserReport, getPersistedUserReport(partialUpdatedUserReport));
    }

    @Test
    @Transactional
    void patchNonExistingUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserReport() throws Exception {
        // Initialize the database
        insertedUserReport = userReportRepository.saveAndFlush(userReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userReport
        restUserReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, userReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userReportRepository.count();
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

    protected UserReport getPersistedUserReport(UserReport userReport) {
        return userReportRepository.findById(userReport.getId()).orElseThrow();
    }

    protected void assertPersistedUserReportToMatchAllProperties(UserReport expectedUserReport) {
        assertUserReportAllPropertiesEquals(expectedUserReport, getPersistedUserReport(expectedUserReport));
    }

    protected void assertPersistedUserReportToMatchUpdatableProperties(UserReport expectedUserReport) {
        assertUserReportAllUpdatablePropertiesEquals(expectedUserReport, getPersistedUserReport(expectedUserReport));
    }
}
