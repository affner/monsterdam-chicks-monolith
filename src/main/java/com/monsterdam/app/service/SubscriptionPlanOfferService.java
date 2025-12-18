package com.monsterdam.app.service;

import com.monsterdam.app.service.LogicalDeletionService;
import com.monsterdam.app.service.dto.SubscriptionPlanOfferDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.SubscriptionPlanOffer}.
 */
public interface SubscriptionPlanOfferService extends LogicalDeletionService<SubscriptionPlanOfferDTO> {
    /**
     * Save a subscriptionPlanOffer.
     *
     * @param subscriptionPlanOfferDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionPlanOfferDTO save(SubscriptionPlanOfferDTO subscriptionPlanOfferDTO);

    /**
     * Updates a subscriptionPlanOffer.
     *
     * @param subscriptionPlanOfferDTO the entity to update.
     * @return the persisted entity.
     */
    SubscriptionPlanOfferDTO update(SubscriptionPlanOfferDTO subscriptionPlanOfferDTO);

    /**
     * Partially updates a subscriptionPlanOffer.
     *
     * @param subscriptionPlanOfferDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubscriptionPlanOfferDTO> partialUpdate(SubscriptionPlanOfferDTO subscriptionPlanOfferDTO);

    /**
     * Get all the subscriptionPlanOffers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubscriptionPlanOfferDTO> findAll(Pageable pageable);

    /**
     * Get the "id" subscriptionPlanOffer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionPlanOfferDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionPlanOffer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
