package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.PostCommentAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PostComment;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.repository.PostCommentRepository;
import com.monsterdam.app.service.dto.PostCommentDTO;
import com.monsterdam.app.service.mapper.PostCommentMapper;
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
 * Integration tests for the {@link PostCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostCommentResourceIT {

    private static final String DEFAULT_COMMENT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_LIKE_COUNT = 1;
    private static final Integer UPDATED_LIKE_COUNT = 2;

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

    private static final String ENTITY_API_URL = "/api/post-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostCommentMapper postCommentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostCommentMockMvc;

    private PostComment postComment;

    private PostComment insertedPostComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostComment createEntity(EntityManager em) {
        PostComment postComment = new PostComment()
            .commentContent(DEFAULT_COMMENT_CONTENT)
            .likeCount(DEFAULT_LIKE_COUNT)
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
        postComment.setCommenter(userLite);
        return postComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostComment createUpdatedEntity(EntityManager em) {
        PostComment updatedPostComment = new PostComment()
            .commentContent(UPDATED_COMMENT_CONTENT)
            .likeCount(UPDATED_LIKE_COUNT)
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
        updatedPostComment.setCommenter(userLite);
        return updatedPostComment;
    }

    @BeforeEach
    void initTest() {
        postComment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPostComment != null) {
            postCommentRepository.delete(insertedPostComment);
            insertedPostComment = null;
        }
    }

    @Test
    @Transactional
    void createPostComment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);
        var returnedPostCommentDTO = om.readValue(
            restPostCommentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postCommentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PostCommentDTO.class
        );

        // Validate the PostComment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPostComment = postCommentMapper.toEntity(returnedPostCommentDTO);
        assertPostCommentUpdatableFieldsEquals(returnedPostComment, getPersistedPostComment(returnedPostComment));

        insertedPostComment = returnedPostComment;
    }

    @Test
    @Transactional
    void createPostCommentWithExistingId() throws Exception {
        // Create the PostComment with an existing ID
        postComment.setId(1L);
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postCommentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostComment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postComment.setCreatedDate(null);

        // Create the PostComment, which fails.
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        restPostCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postCommentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostComments() throws Exception {
        // Initialize the database
        insertedPostComment = postCommentRepository.saveAndFlush(postComment);

        // Get all the postCommentList
        restPostCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].commentContent").value(hasItem(DEFAULT_COMMENT_CONTENT)))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPostComment() throws Exception {
        // Initialize the database
        insertedPostComment = postCommentRepository.saveAndFlush(postComment);

        // Get the postComment
        restPostCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, postComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postComment.getId().intValue()))
            .andExpect(jsonPath("$.commentContent").value(DEFAULT_COMMENT_CONTENT))
            .andExpect(jsonPath("$.likeCount").value(DEFAULT_LIKE_COUNT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPostComment() throws Exception {
        // Get the postComment
        restPostCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostComment() throws Exception {
        // Initialize the database
        insertedPostComment = postCommentRepository.saveAndFlush(postComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postComment
        PostComment updatedPostComment = postCommentRepository.findById(postComment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostComment are not directly saved in db
        em.detach(updatedPostComment);
        updatedPostComment
            .commentContent(UPDATED_COMMENT_CONTENT)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(updatedPostComment);

        restPostCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postCommentDTO))
            )
            .andExpect(status().isOk());

        // Validate the PostComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPostCommentToMatchAllProperties(updatedPostComment);
    }

    @Test
    @Transactional
    void putNonExistingPostComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postCommentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostCommentWithPatch() throws Exception {
        // Initialize the database
        insertedPostComment = postCommentRepository.saveAndFlush(postComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postComment using partial update
        PostComment partialUpdatedPostComment = new PostComment();
        partialUpdatedPostComment.setId(postComment.getId());

        partialUpdatedPostComment
            .commentContent(UPDATED_COMMENT_CONTENT)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPostCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostComment))
            )
            .andExpect(status().isOk());

        // Validate the PostComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostCommentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPostComment, postComment),
            getPersistedPostComment(postComment)
        );
    }

    @Test
    @Transactional
    void fullUpdatePostCommentWithPatch() throws Exception {
        // Initialize the database
        insertedPostComment = postCommentRepository.saveAndFlush(postComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postComment using partial update
        PostComment partialUpdatedPostComment = new PostComment();
        partialUpdatedPostComment.setId(postComment.getId());

        partialUpdatedPostComment
            .commentContent(UPDATED_COMMENT_CONTENT)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restPostCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostComment))
            )
            .andExpect(status().isOk());

        // Validate the PostComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostCommentUpdatableFieldsEquals(partialUpdatedPostComment, getPersistedPostComment(partialUpdatedPostComment));
    }

    @Test
    @Transactional
    void patchNonExistingPostComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postCommentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(postCommentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostComment() throws Exception {
        // Initialize the database
        insertedPostComment = postCommentRepository.saveAndFlush(postComment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the postComment
        restPostCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, postComment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return postCommentRepository.count();
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

    protected PostComment getPersistedPostComment(PostComment postComment) {
        return postCommentRepository.findById(postComment.getId()).orElseThrow();
    }

    protected void assertPersistedPostCommentToMatchAllProperties(PostComment expectedPostComment) {
        assertPostCommentAllPropertiesEquals(expectedPostComment, getPersistedPostComment(expectedPostComment));
    }

    protected void assertPersistedPostCommentToMatchUpdatableProperties(PostComment expectedPostComment) {
        assertPostCommentAllUpdatablePropertiesEquals(expectedPostComment, getPersistedPostComment(expectedPostComment));
    }
}
