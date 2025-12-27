package com.monsterdam.app.web.rest.bff.common;

import com.monsterdam.app.service.UserSettingsService;
import com.monsterdam.app.service.dto.UserSettingsDTO;
import com.monsterdam.app.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/settings")
@Tag(name = "BFF User Settings")
public class UserSettingsController {

    private static final Logger LOG = LoggerFactory.getLogger(UserSettingsController.class);
    private static final String ENTITY_NAME = "userSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSettingsService userSettingsService;

    public UserSettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @PostMapping("")
    public ResponseEntity<UserSettingsDTO> createSettings(@Valid @RequestBody UserSettingsDTO userSettingsDTO) throws URISyntaxException {
        LOG.debug("REST request to create UserSettings from BFF : {}", userSettingsDTO);
        if (userSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new userSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserSettingsDTO result = userSettingsService.save(userSettingsDTO);
        return ResponseEntity.created(new URI("/api/bff/settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSettingsDTO> updateSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserSettingsDTO userSettingsDTO
    ) {
        LOG.debug("REST request to update UserSettings from BFF : {}, {}", id, userSettingsDTO);
        if (userSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (userSettingsService.logicalGet(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        UserSettingsDTO result = userSettingsService.update(userSettingsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserSettingsDTO> partialUpdateSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserSettingsDTO userSettingsDTO
    ) {
        LOG.debug("REST request to partially update UserSettings from BFF : {}, {}", id, userSettingsDTO);
        if (userSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (userSettingsService.logicalGet(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return ResponseUtil.wrapOrNotFound(
            userSettingsService.partialUpdate(userSettingsDTO),
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userSettingsDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<UserSettingsDTO>> getAllSettings(
        @org.springdoc.core.annotations.ParameterObject org.springframework.data.domain.Pageable pageable
    ) {
        LOG.debug("REST request to get page of UserSettings from BFF");
        org.springframework.data.domain.Page<UserSettingsDTO> page = userSettingsService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSettingsDTO> getSettings(@PathVariable Long id) {
        LOG.debug("REST request to get UserSettings from BFF : {}", id);
        return ResponseUtil.wrapOrNotFound(userSettingsService.logicalGet(id));
    }
}
