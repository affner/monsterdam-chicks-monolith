package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.AssistanceTicketDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.AssistanceTicket}.
 */
public interface AssistanceTicketService {
    /**
     * Save a assistanceTicket.
     *
     * @param assistanceTicketDTO the entity to save.
     * @return the persisted entity.
     */
    AssistanceTicketDTO save(AssistanceTicketDTO assistanceTicketDTO);

    /**
     * Updates a assistanceTicket.
     *
     * @param assistanceTicketDTO the entity to update.
     * @return the persisted entity.
     */
    AssistanceTicketDTO update(AssistanceTicketDTO assistanceTicketDTO);

    /**
     * Partially updates a assistanceTicket.
     *
     * @param assistanceTicketDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssistanceTicketDTO> partialUpdate(AssistanceTicketDTO assistanceTicketDTO);

    /**
     * Get all the assistanceTickets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssistanceTicketDTO> findAll(Pageable pageable);

    /**
     * Get the "id" assistanceTicket.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssistanceTicketDTO> findOne(Long id);

    /**
     * Delete the "id" assistanceTicket.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
