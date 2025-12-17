package com.monsterdam.app.web.crud;

import com.monsterdam.app.repository.DocumentReviewObservationRepository;
import com.monsterdam.app.service.DocumentReviewObservationService;
import com.monsterdam.app.service.dto.DocumentReviewObservationDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.DocumentReviewObservation}.
 */
@RestController
@RequestMapping("/api/crud/document-review-observations")
public class DocumentReviewObservationResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentReviewObservationResource.class);

    private static final String ENTITY_NAME = "documentReviewObservation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentReviewObservationService documentReviewObservationService;

    private final DocumentReviewObservationRepository documentReviewObservationRepository;

    public DocumentReviewObservationResource(
        DocumentReviewObservationService documentReviewObservationService,
        DocumentReviewObservationRepository documentReviewObservationRepository
    ) {
        this.documentReviewObservationService = documentReviewObservationService;
        this.documentReviewObservationRepository = documentReviewObservationRepository;
    }

    /**
     * {@code POST  /document-review-observations} : Create a new documentReviewObservation.
     *
     * @param documentReviewObservationDTO the documentReviewObservationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentReviewObservationDTO, or with status {@code 400 (Bad Request)} if the documentReviewObservation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentReviewObservationDTO> createDocumentReviewObservation(
        @Valid @RequestBody DocumentReviewObservationDTO documentReviewObservationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save DocumentReviewObservation : {}", documentReviewObservationDTO);
        if (documentReviewObservationDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentReviewObservation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentReviewObservationDTO = documentReviewObservationService.save(documentReviewObservationDTO);
        return ResponseEntity.created(new URI("/api/crud/document-review-observations/" + documentReviewObservationDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentReviewObservationDTO.getId().toString())
            )
            .body(documentReviewObservationDTO);
    }

    /**
     * {@code PUT  /document-review-observations/:id} : Updates an existing documentReviewObservation.
     *
     * @param id the id of the documentReviewObservationDTO to save.
     * @param documentReviewObservationDTO the documentReviewObservationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentReviewObservationDTO,
     * or with status {@code 400 (Bad Request)} if the documentReviewObservationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentReviewObservationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentReviewObservationDTO> updateDocumentReviewObservation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentReviewObservationDTO documentReviewObservationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentReviewObservation : {}, {}", id, documentReviewObservationDTO);
        if (documentReviewObservationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentReviewObservationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentReviewObservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentReviewObservationDTO = documentReviewObservationService.update(documentReviewObservationDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentReviewObservationDTO.getId().toString())
            )
            .body(documentReviewObservationDTO);
    }

    /**
     * {@code PATCH  /document-review-observations/:id} : Partial updates given fields of an existing documentReviewObservation, field will ignore if it is null
     *
     * @param id the id of the documentReviewObservationDTO to save.
     * @param documentReviewObservationDTO the documentReviewObservationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentReviewObservationDTO,
     * or with status {@code 400 (Bad Request)} if the documentReviewObservationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentReviewObservationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentReviewObservationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentReviewObservationDTO> partialUpdateDocumentReviewObservation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentReviewObservationDTO documentReviewObservationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DocumentReviewObservation partially : {}, {}", id, documentReviewObservationDTO);
        if (documentReviewObservationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentReviewObservationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentReviewObservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentReviewObservationDTO> result = documentReviewObservationService.partialUpdate(documentReviewObservationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentReviewObservationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-review-observations} : get all the documentReviewObservations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentReviewObservations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentReviewObservationDTO>> getAllDocumentReviewObservations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of DocumentReviewObservations");
        Page<DocumentReviewObservationDTO> page = documentReviewObservationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-review-observations/:id} : get the "id" documentReviewObservation.
     *
     * @param id the id of the documentReviewObservationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentReviewObservationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentReviewObservationDTO> getDocumentReviewObservation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentReviewObservation : {}", id);
        Optional<DocumentReviewObservationDTO> documentReviewObservationDTO = documentReviewObservationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentReviewObservationDTO);
    }

    /**
     * {@code DELETE  /document-review-observations/:id} : delete the "id" documentReviewObservation.
     *
     * @param id the id of the documentReviewObservationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentReviewObservation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentReviewObservation : {}", id);
        documentReviewObservationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
