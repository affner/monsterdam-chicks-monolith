package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.TaxInfoRepository;
import com.monsterdam.app.service.TaxInfoService;
import com.monsterdam.app.service.dto.TaxInfoDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.TaxInfo}.
 */
@RestController
@RequestMapping("/api")
public class TaxInfoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaxInfoResource.class);

    private static final String ENTITY_NAME = "taxInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxInfoService taxInfoService;

    private final TaxInfoRepository taxInfoRepository;

    public TaxInfoResource(TaxInfoService taxInfoService, TaxInfoRepository taxInfoRepository) {
        this.taxInfoService = taxInfoService;
        this.taxInfoRepository = taxInfoRepository;
    }

    /**
     * {@code POST  /tax-infos} : Create a new taxInfo.
     *
     * @param taxInfoDTO the taxInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxInfoDTO, or with status {@code 400 (Bad Request)} if the taxInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tax-infos")
    public ResponseEntity<TaxInfoDTO> createTaxInfo(@Valid @RequestBody TaxInfoDTO taxInfoDTO) throws URISyntaxException {
        LOG.debug("REST request to save TaxInfo : {}", taxInfoDTO);
        if (taxInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        taxInfoDTO = taxInfoService.save(taxInfoDTO);
        return ResponseEntity.created(new URI("/api/tax-infos/" + taxInfoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, taxInfoDTO.getId().toString()))
            .body(taxInfoDTO);
    }

    /**
     * {@code PUT  /tax-infos/:id} : Updates an existing taxInfo.
     *
     * @param id the id of the taxInfoDTO to save.
     * @param taxInfoDTO the taxInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxInfoDTO,
     * or with status {@code 400 (Bad Request)} if the taxInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tax-infos/{id}")
    public ResponseEntity<TaxInfoDTO> updateTaxInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaxInfoDTO taxInfoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TaxInfo : {}, {}", id, taxInfoDTO);
        if (taxInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        taxInfoDTO = taxInfoService.update(taxInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxInfoDTO.getId().toString()))
            .body(taxInfoDTO);
    }

    /**
     * {@code PATCH  /tax-infos/:id} : Partial updates given fields of an existing taxInfo, field will ignore if it is null
     *
     * @param id the id of the taxInfoDTO to save.
     * @param taxInfoDTO the taxInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxInfoDTO,
     * or with status {@code 400 (Bad Request)} if the taxInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taxInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tax-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaxInfoDTO> partialUpdateTaxInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaxInfoDTO taxInfoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TaxInfo partially : {}, {}", id, taxInfoDTO);
        if (taxInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaxInfoDTO> result = taxInfoService.partialUpdate(taxInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tax-infos} : get all the taxInfos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxInfos in body.
     */
    @GetMapping("/tax-infos")
    public ResponseEntity<List<TaxInfoDTO>> getAllTaxInfos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of TaxInfos");
        Page<TaxInfoDTO> page = taxInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tax-infos/:id} : get the "id" taxInfo.
     *
     * @param id the id of the taxInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tax-infos/{id}")
    public ResponseEntity<TaxInfoDTO> getTaxInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TaxInfo : {}", id);
        Optional<TaxInfoDTO> taxInfoDTO = taxInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxInfoDTO);
    }

    /**
     * {@code DELETE  /tax-infos/:id} : delete the "id" taxInfo.
     *
     * @param id the id of the taxInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tax-infos/{id}")
    public ResponseEntity<Void> deleteTaxInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TaxInfo : {}", id);
        taxInfoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/tax-infos} : get all the tax-infos without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tax-infos in body.
     */
    @GetMapping("/logical/tax-infos")
    public ResponseEntity<List<TaxInfoDTO>> getAllLogicalTaxInfos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of TaxInfos without logical deletions");
        Page<TaxInfoDTO> page = taxInfoService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/tax-infos/:id} : get the "id" TaxInfo if not logically deleted.
     *
     * @param id the id of the TaxInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the TaxInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/tax-infos/{id}")
    public ResponseEntity<TaxInfoDTO> getLogicalTaxInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical TaxInfo : {}", id);
        Optional<TaxInfoDTO> taxInfoDTO = taxInfoService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(taxInfoDTO);
    }

    /**
     * {@code DELETE  /logical/tax-infos/:id} : logically delete the "id" TaxInfo.
     *
     * @param id the id of the TaxInfoDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/tax-infos/{id}")
    public ResponseEntity<Void> logicalDeleteTaxInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete TaxInfo : {}", id);
        if (!taxInfoRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        taxInfoService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/tax-infos/:id/restore} : restore a logically deleted TaxInfo.
     *
     * @param id the id of the TaxInfo to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored TaxInfoDTO.
     */
    @PutMapping("/logical/tax-infos/{id}/restore")
    public ResponseEntity<TaxInfoDTO> restoreTaxInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore TaxInfo : {}", id);
        if (!taxInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        taxInfoService.restore(id);
        Optional<TaxInfoDTO> restored = taxInfoService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
