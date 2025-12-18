package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.MoneyEarningRepository;
import com.monsterdam.app.service.MoneyEarningService;
import com.monsterdam.app.service.dto.MoneyEarningDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.MoneyEarning}.
 */
@RestController
@RequestMapping("/api")
public class MoneyEarningResource {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyEarningResource.class);

    private static final String ENTITY_NAME = "moneyEarning";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoneyEarningService moneyEarningService;

    private final MoneyEarningRepository moneyEarningRepository;

    public MoneyEarningResource(MoneyEarningService moneyEarningService, MoneyEarningRepository moneyEarningRepository) {
        this.moneyEarningService = moneyEarningService;
        this.moneyEarningRepository = moneyEarningRepository;
    }

    /**
     * {@code POST  /money-earnings} : Create a new moneyEarning.
     *
     * @param moneyEarningDTO the moneyEarningDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moneyEarningDTO, or with status {@code 400 (Bad Request)} if the moneyEarning has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/money-earnings")
    public ResponseEntity<MoneyEarningDTO> createMoneyEarning(@Valid @RequestBody MoneyEarningDTO moneyEarningDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MoneyEarning : {}", moneyEarningDTO);
        if (moneyEarningDTO.getId() != null) {
            throw new BadRequestAlertException("A new moneyEarning cannot already have an ID", ENTITY_NAME, "idexists");
        }
        moneyEarningDTO = moneyEarningService.save(moneyEarningDTO);
        return ResponseEntity.created(new URI("/api/money-earnings/" + moneyEarningDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, moneyEarningDTO.getId().toString()))
            .body(moneyEarningDTO);
    }

    /**
     * {@code PUT  /money-earnings/:id} : Updates an existing moneyEarning.
     *
     * @param id the id of the moneyEarningDTO to save.
     * @param moneyEarningDTO the moneyEarningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyEarningDTO,
     * or with status {@code 400 (Bad Request)} if the moneyEarningDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moneyEarningDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/money-earnings/{id}")
    public ResponseEntity<MoneyEarningDTO> updateMoneyEarning(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoneyEarningDTO moneyEarningDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MoneyEarning : {}, {}", id, moneyEarningDTO);
        if (moneyEarningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyEarningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyEarningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moneyEarningDTO = moneyEarningService.update(moneyEarningDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyEarningDTO.getId().toString()))
            .body(moneyEarningDTO);
    }

    /**
     * {@code PATCH  /money-earnings/:id} : Partial updates given fields of an existing moneyEarning, field will ignore if it is null
     *
     * @param id the id of the moneyEarningDTO to save.
     * @param moneyEarningDTO the moneyEarningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyEarningDTO,
     * or with status {@code 400 (Bad Request)} if the moneyEarningDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moneyEarningDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moneyEarningDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/money-earnings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoneyEarningDTO> partialUpdateMoneyEarning(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoneyEarningDTO moneyEarningDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MoneyEarning partially : {}, {}", id, moneyEarningDTO);
        if (moneyEarningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyEarningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyEarningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoneyEarningDTO> result = moneyEarningService.partialUpdate(moneyEarningDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyEarningDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /money-earnings} : get all the moneyEarnings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moneyEarnings in body.
     */
    @GetMapping("/money-earnings")
    public ResponseEntity<List<MoneyEarningDTO>> getAllMoneyEarnings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of MoneyEarnings");
        Page<MoneyEarningDTO> page = moneyEarningService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /money-earnings/:id} : get the "id" moneyEarning.
     *
     * @param id the id of the moneyEarningDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moneyEarningDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/money-earnings/{id}")
    public ResponseEntity<MoneyEarningDTO> getMoneyEarning(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MoneyEarning : {}", id);
        Optional<MoneyEarningDTO> moneyEarningDTO = moneyEarningService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moneyEarningDTO);
    }

    /**
     * {@code DELETE  /money-earnings/:id} : delete the "id" moneyEarning.
     *
     * @param id the id of the moneyEarningDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/money-earnings/{id}")
    public ResponseEntity<Void> deleteMoneyEarning(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MoneyEarning : {}", id);
        moneyEarningService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/money-earnings} : get all the money-earnings without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of money-earnings in body.
     */
    @GetMapping("/logical/money-earnings")
    public ResponseEntity<List<MoneyEarningDTO>> getAllLogicalMoneyEarnings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of MoneyEarnings without logical deletions");
        Page<MoneyEarningDTO> page = moneyEarningService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/money-earnings/:id} : get the "id" MoneyEarning if not logically deleted.
     *
     * @param id the id of the MoneyEarningDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the MoneyEarningDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/money-earnings/{id}")
    public ResponseEntity<MoneyEarningDTO> getLogicalMoneyEarning(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical MoneyEarning : {}", id);
        Optional<MoneyEarningDTO> moneyEarningDTO = moneyEarningService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(moneyEarningDTO);
    }

    /**
     * {@code DELETE  /logical/money-earnings/:id} : logically delete the "id" MoneyEarning.
     *
     * @param id the id of the MoneyEarningDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/money-earnings/{id}")
    public ResponseEntity<Void> logicalDeleteMoneyEarning(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete MoneyEarning : {}", id);
        if (!moneyEarningRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        moneyEarningService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/money-earnings/:id/restore} : restore a logically deleted MoneyEarning.
     *
     * @param id the id of the MoneyEarning to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored MoneyEarningDTO.
     */
    @PutMapping("/logical/money-earnings/{id}/restore")
    public ResponseEntity<MoneyEarningDTO> restoreMoneyEarning(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore MoneyEarning : {}", id);
        if (!moneyEarningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        moneyEarningService.restore(id);
        Optional<MoneyEarningDTO> restored = moneyEarningService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
