package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.PurchasedContentRepository;
import com.monsterdam.app.service.PurchasedContentService;
import com.monsterdam.app.service.dto.PurchasedContentDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.PurchasedContent}.
 */
@RestController
@RequestMapping("/api")
public class PurchasedContentResource {

    private static final Logger LOG = LoggerFactory.getLogger(PurchasedContentResource.class);

    private static final String ENTITY_NAME = "purchasedContent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchasedContentService purchasedContentService;

    private final PurchasedContentRepository purchasedContentRepository;

    public PurchasedContentResource(
        PurchasedContentService purchasedContentService,
        PurchasedContentRepository purchasedContentRepository
    ) {
        this.purchasedContentService = purchasedContentService;
        this.purchasedContentRepository = purchasedContentRepository;
    }

    /**
     * {@code POST  /purchased-contents} : Create a new purchasedContent.
     *
     * @param purchasedContentDTO the purchasedContentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchasedContentDTO, or with status {@code 400 (Bad Request)} if the purchasedContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchased-contents")
    public ResponseEntity<PurchasedContentDTO> createPurchasedContent(@Valid @RequestBody PurchasedContentDTO purchasedContentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PurchasedContent : {}", purchasedContentDTO);
        if (purchasedContentDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchasedContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        purchasedContentDTO = purchasedContentService.save(purchasedContentDTO);
        return ResponseEntity.created(new URI("/api/purchased-contents/" + purchasedContentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, purchasedContentDTO.getId().toString()))
            .body(purchasedContentDTO);
    }

    /**
     * {@code PUT  /purchased-contents/:id} : Updates an existing purchasedContent.
     *
     * @param id the id of the purchasedContentDTO to save.
     * @param purchasedContentDTO the purchasedContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedContentDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedContentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchasedContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchased-contents/{id}")
    public ResponseEntity<PurchasedContentDTO> updatePurchasedContent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchasedContentDTO purchasedContentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PurchasedContent : {}, {}", id, purchasedContentDTO);
        if (purchasedContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchasedContentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        purchasedContentDTO = purchasedContentService.update(purchasedContentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchasedContentDTO.getId().toString()))
            .body(purchasedContentDTO);
    }

    /**
     * {@code PATCH  /purchased-contents/:id} : Partial updates given fields of an existing purchasedContent, field will ignore if it is null
     *
     * @param id the id of the purchasedContentDTO to save.
     * @param purchasedContentDTO the purchasedContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedContentDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedContentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the purchasedContentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchasedContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/purchased-contents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchasedContentDTO> partialUpdatePurchasedContent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchasedContentDTO purchasedContentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PurchasedContent partially : {}, {}", id, purchasedContentDTO);
        if (purchasedContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchasedContentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchasedContentDTO> result = purchasedContentService.partialUpdate(purchasedContentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchasedContentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /purchased-contents} : get all the purchasedContents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchasedContents in body.
     */
    @GetMapping("/purchased-contents")
    public ResponseEntity<List<PurchasedContentDTO>> getAllPurchasedContents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PurchasedContents");
        Page<PurchasedContentDTO> page = purchasedContentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchased-contents/:id} : get the "id" purchasedContent.
     *
     * @param id the id of the purchasedContentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchasedContentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchased-contents/{id}")
    public ResponseEntity<PurchasedContentDTO> getPurchasedContent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PurchasedContent : {}", id);
        Optional<PurchasedContentDTO> purchasedContentDTO = purchasedContentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchasedContentDTO);
    }

    /**
     * {@code DELETE  /purchased-contents/:id} : delete the "id" purchasedContent.
     *
     * @param id the id of the purchasedContentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchased-contents/{id}")
    public ResponseEntity<Void> deletePurchasedContent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PurchasedContent : {}", id);
        purchasedContentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/purchased-contents} : get all the purchased-contents without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchased-contents in body.
     */
    @GetMapping("/logical/purchased-contents")
    public ResponseEntity<List<PurchasedContentDTO>> getAllLogicalPurchasedContents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PurchasedContents without logical deletions");
        Page<PurchasedContentDTO> page = purchasedContentService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/purchased-contents/:id} : get the "id" PurchasedContent if not logically deleted.
     *
     * @param id the id of the PurchasedContentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PurchasedContentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/purchased-contents/{id}")
    public ResponseEntity<PurchasedContentDTO> getLogicalPurchasedContent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical PurchasedContent : {}", id);
        Optional<PurchasedContentDTO> purchasedContentDTO = purchasedContentService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(purchasedContentDTO);
    }

    /**
     * {@code DELETE  /logical/purchased-contents/:id} : logically delete the "id" PurchasedContent.
     *
     * @param id the id of the PurchasedContentDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/purchased-contents/{id}")
    public ResponseEntity<Void> logicalDeletePurchasedContent(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete PurchasedContent : {}", id);
        if (!purchasedContentRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        purchasedContentService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/purchased-contents/:id/restore} : restore a logically deleted PurchasedContent.
     *
     * @param id the id of the PurchasedContent to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored PurchasedContentDTO.
     */
    @PutMapping("/logical/purchased-contents/{id}/restore")
    public ResponseEntity<PurchasedContentDTO> restorePurchasedContent(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore PurchasedContent : {}", id);
        if (!purchasedContentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        purchasedContentService.restore(id);
        Optional<PurchasedContentDTO> restored = purchasedContentService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
