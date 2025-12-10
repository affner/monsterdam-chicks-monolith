package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.PostMentionRepository;
import com.monsterdam.app.service.PostMentionService;
import com.monsterdam.app.service.dto.PostMentionDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.PostMention}.
 */
@RestController
@RequestMapping("/api/post-mentions")
public class PostMentionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PostMentionResource.class);

    private static final String ENTITY_NAME = "postMention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostMentionService postMentionService;

    private final PostMentionRepository postMentionRepository;

    public PostMentionResource(PostMentionService postMentionService, PostMentionRepository postMentionRepository) {
        this.postMentionService = postMentionService;
        this.postMentionRepository = postMentionRepository;
    }

    /**
     * {@code POST  /post-mentions} : Create a new postMention.
     *
     * @param postMentionDTO the postMentionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postMentionDTO, or with status {@code 400 (Bad Request)} if the postMention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PostMentionDTO> createPostMention(@Valid @RequestBody PostMentionDTO postMentionDTO) throws URISyntaxException {
        LOG.debug("REST request to save PostMention : {}", postMentionDTO);
        if (postMentionDTO.getId() != null) {
            throw new BadRequestAlertException("A new postMention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        postMentionDTO = postMentionService.save(postMentionDTO);
        return ResponseEntity.created(new URI("/api/post-mentions/" + postMentionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, postMentionDTO.getId().toString()))
            .body(postMentionDTO);
    }

    /**
     * {@code PUT  /post-mentions/:id} : Updates an existing postMention.
     *
     * @param id the id of the postMentionDTO to save.
     * @param postMentionDTO the postMentionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postMentionDTO,
     * or with status {@code 400 (Bad Request)} if the postMentionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postMentionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostMentionDTO> updatePostMention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostMentionDTO postMentionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PostMention : {}, {}", id, postMentionDTO);
        if (postMentionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postMentionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postMentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        postMentionDTO = postMentionService.update(postMentionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postMentionDTO.getId().toString()))
            .body(postMentionDTO);
    }

    /**
     * {@code PATCH  /post-mentions/:id} : Partial updates given fields of an existing postMention, field will ignore if it is null
     *
     * @param id the id of the postMentionDTO to save.
     * @param postMentionDTO the postMentionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postMentionDTO,
     * or with status {@code 400 (Bad Request)} if the postMentionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the postMentionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the postMentionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostMentionDTO> partialUpdatePostMention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostMentionDTO postMentionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PostMention partially : {}, {}", id, postMentionDTO);
        if (postMentionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postMentionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postMentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostMentionDTO> result = postMentionService.partialUpdate(postMentionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postMentionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /post-mentions} : get all the postMentions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postMentions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PostMentionDTO>> getAllPostMentions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of PostMentions");
        Page<PostMentionDTO> page = postMentionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /post-mentions/:id} : get the "id" postMention.
     *
     * @param id the id of the postMentionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postMentionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostMentionDTO> getPostMention(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PostMention : {}", id);
        Optional<PostMentionDTO> postMentionDTO = postMentionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postMentionDTO);
    }

    /**
     * {@code DELETE  /post-mentions/:id} : delete the "id" postMention.
     *
     * @param id the id of the postMentionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostMention(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PostMention : {}", id);
        postMentionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
