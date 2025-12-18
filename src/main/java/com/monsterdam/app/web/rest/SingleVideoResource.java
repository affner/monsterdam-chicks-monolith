package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.SingleVideoRepository;
import com.monsterdam.app.service.SingleVideoService;
import com.monsterdam.app.service.dto.SingleVideoDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.SingleVideo}.
 */
@RestController
@RequestMapping("/api")
public class SingleVideoResource {

    private static final Logger LOG = LoggerFactory.getLogger(SingleVideoResource.class);

    private static final String ENTITY_NAME = "singleVideo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SingleVideoService singleVideoService;

    private final SingleVideoRepository singleVideoRepository;

    public SingleVideoResource(SingleVideoService singleVideoService, SingleVideoRepository singleVideoRepository) {
        this.singleVideoService = singleVideoService;
        this.singleVideoRepository = singleVideoRepository;
    }

    /**
     * {@code POST  /single-videos} : Create a new singleVideo.
     *
     * @param singleVideoDTO the singleVideoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new singleVideoDTO, or with status {@code 400 (Bad Request)} if the singleVideo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/single-videos")
    public ResponseEntity<SingleVideoDTO> createSingleVideo(@Valid @RequestBody SingleVideoDTO singleVideoDTO) throws URISyntaxException {
        LOG.debug("REST request to save SingleVideo : {}", singleVideoDTO);
        if (singleVideoDTO.getId() != null) {
            throw new BadRequestAlertException("A new singleVideo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        singleVideoDTO = singleVideoService.save(singleVideoDTO);
        return ResponseEntity.created(new URI("/api/single-videos/" + singleVideoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, singleVideoDTO.getId().toString()))
            .body(singleVideoDTO);
    }

    /**
     * {@code PUT  /single-videos/:id} : Updates an existing singleVideo.
     *
     * @param id the id of the singleVideoDTO to save.
     * @param singleVideoDTO the singleVideoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleVideoDTO,
     * or with status {@code 400 (Bad Request)} if the singleVideoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the singleVideoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/single-videos/{id}")
    public ResponseEntity<SingleVideoDTO> updateSingleVideo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SingleVideoDTO singleVideoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SingleVideo : {}, {}", id, singleVideoDTO);
        if (singleVideoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleVideoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!singleVideoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        singleVideoDTO = singleVideoService.update(singleVideoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, singleVideoDTO.getId().toString()))
            .body(singleVideoDTO);
    }

    /**
     * {@code PATCH  /single-videos/:id} : Partial updates given fields of an existing singleVideo, field will ignore if it is null
     *
     * @param id the id of the singleVideoDTO to save.
     * @param singleVideoDTO the singleVideoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleVideoDTO,
     * or with status {@code 400 (Bad Request)} if the singleVideoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the singleVideoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the singleVideoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/single-videos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SingleVideoDTO> partialUpdateSingleVideo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SingleVideoDTO singleVideoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SingleVideo partially : {}, {}", id, singleVideoDTO);
        if (singleVideoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleVideoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!singleVideoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SingleVideoDTO> result = singleVideoService.partialUpdate(singleVideoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, singleVideoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /single-videos} : get all the singleVideos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of singleVideos in body.
     */
    @GetMapping("/single-videos")
    public ResponseEntity<List<SingleVideoDTO>> getAllSingleVideos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SingleVideos");
        Page<SingleVideoDTO> page = singleVideoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /single-videos/:id} : get the "id" singleVideo.
     *
     * @param id the id of the singleVideoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the singleVideoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/single-videos/{id}")
    public ResponseEntity<SingleVideoDTO> getSingleVideo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SingleVideo : {}", id);
        Optional<SingleVideoDTO> singleVideoDTO = singleVideoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(singleVideoDTO);
    }

    /**
     * {@code DELETE  /single-videos/:id} : delete the "id" singleVideo.
     *
     * @param id the id of the singleVideoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/single-videos/{id}")
    public ResponseEntity<Void> deleteSingleVideo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SingleVideo : {}", id);
        singleVideoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/single-videos} : get all the single-videos without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of single-videos in body.
     */
    @GetMapping("/logical/single-videos")
    public ResponseEntity<List<SingleVideoDTO>> getAllLogicalSingleVideos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of SingleVideos without logical deletions");
        Page<SingleVideoDTO> page = singleVideoService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/single-videos/:id} : get the "id" SingleVideo if not logically deleted.
     *
     * @param id the id of the SingleVideoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the SingleVideoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/single-videos/{id}")
    public ResponseEntity<SingleVideoDTO> getLogicalSingleVideo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical SingleVideo : {}", id);
        Optional<SingleVideoDTO> singleVideoDTO = singleVideoService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(singleVideoDTO);
    }

    /**
     * {@code DELETE  /logical/single-videos/:id} : logically delete the "id" SingleVideo.
     *
     * @param id the id of the SingleVideoDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/single-videos/{id}")
    public ResponseEntity<Void> logicalDeleteSingleVideo(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete SingleVideo : {}", id);
        if (!singleVideoRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        singleVideoService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/single-videos/:id/restore} : restore a logically deleted SingleVideo.
     *
     * @param id the id of the SingleVideo to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored SingleVideoDTO.
     */
    @PutMapping("/logical/single-videos/{id}/restore")
    public ResponseEntity<SingleVideoDTO> restoreSingleVideo(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore SingleVideo : {}", id);
        if (!singleVideoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        singleVideoService.restore(id);
        Optional<SingleVideoDTO> restored = singleVideoService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
