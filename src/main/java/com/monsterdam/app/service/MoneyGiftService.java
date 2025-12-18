package com.monsterdam.app.service;

import com.monsterdam.app.service.LogicalDeletionService;
import com.monsterdam.app.service.dto.MoneyGiftDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.MoneyGift}.
 */
public interface MoneyGiftService extends LogicalDeletionService<MoneyGiftDTO> {
    /**
     * Save a moneyGift.
     *
     * @param moneyGiftDTO the entity to save.
     * @return the persisted entity.
     */
    MoneyGiftDTO save(MoneyGiftDTO moneyGiftDTO);

    /**
     * Updates a moneyGift.
     *
     * @param moneyGiftDTO the entity to update.
     * @return the persisted entity.
     */
    MoneyGiftDTO update(MoneyGiftDTO moneyGiftDTO);

    /**
     * Partially updates a moneyGift.
     *
     * @param moneyGiftDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MoneyGiftDTO> partialUpdate(MoneyGiftDTO moneyGiftDTO);

    /**
     * Get all the moneyGifts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoneyGiftDTO> findAll(Pageable pageable);

    /**
     * Get the "id" moneyGift.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoneyGiftDTO> findOne(Long id);

    /**
     * Delete the "id" moneyGift.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
