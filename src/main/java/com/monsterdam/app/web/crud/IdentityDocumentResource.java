package com.monsterdam.app.web.crud;

import com.monsterdam.app.repository.IdentityDocumentRepository;
import com.monsterdam.app.service.IdentityDocumentService;
import com.monsterdam.app.service.dto.IdentityDocumentDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.IdentityDocument}.
 */
@RestController
@RequestMapping("/api/crud/identity-documents")
public class IdentityDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityDocumentResource.class);

    private static final String ENTITY_NAME = "identityDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdentityDocumentService identityDocumentService;

    private final IdentityDocumentRepository identityDocumentRepository;

    public IdentityDocumentResource(
        IdentityDocumentService identityDocumentService,
        IdentityDocumentRepository identityDocumentRepository
    ) {
        this.identityDocumentService = identityDocumentService;
        this.identityDocumentRepository = identityDocumentRepository;
    }

    /**
     * {@code POST  /identity-documents} : Create a new identityDocument.
     *
     * @param identityDocumentDTO the identityDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new identityDocumentDTO, or with status {@code 400 (Bad Request)} if the identityDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IdentityDocumentDTO> createIdentityDocument(@Valid @RequestBody IdentityDocumentDTO identityDocumentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save IdentityDocument : {}", identityDocumentDTO);
        if (identityDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new identityDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        identityDocumentDTO = identityDocumentService.save(identityDocumentDTO);
        return ResponseEntity.created(new URI("/api/crud/identity-documents/" + identityDocumentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, identityDocumentDTO.getId().toString()))
            .body(identityDocumentDTO);
    }

    /**
     * {@code PUT  /identity-documents/:id} : Updates an existing identityDocument.
     *
     * @param id the id of the identityDocumentDTO to save.
     * @param identityDocumentDTO the identityDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identityDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the identityDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the identityDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IdentityDocumentDTO> updateIdentityDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IdentityDocumentDTO identityDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IdentityDocument : {}, {}", id, identityDocumentDTO);
        if (identityDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identityDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!identityDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        identityDocumentDTO = identityDocumentService.update(identityDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, identityDocumentDTO.getId().toString()))
            .body(identityDocumentDTO);
    }

    /**
     * {@code PATCH  /identity-documents/:id} : Partial updates given fields of an existing identityDocument, field will ignore if it is null
     *
     * @param id the id of the identityDocumentDTO to save.
     * @param identityDocumentDTO the identityDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identityDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the identityDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the identityDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the identityDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IdentityDocumentDTO> partialUpdateIdentityDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IdentityDocumentDTO identityDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IdentityDocument partially : {}, {}", id, identityDocumentDTO);
        if (identityDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identityDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!identityDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IdentityDocumentDTO> result = identityDocumentService.partialUpdate(identityDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, identityDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /identity-documents} : get all the identityDocuments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of identityDocuments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IdentityDocumentDTO>> getAllIdentityDocuments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of IdentityDocuments");
        Page<IdentityDocumentDTO> page = identityDocumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /identity-documents/:id} : get the "id" identityDocument.
     *
     * @param id the id of the identityDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the identityDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IdentityDocumentDTO> getIdentityDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IdentityDocument : {}", id);
        Optional<IdentityDocumentDTO> identityDocumentDTO = identityDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(identityDocumentDTO);
    }

    /**
     * {@code DELETE  /identity-documents/:id} : delete the "id" identityDocument.
     *
     * @param id the id of the identityDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdentityDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IdentityDocument : {}", id);
        identityDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
