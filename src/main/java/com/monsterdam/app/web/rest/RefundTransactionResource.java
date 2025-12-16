package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.RefundTransactionRepository;
import com.monsterdam.app.service.RefundTransactionService;
import com.monsterdam.app.service.dto.RefundTransactionDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.RefundTransaction}.
 */
@RestController
@RequestMapping("/api/refund-transactions")
public class RefundTransactionResource {

    private static final Logger LOG = LoggerFactory.getLogger(RefundTransactionResource.class);

    private static final String ENTITY_NAME = "refundTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RefundTransactionService refundTransactionService;

    private final RefundTransactionRepository refundTransactionRepository;

    public RefundTransactionResource(
        RefundTransactionService refundTransactionService,
        RefundTransactionRepository refundTransactionRepository
    ) {
        this.refundTransactionService = refundTransactionService;
        this.refundTransactionRepository = refundTransactionRepository;
    }

    /**
     * {@code POST  /refund-transactions} : Create a new refundTransaction.
     *
     * @param refundTransactionDTO the refundTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new refundTransactionDTO, or with status {@code 400 (Bad Request)} if the refundTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RefundTransactionDTO> createRefundTransaction(@Valid @RequestBody RefundTransactionDTO refundTransactionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RefundTransaction : {}", refundTransactionDTO);
        if (refundTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new refundTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        refundTransactionDTO = refundTransactionService.save(refundTransactionDTO);
        return ResponseEntity.created(new URI("/api/refund-transactions/" + refundTransactionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, refundTransactionDTO.getId().toString()))
            .body(refundTransactionDTO);
    }

    /**
     * {@code PUT  /refund-transactions/:id} : Updates an existing refundTransaction.
     *
     * @param id the id of the refundTransactionDTO to save.
     * @param refundTransactionDTO the refundTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refundTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the refundTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the refundTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RefundTransactionDTO> updateRefundTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RefundTransactionDTO refundTransactionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RefundTransaction : {}, {}", id, refundTransactionDTO);
        if (refundTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refundTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!refundTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        refundTransactionDTO = refundTransactionService.update(refundTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, refundTransactionDTO.getId().toString()))
            .body(refundTransactionDTO);
    }

    /**
     * {@code PATCH  /refund-transactions/:id} : Partial updates given fields of an existing refundTransaction, field will ignore if it is null
     *
     * @param id the id of the refundTransactionDTO to save.
     * @param refundTransactionDTO the refundTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refundTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the refundTransactionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the refundTransactionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the refundTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RefundTransactionDTO> partialUpdateRefundTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RefundTransactionDTO refundTransactionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RefundTransaction partially : {}, {}", id, refundTransactionDTO);
        if (refundTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refundTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!refundTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RefundTransactionDTO> result = refundTransactionService.partialUpdate(refundTransactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, refundTransactionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /refund-transactions} : get all the refundTransactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of refundTransactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RefundTransactionDTO>> getAllRefundTransactions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of RefundTransactions");
        Page<RefundTransactionDTO> page = refundTransactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /refund-transactions/:id} : get the "id" refundTransaction.
     *
     * @param id the id of the refundTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the refundTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RefundTransactionDTO> getRefundTransaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RefundTransaction : {}", id);
        Optional<RefundTransactionDTO> refundTransactionDTO = refundTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(refundTransactionDTO);
    }

    /**
     * {@code DELETE  /refund-transactions/:id} : delete the "id" refundTransaction.
     *
     * @param id the id of the refundTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRefundTransaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RefundTransaction : {}", id);
        refundTransactionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
