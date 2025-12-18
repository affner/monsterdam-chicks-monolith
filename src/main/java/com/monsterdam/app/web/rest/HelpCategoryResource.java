package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.HelpCategoryRepository;
import com.monsterdam.app.service.HelpCategoryService;
import com.monsterdam.app.service.dto.HelpCategoryDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.HelpCategory}.
 */
@RestController
@RequestMapping("/api")
public class HelpCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(HelpCategoryResource.class);

    private static final String ENTITY_NAME = "helpCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HelpCategoryService helpCategoryService;

    private final HelpCategoryRepository helpCategoryRepository;

    public HelpCategoryResource(HelpCategoryService helpCategoryService, HelpCategoryRepository helpCategoryRepository) {
        this.helpCategoryService = helpCategoryService;
        this.helpCategoryRepository = helpCategoryRepository;
    }

    /**
     * {@code POST  /help-categories} : Create a new helpCategory.
     *
     * @param helpCategoryDTO the helpCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new helpCategoryDTO, or with status {@code 400 (Bad Request)} if the helpCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/help-categories")
    public ResponseEntity<HelpCategoryDTO> createHelpCategory(@Valid @RequestBody HelpCategoryDTO helpCategoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HelpCategory : {}", helpCategoryDTO);
        if (helpCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new helpCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        helpCategoryDTO = helpCategoryService.save(helpCategoryDTO);
        return ResponseEntity.created(new URI("/api/help-categories/" + helpCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, helpCategoryDTO.getId().toString()))
            .body(helpCategoryDTO);
    }

    /**
     * {@code PUT  /help-categories/:id} : Updates an existing helpCategory.
     *
     * @param id the id of the helpCategoryDTO to save.
     * @param helpCategoryDTO the helpCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the helpCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the helpCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/help-categories/{id}")
    public ResponseEntity<HelpCategoryDTO> updateHelpCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HelpCategoryDTO helpCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HelpCategory : {}, {}", id, helpCategoryDTO);
        if (helpCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!helpCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        helpCategoryDTO = helpCategoryService.update(helpCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, helpCategoryDTO.getId().toString()))
            .body(helpCategoryDTO);
    }

    /**
     * {@code PATCH  /help-categories/:id} : Partial updates given fields of an existing helpCategory, field will ignore if it is null
     *
     * @param id the id of the helpCategoryDTO to save.
     * @param helpCategoryDTO the helpCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the helpCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the helpCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the helpCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/help-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HelpCategoryDTO> partialUpdateHelpCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HelpCategoryDTO helpCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HelpCategory partially : {}, {}", id, helpCategoryDTO);
        if (helpCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!helpCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HelpCategoryDTO> result = helpCategoryService.partialUpdate(helpCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, helpCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /help-categories} : get all the helpCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helpCategories in body.
     */
    @GetMapping("/help-categories")
    public ResponseEntity<List<HelpCategoryDTO>> getAllHelpCategories(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of HelpCategories");
        Page<HelpCategoryDTO> page = helpCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /help-categories/:id} : get the "id" helpCategory.
     *
     * @param id the id of the helpCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the helpCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/help-categories/{id}")
    public ResponseEntity<HelpCategoryDTO> getHelpCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HelpCategory : {}", id);
        Optional<HelpCategoryDTO> helpCategoryDTO = helpCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpCategoryDTO);
    }

    /**
     * {@code DELETE  /help-categories/:id} : delete the "id" helpCategory.
     *
     * @param id the id of the helpCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/help-categories/{id}")
    public ResponseEntity<Void> deleteHelpCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HelpCategory : {}", id);
        helpCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/help-categories} : get all the help-categories without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of help-categories in body.
     */
    @GetMapping("/logical/help-categories")
    public ResponseEntity<List<HelpCategoryDTO>> getAllLogicalHelpCategorys(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of HelpCategorys without logical deletions");
        Page<HelpCategoryDTO> page = helpCategoryService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/help-categories/:id} : get the "id" HelpCategory if not logically deleted.
     *
     * @param id the id of the HelpCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the HelpCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/help-categories/{id}")
    public ResponseEntity<HelpCategoryDTO> getLogicalHelpCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical HelpCategory : {}", id);
        Optional<HelpCategoryDTO> helpCategoryDTO = helpCategoryService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(helpCategoryDTO);
    }

    /**
     * {@code DELETE  /logical/help-categories/:id} : logically delete the "id" HelpCategory.
     *
     * @param id the id of the HelpCategoryDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/help-categories/{id}")
    public ResponseEntity<Void> logicalDeleteHelpCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete HelpCategory : {}", id);
        if (!helpCategoryRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        helpCategoryService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/help-categories/:id/restore} : restore a logically deleted HelpCategory.
     *
     * @param id the id of the HelpCategory to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored HelpCategoryDTO.
     */
    @PutMapping("/logical/help-categories/{id}/restore")
    public ResponseEntity<HelpCategoryDTO> restoreHelpCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore HelpCategory : {}", id);
        if (!helpCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        helpCategoryService.restore(id);
        Optional<HelpCategoryDTO> restored = helpCategoryService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
