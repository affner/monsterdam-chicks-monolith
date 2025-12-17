package com.monsterdam.app.web.crud;

import com.monsterdam.app.repository.MoneyWithdrawRepository;
import com.monsterdam.app.service.MoneyWithdrawService;
import com.monsterdam.app.service.dto.MoneyWithdrawDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.MoneyWithdraw}.
 */
@RestController
@RequestMapping("/api/crud/money-withdraws")
public class MoneyWithdrawResource {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyWithdrawResource.class);

    private static final String ENTITY_NAME = "moneyWithdraw";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoneyWithdrawService moneyWithdrawService;

    private final MoneyWithdrawRepository moneyWithdrawRepository;

    public MoneyWithdrawResource(MoneyWithdrawService moneyWithdrawService, MoneyWithdrawRepository moneyWithdrawRepository) {
        this.moneyWithdrawService = moneyWithdrawService;
        this.moneyWithdrawRepository = moneyWithdrawRepository;
    }

    /**
     * {@code POST  /money-withdraws} : Create a new moneyWithdraw.
     *
     * @param moneyWithdrawDTO the moneyWithdrawDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moneyWithdrawDTO, or with status {@code 400 (Bad Request)} if the moneyWithdraw has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MoneyWithdrawDTO> createMoneyWithdraw(@Valid @RequestBody MoneyWithdrawDTO moneyWithdrawDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MoneyWithdraw : {}", moneyWithdrawDTO);
        if (moneyWithdrawDTO.getId() != null) {
            throw new BadRequestAlertException("A new moneyWithdraw cannot already have an ID", ENTITY_NAME, "idexists");
        }
        moneyWithdrawDTO = moneyWithdrawService.save(moneyWithdrawDTO);
        return ResponseEntity.created(new URI("/api/crud/money-withdraws/" + moneyWithdrawDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, moneyWithdrawDTO.getId().toString()))
            .body(moneyWithdrawDTO);
    }

    /**
     * {@code PUT  /money-withdraws/:id} : Updates an existing moneyWithdraw.
     *
     * @param id the id of the moneyWithdrawDTO to save.
     * @param moneyWithdrawDTO the moneyWithdrawDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyWithdrawDTO,
     * or with status {@code 400 (Bad Request)} if the moneyWithdrawDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moneyWithdrawDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MoneyWithdrawDTO> updateMoneyWithdraw(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoneyWithdrawDTO moneyWithdrawDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MoneyWithdraw : {}, {}", id, moneyWithdrawDTO);
        if (moneyWithdrawDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyWithdrawDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyWithdrawRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moneyWithdrawDTO = moneyWithdrawService.update(moneyWithdrawDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyWithdrawDTO.getId().toString()))
            .body(moneyWithdrawDTO);
    }

    /**
     * {@code PATCH  /money-withdraws/:id} : Partial updates given fields of an existing moneyWithdraw, field will ignore if it is null
     *
     * @param id the id of the moneyWithdrawDTO to save.
     * @param moneyWithdrawDTO the moneyWithdrawDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyWithdrawDTO,
     * or with status {@code 400 (Bad Request)} if the moneyWithdrawDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moneyWithdrawDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moneyWithdrawDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoneyWithdrawDTO> partialUpdateMoneyWithdraw(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoneyWithdrawDTO moneyWithdrawDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MoneyWithdraw partially : {}, {}", id, moneyWithdrawDTO);
        if (moneyWithdrawDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyWithdrawDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyWithdrawRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoneyWithdrawDTO> result = moneyWithdrawService.partialUpdate(moneyWithdrawDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyWithdrawDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /money-withdraws} : get all the moneyWithdraws.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moneyWithdraws in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MoneyWithdrawDTO>> getAllMoneyWithdraws(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of MoneyWithdraws");
        Page<MoneyWithdrawDTO> page = moneyWithdrawService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /money-withdraws/:id} : get the "id" moneyWithdraw.
     *
     * @param id the id of the moneyWithdrawDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moneyWithdrawDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoneyWithdrawDTO> getMoneyWithdraw(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MoneyWithdraw : {}", id);
        Optional<MoneyWithdrawDTO> moneyWithdrawDTO = moneyWithdrawService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moneyWithdrawDTO);
    }

    /**
     * {@code DELETE  /money-withdraws/:id} : delete the "id" moneyWithdraw.
     *
     * @param id the id of the moneyWithdrawDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoneyWithdraw(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MoneyWithdraw : {}", id);
        moneyWithdrawService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
