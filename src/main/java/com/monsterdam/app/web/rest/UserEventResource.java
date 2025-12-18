package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.UserEventRepository;
import com.monsterdam.app.service.UserEventService;
import com.monsterdam.app.service.dto.UserEventDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.UserEvent}.
 */
@RestController
@RequestMapping("/api")
public class UserEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserEventResource.class);

    private static final String ENTITY_NAME = "userEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserEventService userEventService;

    private final UserEventRepository userEventRepository;

    public UserEventResource(UserEventService userEventService, UserEventRepository userEventRepository) {
        this.userEventService = userEventService;
        this.userEventRepository = userEventRepository;
    }

    /**
     * {@code POST  /user-events} : Create a new userEvent.
     *
     * @param userEventDTO the userEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userEventDTO, or with status {@code 400 (Bad Request)} if the userEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-events")
    public ResponseEntity<UserEventDTO> createUserEvent(@Valid @RequestBody UserEventDTO userEventDTO) throws URISyntaxException {
        LOG.debug("REST request to save UserEvent : {}", userEventDTO);
        if (userEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new userEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userEventDTO = userEventService.save(userEventDTO);
        return ResponseEntity.created(new URI("/api/user-events/" + userEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userEventDTO.getId().toString()))
            .body(userEventDTO);
    }

    /**
     * {@code PUT  /user-events/:id} : Updates an existing userEvent.
     *
     * @param id the id of the userEventDTO to save.
     * @param userEventDTO the userEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userEventDTO,
     * or with status {@code 400 (Bad Request)} if the userEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-events/{id}")
    public ResponseEntity<UserEventDTO> updateUserEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserEventDTO userEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserEvent : {}, {}", id, userEventDTO);
        if (userEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userEventDTO = userEventService.update(userEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userEventDTO.getId().toString()))
            .body(userEventDTO);
    }

    /**
     * {@code PATCH  /user-events/:id} : Partial updates given fields of an existing userEvent, field will ignore if it is null
     *
     * @param id the id of the userEventDTO to save.
     * @param userEventDTO the userEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userEventDTO,
     * or with status {@code 400 (Bad Request)} if the userEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-events/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserEventDTO> partialUpdateUserEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserEventDTO userEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserEvent partially : {}, {}", id, userEventDTO);
        if (userEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserEventDTO> result = userEventService.partialUpdate(userEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-events} : get all the userEvents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userEvents in body.
     */
    @GetMapping("/user-events")
    public ResponseEntity<List<UserEventDTO>> getAllUserEvents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of UserEvents");
        Page<UserEventDTO> page = userEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-events/:id} : get the "id" userEvent.
     *
     * @param id the id of the userEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-events/{id}")
    public ResponseEntity<UserEventDTO> getUserEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserEvent : {}", id);
        Optional<UserEventDTO> userEventDTO = userEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userEventDTO);
    }

    /**
     * {@code DELETE  /user-events/:id} : delete the "id" userEvent.
     *
     * @param id the id of the userEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-events/{id}")
    public ResponseEntity<Void> deleteUserEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserEvent : {}", id);
        userEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/user-events} : get all the user-events without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of user-events in body.
     */
    @GetMapping("/logical/user-events")
    public ResponseEntity<List<UserEventDTO>> getAllLogicalUserEvents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of UserEvents without logical deletions");
        Page<UserEventDTO> page = userEventService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/user-events/:id} : get the "id" UserEvent if not logically deleted.
     *
     * @param id the id of the UserEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the UserEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/user-events/{id}")
    public ResponseEntity<UserEventDTO> getLogicalUserEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical UserEvent : {}", id);
        Optional<UserEventDTO> userEventDTO = userEventService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(userEventDTO);
    }

    /**
     * {@code DELETE  /logical/user-events/:id} : logically delete the "id" UserEvent.
     *
     * @param id the id of the UserEventDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/user-events/{id}")
    public ResponseEntity<Void> logicalDeleteUserEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete UserEvent : {}", id);
        if (!userEventRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        userEventService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/user-events/:id/restore} : restore a logically deleted UserEvent.
     *
     * @param id the id of the UserEvent to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored UserEventDTO.
     */
    @PutMapping("/logical/user-events/{id}/restore")
    public ResponseEntity<UserEventDTO> restoreUserEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore UserEvent : {}", id);
        if (!userEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        userEventService.restore(id);
        Optional<UserEventDTO> restored = userEventService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
