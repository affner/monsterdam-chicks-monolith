package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.MoneyWithdrawDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.MoneyWithdraw}.
 */
public interface MoneyWithdrawService {
    /**
     * Save a moneyWithdraw.
     *
     * @param moneyWithdrawDTO the entity to save.
     * @return the persisted entity.
     */
    MoneyWithdrawDTO save(MoneyWithdrawDTO moneyWithdrawDTO);

    /**
     * Updates a moneyWithdraw.
     *
     * @param moneyWithdrawDTO the entity to update.
     * @return the persisted entity.
     */
    MoneyWithdrawDTO update(MoneyWithdrawDTO moneyWithdrawDTO);

    /**
     * Partially updates a moneyWithdraw.
     *
     * @param moneyWithdrawDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MoneyWithdrawDTO> partialUpdate(MoneyWithdrawDTO moneyWithdrawDTO);

    /**
     * Get all the moneyWithdraws.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoneyWithdrawDTO> findAll(Pageable pageable);

    /**
     * Get the "id" moneyWithdraw.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoneyWithdrawDTO> findOne(Long id);

    /**
     * Delete the "id" moneyWithdraw.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
