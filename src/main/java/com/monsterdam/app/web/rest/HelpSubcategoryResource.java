package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.HelpSubcategoryRepository;
import com.monsterdam.app.service.HelpSubcategoryService;
import com.monsterdam.app.service.dto.HelpSubcategoryDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.HelpSubcategory}.
 */
@RestController
@RequestMapping("/api")
public class HelpSubcategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(HelpSubcategoryResource.class);

    private static final String ENTITY_NAME = "helpSubcategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HelpSubcategoryService helpSubcategoryService;

    private final HelpSubcategoryRepository helpSubcategoryRepository;

    public HelpSubcategoryResource(HelpSubcategoryService helpSubcategoryService, HelpSubcategoryRepository helpSubcategoryRepository) {
        this.helpSubcategoryService = helpSubcategoryService;
        this.helpSubcategoryRepository = helpSubcategoryRepository;
    }

    /**
     * {@code POST  /help-subcategories} : Create a new helpSubcategory.
     *
     * @param helpSubcategoryDTO the helpSubcategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new helpSubcategoryDTO, or with status {@code 400 (Bad Request)} if the helpSubcategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/help-subcategories")
    public ResponseEntity<HelpSubcategoryDTO> createHelpSubcategory(@Valid @RequestBody HelpSubcategoryDTO helpSubcategoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HelpSubcategory : {}", helpSubcategoryDTO);
        if (helpSubcategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new helpSubcategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        helpSubcategoryDTO = helpSubcategoryService.save(helpSubcategoryDTO);
        return ResponseEntity.created(new URI("/api/help-subcategories/" + helpSubcategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, helpSubcategoryDTO.getId().toString()))
            .body(helpSubcategoryDTO);
    }

    /**
     * {@code PUT  /help-subcategories/:id} : Updates an existing helpSubcategory.
     *
     * @param id the id of the helpSubcategoryDTO to save.
     * @param helpSubcategoryDTO the helpSubcategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpSubcategoryDTO,
     * or with status {@code 400 (Bad Request)} if the helpSubcategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the helpSubcategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/help-subcategories/{id}")
    public ResponseEntity<HelpSubcategoryDTO> updateHelpSubcategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HelpSubcategoryDTO helpSubcategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HelpSubcategory : {}, {}", id, helpSubcategoryDTO);
        if (helpSubcategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpSubcategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!helpSubcategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        helpSubcategoryDTO = helpSubcategoryService.update(helpSubcategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, helpSubcategoryDTO.getId().toString()))
            .body(helpSubcategoryDTO);
    }

    /**
     * {@code PATCH  /help-subcategories/:id} : Partial updates given fields of an existing helpSubcategory, field will ignore if it is null
     *
     * @param id the id of the helpSubcategoryDTO to save.
     * @param helpSubcategoryDTO the helpSubcategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpSubcategoryDTO,
     * or with status {@code 400 (Bad Request)} if the helpSubcategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the helpSubcategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the helpSubcategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/help-subcategories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HelpSubcategoryDTO> partialUpdateHelpSubcategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HelpSubcategoryDTO helpSubcategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HelpSubcategory partially : {}, {}", id, helpSubcategoryDTO);
        if (helpSubcategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpSubcategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!helpSubcategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HelpSubcategoryDTO> result = helpSubcategoryService.partialUpdate(helpSubcategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, helpSubcategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /help-subcategories} : get all the helpSubcategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helpSubcategories in body.
     */
    @GetMapping("/help-subcategories")
    public ResponseEntity<List<HelpSubcategoryDTO>> getAllHelpSubcategories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of HelpSubcategories");
        Page<HelpSubcategoryDTO> page = helpSubcategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /help-subcategories/:id} : get the "id" helpSubcategory.
     *
     * @param id the id of the helpSubcategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the helpSubcategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/help-subcategories/{id}")
    public ResponseEntity<HelpSubcategoryDTO> getHelpSubcategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HelpSubcategory : {}", id);
        Optional<HelpSubcategoryDTO> helpSubcategoryDTO = helpSubcategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpSubcategoryDTO);
    }

    /**
     * {@code DELETE  /help-subcategories/:id} : delete the "id" helpSubcategory.
     *
     * @param id the id of the helpSubcategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/help-subcategories/{id}")
    public ResponseEntity<Void> deleteHelpSubcategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HelpSubcategory : {}", id);
        helpSubcategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/help-subcategories} : get all the help-subcategories without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of help-subcategories in body.
     */
    @GetMapping("/logical/help-subcategories")
    public ResponseEntity<List<HelpSubcategoryDTO>> getAllLogicalHelpSubcategorys(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of HelpSubcategorys without logical deletions");
        Page<HelpSubcategoryDTO> page = helpSubcategoryService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/help-subcategories/:id} : get the "id" HelpSubcategory if not logically deleted.
     *
     * @param id the id of the HelpSubcategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the HelpSubcategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/help-subcategories/{id}")
    public ResponseEntity<HelpSubcategoryDTO> getLogicalHelpSubcategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical HelpSubcategory : {}", id);
        Optional<HelpSubcategoryDTO> helpSubcategoryDTO = helpSubcategoryService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(helpSubcategoryDTO);
    }

    /**
     * {@code DELETE  /logical/help-subcategories/:id} : logically delete the "id" HelpSubcategory.
     *
     * @param id the id of the HelpSubcategoryDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/help-subcategories/{id}")
    public ResponseEntity<Void> logicalDeleteHelpSubcategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete HelpSubcategory : {}", id);
        if (!helpSubcategoryRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        helpSubcategoryService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/help-subcategories/:id/restore} : restore a logically deleted HelpSubcategory.
     *
     * @param id the id of the HelpSubcategory to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored HelpSubcategoryDTO.
     */
    @PutMapping("/logical/help-subcategories/{id}/restore")
    public ResponseEntity<HelpSubcategoryDTO> restoreHelpSubcategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore HelpSubcategory : {}", id);
        if (!helpSubcategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        helpSubcategoryService.restore(id);
        Optional<HelpSubcategoryDTO> restored = helpSubcategoryService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
