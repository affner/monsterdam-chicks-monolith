package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.ViewerWalletRepository;
import com.monsterdam.app.service.ViewerWalletService;
import com.monsterdam.app.service.dto.ViewerWalletDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.ViewerWallet}.
 */
@RestController
@RequestMapping("/api/viewer-wallets")
public class ViewerWalletResource {

    private static final Logger LOG = LoggerFactory.getLogger(ViewerWalletResource.class);

    private static final String ENTITY_NAME = "viewerWallet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViewerWalletService viewerWalletService;

    private final ViewerWalletRepository viewerWalletRepository;

    public ViewerWalletResource(ViewerWalletService viewerWalletService, ViewerWalletRepository viewerWalletRepository) {
        this.viewerWalletService = viewerWalletService;
        this.viewerWalletRepository = viewerWalletRepository;
    }

    /**
     * {@code POST  /viewer-wallets} : Create a new viewerWallet.
     *
     * @param viewerWalletDTO the viewerWalletDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new viewerWalletDTO, or with status {@code 400 (Bad Request)} if the viewerWallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ViewerWalletDTO> createViewerWallet(@Valid @RequestBody ViewerWalletDTO viewerWalletDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ViewerWallet : {}", viewerWalletDTO);
        if (viewerWalletDTO.getId() != null) {
            throw new BadRequestAlertException("A new viewerWallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        viewerWalletDTO = viewerWalletService.save(viewerWalletDTO);
        return ResponseEntity.created(new URI("/api/viewer-wallets/" + viewerWalletDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, viewerWalletDTO.getId().toString()))
            .body(viewerWalletDTO);
    }

    /**
     * {@code PUT  /viewer-wallets/:id} : Updates an existing viewerWallet.
     *
     * @param id the id of the viewerWalletDTO to save.
     * @param viewerWalletDTO the viewerWalletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewerWalletDTO,
     * or with status {@code 400 (Bad Request)} if the viewerWalletDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewerWalletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ViewerWalletDTO> updateViewerWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ViewerWalletDTO viewerWalletDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ViewerWallet : {}, {}", id, viewerWalletDTO);
        if (viewerWalletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewerWalletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viewerWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        viewerWalletDTO = viewerWalletService.update(viewerWalletDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewerWalletDTO.getId().toString()))
            .body(viewerWalletDTO);
    }

    /**
     * {@code PATCH  /viewer-wallets/:id} : Partial updates given fields of an existing viewerWallet, field will ignore if it is null
     *
     * @param id the id of the viewerWalletDTO to save.
     * @param viewerWalletDTO the viewerWalletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewerWalletDTO,
     * or with status {@code 400 (Bad Request)} if the viewerWalletDTO is not valid,
     * or with status {@code 404 (Not Found)} if the viewerWalletDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the viewerWalletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ViewerWalletDTO> partialUpdateViewerWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ViewerWalletDTO viewerWalletDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ViewerWallet partially : {}, {}", id, viewerWalletDTO);
        if (viewerWalletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewerWalletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viewerWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ViewerWalletDTO> result = viewerWalletService.partialUpdate(viewerWalletDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewerWalletDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /viewer-wallets} : get all the viewerWallets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viewerWallets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ViewerWalletDTO>> getAllViewerWallets(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ViewerWallets");
        Page<ViewerWalletDTO> page = viewerWalletService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /viewer-wallets/:id} : get the "id" viewerWallet.
     *
     * @param id the id of the viewerWalletDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the viewerWalletDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ViewerWalletDTO> getViewerWallet(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ViewerWallet : {}", id);
        Optional<ViewerWalletDTO> viewerWalletDTO = viewerWalletService.findOne(id);
        return ResponseUtil.wrapOrNotFound(viewerWalletDTO);
    }

    /**
     * {@code DELETE  /viewer-wallets/:id} : delete the "id" viewerWallet.
     *
     * @param id the id of the viewerWalletDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViewerWallet(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ViewerWallet : {}", id);
        viewerWalletService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
