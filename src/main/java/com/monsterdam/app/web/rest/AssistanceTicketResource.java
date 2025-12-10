package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.AssistanceTicketRepository;
import com.monsterdam.app.service.AssistanceTicketService;
import com.monsterdam.app.service.dto.AssistanceTicketDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.AssistanceTicket}.
 */
@RestController
@RequestMapping("/api/assistance-tickets")
public class AssistanceTicketResource {

    private static final Logger LOG = LoggerFactory.getLogger(AssistanceTicketResource.class);

    private static final String ENTITY_NAME = "assistanceTicket";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssistanceTicketService assistanceTicketService;

    private final AssistanceTicketRepository assistanceTicketRepository;

    public AssistanceTicketResource(
        AssistanceTicketService assistanceTicketService,
        AssistanceTicketRepository assistanceTicketRepository
    ) {
        this.assistanceTicketService = assistanceTicketService;
        this.assistanceTicketRepository = assistanceTicketRepository;
    }

    /**
     * {@code POST  /assistance-tickets} : Create a new assistanceTicket.
     *
     * @param assistanceTicketDTO the assistanceTicketDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assistanceTicketDTO, or with status {@code 400 (Bad Request)} if the assistanceTicket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AssistanceTicketDTO> createAssistanceTicket(@Valid @RequestBody AssistanceTicketDTO assistanceTicketDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AssistanceTicket : {}", assistanceTicketDTO);
        if (assistanceTicketDTO.getId() != null) {
            throw new BadRequestAlertException("A new assistanceTicket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        assistanceTicketDTO = assistanceTicketService.save(assistanceTicketDTO);
        return ResponseEntity.created(new URI("/api/assistance-tickets/" + assistanceTicketDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, assistanceTicketDTO.getId().toString()))
            .body(assistanceTicketDTO);
    }

    /**
     * {@code PUT  /assistance-tickets/:id} : Updates an existing assistanceTicket.
     *
     * @param id the id of the assistanceTicketDTO to save.
     * @param assistanceTicketDTO the assistanceTicketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assistanceTicketDTO,
     * or with status {@code 400 (Bad Request)} if the assistanceTicketDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assistanceTicketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssistanceTicketDTO> updateAssistanceTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssistanceTicketDTO assistanceTicketDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AssistanceTicket : {}, {}", id, assistanceTicketDTO);
        if (assistanceTicketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assistanceTicketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assistanceTicketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        assistanceTicketDTO = assistanceTicketService.update(assistanceTicketDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assistanceTicketDTO.getId().toString()))
            .body(assistanceTicketDTO);
    }

    /**
     * {@code PATCH  /assistance-tickets/:id} : Partial updates given fields of an existing assistanceTicket, field will ignore if it is null
     *
     * @param id the id of the assistanceTicketDTO to save.
     * @param assistanceTicketDTO the assistanceTicketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assistanceTicketDTO,
     * or with status {@code 400 (Bad Request)} if the assistanceTicketDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assistanceTicketDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assistanceTicketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssistanceTicketDTO> partialUpdateAssistanceTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssistanceTicketDTO assistanceTicketDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AssistanceTicket partially : {}, {}", id, assistanceTicketDTO);
        if (assistanceTicketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assistanceTicketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assistanceTicketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssistanceTicketDTO> result = assistanceTicketService.partialUpdate(assistanceTicketDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assistanceTicketDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /assistance-tickets} : get all the assistanceTickets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assistanceTickets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AssistanceTicketDTO>> getAllAssistanceTickets(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AssistanceTickets");
        Page<AssistanceTicketDTO> page = assistanceTicketService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assistance-tickets/:id} : get the "id" assistanceTicket.
     *
     * @param id the id of the assistanceTicketDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assistanceTicketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssistanceTicketDTO> getAssistanceTicket(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AssistanceTicket : {}", id);
        Optional<AssistanceTicketDTO> assistanceTicketDTO = assistanceTicketService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assistanceTicketDTO);
    }

    /**
     * {@code DELETE  /assistance-tickets/:id} : delete the "id" assistanceTicket.
     *
     * @param id the id of the assistanceTicketDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssistanceTicket(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AssistanceTicket : {}", id);
        assistanceTicketService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
