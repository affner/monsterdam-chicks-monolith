package com.monsterdam.app.service;

import com.monsterdam.app.service.LogicalDeletionService;
import com.monsterdam.app.service.dto.TrialLinkDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.TrialLink}.
 */
public interface TrialLinkService extends LogicalDeletionService<TrialLinkDTO> {
    /**
     * Save a trialLink.
     *
     * @param trialLinkDTO the entity to save.
     * @return the persisted entity.
     */
    TrialLinkDTO save(TrialLinkDTO trialLinkDTO);

    /**
     * Updates a trialLink.
     *
     * @param trialLinkDTO the entity to update.
     * @return the persisted entity.
     */
    TrialLinkDTO update(TrialLinkDTO trialLinkDTO);

    /**
     * Partially updates a trialLink.
     *
     * @param trialLinkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrialLinkDTO> partialUpdate(TrialLinkDTO trialLinkDTO);

    /**
     * Get all the trialLinks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TrialLinkDTO> findAll(Pageable pageable);

    /**
     * Get the "id" trialLink.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrialLinkDTO> findOne(Long id);

    /**
     * Delete the "id" trialLink.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
