package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.ChatRoomRepository;
import com.monsterdam.app.service.ChatRoomService;
import com.monsterdam.app.service.dto.ChatRoomDTO;
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
 * REST controller for managing {@link com.monsterdam.app.domain.ChatRoom}.
 */
@RestController
@RequestMapping("/api")
public class ChatRoomResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChatRoomResource.class);

    private static final String ENTITY_NAME = "chatRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatRoomService chatRoomService;

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomResource(ChatRoomService chatRoomService, ChatRoomRepository chatRoomRepository) {
        this.chatRoomService = chatRoomService;
        this.chatRoomRepository = chatRoomRepository;
    }

    /**
     * {@code POST  /chat-rooms} : Create a new chatRoom.
     *
     * @param chatRoomDTO the chatRoomDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatRoomDTO, or with status {@code 400 (Bad Request)} if the chatRoom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chat-rooms")
    public ResponseEntity<ChatRoomDTO> createChatRoom(@Valid @RequestBody ChatRoomDTO chatRoomDTO) throws URISyntaxException {
        LOG.debug("REST request to save ChatRoom : {}", chatRoomDTO);
        if (chatRoomDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        chatRoomDTO = chatRoomService.save(chatRoomDTO);
        return ResponseEntity.created(new URI("/api/chat-rooms/" + chatRoomDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, chatRoomDTO.getId().toString()))
            .body(chatRoomDTO);
    }

    /**
     * {@code PUT  /chat-rooms/:id} : Updates an existing chatRoom.
     *
     * @param id the id of the chatRoomDTO to save.
     * @param chatRoomDTO the chatRoomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatRoomDTO,
     * or with status {@code 400 (Bad Request)} if the chatRoomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatRoomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chat-rooms/{id}")
    public ResponseEntity<ChatRoomDTO> updateChatRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChatRoomDTO chatRoomDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ChatRoom : {}, {}", id, chatRoomDTO);
        if (chatRoomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatRoomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        chatRoomDTO = chatRoomService.update(chatRoomDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatRoomDTO.getId().toString()))
            .body(chatRoomDTO);
    }

    /**
     * {@code PATCH  /chat-rooms/:id} : Partial updates given fields of an existing chatRoom, field will ignore if it is null
     *
     * @param id the id of the chatRoomDTO to save.
     * @param chatRoomDTO the chatRoomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatRoomDTO,
     * or with status {@code 400 (Bad Request)} if the chatRoomDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chatRoomDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chatRoomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chat-rooms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChatRoomDTO> partialUpdateChatRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChatRoomDTO chatRoomDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ChatRoom partially : {}, {}", id, chatRoomDTO);
        if (chatRoomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatRoomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChatRoomDTO> result = chatRoomService.partialUpdate(chatRoomDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatRoomDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /chat-rooms} : get all the chatRooms.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatRooms in body.
     */
    @GetMapping("/chat-rooms")
    public ResponseEntity<List<ChatRoomDTO>> getAllChatRooms(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ChatRooms");
        Page<ChatRoomDTO> page;
        if (eagerload) {
            page = chatRoomService.findAllWithEagerRelationships(pageable);
        } else {
            page = chatRoomService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chat-rooms/:id} : get the "id" chatRoom.
     *
     * @param id the id of the chatRoomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatRoomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chat-rooms/{id}")
    public ResponseEntity<ChatRoomDTO> getChatRoom(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ChatRoom : {}", id);
        Optional<ChatRoomDTO> chatRoomDTO = chatRoomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatRoomDTO);
    }

    /**
     * {@code DELETE  /chat-rooms/:id} : delete the "id" chatRoom.
     *
     * @param id the id of the chatRoomDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chat-rooms/{id}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ChatRoom : {}", id);
        chatRoomService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /logical/chat-rooms} : get all the chat-rooms without logical deletions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chat-rooms in body.
     */
    @GetMapping("/logical/chat-rooms")
    public ResponseEntity<List<ChatRoomDTO>> getAllLogicalChatRooms(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ChatRooms without logical deletions");
        Page<ChatRoomDTO> page = chatRoomService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logical/chat-rooms/:id} : get the "id" ChatRoom if not logically deleted.
     *
     * @param id the id of the ChatRoomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ChatRoomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logical/chat-rooms/{id}")
    public ResponseEntity<ChatRoomDTO> getLogicalChatRoom(@PathVariable("id") Long id) {
        LOG.debug("REST request to get logical ChatRoom : {}", id);
        Optional<ChatRoomDTO> chatRoomDTO = chatRoomService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(chatRoomDTO);
    }

    /**
     * {@code DELETE  /logical/chat-rooms/:id} : logically delete the "id" ChatRoom.
     *
     * @param id the id of the ChatRoomDTO to logically delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logical/chat-rooms/{id}")
    public ResponseEntity<Void> logicalDeleteChatRoom(@PathVariable("id") Long id) {
        LOG.debug("REST request to logical delete ChatRoom : {}", id);
        if (!chatRoomRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        chatRoomService.logicalDelete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUT  /logical/chat-rooms/:id/restore} : restore a logically deleted ChatRoom.
     *
     * @param id the id of the ChatRoom to restore.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restored ChatRoomDTO.
     */
    @PutMapping("/logical/chat-rooms/{id}/restore")
    public ResponseEntity<ChatRoomDTO> restoreChatRoom(@PathVariable("id") Long id) {
        LOG.debug("REST request to restore ChatRoom : {}", id);
        if (!chatRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        chatRoomService.restore(id);
        Optional<ChatRoomDTO> restored = chatRoomService.logicalGet(id);
        return ResponseUtil.wrapOrNotFound(restored, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()));
    }
}
