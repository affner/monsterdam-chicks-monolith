package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.SubscriptionBundleRepository;
import com.monsterdam.app.service.SubscriptionBundleService;
import com.monsterdam.app.service.dto.SubscriptionBundleDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.SubscriptionBundle}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionBundleResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionBundleResource.class);

    private static final String ENTITY_NAME = "subscriptionBundle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionBundleService subscriptionBundleService;

    private final SubscriptionBundleRepository subscriptionBundleRepository;

    public SubscriptionBundleResource(
        SubscriptionBundleService subscriptionBundleService,
        SubscriptionBundleRepository subscriptionBundleRepository
    ) {
        this.subscriptionBundleService = subscriptionBundleService;
        this.subscriptionBundleRepository = subscriptionBundleRepository;
    }

    /**
     * {@code POST  /subscription-bundles} : Create a new subscriptionBundle.
     *
     * @param subscriptionBundleDTO the subscriptionBundleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionBundleDTO, or with status {@code 400 (Bad Request)} if the subscriptionBundle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-bundles")
    public ResponseEntity<SubscriptionBundleDTO> createSubscriptionBundle(@Valid @RequestBody SubscriptionBundleDTO subscriptionBundleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SubscriptionBundle : {}", subscriptionBundleDTO);
        if (subscriptionBundleDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionBundle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subscriptionBundleDTO = subscriptionBundleService.save(subscriptionBundleDTO);
        return ResponseEntity.created(new URI("/api/subscription-bundles/" + subscriptionBundleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subscriptionBundleDTO.getId().toString()))
            .body(subscriptionBundleDTO);
    }

    /**
     * {@code PUT  /subscription-bundles/:id} : Updates an existing subscriptionBundle.
     *
     * @param id the id of the subscriptionBundleDTO to save.
     * @param subscriptionBundleDTO the subscriptionBundleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionBundleDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionBundleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionBundleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-bundles/{id}")
    public ResponseEntity<SubscriptionBundleDTO> updateSubscriptionBundle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionBundleDTO subscriptionBundleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubscriptionBundle : {}, {}", id, subscriptionBundleDTO);
        if (subscriptionBundleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionBundleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionBundleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subscriptionBundleDTO = subscriptionBundleService.update(subscriptionBundleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionBundleDTO.getId().toString()))
            .body(subscriptionBundleDTO);
    }

    /**
     * {@code PATCH  /subscription-bundles/:id} : Partial updates given fields of an existing subscriptionBundle, field will ignore if it is null
     *
     * @param id the id of the subscriptionBundleDTO to save.
     * @param subscriptionBundleDTO the subscriptionBundleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionBundleDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionBundleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionBundleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionBundleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subscription-bundles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscriptionBundleDTO> partialUpdateSubscriptionBundle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionBundleDTO subscriptionBundleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubscriptionBundle partially : {}, {}", id, subscriptionBundleDTO);
        if (subscriptionBundleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionBundleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionBundleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionBundleDTO> result = subscriptionBundleService.partialUpdate(subscriptionBundleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionBundleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-bundles} : get all the subscriptionBundles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionBundles in body.
     */
    @GetMapping("/subscription-bundles")
    public ResponseEntity<List<SubscriptionBundleDTO>> getAllSubscriptionBundles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of SubscriptionBundles");
        Page<SubscriptionBundleDTO> page = subscriptionBundleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscription-bundles/:id} : get the "id" subscriptionBundle.
     *
     * @param id the id of the subscriptionBundleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionBundleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-bundles/{id}")
    public ResponseEntity<SubscriptionBundleDTO> getSubscriptionBundle(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubscriptionBundle : {}", id);
        Optional<SubscriptionBundleDTO> subscriptionBundleDTO = subscriptionBundleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionBundleDTO);
    }

    /**
     * {@code DELETE  /subscription-bundles/:id} : delete the "id" subscriptionBundle.
     *
     * @param id the id of the subscriptionBundleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-bundles/{id}")
    public ResponseEntity<Void> deleteSubscriptionBundle(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubscriptionBundle : {}", id);
        subscriptionBundleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/subscription-bundles} : get all the subscription-bundles without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscription-bundles in body.
     */
    @GetMapping("/logical/subscription-bundles")
    public ResponseEntity<List<SubscriptionBundleDTO>> getAllLogicalSubscriptionBundles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of SubscriptionBundles without logical deletions");
        Page<SubscriptionBundleDTO> page = subscriptionBundleService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/subscription-bundles/:id} : get the "id" SubscriptionBundle if not logically deleted.
     *
     * @param id the id of the SubscriptionBundleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the SubscriptionBundleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/subscription-bundles/{id}")
    public ResponseEntity<SubscriptionBundleDTO> getLogicalSubscriptionBundle(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical SubscriptionBundle : {}", id);
        Optional<SubscriptionBundleDTO> subscriptionBundleDTO = subscriptionBundleService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(subscriptionBundleDTO);
    }

    /**
     * {@code DELETE  /logical/subscription-bundles/:id} : logically delete the "id" SubscriptionBundle.
     *
     * @param id the id of the SubscriptionBundleDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/subscription-bundles/{id}")
    public ResponseEntity<Void> logicalDeleteSubscriptionBundle(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete SubscriptionBundle : {}", id);
        if (!subscriptionBundleRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        subscriptionBundleService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/subscription-bundles/:id/restore} : restore a logically deleted SubscriptionBundle.
     *
     * @param id the id of the SubscriptionBundle to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored SubscriptionBundleDTO.
     */
    @PutMapping("/logical/subscription-bundles/{id}/restore")
    public ResponseEntity<SubscriptionBundleDTO> restoreSubscriptionBundle(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore SubscriptionBundle : {}", id);
        if (!subscriptionBundleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        subscriptionBundleService.restore(id);
        Optional<SubscriptionBundleDTO> restored = subscriptionBundleService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
