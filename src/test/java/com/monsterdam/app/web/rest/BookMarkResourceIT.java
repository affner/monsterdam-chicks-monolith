package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.BookMarkAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.BookMark;
import com.monsterdam.app.repository.BookMarkRepository;
import com.monsterdam.app.service.dto.BookMarkDTO;
import com.monsterdam.app.service.mapper.BookMarkMapper;
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
 * Integration tests for the {@link BookMarkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookMarkResourceIT {

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

    private static final String ENTITY_API_URL = "/api/book-marks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookMarkRepository bookMarkRepository;

    @Autowired
    private BookMarkMapper bookMarkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookMarkMockMvc;

    private BookMark bookMark;

    private BookMark insertedBookMark;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookMark createEntity() {
        return new BookMark()
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookMark createUpdatedEntity() {
        return new BookMark()
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    void initTest() {
        bookMark = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBookMark != null) {
            bookMarkRepository.delete(insertedBookMark);
            insertedBookMark = null;
        }
    }

    @Test
    @Transactional
    void createBookMark() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);
        var returnedBookMarkDTO = om.readValue(
            restBookMarkMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookMarkDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookMarkDTO.class
        );

        // Validate the BookMark in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookMark = bookMarkMapper.toEntity(returnedBookMarkDTO);
        assertBookMarkUpdatableFieldsEquals(returnedBookMark, getPersistedBookMark(returnedBookMark));

        insertedBookMark = returnedBookMark;
    }

    @Test
    @Transactional
    void createBookMarkWithExistingId() throws Exception {
        // Create the BookMark with an existing ID
        bookMark.setId(1L);
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookMarkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookMark in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookMark.setCreatedDate(null);

        // Create the BookMark, which fails.
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        restBookMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookMarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookMark.setIsDeleted(null);

        // Create the BookMark, which fails.
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        restBookMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookMarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookMarks() throws Exception {
        // Initialize the database
        insertedBookMark = bookMarkRepository.saveAndFlush(bookMark);

        // Get all the bookMarkList
        restBookMarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookMark.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)));
    }

    @Test
    @Transactional
    void getBookMark() throws Exception {
        // Initialize the database
        insertedBookMark = bookMarkRepository.saveAndFlush(bookMark);

        // Get the bookMark
        restBookMarkMockMvc
            .perform(get(ENTITY_API_URL_ID, bookMark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookMark.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED));
    }

    @Test
    @Transactional
    void getNonExistingBookMark() throws Exception {
        // Get the bookMark
        restBookMarkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookMark() throws Exception {
        // Initialize the database
        insertedBookMark = bookMarkRepository.saveAndFlush(bookMark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookMark
        BookMark updatedBookMark = bookMarkRepository.findById(bookMark.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookMark are not directly saved in db
        em.detach(updatedBookMark);
        updatedBookMark
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(updatedBookMark);

        restBookMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookMarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookMarkDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookMarkToMatchAllProperties(updatedBookMark);
    }

    @Test
    @Transactional
    void putNonExistingBookMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookMarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookMarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookMarkWithPatch() throws Exception {
        // Initialize the database
        insertedBookMark = bookMarkRepository.saveAndFlush(bookMark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookMark using partial update
        BookMark partialUpdatedBookMark = new BookMark();
        partialUpdatedBookMark.setId(bookMark.getId());

        partialUpdatedBookMark.lastModifiedBy(UPDATED_LAST_MODIFIED_BY).isDeleted(UPDATED_IS_DELETED);

        restBookMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookMark))
            )
            .andExpect(status().isOk());

        // Validate the BookMark in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookMarkUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBookMark, bookMark), getPersistedBookMark(bookMark));
    }

    @Test
    @Transactional
    void fullUpdateBookMarkWithPatch() throws Exception {
        // Initialize the database
        insertedBookMark = bookMarkRepository.saveAndFlush(bookMark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookMark using partial update
        BookMark partialUpdatedBookMark = new BookMark();
        partialUpdatedBookMark.setId(bookMark.getId());

        partialUpdatedBookMark
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restBookMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookMark))
            )
            .andExpect(status().isOk());

        // Validate the BookMark in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookMarkUpdatableFieldsEquals(partialUpdatedBookMark, getPersistedBookMark(partialUpdatedBookMark));
    }

    @Test
    @Transactional
    void patchNonExistingBookMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookMarkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookMark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookMark.setId(longCount.incrementAndGet());

        // Create the BookMark
        BookMarkDTO bookMarkDTO = bookMarkMapper.toDto(bookMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMarkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookMarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookMark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookMark() throws Exception {
        // Initialize the database
        insertedBookMark = bookMarkRepository.saveAndFlush(bookMark);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookMark
        restBookMarkMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookMark.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookMarkRepository.count();
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

    protected BookMark getPersistedBookMark(BookMark bookMark) {
        return bookMarkRepository.findById(bookMark.getId()).orElseThrow();
    }

    protected void assertPersistedBookMarkToMatchAllProperties(BookMark expectedBookMark) {
        assertBookMarkAllPropertiesEquals(expectedBookMark, getPersistedBookMark(expectedBookMark));
    }

    protected void assertPersistedBookMarkToMatchUpdatableProperties(BookMark expectedBookMark) {
        assertBookMarkAllUpdatablePropertiesEquals(expectedBookMark, getPersistedBookMark(expectedBookMark));
    }
}
