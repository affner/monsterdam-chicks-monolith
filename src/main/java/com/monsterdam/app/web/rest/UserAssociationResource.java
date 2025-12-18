package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.UserAssociationRepository;
import com.monsterdam.app.service.UserAssociationService;
import com.monsterdam.app.service.dto.UserAssociationDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.UserAssociation}.
 */
@RestController
@RequestMapping("/api")
public class UserAssociationResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserAssociationResource.class);

    private static final String ENTITY_NAME = "userAssociation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAssociationService userAssociationService;

    private final UserAssociationRepository userAssociationRepository;

    public UserAssociationResource(UserAssociationService userAssociationService, UserAssociationRepository userAssociationRepository) {
        this.userAssociationService = userAssociationService;
        this.userAssociationRepository = userAssociationRepository;
    }

    /**
     * {@code POST  /user-associations} : Create a new userAssociation.
     *
     * @param userAssociationDTO the userAssociationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAssociationDTO, or with status {@code 400 (Bad Request)} if the userAssociation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-associations")
    public ResponseEntity<UserAssociationDTO> createUserAssociation(@Valid @RequestBody UserAssociationDTO userAssociationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UserAssociation : {}", userAssociationDTO);
        if (userAssociationDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAssociation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userAssociationDTO = userAssociationService.save(userAssociationDTO);
        return ResponseEntity.created(new URI("/api/user-associations/" + userAssociationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userAssociationDTO.getId().toString()))
            .body(userAssociationDTO);
    }

    /**
     * {@code PUT  /user-associations/:id} : Updates an existing userAssociation.
     *
     * @param id the id of the userAssociationDTO to save.
     * @param userAssociationDTO the userAssociationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAssociationDTO,
     * or with status {@code 400 (Bad Request)} if the userAssociationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAssociationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-associations/{id}")
    public ResponseEntity<UserAssociationDTO> updateUserAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAssociationDTO userAssociationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserAssociation : {}, {}", id, userAssociationDTO);
        if (userAssociationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAssociationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAssociationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userAssociationDTO = userAssociationService.update(userAssociationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAssociationDTO.getId().toString()))
            .body(userAssociationDTO);
    }

    /**
     * {@code PATCH  /user-associations/:id} : Partial updates given fields of an existing userAssociation, field will ignore if it is null
     *
     * @param id the id of the userAssociationDTO to save.
     * @param userAssociationDTO the userAssociationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAssociationDTO,
     * or with status {@code 400 (Bad Request)} if the userAssociationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAssociationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAssociationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-associations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAssociationDTO> partialUpdateUserAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAssociationDTO userAssociationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserAssociation partially : {}, {}", id, userAssociationDTO);
        if (userAssociationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAssociationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAssociationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAssociationDTO> result = userAssociationService.partialUpdate(userAssociationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAssociationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-associations} : get all the userAssociations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAssociations in body.
     */
    @GetMapping("/user-associations")
    public ResponseEntity<List<UserAssociationDTO>> getAllUserAssociations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of UserAssociations");
        Page<UserAssociationDTO> page = userAssociationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-associations/:id} : get the "id" userAssociation.
     *
     * @param id the id of the userAssociationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAssociationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-associations/{id}")
    public ResponseEntity<UserAssociationDTO> getUserAssociation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserAssociation : {}", id);
        Optional<UserAssociationDTO> userAssociationDTO = userAssociationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAssociationDTO);
    }

    /**
     * {@code DELETE  /user-associations/:id} : delete the "id" userAssociation.
     *
     * @param id the id of the userAssociationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-associations/{id}")
    public ResponseEntity<Void> deleteUserAssociation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserAssociation : {}", id);
        userAssociationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/user-associations} : get all the user-associations without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of user-associations in body.
     */
    @GetMapping("/logical/user-associations")
    public ResponseEntity<List<UserAssociationDTO>> getAllLogicalUserAssociations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of UserAssociations without logical deletions");
        Page<UserAssociationDTO> page = userAssociationService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/user-associations/:id} : get the "id" UserAssociation if not logically deleted.
     *
     * @param id the id of the UserAssociationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the UserAssociationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/user-associations/{id}")
    public ResponseEntity<UserAssociationDTO> getLogicalUserAssociation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical UserAssociation : {}", id);
        Optional<UserAssociationDTO> userAssociationDTO = userAssociationService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(userAssociationDTO);
    }

    /**
     * {@code DELETE  /logical/user-associations/:id} : logically delete the "id" UserAssociation.
     *
     * @param id the id of the UserAssociationDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/user-associations/{id}")
    public ResponseEntity<Void> logicalDeleteUserAssociation(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete UserAssociation : {}", id);
        if (!userAssociationRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        userAssociationService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/user-associations/:id/restore} : restore a logically deleted UserAssociation.
     *
     * @param id the id of the UserAssociation to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored UserAssociationDTO.
     */
    @PutMapping("/logical/user-associations/{id}/restore")
    public ResponseEntity<UserAssociationDTO> restoreUserAssociation(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore UserAssociation : {}", id);
        if (!userAssociationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        userAssociationService.restore(id);
        Optional<UserAssociationDTO> restored = userAssociationService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
