package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.IdentityDocumentReviewDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.IdentityDocumentReview}.
 */
public interface IdentityDocumentReviewService {
    /**
     * Save a identityDocumentReview.
     *
     * @param identityDocumentReviewDTO the entity to save.
     * @return the persisted entity.
     */
    IdentityDocumentReviewDTO save(IdentityDocumentReviewDTO identityDocumentReviewDTO);

    /**
     * Updates a identityDocumentReview.
     *
     * @param identityDocumentReviewDTO the entity to update.
     * @return the persisted entity.
     */
    IdentityDocumentReviewDTO update(IdentityDocumentReviewDTO identityDocumentReviewDTO);

    /**
     * Partially updates a identityDocumentReview.
     *
     * @param identityDocumentReviewDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IdentityDocumentReviewDTO> partialUpdate(IdentityDocumentReviewDTO identityDocumentReviewDTO);

    /**
     * Get all the identityDocumentReviews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IdentityDocumentReviewDTO> findAll(Pageable pageable);

    /**
     * Get the "id" identityDocumentReview.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IdentityDocumentReviewDTO> findOne(Long id);

    /**
     * Delete the "id" identityDocumentReview.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
