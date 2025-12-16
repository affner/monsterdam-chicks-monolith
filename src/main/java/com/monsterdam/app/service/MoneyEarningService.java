package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.MoneyEarningDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.MoneyEarning}.
 */
public interface MoneyEarningService {
    /**
     * Save a moneyEarning.
     *
     * @param moneyEarningDTO the entity to save.
     * @return the persisted entity.
     */
    MoneyEarningDTO save(MoneyEarningDTO moneyEarningDTO);

    /**
     * Updates a moneyEarning.
     *
     * @param moneyEarningDTO the entity to update.
     * @return the persisted entity.
     */
    MoneyEarningDTO update(MoneyEarningDTO moneyEarningDTO);

    /**
     * Partially updates a moneyEarning.
     *
     * @param moneyEarningDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MoneyEarningDTO> partialUpdate(MoneyEarningDTO moneyEarningDTO);

    /**
     * Get all the moneyEarnings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoneyEarningDTO> findAll(Pageable pageable);

    /**
     * Get the "id" moneyEarning.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoneyEarningDTO> findOne(Long id);

    /**
     * Delete the "id" moneyEarning.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
