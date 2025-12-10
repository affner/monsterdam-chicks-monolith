package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.DirectMessageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.DirectMessage}.
 */
public interface DirectMessageService {
    /**
     * Save a directMessage.
     *
     * @param directMessageDTO the entity to save.
     * @return the persisted entity.
     */
    DirectMessageDTO save(DirectMessageDTO directMessageDTO);

    /**
     * Updates a directMessage.
     *
     * @param directMessageDTO the entity to update.
     * @return the persisted entity.
     */
    DirectMessageDTO update(DirectMessageDTO directMessageDTO);

    /**
     * Partially updates a directMessage.
     *
     * @param directMessageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DirectMessageDTO> partialUpdate(DirectMessageDTO directMessageDTO);

    /**
     * Get all the directMessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DirectMessageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" directMessage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DirectMessageDTO> findOne(Long id);

    /**
     * Delete the "id" directMessage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
