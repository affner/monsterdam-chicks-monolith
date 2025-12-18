package com.monsterdam.app.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.PostComment;
import com.monsterdam.app.repository.PostCommentRepository;
import com.monsterdam.app.service.dto.PostCommentDTO;
import com.monsterdam.app.web.rest.PostCommentResourceIT;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
class PostCommentServiceIT {

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostCommentService postCommentService;

    @Autowired
    private EntityManager em;

    private PostComment activeComment;
    private PostComment deletedComment;

    @BeforeEach
    void init() {
        postCommentRepository.deleteAll();
        activeComment = PostCommentResourceIT.createEntity(em);
        activeComment.setDeletedDate(null);
        deletedComment = PostCommentResourceIT.createEntity(em);
        deletedComment.setDeletedDate(Instant.now());
    }

    @Test
    @Transactional
    void logicalFindAllShouldSkipDeleted() {
        postCommentRepository.saveAndFlush(activeComment);
        postCommentRepository.saveAndFlush(deletedComment);

        Page<PostCommentDTO> page = postCommentService.logicalFindAll(PageRequest.of(0, 20));

        List<PostCommentDTO> results = page.getContent();
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getId()).isEqualTo(activeComment.getId());
    }

    @Test
    @Transactional
    void logicalDeleteShouldSetDeletedDate() {
        PostComment saved = postCommentRepository.saveAndFlush(activeComment);

        postCommentService.logicalDelete(saved.getId());

        PostComment reloaded = postCommentRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getDeletedDate()).isNotNull();
    }

    @Test
    @Transactional
    void restoreShouldClearDeletedDate() {
        PostComment saved = postCommentRepository.saveAndFlush(deletedComment);

        postCommentService.restore(saved.getId());

        PostComment reloaded = postCommentRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getDeletedDate()).isNull();
    }
}
