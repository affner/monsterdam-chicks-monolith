package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.ModerationActionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.ModerationAction}.
 */
public interface ModerationActionService {
    /**
     * Save a moderationAction.
     *
     * @param moderationActionDTO the entity to save.
     * @return the persisted entity.
     */
    ModerationActionDTO save(ModerationActionDTO moderationActionDTO);

    /**
     * Updates a moderationAction.
     *
     * @param moderationActionDTO the entity to update.
     * @return the persisted entity.
     */
    ModerationActionDTO update(ModerationActionDTO moderationActionDTO);

    /**
     * Partially updates a moderationAction.
     *
     * @param moderationActionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ModerationActionDTO> partialUpdate(ModerationActionDTO moderationActionDTO);

    /**
     * Get all the moderationActions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ModerationActionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" moderationAction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ModerationActionDTO> findOne(Long id);

    /**
     * Delete the "id" moderationAction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
