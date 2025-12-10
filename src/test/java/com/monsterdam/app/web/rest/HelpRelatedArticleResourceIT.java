package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.HelpRelatedArticleAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.HelpRelatedArticle;
import com.monsterdam.app.repository.HelpRelatedArticleRepository;
import com.monsterdam.app.service.dto.HelpRelatedArticleDTO;
import com.monsterdam.app.service.mapper.HelpRelatedArticleMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link HelpRelatedArticleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HelpRelatedArticleResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/help-related-articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HelpRelatedArticleRepository helpRelatedArticleRepository;

    @Autowired
    private HelpRelatedArticleMapper helpRelatedArticleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHelpRelatedArticleMockMvc;

    private HelpRelatedArticle helpRelatedArticle;

    private HelpRelatedArticle insertedHelpRelatedArticle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpRelatedArticle createEntity() {
        return new HelpRelatedArticle().title(DEFAULT_TITLE).content(DEFAULT_CONTENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpRelatedArticle createUpdatedEntity() {
        return new HelpRelatedArticle().title(UPDATED_TITLE).content(UPDATED_CONTENT);
    }

    @BeforeEach
    void initTest() {
        helpRelatedArticle = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHelpRelatedArticle != null) {
            helpRelatedArticleRepository.delete(insertedHelpRelatedArticle);
            insertedHelpRelatedArticle = null;
        }
    }

    @Test
    @Transactional
    void createHelpRelatedArticle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);
        var returnedHelpRelatedArticleDTO = om.readValue(
            restHelpRelatedArticleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpRelatedArticleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HelpRelatedArticleDTO.class
        );

        // Validate the HelpRelatedArticle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHelpRelatedArticle = helpRelatedArticleMapper.toEntity(returnedHelpRelatedArticleDTO);
        assertHelpRelatedArticleUpdatableFieldsEquals(
            returnedHelpRelatedArticle,
            getPersistedHelpRelatedArticle(returnedHelpRelatedArticle)
        );

        insertedHelpRelatedArticle = returnedHelpRelatedArticle;
    }

    @Test
    @Transactional
    void createHelpRelatedArticleWithExistingId() throws Exception {
        // Create the HelpRelatedArticle with an existing ID
        helpRelatedArticle.setId(1L);
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHelpRelatedArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpRelatedArticleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HelpRelatedArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        helpRelatedArticle.setTitle(null);

        // Create the HelpRelatedArticle, which fails.
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        restHelpRelatedArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpRelatedArticleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHelpRelatedArticles() throws Exception {
        // Initialize the database
        insertedHelpRelatedArticle = helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        // Get all the helpRelatedArticleList
        restHelpRelatedArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpRelatedArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getHelpRelatedArticle() throws Exception {
        // Initialize the database
        insertedHelpRelatedArticle = helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        // Get the helpRelatedArticle
        restHelpRelatedArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, helpRelatedArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(helpRelatedArticle.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingHelpRelatedArticle() throws Exception {
        // Get the helpRelatedArticle
        restHelpRelatedArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHelpRelatedArticle() throws Exception {
        // Initialize the database
        insertedHelpRelatedArticle = helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpRelatedArticle
        HelpRelatedArticle updatedHelpRelatedArticle = helpRelatedArticleRepository.findById(helpRelatedArticle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHelpRelatedArticle are not directly saved in db
        em.detach(updatedHelpRelatedArticle);
        updatedHelpRelatedArticle.title(UPDATED_TITLE).content(UPDATED_CONTENT);
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(updatedHelpRelatedArticle);

        restHelpRelatedArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpRelatedArticleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isOk());

        // Validate the HelpRelatedArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHelpRelatedArticleToMatchAllProperties(updatedHelpRelatedArticle);
    }

    @Test
    @Transactional
    void putNonExistingHelpRelatedArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpRelatedArticleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpRelatedArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHelpRelatedArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpRelatedArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHelpRelatedArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(helpRelatedArticleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpRelatedArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHelpRelatedArticleWithPatch() throws Exception {
        // Initialize the database
        insertedHelpRelatedArticle = helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpRelatedArticle using partial update
        HelpRelatedArticle partialUpdatedHelpRelatedArticle = new HelpRelatedArticle();
        partialUpdatedHelpRelatedArticle.setId(helpRelatedArticle.getId());

        restHelpRelatedArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpRelatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHelpRelatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the HelpRelatedArticle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHelpRelatedArticleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHelpRelatedArticle, helpRelatedArticle),
            getPersistedHelpRelatedArticle(helpRelatedArticle)
        );
    }

    @Test
    @Transactional
    void fullUpdateHelpRelatedArticleWithPatch() throws Exception {
        // Initialize the database
        insertedHelpRelatedArticle = helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the helpRelatedArticle using partial update
        HelpRelatedArticle partialUpdatedHelpRelatedArticle = new HelpRelatedArticle();
        partialUpdatedHelpRelatedArticle.setId(helpRelatedArticle.getId());

        partialUpdatedHelpRelatedArticle.title(UPDATED_TITLE).content(UPDATED_CONTENT);

        restHelpRelatedArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpRelatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHelpRelatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the HelpRelatedArticle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHelpRelatedArticleUpdatableFieldsEquals(
            partialUpdatedHelpRelatedArticle,
            getPersistedHelpRelatedArticle(partialUpdatedHelpRelatedArticle)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHelpRelatedArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, helpRelatedArticleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpRelatedArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHelpRelatedArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(helpRelatedArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpRelatedArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHelpRelatedArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpRelatedArticleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(helpRelatedArticleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpRelatedArticle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHelpRelatedArticle() throws Exception {
        // Initialize the database
        insertedHelpRelatedArticle = helpRelatedArticleRepository.saveAndFlush(helpRelatedArticle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the helpRelatedArticle
        restHelpRelatedArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, helpRelatedArticle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return helpRelatedArticleRepository.count();
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

    protected HelpRelatedArticle getPersistedHelpRelatedArticle(HelpRelatedArticle helpRelatedArticle) {
        return helpRelatedArticleRepository.findById(helpRelatedArticle.getId()).orElseThrow();
    }

    protected void assertPersistedHelpRelatedArticleToMatchAllProperties(HelpRelatedArticle expectedHelpRelatedArticle) {
        assertHelpRelatedArticleAllPropertiesEquals(expectedHelpRelatedArticle, getPersistedHelpRelatedArticle(expectedHelpRelatedArticle));
    }

    protected void assertPersistedHelpRelatedArticleToMatchUpdatableProperties(HelpRelatedArticle expectedHelpRelatedArticle) {
        assertHelpRelatedArticleAllUpdatablePropertiesEquals(
            expectedHelpRelatedArticle,
            getPersistedHelpRelatedArticle(expectedHelpRelatedArticle)
        );
    }
}
