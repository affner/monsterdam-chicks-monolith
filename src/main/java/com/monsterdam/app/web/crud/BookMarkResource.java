package com.monsterdam.app.web.crud;

import com.monsterdam.app.repository.BookMarkRepository;
import com.monsterdam.app.service.BookMarkService;
import com.monsterdam.app.service.dto.BookMarkDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.BookMark}.
 */
@RestController
@RequestMapping("/api/crud/book-marks")
public class BookMarkResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookMarkResource.class);

    private static final String ENTITY_NAME = "bookMark";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookMarkService bookMarkService;

    private final BookMarkRepository bookMarkRepository;

    public BookMarkResource(BookMarkService bookMarkService, BookMarkRepository bookMarkRepository) {
        this.bookMarkService = bookMarkService;
        this.bookMarkRepository = bookMarkRepository;
    }

    /**
     * {@code POST  /book-marks} : Create a new bookMark.
     *
     * @param bookMarkDTO the bookMarkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookMarkDTO, or with status {@code 400 (Bad Request)} if the bookMark has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookMarkDTO> createBookMark(@Valid @RequestBody BookMarkDTO bookMarkDTO) throws URISyntaxException {
        LOG.debug("REST request to save BookMark : {}", bookMarkDTO);
        if (bookMarkDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookMark cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookMarkDTO = bookMarkService.save(bookMarkDTO);
        return ResponseEntity.created(new URI("/api/crud/book-marks/" + bookMarkDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bookMarkDTO.getId().toString()))
            .body(bookMarkDTO);
    }

    /**
     * {@code PUT  /book-marks/:id} : Updates an existing bookMark.
     *
     * @param id the id of the bookMarkDTO to save.
     * @param bookMarkDTO the bookMarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookMarkDTO,
     * or with status {@code 400 (Bad Request)} if the bookMarkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookMarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookMarkDTO> updateBookMark(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookMarkDTO bookMarkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookMark : {}, {}", id, bookMarkDTO);
        if (bookMarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookMarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookMarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookMarkDTO = bookMarkService.update(bookMarkDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookMarkDTO.getId().toString()))
            .body(bookMarkDTO);
    }

    /**
     * {@code PATCH  /book-marks/:id} : Partial updates given fields of an existing bookMark, field will ignore if it is null
     *
     * @param id the id of the bookMarkDTO to save.
     * @param bookMarkDTO the bookMarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookMarkDTO,
     * or with status {@code 400 (Bad Request)} if the bookMarkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookMarkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookMarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookMarkDTO> partialUpdateBookMark(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookMarkDTO bookMarkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookMark partially : {}, {}", id, bookMarkDTO);
        if (bookMarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookMarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookMarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookMarkDTO> result = bookMarkService.partialUpdate(bookMarkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookMarkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /book-marks} : get all the bookMarks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookMarks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookMarkDTO>> getAllBookMarks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of BookMarks");
        Page<BookMarkDTO> page = bookMarkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-marks/:id} : get the "id" bookMark.
     *
     * @param id the id of the bookMarkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookMarkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookMarkDTO> getBookMark(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookMark : {}", id);
        Optional<BookMarkDTO> bookMarkDTO = bookMarkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookMarkDTO);
    }

    /**
     * {@code DELETE  /book-marks/:id} : delete the "id" bookMark.
     *
     * @param id the id of the bookMarkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookMark(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookMark : {}", id);
        bookMarkService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
