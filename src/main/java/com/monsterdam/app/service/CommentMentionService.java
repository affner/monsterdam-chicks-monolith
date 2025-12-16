package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.CommentMentionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.CommentMention}.
 */
public interface CommentMentionService {
    /**
     * Save a commentMention.
     *
     * @param commentMentionDTO the entity to save.
     * @return the persisted entity.
     */
    CommentMentionDTO save(CommentMentionDTO commentMentionDTO);

    /**
     * Updates a commentMention.
     *
     * @param commentMentionDTO the entity to update.
     * @return the persisted entity.
     */
    CommentMentionDTO update(CommentMentionDTO commentMentionDTO);

    /**
     * Partially updates a commentMention.
     *
     * @param commentMentionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommentMentionDTO> partialUpdate(CommentMentionDTO commentMentionDTO);

    /**
     * Get all the commentMentions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentMentionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" commentMention.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentMentionDTO> findOne(Long id);

    /**
     * Delete the "id" commentMention.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
