package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.TrialLinkRepository;
import com.monsterdam.app.service.TrialLinkService;
import com.monsterdam.app.service.dto.TrialLinkDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.TrialLink}.
 */
@RestController
@RequestMapping("/api")
public class TrialLinkResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrialLinkResource.class);

    private static final String ENTITY_NAME = "trialLink";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrialLinkService trialLinkService;

    private final TrialLinkRepository trialLinkRepository;

    public TrialLinkResource(TrialLinkService trialLinkService, TrialLinkRepository trialLinkRepository) {
        this.trialLinkService = trialLinkService;
        this.trialLinkRepository = trialLinkRepository;
    }

    /**
     * {@code POST  /trial-links} : Create a new trialLink.
     *
     * @param trialLinkDTO the trialLinkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trialLinkDTO, or with status {@code 400 (Bad Request)} if the trialLink has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trial-links")
    public ResponseEntity<TrialLinkDTO> createTrialLink(@Valid @RequestBody TrialLinkDTO trialLinkDTO) throws URISyntaxException {
        LOG.debug("REST request to save TrialLink : {}", trialLinkDTO);
        if (trialLinkDTO.getId() != null) {
            throw new BadRequestAlertException("A new trialLink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trialLinkDTO = trialLinkService.save(trialLinkDTO);
        return ResponseEntity.created(new URI("/api/trial-links/" + trialLinkDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, trialLinkDTO.getId().toString()))
            .body(trialLinkDTO);
    }

    /**
     * {@code PUT  /trial-links/:id} : Updates an existing trialLink.
     *
     * @param id the id of the trialLinkDTO to save.
     * @param trialLinkDTO the trialLinkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trialLinkDTO,
     * or with status {@code 400 (Bad Request)} if the trialLinkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trialLinkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trial-links/{id}")
    public ResponseEntity<TrialLinkDTO> updateTrialLink(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrialLinkDTO trialLinkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrialLink : {}, {}", id, trialLinkDTO);
        if (trialLinkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trialLinkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trialLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trialLinkDTO = trialLinkService.update(trialLinkDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trialLinkDTO.getId().toString()))
            .body(trialLinkDTO);
    }

    /**
     * {@code PATCH  /trial-links/:id} : Partial updates given fields of an existing trialLink, field will ignore if it is null
     *
     * @param id the id of the trialLinkDTO to save.
     * @param trialLinkDTO the trialLinkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trialLinkDTO,
     * or with status {@code 400 (Bad Request)} if the trialLinkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trialLinkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trialLinkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trial-links/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrialLinkDTO> partialUpdateTrialLink(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrialLinkDTO trialLinkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrialLink partially : {}, {}", id, trialLinkDTO);
        if (trialLinkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trialLinkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trialLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrialLinkDTO> result = trialLinkService.partialUpdate(trialLinkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trialLinkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trial-links} : get all the trialLinks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trialLinks in body.
     */
    @GetMapping("/trial-links")
    public ResponseEntity<List<TrialLinkDTO>> getAllTrialLinks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of TrialLinks");
        Page<TrialLinkDTO> page = trialLinkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trial-links/:id} : get the "id" trialLink.
     *
     * @param id the id of the trialLinkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trialLinkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trial-links/{id}")
    public ResponseEntity<TrialLinkDTO> getTrialLink(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TrialLink : {}", id);
        Optional<TrialLinkDTO> trialLinkDTO = trialLinkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trialLinkDTO);
    }

    /**
     * {@code DELETE  /trial-links/:id} : delete the "id" trialLink.
     *
     * @param id the id of the trialLinkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trial-links/{id}")
    public ResponseEntity<Void> deleteTrialLink(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TrialLink : {}", id);
        trialLinkService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/trial-links} : get all the trial-links without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trial-links in body.
     */
    @GetMapping("/logical/trial-links")
    public ResponseEntity<List<TrialLinkDTO>> getAllLogicalTrialLinks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of TrialLinks without logical deletions");
        Page<TrialLinkDTO> page = trialLinkService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/trial-links/:id} : get the "id" TrialLink if not logically deleted.
     *
     * @param id the id of the TrialLinkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the TrialLinkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/trial-links/{id}")
    public ResponseEntity<TrialLinkDTO> getLogicalTrialLink(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical TrialLink : {}", id);
        Optional<TrialLinkDTO> trialLinkDTO = trialLinkService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(trialLinkDTO);
    }

    /**
     * {@code DELETE  /logical/trial-links/:id} : logically delete the "id" TrialLink.
     *
     * @param id the id of the TrialLinkDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/trial-links/{id}")
    public ResponseEntity<Void> logicalDeleteTrialLink(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete TrialLink : {}", id);
        if (!trialLinkRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        trialLinkService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/trial-links/:id/restore} : restore a logically deleted TrialLink.
     *
     * @param id the id of the TrialLink to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored TrialLinkDTO.
     */
    @PutMapping("/logical/trial-links/{id}/restore")
    public ResponseEntity<TrialLinkDTO> restoreTrialLink(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore TrialLink : {}", id);
        if (!trialLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        trialLinkService.restore(id);
        Optional<TrialLinkDTO> restored = trialLinkService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
