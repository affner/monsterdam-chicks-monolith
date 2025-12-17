package com.monsterdam.app.web.crud;

import com.monsterdam.app.repository.ModerationActionRepository;
import com.monsterdam.app.service.ModerationActionService;
import com.monsterdam.app.service.dto.ModerationActionDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.ModerationAction}.
 */
@RestController
@RequestMapping("/api/crud/moderation-actions")
public class ModerationActionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ModerationActionResource.class);

    private static final String ENTITY_NAME = "moderationAction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModerationActionService moderationActionService;

    private final ModerationActionRepository moderationActionRepository;

    public ModerationActionResource(
        ModerationActionService moderationActionService,
        ModerationActionRepository moderationActionRepository
    ) {
        this.moderationActionService = moderationActionService;
        this.moderationActionRepository = moderationActionRepository;
    }

    /**
     * {@code POST  /moderation-actions} : Create a new moderationAction.
     *
     * @param moderationActionDTO the moderationActionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moderationActionDTO, or with status {@code 400 (Bad Request)} if the moderationAction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ModerationActionDTO> createModerationAction(@Valid @RequestBody ModerationActionDTO moderationActionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ModerationAction : {}", moderationActionDTO);
        if (moderationActionDTO.getId() != null) {
            throw new BadRequestAlertException("A new moderationAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        moderationActionDTO = moderationActionService.save(moderationActionDTO);
        return ResponseEntity.created(new URI("/api/crud/moderation-actions/" + moderationActionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, moderationActionDTO.getId().toString()))
            .body(moderationActionDTO);
    }

    /**
     * {@code PUT  /moderation-actions/:id} : Updates an existing moderationAction.
     *
     * @param id the id of the moderationActionDTO to save.
     * @param moderationActionDTO the moderationActionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moderationActionDTO,
     * or with status {@code 400 (Bad Request)} if the moderationActionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moderationActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ModerationActionDTO> updateModerationAction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ModerationActionDTO moderationActionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ModerationAction : {}, {}", id, moderationActionDTO);
        if (moderationActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moderationActionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moderationActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moderationActionDTO = moderationActionService.update(moderationActionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moderationActionDTO.getId().toString()))
            .body(moderationActionDTO);
    }

    /**
     * {@code PATCH  /moderation-actions/:id} : Partial updates given fields of an existing moderationAction, field will ignore if it is null
     *
     * @param id the id of the moderationActionDTO to save.
     * @param moderationActionDTO the moderationActionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moderationActionDTO,
     * or with status {@code 400 (Bad Request)} if the moderationActionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moderationActionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moderationActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ModerationActionDTO> partialUpdateModerationAction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ModerationActionDTO moderationActionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ModerationAction partially : {}, {}", id, moderationActionDTO);
        if (moderationActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moderationActionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moderationActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ModerationActionDTO> result = moderationActionService.partialUpdate(moderationActionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moderationActionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /moderation-actions} : get all the moderationActions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moderationActions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ModerationActionDTO>> getAllModerationActions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ModerationActions");
        Page<ModerationActionDTO> page = moderationActionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /moderation-actions/:id} : get the "id" moderationAction.
     *
     * @param id the id of the moderationActionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moderationActionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ModerationActionDTO> getModerationAction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ModerationAction : {}", id);
        Optional<ModerationActionDTO> moderationActionDTO = moderationActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moderationActionDTO);
    }

    /**
     * {@code DELETE  /moderation-actions/:id} : delete the "id" moderationAction.
     *
     * @param id the id of the moderationActionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModerationAction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ModerationAction : {}", id);
        moderationActionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
