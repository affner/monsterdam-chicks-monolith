package com.monsterdam.app.service;

import com.monsterdam.app.service.LogicalDeletionService;
import com.monsterdam.app.service.dto.PostMentionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.PostMention}.
 */
public interface PostMentionService extends LogicalDeletionService<PostMentionDTO> {
    /**
     * Save a postMention.
     *
     * @param postMentionDTO the entity to save.
     * @return the persisted entity.
     */
    PostMentionDTO save(PostMentionDTO postMentionDTO);

    /**
     * Updates a postMention.
     *
     * @param postMentionDTO the entity to update.
     * @return the persisted entity.
     */
    PostMentionDTO update(PostMentionDTO postMentionDTO);

    /**
     * Partially updates a postMention.
     *
     * @param postMentionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PostMentionDTO> partialUpdate(PostMentionDTO postMentionDTO);

    /**
     * Get all the postMentions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostMentionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" postMention.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostMentionDTO> findOne(Long id);

    /**
     * Delete the "id" postMention.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
