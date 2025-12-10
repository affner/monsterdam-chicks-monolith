package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.AuctionRepository;
import com.monsterdam.app.service.AuctionService;
import com.monsterdam.app.service.dto.AuctionDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.Auction}.
 */
@RestController
@RequestMapping("/api/auctions")
public class AuctionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AuctionResource.class);

    private static final String ENTITY_NAME = "auction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuctionService auctionService;

    private final AuctionRepository auctionRepository;

    public AuctionResource(AuctionService auctionService, AuctionRepository auctionRepository) {
        this.auctionService = auctionService;
        this.auctionRepository = auctionRepository;
    }

    /**
     * {@code POST  /auctions} : Create a new auction.
     *
     * @param auctionDTO the auctionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auctionDTO, or with status {@code 400 (Bad Request)} if the auction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AuctionDTO> createAuction(@Valid @RequestBody AuctionDTO auctionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Auction : {}", auctionDTO);
        if (auctionDTO.getId() != null) {
            throw new BadRequestAlertException("A new auction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        auctionDTO = auctionService.save(auctionDTO);
        return ResponseEntity.created(new URI("/api/auctions/" + auctionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, auctionDTO.getId().toString()))
            .body(auctionDTO);
    }

    /**
     * {@code PUT  /auctions/:id} : Updates an existing auction.
     *
     * @param id the id of the auctionDTO to save.
     * @param auctionDTO the auctionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auctionDTO,
     * or with status {@code 400 (Bad Request)} if the auctionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auctionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuctionDTO> updateAuction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AuctionDTO auctionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Auction : {}, {}", id, auctionDTO);
        if (auctionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auctionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        auctionDTO = auctionService.update(auctionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auctionDTO.getId().toString()))
            .body(auctionDTO);
    }

    /**
     * {@code PATCH  /auctions/:id} : Partial updates given fields of an existing auction, field will ignore if it is null
     *
     * @param id the id of the auctionDTO to save.
     * @param auctionDTO the auctionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auctionDTO,
     * or with status {@code 400 (Bad Request)} if the auctionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the auctionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the auctionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AuctionDTO> partialUpdateAuction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AuctionDTO auctionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Auction partially : {}, {}", id, auctionDTO);
        if (auctionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auctionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuctionDTO> result = auctionService.partialUpdate(auctionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auctionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /auctions} : get all the auctions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auctions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AuctionDTO>> getAllAuctions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Auctions");
        Page<AuctionDTO> page = auctionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /auctions/:id} : get the "id" auction.
     *
     * @param id the id of the auctionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auctionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuctionDTO> getAuction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Auction : {}", id);
        Optional<AuctionDTO> auctionDTO = auctionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auctionDTO);
    }

    /**
     * {@code DELETE  /auctions/:id} : delete the "id" auction.
     *
     * @param id the id of the auctionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Auction : {}", id);
        auctionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
