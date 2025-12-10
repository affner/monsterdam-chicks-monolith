package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.AdminAnnouncementRepository;
import com.monsterdam.app.service.AdminAnnouncementService;
import com.monsterdam.app.service.dto.AdminAnnouncementDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.AdminAnnouncement}.
 */
@RestController
@RequestMapping("/api/admin-announcements")
public class AdminAnnouncementResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdminAnnouncementResource.class);

    private static final String ENTITY_NAME = "adminAnnouncement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdminAnnouncementService adminAnnouncementService;

    private final AdminAnnouncementRepository adminAnnouncementRepository;

    public AdminAnnouncementResource(
        AdminAnnouncementService adminAnnouncementService,
        AdminAnnouncementRepository adminAnnouncementRepository
    ) {
        this.adminAnnouncementService = adminAnnouncementService;
        this.adminAnnouncementRepository = adminAnnouncementRepository;
    }

    /**
     * {@code POST  /admin-announcements} : Create a new adminAnnouncement.
     *
     * @param adminAnnouncementDTO the adminAnnouncementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adminAnnouncementDTO, or with status {@code 400 (Bad Request)} if the adminAnnouncement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdminAnnouncementDTO> createAdminAnnouncement(@Valid @RequestBody AdminAnnouncementDTO adminAnnouncementDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AdminAnnouncement : {}", adminAnnouncementDTO);
        if (adminAnnouncementDTO.getId() != null) {
            throw new BadRequestAlertException("A new adminAnnouncement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        adminAnnouncementDTO = adminAnnouncementService.save(adminAnnouncementDTO);
        return ResponseEntity.created(new URI("/api/admin-announcements/" + adminAnnouncementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, adminAnnouncementDTO.getId().toString()))
            .body(adminAnnouncementDTO);
    }

    /**
     * {@code PUT  /admin-announcements/:id} : Updates an existing adminAnnouncement.
     *
     * @param id the id of the adminAnnouncementDTO to save.
     * @param adminAnnouncementDTO the adminAnnouncementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminAnnouncementDTO,
     * or with status {@code 400 (Bad Request)} if the adminAnnouncementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adminAnnouncementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdminAnnouncementDTO> updateAdminAnnouncement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdminAnnouncementDTO adminAnnouncementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AdminAnnouncement : {}, {}", id, adminAnnouncementDTO);
        if (adminAnnouncementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminAnnouncementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adminAnnouncementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        adminAnnouncementDTO = adminAnnouncementService.update(adminAnnouncementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adminAnnouncementDTO.getId().toString()))
            .body(adminAnnouncementDTO);
    }

    /**
     * {@code PATCH  /admin-announcements/:id} : Partial updates given fields of an existing adminAnnouncement, field will ignore if it is null
     *
     * @param id the id of the adminAnnouncementDTO to save.
     * @param adminAnnouncementDTO the adminAnnouncementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminAnnouncementDTO,
     * or with status {@code 400 (Bad Request)} if the adminAnnouncementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the adminAnnouncementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the adminAnnouncementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdminAnnouncementDTO> partialUpdateAdminAnnouncement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdminAnnouncementDTO adminAnnouncementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AdminAnnouncement partially : {}, {}", id, adminAnnouncementDTO);
        if (adminAnnouncementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminAnnouncementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adminAnnouncementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdminAnnouncementDTO> result = adminAnnouncementService.partialUpdate(adminAnnouncementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adminAnnouncementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /admin-announcements} : get all the adminAnnouncements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminAnnouncements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AdminAnnouncementDTO>> getAllAdminAnnouncements(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AdminAnnouncements");
        Page<AdminAnnouncementDTO> page = adminAnnouncementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /admin-announcements/:id} : get the "id" adminAnnouncement.
     *
     * @param id the id of the adminAnnouncementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminAnnouncementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminAnnouncementDTO> getAdminAnnouncement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AdminAnnouncement : {}", id);
        Optional<AdminAnnouncementDTO> adminAnnouncementDTO = adminAnnouncementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adminAnnouncementDTO);
    }

    /**
     * {@code DELETE  /admin-announcements/:id} : delete the "id" adminAnnouncement.
     *
     * @param id the id of the adminAnnouncementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminAnnouncement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AdminAnnouncement : {}", id);
        adminAnnouncementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
