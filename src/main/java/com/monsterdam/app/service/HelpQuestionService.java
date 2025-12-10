package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.HelpQuestionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.HelpQuestion}.
 */
public interface HelpQuestionService {
    /**
     * Save a helpQuestion.
     *
     * @param helpQuestionDTO the entity to save.
     * @return the persisted entity.
     */
    HelpQuestionDTO save(HelpQuestionDTO helpQuestionDTO);

    /**
     * Updates a helpQuestion.
     *
     * @param helpQuestionDTO the entity to update.
     * @return the persisted entity.
     */
    HelpQuestionDTO update(HelpQuestionDTO helpQuestionDTO);

    /**
     * Partially updates a helpQuestion.
     *
     * @param helpQuestionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HelpQuestionDTO> partialUpdate(HelpQuestionDTO helpQuestionDTO);

    /**
     * Get all the helpQuestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HelpQuestionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" helpQuestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HelpQuestionDTO> findOne(Long id);

    /**
     * Delete the "id" helpQuestion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
