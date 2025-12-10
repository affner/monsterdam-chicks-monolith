package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.DocumentReviewObservationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.DocumentReviewObservation}.
 */
public interface DocumentReviewObservationService {
    /**
     * Save a documentReviewObservation.
     *
     * @param documentReviewObservationDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentReviewObservationDTO save(DocumentReviewObservationDTO documentReviewObservationDTO);

    /**
     * Updates a documentReviewObservation.
     *
     * @param documentReviewObservationDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentReviewObservationDTO update(DocumentReviewObservationDTO documentReviewObservationDTO);

    /**
     * Partially updates a documentReviewObservation.
     *
     * @param documentReviewObservationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentReviewObservationDTO> partialUpdate(DocumentReviewObservationDTO documentReviewObservationDTO);

    /**
     * Get all the documentReviewObservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentReviewObservationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentReviewObservation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentReviewObservationDTO> findOne(Long id);

    /**
     * Delete the "id" documentReviewObservation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
