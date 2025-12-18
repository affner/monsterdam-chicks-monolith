package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.SubscriptionPlanOfferRepository;
import com.monsterdam.app.service.SubscriptionPlanOfferService;
import com.monsterdam.app.service.dto.SubscriptionPlanOfferDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.SubscriptionPlanOffer}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionPlanOfferResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionPlanOfferResource.class);

    private static final String ENTITY_NAME = "subscriptionPlanOffer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionPlanOfferService subscriptionPlanOfferService;

    private final SubscriptionPlanOfferRepository subscriptionPlanOfferRepository;

    public SubscriptionPlanOfferResource(
        SubscriptionPlanOfferService subscriptionPlanOfferService,
        SubscriptionPlanOfferRepository subscriptionPlanOfferRepository
    ) {
        this.subscriptionPlanOfferService = subscriptionPlanOfferService;
        this.subscriptionPlanOfferRepository = subscriptionPlanOfferRepository;
    }

    /**
     * {@code POST  /subscription-plan-offers} : Create a new subscriptionPlanOffer.
     *
     * @param subscriptionPlanOfferDTO the subscriptionPlanOfferDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionPlanOfferDTO, or with status {@code 400 (Bad Request)} if the subscriptionPlanOffer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-plan-offers")
    public ResponseEntity<SubscriptionPlanOfferDTO> createSubscriptionPlanOffer(
        @Valid @RequestBody SubscriptionPlanOfferDTO subscriptionPlanOfferDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save SubscriptionPlanOffer : {}", subscriptionPlanOfferDTO);
        if (subscriptionPlanOfferDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionPlanOffer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subscriptionPlanOfferDTO = subscriptionPlanOfferService.save(subscriptionPlanOfferDTO);
        return ResponseEntity.created(new URI("/api/subscription-plan-offers/" + subscriptionPlanOfferDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subscriptionPlanOfferDTO.getId().toString()))
            .body(subscriptionPlanOfferDTO);
    }

    /**
     * {@code PUT  /subscription-plan-offers/:id} : Updates an existing subscriptionPlanOffer.
     *
     * @param id the id of the subscriptionPlanOfferDTO to save.
     * @param subscriptionPlanOfferDTO the subscriptionPlanOfferDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionPlanOfferDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionPlanOfferDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionPlanOfferDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-plan-offers/{id}")
    public ResponseEntity<SubscriptionPlanOfferDTO> updateSubscriptionPlanOffer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionPlanOfferDTO subscriptionPlanOfferDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubscriptionPlanOffer : {}, {}", id, subscriptionPlanOfferDTO);
        if (subscriptionPlanOfferDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionPlanOfferDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionPlanOfferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subscriptionPlanOfferDTO = subscriptionPlanOfferService.update(subscriptionPlanOfferDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionPlanOfferDTO.getId().toString()))
            .body(subscriptionPlanOfferDTO);
    }

    /**
     * {@code PATCH  /subscription-plan-offers/:id} : Partial updates given fields of an existing subscriptionPlanOffer, field will ignore if it is null
     *
     * @param id the id of the subscriptionPlanOfferDTO to save.
     * @param subscriptionPlanOfferDTO the subscriptionPlanOfferDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionPlanOfferDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionPlanOfferDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionPlanOfferDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionPlanOfferDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subscription-plan-offers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscriptionPlanOfferDTO> partialUpdateSubscriptionPlanOffer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionPlanOfferDTO subscriptionPlanOfferDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubscriptionPlanOffer partially : {}, {}", id, subscriptionPlanOfferDTO);
        if (subscriptionPlanOfferDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionPlanOfferDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionPlanOfferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionPlanOfferDTO> result = subscriptionPlanOfferService.partialUpdate(subscriptionPlanOfferDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionPlanOfferDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-plan-offers} : get all the subscriptionPlanOffers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionPlanOffers in body.
     */
    @GetMapping("/subscription-plan-offers")
    public ResponseEntity<List<SubscriptionPlanOfferDTO>> getAllSubscriptionPlanOffers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of SubscriptionPlanOffers");
        Page<SubscriptionPlanOfferDTO> page = subscriptionPlanOfferService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscription-plan-offers/:id} : get the "id" subscriptionPlanOffer.
     *
     * @param id the id of the subscriptionPlanOfferDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionPlanOfferDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-plan-offers/{id}")
    public ResponseEntity<SubscriptionPlanOfferDTO> getSubscriptionPlanOffer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubscriptionPlanOffer : {}", id);
        Optional<SubscriptionPlanOfferDTO> subscriptionPlanOfferDTO = subscriptionPlanOfferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionPlanOfferDTO);
    }

    /**
     * {@code DELETE  /subscription-plan-offers/:id} : delete the "id" subscriptionPlanOffer.
     *
     * @param id the id of the subscriptionPlanOfferDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-plan-offers/{id}")
    public ResponseEntity<Void> deleteSubscriptionPlanOffer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubscriptionPlanOffer : {}", id);
        subscriptionPlanOfferService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/subscription-plan-offers} : get all the subscription-plan-offers without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscription-plan-offers in body.
     */
    @GetMapping("/logical/subscription-plan-offers")
    public ResponseEntity<List<SubscriptionPlanOfferDTO>> getAllLogicalSubscriptionPlanOffers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of SubscriptionPlanOffers without logical deletions");
        Page<SubscriptionPlanOfferDTO> page = subscriptionPlanOfferService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/subscription-plan-offers/:id} : get the "id" SubscriptionPlanOffer if not logically deleted.
     *
     * @param id the id of the SubscriptionPlanOfferDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the SubscriptionPlanOfferDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/subscription-plan-offers/{id}")
    public ResponseEntity<SubscriptionPlanOfferDTO> getLogicalSubscriptionPlanOffer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical SubscriptionPlanOffer : {}", id);
        Optional<SubscriptionPlanOfferDTO> subscriptionPlanOfferDTO = subscriptionPlanOfferService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(subscriptionPlanOfferDTO);
    }

    /**
     * {@code DELETE  /logical/subscription-plan-offers/:id} : logically delete the "id" SubscriptionPlanOffer.
     *
     * @param id the id of the SubscriptionPlanOfferDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/subscription-plan-offers/{id}")
    public ResponseEntity<Void> logicalDeleteSubscriptionPlanOffer(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete SubscriptionPlanOffer : {}", id);
        if (!subscriptionPlanOfferRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        subscriptionPlanOfferService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/subscription-plan-offers/:id/restore} : restore a logically deleted SubscriptionPlanOffer.
     *
     * @param id the id of the SubscriptionPlanOffer to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored SubscriptionPlanOfferDTO.
     */
    @PutMapping("/logical/subscription-plan-offers/{id}/restore")
    public ResponseEntity<SubscriptionPlanOfferDTO> restoreSubscriptionPlanOffer(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore SubscriptionPlanOffer : {}", id);
        if (!subscriptionPlanOfferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        subscriptionPlanOfferService.restore(id);
        Optional<SubscriptionPlanOfferDTO> restored = subscriptionPlanOfferService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
