package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.RefundTransactionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.RefundTransaction}.
 */
public interface RefundTransactionService {
    /**
     * Save a refundTransaction.
     *
     * @param refundTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    RefundTransactionDTO save(RefundTransactionDTO refundTransactionDTO);

    /**
     * Updates a refundTransaction.
     *
     * @param refundTransactionDTO the entity to update.
     * @return the persisted entity.
     */
    RefundTransactionDTO update(RefundTransactionDTO refundTransactionDTO);

    /**
     * Partially updates a refundTransaction.
     *
     * @param refundTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RefundTransactionDTO> partialUpdate(RefundTransactionDTO refundTransactionDTO);

    /**
     * Get all the refundTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RefundTransactionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" refundTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RefundTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" refundTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
