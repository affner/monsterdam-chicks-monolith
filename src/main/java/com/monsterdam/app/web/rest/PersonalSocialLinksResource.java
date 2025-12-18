package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.PersonalSocialLinksRepository;
import com.monsterdam.app.service.PersonalSocialLinksService;
import com.monsterdam.app.service.dto.PersonalSocialLinksDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.PersonalSocialLinks}.
 */
@RestController
@RequestMapping("/api")
public class PersonalSocialLinksResource {

    private static final Logger LOG = LoggerFactory.getLogger(PersonalSocialLinksResource.class);

    private static final String ENTITY_NAME = "personalSocialLinks";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalSocialLinksService personalSocialLinksService;

    private final PersonalSocialLinksRepository personalSocialLinksRepository;

    public PersonalSocialLinksResource(
        PersonalSocialLinksService personalSocialLinksService,
        PersonalSocialLinksRepository personalSocialLinksRepository
    ) {
        this.personalSocialLinksService = personalSocialLinksService;
        this.personalSocialLinksRepository = personalSocialLinksRepository;
    }

    /**
     * {@code POST  /personal-social-links} : Create a new personalSocialLinks.
     *
     * @param personalSocialLinksDTO the personalSocialLinksDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personalSocialLinksDTO, or with status {@code 400 (Bad Request)} if the personalSocialLinks has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personal-social-links")
    public ResponseEntity<PersonalSocialLinksDTO> createPersonalSocialLinks(
        @Valid @RequestBody PersonalSocialLinksDTO personalSocialLinksDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PersonalSocialLinks : {}", personalSocialLinksDTO);
        if (personalSocialLinksDTO.getId() != null) {
            throw new BadRequestAlertException("A new personalSocialLinks cannot already have an ID", ENTITY_NAME, "idexists");
        }
        personalSocialLinksDTO = personalSocialLinksService.save(personalSocialLinksDTO);
        return ResponseEntity.created(new URI("/api/personal-social-links/" + personalSocialLinksDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, personalSocialLinksDTO.getId().toString()))
            .body(personalSocialLinksDTO);
    }

    /**
     * {@code PUT  /personal-social-links/:id} : Updates an existing personalSocialLinks.
     *
     * @param id the id of the personalSocialLinksDTO to save.
     * @param personalSocialLinksDTO the personalSocialLinksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalSocialLinksDTO,
     * or with status {@code 400 (Bad Request)} if the personalSocialLinksDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personalSocialLinksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personal-social-links/{id}")
    public ResponseEntity<PersonalSocialLinksDTO> updatePersonalSocialLinks(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonalSocialLinksDTO personalSocialLinksDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PersonalSocialLinks : {}, {}", id, personalSocialLinksDTO);
        if (personalSocialLinksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalSocialLinksDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalSocialLinksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        personalSocialLinksDTO = personalSocialLinksService.update(personalSocialLinksDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalSocialLinksDTO.getId().toString()))
            .body(personalSocialLinksDTO);
    }

    /**
     * {@code PATCH  /personal-social-links/:id} : Partial updates given fields of an existing personalSocialLinks, field will ignore if it is null
     *
     * @param id the id of the personalSocialLinksDTO to save.
     * @param personalSocialLinksDTO the personalSocialLinksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalSocialLinksDTO,
     * or with status {@code 400 (Bad Request)} if the personalSocialLinksDTO is not valid,
     * or with status {@code 404 (Not Found)} if the personalSocialLinksDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the personalSocialLinksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personal-social-links/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonalSocialLinksDTO> partialUpdatePersonalSocialLinks(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonalSocialLinksDTO personalSocialLinksDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PersonalSocialLinks partially : {}, {}", id, personalSocialLinksDTO);
        if (personalSocialLinksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalSocialLinksDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalSocialLinksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonalSocialLinksDTO> result = personalSocialLinksService.partialUpdate(personalSocialLinksDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalSocialLinksDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /personal-social-links} : get all the personalSocialLinks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personalSocialLinks in body.
     */
    @GetMapping("/personal-social-links")
    public ResponseEntity<List<PersonalSocialLinksDTO>> getAllPersonalSocialLinks(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PersonalSocialLinks");
        Page<PersonalSocialLinksDTO> page = personalSocialLinksService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personal-social-links/:id} : get the "id" personalSocialLinks.
     *
     * @param id the id of the personalSocialLinksDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personalSocialLinksDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personal-social-links/{id}")
    public ResponseEntity<PersonalSocialLinksDTO> getPersonalSocialLinks(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PersonalSocialLinks : {}", id);
        Optional<PersonalSocialLinksDTO> personalSocialLinksDTO = personalSocialLinksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personalSocialLinksDTO);
    }

    /**
     * {@code DELETE  /personal-social-links/:id} : delete the "id" personalSocialLinks.
     *
     * @param id the id of the personalSocialLinksDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personal-social-links/{id}")
    public ResponseEntity<Void> deletePersonalSocialLinks(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PersonalSocialLinks : {}", id);
        personalSocialLinksService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/personal-social-links} : get all the personal-social-links without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personal-social-links in body.
     */
    @GetMapping("/logical/personal-social-links")
    public ResponseEntity<List<PersonalSocialLinksDTO>> getAllLogicalPersonalSocialLinkss(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PersonalSocialLinkss without logical deletions");
        Page<PersonalSocialLinksDTO> page = personalSocialLinksService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/personal-social-links/:id} : get the "id" PersonalSocialLinks if not logically deleted.
     *
     * @param id the id of the PersonalSocialLinksDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PersonalSocialLinksDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/personal-social-links/{id}")
    public ResponseEntity<PersonalSocialLinksDTO> getLogicalPersonalSocialLinks(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical PersonalSocialLinks : {}", id);
        Optional<PersonalSocialLinksDTO> personalSocialLinksDTO = personalSocialLinksService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(personalSocialLinksDTO);
    }

    /**
     * {@code DELETE  /logical/personal-social-links/:id} : logically delete the "id" PersonalSocialLinks.
     *
     * @param id the id of the PersonalSocialLinksDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/personal-social-links/{id}")
    public ResponseEntity<Void> logicalDeletePersonalSocialLinks(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete PersonalSocialLinks : {}", id);
        if (!personalSocialLinksRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        personalSocialLinksService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/personal-social-links/:id/restore} : restore a logically deleted PersonalSocialLinks.
     *
     * @param id the id of the PersonalSocialLinks to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored PersonalSocialLinksDTO.
     */
    @PutMapping("/logical/personal-social-links/{id}/restore")
    public ResponseEntity<PersonalSocialLinksDTO> restorePersonalSocialLinks(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore PersonalSocialLinks : {}", id);
        if (!personalSocialLinksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        personalSocialLinksService.restore(id);
        Optional<PersonalSocialLinksDTO> restored = personalSocialLinksService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
