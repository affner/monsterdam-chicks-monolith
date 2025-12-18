package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.CommentMentionRepository;
import com.monsterdam.app.service.CommentMentionService;
import com.monsterdam.app.service.dto.CommentMentionDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.CommentMention}.
 */
@RestController
@RequestMapping("/api")
public class CommentMentionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommentMentionResource.class);

    private static final String ENTITY_NAME = "commentMention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentMentionService commentMentionService;

    private final CommentMentionRepository commentMentionRepository;

    public CommentMentionResource(CommentMentionService commentMentionService, CommentMentionRepository commentMentionRepository) {
        this.commentMentionService = commentMentionService;
        this.commentMentionRepository = commentMentionRepository;
    }

    /**
     * {@code POST  /comment-mentions} : Create a new commentMention.
     *
     * @param commentMentionDTO the commentMentionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentMentionDTO, or with status {@code 400 (Bad Request)} if the commentMention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comment-mentions")
    public ResponseEntity<CommentMentionDTO> createCommentMention(@Valid @RequestBody CommentMentionDTO commentMentionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CommentMention : {}", commentMentionDTO);
        if (commentMentionDTO.getId() != null) {
            throw new BadRequestAlertException("A new commentMention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        commentMentionDTO = commentMentionService.save(commentMentionDTO);
        return ResponseEntity.created(new URI("/api/comment-mentions/" + commentMentionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, commentMentionDTO.getId().toString()))
            .body(commentMentionDTO);
    }

    /**
     * {@code PUT  /comment-mentions/:id} : Updates an existing commentMention.
     *
     * @param id the id of the commentMentionDTO to save.
     * @param commentMentionDTO the commentMentionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentMentionDTO,
     * or with status {@code 400 (Bad Request)} if the commentMentionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentMentionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comment-mentions/{id}")
    public ResponseEntity<CommentMentionDTO> updateCommentMention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommentMentionDTO commentMentionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CommentMention : {}, {}", id, commentMentionDTO);
        if (commentMentionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentMentionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentMentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        commentMentionDTO = commentMentionService.update(commentMentionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentMentionDTO.getId().toString()))
            .body(commentMentionDTO);
    }

    /**
     * {@code PATCH  /comment-mentions/:id} : Partial updates given fields of an existing commentMention, field will ignore if it is null
     *
     * @param id the id of the commentMentionDTO to save.
     * @param commentMentionDTO the commentMentionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentMentionDTO,
     * or with status {@code 400 (Bad Request)} if the commentMentionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commentMentionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commentMentionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comment-mentions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommentMentionDTO> partialUpdateCommentMention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommentMentionDTO commentMentionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CommentMention partially : {}, {}", id, commentMentionDTO);
        if (commentMentionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentMentionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentMentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommentMentionDTO> result = commentMentionService.partialUpdate(commentMentionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentMentionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /comment-mentions} : get all the commentMentions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commentMentions in body.
     */
    @GetMapping("/comment-mentions")
    public ResponseEntity<List<CommentMentionDTO>> getAllCommentMentions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CommentMentions");
        Page<CommentMentionDTO> page = commentMentionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comment-mentions/:id} : get the "id" commentMention.
     *
     * @param id the id of the commentMentionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentMentionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comment-mentions/{id}")
    public ResponseEntity<CommentMentionDTO> getCommentMention(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CommentMention : {}", id);
        Optional<CommentMentionDTO> commentMentionDTO = commentMentionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentMentionDTO);
    }

    /**
     * {@code DELETE  /comment-mentions/:id} : delete the "id" commentMention.
     *
     * @param id the id of the commentMentionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comment-mentions/{id}")
    public ResponseEntity<Void> deleteCommentMention(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CommentMention : {}", id);
        commentMentionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/comment-mentions} : get all the comment-mentions without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comment-mentions in body.
     */
    @GetMapping("/logical/comment-mentions")
    public ResponseEntity<List<CommentMentionDTO>> getAllLogicalCommentMentions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CommentMentions without logical deletions");
        Page<CommentMentionDTO> page = commentMentionService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/comment-mentions/:id} : get the "id" CommentMention if not logically deleted.
     *
     * @param id the id of the CommentMentionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the CommentMentionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/comment-mentions/{id}")
    public ResponseEntity<CommentMentionDTO> getLogicalCommentMention(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical CommentMention : {}", id);
        Optional<CommentMentionDTO> commentMentionDTO = commentMentionService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(commentMentionDTO);
    }

    /**
     * {@code DELETE  /logical/comment-mentions/:id} : logically delete the "id" CommentMention.
     *
     * @param id the id of the CommentMentionDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/comment-mentions/{id}")
    public ResponseEntity<Void> logicalDeleteCommentMention(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete CommentMention : {}", id);
        if (!commentMentionRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        commentMentionService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/comment-mentions/:id/restore} : restore a logically deleted CommentMention.
     *
     * @param id the id of the CommentMention to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored CommentMentionDTO.
     */
    @PutMapping("/logical/comment-mentions/{id}/restore")
    public ResponseEntity<CommentMentionDTO> restoreCommentMention(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore CommentMention : {}", id);
        if (!commentMentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        commentMentionService.restore(id);
        Optional<CommentMentionDTO> restored = commentMentionService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
