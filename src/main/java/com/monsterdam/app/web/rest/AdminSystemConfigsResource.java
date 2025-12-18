package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.AdminSystemConfigsRepository;
import com.monsterdam.app.service.AdminSystemConfigsService;
import com.monsterdam.app.service.dto.AdminSystemConfigsDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.AdminSystemConfigs}.
 */
@RestController
@RequestMapping("/api")
public class AdminSystemConfigsResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdminSystemConfigsResource.class);

    private static final String ENTITY_NAME = "adminSystemConfigs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdminSystemConfigsService adminSystemConfigsService;

    private final AdminSystemConfigsRepository adminSystemConfigsRepository;

    public AdminSystemConfigsResource(
        AdminSystemConfigsService adminSystemConfigsService,
        AdminSystemConfigsRepository adminSystemConfigsRepository
    ) {
        this.adminSystemConfigsService = adminSystemConfigsService;
        this.adminSystemConfigsRepository = adminSystemConfigsRepository;
    }

    /**
     * {@code POST  /admin-system-configs} : Create a new adminSystemConfigs.
     *
     * @param adminSystemConfigsDTO the adminSystemConfigsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adminSystemConfigsDTO, or with status {@code 400 (Bad Request)} if the adminSystemConfigs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin-system-configs")
    public ResponseEntity<AdminSystemConfigsDTO> createAdminSystemConfigs(@Valid @RequestBody AdminSystemConfigsDTO adminSystemConfigsDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AdminSystemConfigs : {}", adminSystemConfigsDTO);
        if (adminSystemConfigsDTO.getId() != null) {
            throw new BadRequestAlertException("A new adminSystemConfigs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        adminSystemConfigsDTO = adminSystemConfigsService.save(adminSystemConfigsDTO);
        return ResponseEntity.created(new URI("/api/admin-system-configs/" + adminSystemConfigsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, adminSystemConfigsDTO.getId().toString()))
            .body(adminSystemConfigsDTO);
    }

    /**
     * {@code PUT  /admin-system-configs/:id} : Updates an existing adminSystemConfigs.
     *
     * @param id the id of the adminSystemConfigsDTO to save.
     * @param adminSystemConfigsDTO the adminSystemConfigsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminSystemConfigsDTO,
     * or with status {@code 400 (Bad Request)} if the adminSystemConfigsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adminSystemConfigsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin-system-configs/{id}")
    public ResponseEntity<AdminSystemConfigsDTO> updateAdminSystemConfigs(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdminSystemConfigsDTO adminSystemConfigsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AdminSystemConfigs : {}, {}", id, adminSystemConfigsDTO);
        if (adminSystemConfigsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminSystemConfigsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adminSystemConfigsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        adminSystemConfigsDTO = adminSystemConfigsService.update(adminSystemConfigsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adminSystemConfigsDTO.getId().toString()))
            .body(adminSystemConfigsDTO);
    }

    /**
     * {@code PATCH  /admin-system-configs/:id} : Partial updates given fields of an existing adminSystemConfigs, field will ignore if it is null
     *
     * @param id the id of the adminSystemConfigsDTO to save.
     * @param adminSystemConfigsDTO the adminSystemConfigsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminSystemConfigsDTO,
     * or with status {@code 400 (Bad Request)} if the adminSystemConfigsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the adminSystemConfigsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the adminSystemConfigsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/admin-system-configs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdminSystemConfigsDTO> partialUpdateAdminSystemConfigs(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdminSystemConfigsDTO adminSystemConfigsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AdminSystemConfigs partially : {}, {}", id, adminSystemConfigsDTO);
        if (adminSystemConfigsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminSystemConfigsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adminSystemConfigsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdminSystemConfigsDTO> result = adminSystemConfigsService.partialUpdate(adminSystemConfigsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adminSystemConfigsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /admin-system-configs} : get all the adminSystemConfigs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminSystemConfigs in body.
     */
    @GetMapping("/admin-system-configs")
    public ResponseEntity<List<AdminSystemConfigsDTO>> getAllAdminSystemConfigs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AdminSystemConfigs");
        Page<AdminSystemConfigsDTO> page = adminSystemConfigsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /admin-system-configs/:id} : get the "id" adminSystemConfigs.
     *
     * @param id the id of the adminSystemConfigsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminSystemConfigsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/admin-system-configs/{id}")
    public ResponseEntity<AdminSystemConfigsDTO> getAdminSystemConfigs(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AdminSystemConfigs : {}", id);
        Optional<AdminSystemConfigsDTO> adminSystemConfigsDTO = adminSystemConfigsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adminSystemConfigsDTO);
    }

    /**
     * {@code DELETE  /admin-system-configs/:id} : delete the "id" adminSystemConfigs.
     *
     * @param id the id of the adminSystemConfigsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin-system-configs/{id}")
    public ResponseEntity<Void> deleteAdminSystemConfigs(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AdminSystemConfigs : {}", id);
        adminSystemConfigsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/admin-system-configs} : get all the admin-system-configs without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of admin-system-configs in body.
     */
    @GetMapping("/logical/admin-system-configs")
    public ResponseEntity<List<AdminSystemConfigsDTO>> getAllLogicalAdminSystemConfigss(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AdminSystemConfigss without logical deletions");
        Page<AdminSystemConfigsDTO> page = adminSystemConfigsService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/admin-system-configs/:id} : get the "id" AdminSystemConfigs if not logically deleted.
     *
     * @param id the id of the AdminSystemConfigsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the AdminSystemConfigsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/admin-system-configs/{id}")
    public ResponseEntity<AdminSystemConfigsDTO> getLogicalAdminSystemConfigs(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical AdminSystemConfigs : {}", id);
        Optional<AdminSystemConfigsDTO> adminSystemConfigsDTO = adminSystemConfigsService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(adminSystemConfigsDTO);
    }

    /**
     * {@code DELETE  /logical/admin-system-configs/:id} : logically delete the "id" AdminSystemConfigs.
     *
     * @param id the id of the AdminSystemConfigsDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/admin-system-configs/{id}")
    public ResponseEntity<Void> logicalDeleteAdminSystemConfigs(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete AdminSystemConfigs : {}", id);
        if (!adminSystemConfigsRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        adminSystemConfigsService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/admin-system-configs/:id/restore} : restore a logically deleted AdminSystemConfigs.
     *
     * @param id the id of the AdminSystemConfigs to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored AdminSystemConfigsDTO.
     */
    @PutMapping("/logical/admin-system-configs/{id}/restore")
    public ResponseEntity<AdminSystemConfigsDTO> restoreAdminSystemConfigs(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore AdminSystemConfigs : {}", id);
        if (!adminSystemConfigsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        adminSystemConfigsService.restore(id);
        Optional<AdminSystemConfigsDTO> restored = adminSystemConfigsService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
