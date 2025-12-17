package com.monsterdam.app.web.crud;

import static com.monsterdam.app.domain.HelpQuestionAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.HelpQuestion;
import com.monsterdam.app.repository.HelpQuestionRepository;
import com.monsterdam.app.service.dto.HelpQuestionDTO;
import com.monsterdam.app.service.mapper.HelpQuestionMapper;
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
 * Integration tests for the {@link HelpQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HelpQuestionResourceIT {

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

    private static final Instant DEFAULT_DELETED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/crud/help-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HelpQuestionRepository helpQuestionRepository;

    @Autowired
    private HelpQuestionMapper helpQuestionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHelpQuestionMockMvc;

    private HelpQuestion helpQuestion;

    private HelpQuestion insertedHelpQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpQuestion createEntity() {
        return new HelpQuestion()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
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
    public static HelpQuestion createUpdatedEntity() {
        return new HelpQuestion()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        helpQuestion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHelpQuestion != null) {
            helpQuestionRepository.delete(insertedHelpQuestion);
            insertedHelpQuestion = null;
        }
    }

    @Test
    @Transactional
    void createHelpQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);
        var returnedHelpQuestionDTO = om.readValue(
            restHelpQuestionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpQuestionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HelpQuestionDTO.class
        );

        // Validate the HelpQuestion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHelpQuestion = helpQuestionMapper.toEntity(returnedHelpQuestionDTO);
        assertHelpQuestionUpdatableFieldsEquals(returnedHelpQuestion, getPersistedHelpQuestion(returnedHelpQuestion));

        insertedHelpQuestion = returnedHelpQuestion;
    }

    @Test
    @Transactional
    void createHelpQuestionWithExistingId() throws Exception {
        // Create the HelpQuestion with an existing ID
        helpQuestion.setId(1L);
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHelpQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpQuestionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HelpQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        helpQuestion.setTitle(null);

        // Create the HelpQuestion, which fails.
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        restHelpQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpQuestionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        helpQuestion.setCreatedDate(null);

        // Create the HelpQuestion, which fails.
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        restHelpQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpQuestionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHelpQuestions() throws Exception {
        // Initialize the database
        insertedHelpQuestion = helpQuestionRepository.saveAndFlush(helpQuestion);

        // Get all the helpQuestionList
        restHelpQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getHelpQuestion() throws Exception {
        // Initialize the database
        insertedHelpQuestion = helpQuestionRepository.saveAndFlush(helpQuestion);

        // Get the helpQuestion
        restHelpQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, helpQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(helpQuestion.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHelpQuestion() throws Exception {
        // Get the helpQuestion
        restHelpQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHelpQuestion() throws Exception {
        // Initialize the database
        insertedHelpQuestion = helpQuestionRepository.saveAndFlush(helpQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpQuestion
        HelpQuestion updatedHelpQuestion = helpQuestionRepository.findById(helpQuestion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHelpQuestion are not directly saved in db
        em.detach(updatedHelpQuestion);
        updatedHelpQuestion
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(updatedHelpQuestion);

        restHelpQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpQuestionDTO))
            )
            .andExpect(status().isOk());

        // Validate the HelpQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHelpQuestionToMatchAllProperties(updatedHelpQuestion);
    }

    @Test
    @Transactional
    void putNonExistingHelpQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHelpQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHelpQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpQuestionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHelpQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedHelpQuestion = helpQuestionRepository.saveAndFlush(helpQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpQuestion using partial update
        HelpQuestion partialUpdatedHelpQuestion = new HelpQuestion();
        partialUpdatedHelpQuestion.setId(helpQuestion.getId());

        partialUpdatedHelpQuestion.title(UPDATED_TITLE).createdDate(UPDATED_CREATED_DATE).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restHelpQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHelpQuestion))
            )
            .andExpect(status().isOk());

        // Validate the HelpQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHelpQuestionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHelpQuestion, helpQuestion),
            getPersistedHelpQuestion(helpQuestion)
        );
    }

    @Test
    @Transactional
    void fullUpdateHelpQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedHelpQuestion = helpQuestionRepository.saveAndFlush(helpQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpQuestion using partial update
        HelpQuestion partialUpdatedHelpQuestion = new HelpQuestion();
        partialUpdatedHelpQuestion.setId(helpQuestion.getId());

        partialUpdatedHelpQuestion
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restHelpQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHelpQuestion))
            )
            .andExpect(status().isOk());

        // Validate the HelpQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHelpQuestionUpdatableFieldsEquals(partialUpdatedHelpQuestion, getPersistedHelpQuestion(partialUpdatedHelpQuestion));
    }

    @Test
    @Transactional
    void patchNonExistingHelpQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, helpQuestionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(helpQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHelpQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(helpQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHelpQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpQuestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(helpQuestionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHelpQuestion() throws Exception {
        // Initialize the database
        insertedHelpQuestion = helpQuestionRepository.saveAndFlush(helpQuestion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the helpQuestion
        restHelpQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, helpQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return helpQuestionRepository.count();
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

    protected HelpQuestion getPersistedHelpQuestion(HelpQuestion helpQuestion) {
        return helpQuestionRepository.findById(helpQuestion.getId()).orElseThrow();
    }

    protected void assertPersistedHelpQuestionToMatchAllProperties(HelpQuestion expectedHelpQuestion) {
        assertHelpQuestionAllPropertiesEquals(expectedHelpQuestion, getPersistedHelpQuestion(expectedHelpQuestion));
    }

    protected void assertPersistedHelpQuestionToMatchUpdatableProperties(HelpQuestion expectedHelpQuestion) {
        assertHelpQuestionAllUpdatablePropertiesEquals(expectedHelpQuestion, getPersistedHelpQuestion(expectedHelpQuestion));
    }
}
