package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.PostCommentRepository;
import com.monsterdam.app.service.PostCommentService;
import com.monsterdam.app.service.dto.PostCommentDTO;
import com.monsterdam.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.monsterdam.app.domain.PostComment}.
 */
@RestController
@RequestMapping("/api")
public class PostCommentResource {

    private static final Logger LOG = LoggerFactory.getLogger(PostCommentResource.class);

    private static final String ENTITY_NAME = "postComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostCommentService postCommentService;

    private final PostCommentRepository postCommentRepository;

    public PostCommentResource(PostCommentService postCommentService, PostCommentRepository postCommentRepository) {
        this.postCommentService = postCommentService;
        this.postCommentRepository = postCommentRepository;
    }

    /**
     * {@code POST  /post-comments} : Create a new postComment.
     *
     * @param postCommentDTO the postCommentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postCommentDTO, or with status {@code 400 (Bad Request)} if the postComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/post-comments")
    public ResponseEntity<PostCommentDTO> createPostComment(@Valid @RequestBody PostCommentDTO postCommentDTO) throws URISyntaxException {
        LOG.debug("REST request to save PostComment : {}", postCommentDTO);
        if (postCommentDTO.getId() != null) {
            throw new BadRequestAlertException("A new postComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        postCommentDTO = postCommentService.save(postCommentDTO);
        return ResponseEntity.created(new URI("/api/post-comments/" + postCommentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, postCommentDTO.getId().toString()))
            .body(postCommentDTO);
    }

    /**
     * {@code PUT  /post-comments/:id} : Updates an existing postComment.
     *
     * @param id the id of the postCommentDTO to save.
     * @param postCommentDTO the postCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postCommentDTO,
     * or with status {@code 400 (Bad Request)} if the postCommentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/post-comments/{id}")
    public ResponseEntity<PostCommentDTO> updatePostComment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostCommentDTO postCommentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PostComment : {}, {}", id, postCommentDTO);
        if (postCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        postCommentDTO = postCommentService.update(postCommentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postCommentDTO.getId().toString()))
            .body(postCommentDTO);
    }

    /**
     * {@code PATCH  /post-comments/:id} : Partial updates given fields of an existing postComment, field will ignore if it is null
     *
     * @param id the id of the postCommentDTO to save.
     * @param postCommentDTO the postCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postCommentDTO,
     * or with status {@code 400 (Bad Request)} if the postCommentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the postCommentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the postCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/post-comments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostCommentDTO> partialUpdatePostComment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostCommentDTO postCommentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PostComment partially : {}, {}", id, postCommentDTO);
        if (postCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostCommentDTO> result = postCommentService.partialUpdate(postCommentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postCommentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /post-comments} : get all the postComments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postComments in body.
     */
    @GetMapping("/post-comments")
    public ResponseEntity<List<PostCommentDTO>> getAllPostComments(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of PostComments");
        Page<PostCommentDTO> page = postCommentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /post-comments/:id} : get the "id" postComment.
     *
     * @param id the id of the postCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/post-comments/{id}")
    public ResponseEntity<PostCommentDTO> getPostComment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PostComment : {}", id);
        Optional<PostCommentDTO> postCommentDTO = postCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postCommentDTO);
    }

    /**
     * {@code DELETE  /post-comments/:id} : delete the "id" postComment.
     *
     * @param id the id of the postCommentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/post-comments/{id}")
    public ResponseEntity<Void> deletePostComment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PostComment : {}", id);
        postCommentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/post-comments} : get all the post-comments without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of post-comments in body.
     */
    @GetMapping("/logical/post-comments")
    public ResponseEntity<List<PostCommentDTO>> getAllLogicalPostComments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PostComments without logical deletions");
        Page<PostCommentDTO> page = postCommentService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/post-comments/:id} : get the "id" PostComment if not logically deleted.
     *
     * @param id the id of the PostCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PostCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/post-comments/{id}")
    public ResponseEntity<PostCommentDTO> getLogicalPostComment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical PostComment : {}", id);
        Optional<PostCommentDTO> postCommentDTO = postCommentService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(postCommentDTO);
    }

    /**
     * {@code DELETE  /logical/post-comments/:id} : logically delete the "id" PostComment.
     *
     * @param id the id of the PostCommentDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/post-comments/{id}")
    public ResponseEntity<Void> logicalDeletePostComment(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete PostComment : {}", id);
        if (!postCommentRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        postCommentService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/post-comments/:id/restore} : restore a logically deleted PostComment.
     *
     * @param id the id of the PostComment to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored PostCommentDTO.
     */
    @PutMapping("/logical/post-comments/{id}/restore")
    public ResponseEntity<PostCommentDTO> restorePostComment(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore PostComment : {}", id);
        if (!postCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        postCommentService.restore(id);
        Optional<PostCommentDTO> restored = postCommentService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
