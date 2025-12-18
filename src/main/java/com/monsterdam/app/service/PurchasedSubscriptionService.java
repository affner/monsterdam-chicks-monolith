package com.monsterdam.app.service;

import com.monsterdam.app.service.LogicalDeletionService;
import com.monsterdam.app.service.dto.PurchasedSubscriptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.PurchasedSubscription}.
 */
public interface PurchasedSubscriptionService extends LogicalDeletionService<PurchasedSubscriptionDTO> {
    /**
     * Save a purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    PurchasedSubscriptionDTO save(PurchasedSubscriptionDTO purchasedSubscriptionDTO);

    /**
     * Updates a purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the entity to update.
     * @return the persisted entity.
     */
    PurchasedSubscriptionDTO update(PurchasedSubscriptionDTO purchasedSubscriptionDTO);

    /**
     * Partially updates a purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PurchasedSubscriptionDTO> partialUpdate(PurchasedSubscriptionDTO purchasedSubscriptionDTO);

    /**
     * Get all the purchasedSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchasedSubscriptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" purchasedSubscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PurchasedSubscriptionDTO> findOne(Long id);

    /**
     * Delete the "id" purchasedSubscription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
