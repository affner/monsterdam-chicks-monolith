package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.PaymentProviderEventRepository;
import com.monsterdam.app.service.PaymentProviderEventService;
import com.monsterdam.app.service.dto.PaymentProviderEventDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.PaymentProviderEvent}.
 */
@RestController
@RequestMapping("/api/payment-provider-events")
public class PaymentProviderEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentProviderEventResource.class);

    private static final String ENTITY_NAME = "paymentProviderEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentProviderEventService paymentProviderEventService;

    private final PaymentProviderEventRepository paymentProviderEventRepository;

    public PaymentProviderEventResource(
        PaymentProviderEventService paymentProviderEventService,
        PaymentProviderEventRepository paymentProviderEventRepository
    ) {
        this.paymentProviderEventService = paymentProviderEventService;
        this.paymentProviderEventRepository = paymentProviderEventRepository;
    }

    /**
     * {@code POST  /payment-provider-events} : Create a new paymentProviderEvent.
     *
     * @param paymentProviderEventDTO the paymentProviderEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentProviderEventDTO, or with status {@code 400 (Bad Request)} if the paymentProviderEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentProviderEventDTO> createPaymentProviderEvent(
        @Valid @RequestBody PaymentProviderEventDTO paymentProviderEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PaymentProviderEvent : {}", paymentProviderEventDTO);
        if (paymentProviderEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentProviderEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentProviderEventDTO = paymentProviderEventService.save(paymentProviderEventDTO);
        return ResponseEntity.created(new URI("/api/payment-provider-events/" + paymentProviderEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentProviderEventDTO.getId().toString()))
            .body(paymentProviderEventDTO);
    }

    /**
     * {@code PUT  /payment-provider-events/:id} : Updates an existing paymentProviderEvent.
     *
     * @param id the id of the paymentProviderEventDTO to save.
     * @param paymentProviderEventDTO the paymentProviderEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentProviderEventDTO,
     * or with status {@code 400 (Bad Request)} if the paymentProviderEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentProviderEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentProviderEventDTO> updatePaymentProviderEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentProviderEventDTO paymentProviderEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentProviderEvent : {}, {}", id, paymentProviderEventDTO);
        if (paymentProviderEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentProviderEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentProviderEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentProviderEventDTO = paymentProviderEventService.update(paymentProviderEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentProviderEventDTO.getId().toString()))
            .body(paymentProviderEventDTO);
    }

    /**
     * {@code PATCH  /payment-provider-events/:id} : Partial updates given fields of an existing paymentProviderEvent, field will ignore if it is null
     *
     * @param id the id of the paymentProviderEventDTO to save.
     * @param paymentProviderEventDTO the paymentProviderEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentProviderEventDTO,
     * or with status {@code 400 (Bad Request)} if the paymentProviderEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentProviderEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentProviderEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentProviderEventDTO> partialUpdatePaymentProviderEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentProviderEventDTO paymentProviderEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentProviderEvent partially : {}, {}", id, paymentProviderEventDTO);
        if (paymentProviderEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentProviderEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentProviderEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentProviderEventDTO> result = paymentProviderEventService.partialUpdate(paymentProviderEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentProviderEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-provider-events} : get all the paymentProviderEvents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentProviderEvents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentProviderEventDTO>> getAllPaymentProviderEvents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PaymentProviderEvents");
        Page<PaymentProviderEventDTO> page = paymentProviderEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-provider-events/:id} : get the "id" paymentProviderEvent.
     *
     * @param id the id of the paymentProviderEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentProviderEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentProviderEventDTO> getPaymentProviderEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentProviderEvent : {}", id);
        Optional<PaymentProviderEventDTO> paymentProviderEventDTO = paymentProviderEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentProviderEventDTO);
    }

    /**
     * {@code DELETE  /payment-provider-events/:id} : delete the "id" paymentProviderEvent.
     *
     * @param id the id of the paymentProviderEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentProviderEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentProviderEvent : {}", id);
        paymentProviderEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
