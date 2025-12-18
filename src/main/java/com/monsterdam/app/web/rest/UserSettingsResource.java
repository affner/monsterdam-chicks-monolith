package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.UserSettingsRepository;
import com.monsterdam.app.service.UserSettingsService;
import com.monsterdam.app.service.dto.UserSettingsDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.monsterdam.app.domain.UserSettings}.
 */
@RestController
@RequestMapping("/api")
public class UserSettingsResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserSettingsResource.class);

    private static final String ENTITY_NAME = "userSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSettingsService userSettingsService;

    private final UserSettingsRepository userSettingsRepository;

    public UserSettingsResource(UserSettingsService userSettingsService, UserSettingsRepository userSettingsRepository) {
        this.userSettingsService = userSettingsService;
        this.userSettingsRepository = userSettingsRepository;
    }

    /**
     * {@code POST  /user-settings} : Create a new userSettings.
     *
     * @param userSettingsDTO the userSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSettingsDTO, or with status {@code 400 (Bad Request)} if the userSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-settings")
    public ResponseEntity<UserSettingsDTO> createUserSettings(@Valid @RequestBody UserSettingsDTO userSettingsDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UserSettings : {}", userSettingsDTO);
        if (userSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new userSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userSettingsDTO = userSettingsService.save(userSettingsDTO);
        return ResponseEntity.created(new URI("/api/user-settings/" + userSettingsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userSettingsDTO.getId().toString()))
            .body(userSettingsDTO);
    }

    /**
     * {@code PUT  /user-settings/:id} : Updates an existing userSettings.
     *
     * @param id the id of the userSettingsDTO to save.
     * @param userSettingsDTO the userSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the userSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-settings/{id}")
    public ResponseEntity<UserSettingsDTO> updateUserSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserSettingsDTO userSettingsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserSettings : {}, {}", id, userSettingsDTO);
        if (userSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userSettingsDTO = userSettingsService.update(userSettingsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userSettingsDTO.getId().toString()))
            .body(userSettingsDTO);
    }

    /**
     * {@code PATCH  /user-settings/:id} : Partial updates given fields of an existing userSettings, field will ignore if it is null
     *
     * @param id the id of the userSettingsDTO to save.
     * @param userSettingsDTO the userSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the userSettingsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userSettingsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-settings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserSettingsDTO> partialUpdateUserSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserSettingsDTO userSettingsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserSettings partially : {}, {}", id, userSettingsDTO);
        if (userSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserSettingsDTO> result = userSettingsService.partialUpdate(userSettingsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userSettingsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-settings} : get all the userSettings.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSettings in body.
     */
    @GetMapping("/user-settings")
    public ResponseEntity<List<UserSettingsDTO>> getAllUserSettings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("user-is-null".equals(filter)) {
            LOG.debug("REST request to get all UserSettingss where user is null");
            return new ResponseEntity<>(userSettingsService.findAllWhereUserIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of UserSettings");
        Page<UserSettingsDTO> page = userSettingsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-settings/:id} : get the "id" userSettings.
     *
     * @param id the id of the userSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-settings/{id}")
    public ResponseEntity<UserSettingsDTO> getUserSettings(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserSettings : {}", id);
        Optional<UserSettingsDTO> userSettingsDTO = userSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userSettingsDTO);
    }

    /**
     * {@code DELETE  /user-settings/:id} : delete the "id" userSettings.
     *
     * @param id the id of the userSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-settings/{id}")
    public ResponseEntity<Void> deleteUserSettings(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserSettings : {}", id);
        userSettingsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/user-settings} : get all the user-settings without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of user-settings in body.
     */
    @GetMapping("/logical/user-settings")
    public ResponseEntity<List<UserSettingsDTO>> getAllLogicalUserSettingss(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of UserSettingss without logical deletions");
        Page<UserSettingsDTO> page = userSettingsService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/user-settings/:id} : get the "id" UserSettings if not logically deleted.
     *
     * @param id the id of the UserSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the UserSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/user-settings/{id}")
    public ResponseEntity<UserSettingsDTO> getLogicalUserSettings(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical UserSettings : {}", id);
        Optional<UserSettingsDTO> userSettingsDTO = userSettingsService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(userSettingsDTO);
    }

    /**
     * {@code DELETE  /logical/user-settings/:id} : logically delete the "id" UserSettings.
     *
     * @param id the id of the UserSettingsDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/user-settings/{id}")
    public ResponseEntity<Void> logicalDeleteUserSettings(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete UserSettings : {}", id);
        if (!userSettingsRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        userSettingsService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/user-settings/:id/restore} : restore a logically deleted UserSettings.
     *
     * @param id the id of the UserSettings to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored UserSettingsDTO.
     */
    @PutMapping("/logical/user-settings/{id}/restore")
    public ResponseEntity<UserSettingsDTO> restoreUserSettings(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore UserSettings : {}", id);
        if (!userSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        userSettingsService.restore(id);
        Optional<UserSettingsDTO> restored = userSettingsService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
