package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.IdentityDocumentReviewRepository;
import com.monsterdam.app.service.IdentityDocumentReviewService;
import com.monsterdam.app.service.dto.IdentityDocumentReviewDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.IdentityDocumentReview}.
 */
@RestController
@RequestMapping("/api/identity-document-reviews")
public class IdentityDocumentReviewResource {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityDocumentReviewResource.class);

    private static final String ENTITY_NAME = "identityDocumentReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdentityDocumentReviewService identityDocumentReviewService;

    private final IdentityDocumentReviewRepository identityDocumentReviewRepository;

    public IdentityDocumentReviewResource(
        IdentityDocumentReviewService identityDocumentReviewService,
        IdentityDocumentReviewRepository identityDocumentReviewRepository
    ) {
        this.identityDocumentReviewService = identityDocumentReviewService;
        this.identityDocumentReviewRepository = identityDocumentReviewRepository;
    }

    /**
     * {@code POST  /identity-document-reviews} : Create a new identityDocumentReview.
     *
     * @param identityDocumentReviewDTO the identityDocumentReviewDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new identityDocumentReviewDTO, or with status {@code 400 (Bad Request)} if the identityDocumentReview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IdentityDocumentReviewDTO> createIdentityDocumentReview(
        @Valid @RequestBody IdentityDocumentReviewDTO identityDocumentReviewDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save IdentityDocumentReview : {}", identityDocumentReviewDTO);
        if (identityDocumentReviewDTO.getId() != null) {
            throw new BadRequestAlertException("A new identityDocumentReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        identityDocumentReviewDTO = identityDocumentReviewService.save(identityDocumentReviewDTO);
        return ResponseEntity.created(new URI("/api/identity-document-reviews/" + identityDocumentReviewDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, identityDocumentReviewDTO.getId().toString()))
            .body(identityDocumentReviewDTO);
    }

    /**
     * {@code PUT  /identity-document-reviews/:id} : Updates an existing identityDocumentReview.
     *
     * @param id the id of the identityDocumentReviewDTO to save.
     * @param identityDocumentReviewDTO the identityDocumentReviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identityDocumentReviewDTO,
     * or with status {@code 400 (Bad Request)} if the identityDocumentReviewDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the identityDocumentReviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IdentityDocumentReviewDTO> updateIdentityDocumentReview(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IdentityDocumentReviewDTO identityDocumentReviewDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IdentityDocumentReview : {}, {}", id, identityDocumentReviewDTO);
        if (identityDocumentReviewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identityDocumentReviewDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!identityDocumentReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        identityDocumentReviewDTO = identityDocumentReviewService.update(identityDocumentReviewDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, identityDocumentReviewDTO.getId().toString()))
            .body(identityDocumentReviewDTO);
    }

    /**
     * {@code PATCH  /identity-document-reviews/:id} : Partial updates given fields of an existing identityDocumentReview, field will ignore if it is null
     *
     * @param id the id of the identityDocumentReviewDTO to save.
     * @param identityDocumentReviewDTO the identityDocumentReviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identityDocumentReviewDTO,
     * or with status {@code 400 (Bad Request)} if the identityDocumentReviewDTO is not valid,
     * or with status {@code 404 (Not Found)} if the identityDocumentReviewDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the identityDocumentReviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IdentityDocumentReviewDTO> partialUpdateIdentityDocumentReview(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IdentityDocumentReviewDTO identityDocumentReviewDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IdentityDocumentReview partially : {}, {}", id, identityDocumentReviewDTO);
        if (identityDocumentReviewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identityDocumentReviewDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!identityDocumentReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IdentityDocumentReviewDTO> result = identityDocumentReviewService.partialUpdate(identityDocumentReviewDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, identityDocumentReviewDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /identity-document-reviews} : get all the identityDocumentReviews.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of identityDocumentReviews in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IdentityDocumentReviewDTO>> getAllIdentityDocumentReviews(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of IdentityDocumentReviews");
        Page<IdentityDocumentReviewDTO> page = identityDocumentReviewService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /identity-document-reviews/:id} : get the "id" identityDocumentReview.
     *
     * @param id the id of the identityDocumentReviewDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the identityDocumentReviewDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IdentityDocumentReviewDTO> getIdentityDocumentReview(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IdentityDocumentReview : {}", id);
        Optional<IdentityDocumentReviewDTO> identityDocumentReviewDTO = identityDocumentReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(identityDocumentReviewDTO);
    }

    /**
     * {@code DELETE  /identity-document-reviews/:id} : delete the "id" identityDocumentReview.
     *
     * @param id the id of the identityDocumentReviewDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdentityDocumentReview(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IdentityDocumentReview : {}", id);
        identityDocumentReviewService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
