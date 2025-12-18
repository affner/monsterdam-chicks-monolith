package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.PurchasedSubscriptionRepository;
import com.monsterdam.app.service.PurchasedSubscriptionService;
import com.monsterdam.app.service.dto.PurchasedSubscriptionDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.PurchasedSubscription}.
 */
@RestController
@RequestMapping("/api")
public class PurchasedSubscriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PurchasedSubscriptionResource.class);

    private static final String ENTITY_NAME = "purchasedSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchasedSubscriptionService purchasedSubscriptionService;

    private final PurchasedSubscriptionRepository purchasedSubscriptionRepository;

    public PurchasedSubscriptionResource(
        PurchasedSubscriptionService purchasedSubscriptionService,
        PurchasedSubscriptionRepository purchasedSubscriptionRepository
    ) {
        this.purchasedSubscriptionService = purchasedSubscriptionService;
        this.purchasedSubscriptionRepository = purchasedSubscriptionRepository;
    }

    /**
     * {@code POST  /purchased-subscriptions} : Create a new purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the purchasedSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchasedSubscriptionDTO, or with status {@code 400 (Bad Request)} if the purchasedSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchased-subscriptions")
    public ResponseEntity<PurchasedSubscriptionDTO> createPurchasedSubscription(
        @Valid @RequestBody PurchasedSubscriptionDTO purchasedSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PurchasedSubscription : {}", purchasedSubscriptionDTO);
        if (purchasedSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchasedSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        purchasedSubscriptionDTO = purchasedSubscriptionService.save(purchasedSubscriptionDTO);
        return ResponseEntity.created(new URI("/api/purchased-subscriptions/" + purchasedSubscriptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, purchasedSubscriptionDTO.getId().toString()))
            .body(purchasedSubscriptionDTO);
    }

    /**
     * {@code PUT  /purchased-subscriptions/:id} : Updates an existing purchasedSubscription.
     *
     * @param id the id of the purchasedSubscriptionDTO to save.
     * @param purchasedSubscriptionDTO the purchasedSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchasedSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchased-subscriptions/{id}")
    public ResponseEntity<PurchasedSubscriptionDTO> updatePurchasedSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchasedSubscriptionDTO purchasedSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PurchasedSubscription : {}, {}", id, purchasedSubscriptionDTO);
        if (purchasedSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchasedSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        purchasedSubscriptionDTO = purchasedSubscriptionService.update(purchasedSubscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchasedSubscriptionDTO.getId().toString()))
            .body(purchasedSubscriptionDTO);
    }

    /**
     * {@code PATCH  /purchased-subscriptions/:id} : Partial updates given fields of an existing purchasedSubscription, field will ignore if it is null
     *
     * @param id the id of the purchasedSubscriptionDTO to save.
     * @param purchasedSubscriptionDTO the purchasedSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the purchasedSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchasedSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/purchased-subscriptions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchasedSubscriptionDTO> partialUpdatePurchasedSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchasedSubscriptionDTO purchasedSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PurchasedSubscription partially : {}, {}", id, purchasedSubscriptionDTO);
        if (purchasedSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchasedSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchasedSubscriptionDTO> result = purchasedSubscriptionService.partialUpdate(purchasedSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchasedSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /purchased-subscriptions} : get all the purchasedSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchasedSubscriptions in body.
     */
    @GetMapping("/purchased-subscriptions")
    public ResponseEntity<List<PurchasedSubscriptionDTO>> getAllPurchasedSubscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PurchasedSubscriptions");
        Page<PurchasedSubscriptionDTO> page = purchasedSubscriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchased-subscriptions/:id} : get the "id" purchasedSubscription.
     *
     * @param id the id of the purchasedSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchasedSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchased-subscriptions/{id}")
    public ResponseEntity<PurchasedSubscriptionDTO> getPurchasedSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PurchasedSubscription : {}", id);
        Optional<PurchasedSubscriptionDTO> purchasedSubscriptionDTO = purchasedSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchasedSubscriptionDTO);
    }

    /**
     * {@code DELETE  /purchased-subscriptions/:id} : delete the "id" purchasedSubscription.
     *
     * @param id the id of the purchasedSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchased-subscriptions/{id}")
    public ResponseEntity<Void> deletePurchasedSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PurchasedSubscription : {}", id);
        purchasedSubscriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/purchased-subscriptions} : get all the purchased-subscriptions without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchased-subscriptions in body.
     */
    @GetMapping("/logical/purchased-subscriptions")
    public ResponseEntity<List<PurchasedSubscriptionDTO>> getAllLogicalPurchasedSubscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PurchasedSubscriptions without logical deletions");
        Page<PurchasedSubscriptionDTO> page = purchasedSubscriptionService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/purchased-subscriptions/:id} : get the "id" PurchasedSubscription if not logically deleted.
     *
     * @param id the id of the PurchasedSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PurchasedSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/purchased-subscriptions/{id}")
    public ResponseEntity<PurchasedSubscriptionDTO> getLogicalPurchasedSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical PurchasedSubscription : {}", id);
        Optional<PurchasedSubscriptionDTO> purchasedSubscriptionDTO = purchasedSubscriptionService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(purchasedSubscriptionDTO);
    }

    /**
     * {@code DELETE  /logical/purchased-subscriptions/:id} : logically delete the "id" PurchasedSubscription.
     *
     * @param id the id of the PurchasedSubscriptionDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/purchased-subscriptions/{id}")
    public ResponseEntity<Void> logicalDeletePurchasedSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete PurchasedSubscription : {}", id);
        if (!purchasedSubscriptionRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        purchasedSubscriptionService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/purchased-subscriptions/:id/restore} : restore a logically deleted PurchasedSubscription.
     *
     * @param id the id of the PurchasedSubscription to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored PurchasedSubscriptionDTO.
     */
    @PutMapping("/logical/purchased-subscriptions/{id}/restore")
    public ResponseEntity<PurchasedSubscriptionDTO> restorePurchasedSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore PurchasedSubscription : {}", id);
        if (!purchasedSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        purchasedSubscriptionService.restore(id);
        Optional<PurchasedSubscriptionDTO> restored = purchasedSubscriptionService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
