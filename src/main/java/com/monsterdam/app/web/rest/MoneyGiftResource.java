package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.MoneyGiftRepository;
import com.monsterdam.app.service.MoneyGiftService;
import com.monsterdam.app.service.dto.MoneyGiftDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.MoneyGift}.
 */
@RestController
@RequestMapping("/api")
public class MoneyGiftResource {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyGiftResource.class);

    private static final String ENTITY_NAME = "moneyGift";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoneyGiftService moneyGiftService;

    private final MoneyGiftRepository moneyGiftRepository;

    public MoneyGiftResource(MoneyGiftService moneyGiftService, MoneyGiftRepository moneyGiftRepository) {
        this.moneyGiftService = moneyGiftService;
        this.moneyGiftRepository = moneyGiftRepository;
    }

    /**
     * {@code POST  /money-gifts} : Create a new moneyGift.
     *
     * @param moneyGiftDTO the moneyGiftDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moneyGiftDTO, or with status {@code 400 (Bad Request)} if the moneyGift has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/money-gifts")
    public ResponseEntity<MoneyGiftDTO> createMoneyGift(@Valid @RequestBody MoneyGiftDTO moneyGiftDTO) throws URISyntaxException {
        LOG.debug("REST request to save MoneyGift : {}", moneyGiftDTO);
        if (moneyGiftDTO.getId() != null) {
            throw new BadRequestAlertException("A new moneyGift cannot already have an ID", ENTITY_NAME, "idexists");
        }
        moneyGiftDTO = moneyGiftService.save(moneyGiftDTO);
        return ResponseEntity.created(new URI("/api/money-gifts/" + moneyGiftDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, moneyGiftDTO.getId().toString()))
            .body(moneyGiftDTO);
    }

    /**
     * {@code PUT  /money-gifts/:id} : Updates an existing moneyGift.
     *
     * @param id the id of the moneyGiftDTO to save.
     * @param moneyGiftDTO the moneyGiftDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyGiftDTO,
     * or with status {@code 400 (Bad Request)} if the moneyGiftDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moneyGiftDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/money-gifts/{id}")
    public ResponseEntity<MoneyGiftDTO> updateMoneyGift(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoneyGiftDTO moneyGiftDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MoneyGift : {}, {}", id, moneyGiftDTO);
        if (moneyGiftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyGiftDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyGiftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moneyGiftDTO = moneyGiftService.update(moneyGiftDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyGiftDTO.getId().toString()))
            .body(moneyGiftDTO);
    }

    /**
     * {@code PATCH  /money-gifts/:id} : Partial updates given fields of an existing moneyGift, field will ignore if it is null
     *
     * @param id the id of the moneyGiftDTO to save.
     * @param moneyGiftDTO the moneyGiftDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyGiftDTO,
     * or with status {@code 400 (Bad Request)} if the moneyGiftDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moneyGiftDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moneyGiftDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/money-gifts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoneyGiftDTO> partialUpdateMoneyGift(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoneyGiftDTO moneyGiftDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MoneyGift partially : {}, {}", id, moneyGiftDTO);
        if (moneyGiftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyGiftDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyGiftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoneyGiftDTO> result = moneyGiftService.partialUpdate(moneyGiftDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyGiftDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /money-gifts} : get all the moneyGifts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moneyGifts in body.
     */
    @GetMapping("/money-gifts")
    public ResponseEntity<List<MoneyGiftDTO>> getAllMoneyGifts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of MoneyGifts");
        Page<MoneyGiftDTO> page = moneyGiftService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /money-gifts/:id} : get the "id" moneyGift.
     *
     * @param id the id of the moneyGiftDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moneyGiftDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/money-gifts/{id}")
    public ResponseEntity<MoneyGiftDTO> getMoneyGift(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MoneyGift : {}", id);
        Optional<MoneyGiftDTO> moneyGiftDTO = moneyGiftService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moneyGiftDTO);
    }

    /**
     * {@code DELETE  /money-gifts/:id} : delete the "id" moneyGift.
     *
     * @param id the id of the moneyGiftDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/money-gifts/{id}")
    public ResponseEntity<Void> deleteMoneyGift(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MoneyGift : {}", id);
        moneyGiftService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/money-gifts} : get all the money-gifts without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of money-gifts in body.
     */
    @GetMapping("/logical/money-gifts")
    public ResponseEntity<List<MoneyGiftDTO>> getAllLogicalMoneyGifts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of MoneyGifts without logical deletions");
        Page<MoneyGiftDTO> page = moneyGiftService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/money-gifts/:id} : get the "id" MoneyGift if not logically deleted.
     *
     * @param id the id of the MoneyGiftDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the MoneyGiftDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/money-gifts/{id}")
    public ResponseEntity<MoneyGiftDTO> getLogicalMoneyGift(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical MoneyGift : {}", id);
        Optional<MoneyGiftDTO> moneyGiftDTO = moneyGiftService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(moneyGiftDTO);
    }

    /**
     * {@code DELETE  /logical/money-gifts/:id} : logically delete the "id" MoneyGift.
     *
     * @param id the id of the MoneyGiftDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/money-gifts/{id}")
    public ResponseEntity<Void> logicalDeleteMoneyGift(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete MoneyGift : {}", id);
        if (!moneyGiftRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        moneyGiftService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/money-gifts/:id/restore} : restore a logically deleted MoneyGift.
     *
     * @param id the id of the MoneyGift to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored MoneyGiftDTO.
     */
    @PutMapping("/logical/money-gifts/{id}/restore")
    public ResponseEntity<MoneyGiftDTO> restoreMoneyGift(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore MoneyGift : {}", id);
        if (!moneyGiftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        moneyGiftService.restore(id);
        Optional<MoneyGiftDTO> restored = moneyGiftService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
