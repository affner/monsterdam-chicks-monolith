package com.monsterdam.app.web.crud;

import com.monsterdam.app.repository.UserReportRepository;
import com.monsterdam.app.service.UserReportService;
import com.monsterdam.app.service.dto.UserReportDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.UserReport}.
 */
@RestController
@RequestMapping("/api/crud/user-reports")
public class UserReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserReportResource.class);

    private static final String ENTITY_NAME = "userReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserReportService userReportService;

    private final UserReportRepository userReportRepository;

    public UserReportResource(UserReportService userReportService, UserReportRepository userReportRepository) {
        this.userReportService = userReportService;
        this.userReportRepository = userReportRepository;
    }

    /**
     * {@code POST  /user-reports} : Create a new userReport.
     *
     * @param userReportDTO the userReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userReportDTO, or with status {@code 400 (Bad Request)} if the userReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserReportDTO> createUserReport(@Valid @RequestBody UserReportDTO userReportDTO) throws URISyntaxException {
        LOG.debug("REST request to save UserReport : {}", userReportDTO);
        if (userReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new userReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userReportDTO = userReportService.save(userReportDTO);
        return ResponseEntity.created(new URI("/api/crud/user-reports/" + userReportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userReportDTO.getId().toString()))
            .body(userReportDTO);
    }

    /**
     * {@code PUT  /user-reports/:id} : Updates an existing userReport.
     *
     * @param id the id of the userReportDTO to save.
     * @param userReportDTO the userReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userReportDTO,
     * or with status {@code 400 (Bad Request)} if the userReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserReportDTO> updateUserReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserReportDTO userReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserReport : {}, {}", id, userReportDTO);
        if (userReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userReportDTO = userReportService.update(userReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userReportDTO.getId().toString()))
            .body(userReportDTO);
    }

    /**
     * {@code PATCH  /user-reports/:id} : Partial updates given fields of an existing userReport, field will ignore if it is null
     *
     * @param id the id of the userReportDTO to save.
     * @param userReportDTO the userReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userReportDTO,
     * or with status {@code 400 (Bad Request)} if the userReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserReportDTO> partialUpdateUserReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserReportDTO userReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserReport partially : {}, {}", id, userReportDTO);
        if (userReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserReportDTO> result = userReportService.partialUpdate(userReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-reports} : get all the userReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userReports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserReportDTO>> getAllUserReports(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of UserReports");
        Page<UserReportDTO> page = userReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-reports/:id} : get the "id" userReport.
     *
     * @param id the id of the userReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserReportDTO> getUserReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserReport : {}", id);
        Optional<UserReportDTO> userReportDTO = userReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userReportDTO);
    }

    /**
     * {@code DELETE  /user-reports/:id} : delete the "id" userReport.
     *
     * @param id the id of the userReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserReport : {}", id);
        userReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
