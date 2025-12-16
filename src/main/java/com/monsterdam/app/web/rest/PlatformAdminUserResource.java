package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.PlatformAdminUserRepository;
import com.monsterdam.app.service.PlatformAdminUserService;
import com.monsterdam.app.service.dto.PlatformAdminUserDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.PlatformAdminUser}.
 */
@RestController
@RequestMapping("/api/platform-admin-users")
public class PlatformAdminUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlatformAdminUserResource.class);

    private static final String ENTITY_NAME = "platformAdminUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlatformAdminUserService platformAdminUserService;

    private final PlatformAdminUserRepository platformAdminUserRepository;

    public PlatformAdminUserResource(
        PlatformAdminUserService platformAdminUserService,
        PlatformAdminUserRepository platformAdminUserRepository
    ) {
        this.platformAdminUserService = platformAdminUserService;
        this.platformAdminUserRepository = platformAdminUserRepository;
    }

    /**
     * {@code POST  /platform-admin-users} : Create a new platformAdminUser.
     *
     * @param platformAdminUserDTO the platformAdminUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new platformAdminUserDTO, or with status {@code 400 (Bad Request)} if the platformAdminUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlatformAdminUserDTO> createPlatformAdminUser(@Valid @RequestBody PlatformAdminUserDTO platformAdminUserDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PlatformAdminUser : {}", platformAdminUserDTO);
        if (platformAdminUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new platformAdminUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        platformAdminUserDTO = platformAdminUserService.save(platformAdminUserDTO);
        return ResponseEntity.created(new URI("/api/platform-admin-users/" + platformAdminUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, platformAdminUserDTO.getId().toString()))
            .body(platformAdminUserDTO);
    }

    /**
     * {@code PUT  /platform-admin-users/:id} : Updates an existing platformAdminUser.
     *
     * @param id the id of the platformAdminUserDTO to save.
     * @param platformAdminUserDTO the platformAdminUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated platformAdminUserDTO,
     * or with status {@code 400 (Bad Request)} if the platformAdminUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the platformAdminUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlatformAdminUserDTO> updatePlatformAdminUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlatformAdminUserDTO platformAdminUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PlatformAdminUser : {}, {}", id, platformAdminUserDTO);
        if (platformAdminUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, platformAdminUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!platformAdminUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        platformAdminUserDTO = platformAdminUserService.update(platformAdminUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, platformAdminUserDTO.getId().toString()))
            .body(platformAdminUserDTO);
    }

    /**
     * {@code PATCH  /platform-admin-users/:id} : Partial updates given fields of an existing platformAdminUser, field will ignore if it is null
     *
     * @param id the id of the platformAdminUserDTO to save.
     * @param platformAdminUserDTO the platformAdminUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated platformAdminUserDTO,
     * or with status {@code 400 (Bad Request)} if the platformAdminUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the platformAdminUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the platformAdminUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlatformAdminUserDTO> partialUpdatePlatformAdminUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlatformAdminUserDTO platformAdminUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PlatformAdminUser partially : {}, {}", id, platformAdminUserDTO);
        if (platformAdminUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, platformAdminUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!platformAdminUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlatformAdminUserDTO> result = platformAdminUserService.partialUpdate(platformAdminUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, platformAdminUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /platform-admin-users} : get all the platformAdminUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of platformAdminUsers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlatformAdminUserDTO>> getAllPlatformAdminUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PlatformAdminUsers");
        Page<PlatformAdminUserDTO> page = platformAdminUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /platform-admin-users/:id} : get the "id" platformAdminUser.
     *
     * @param id the id of the platformAdminUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the platformAdminUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlatformAdminUserDTO> getPlatformAdminUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PlatformAdminUser : {}", id);
        Optional<PlatformAdminUserDTO> platformAdminUserDTO = platformAdminUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(platformAdminUserDTO);
    }

    /**
     * {@code DELETE  /platform-admin-users/:id} : delete the "id" platformAdminUser.
     *
     * @param id the id of the platformAdminUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlatformAdminUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PlatformAdminUser : {}", id);
        platformAdminUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
