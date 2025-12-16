package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.PaymentProviderEventDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.PaymentProviderEvent}.
 */
public interface PaymentProviderEventService {
    /**
     * Save a paymentProviderEvent.
     *
     * @param paymentProviderEventDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentProviderEventDTO save(PaymentProviderEventDTO paymentProviderEventDTO);

    /**
     * Updates a paymentProviderEvent.
     *
     * @param paymentProviderEventDTO the entity to update.
     * @return the persisted entity.
     */
    PaymentProviderEventDTO update(PaymentProviderEventDTO paymentProviderEventDTO);

    /**
     * Partially updates a paymentProviderEvent.
     *
     * @param paymentProviderEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentProviderEventDTO> partialUpdate(PaymentProviderEventDTO paymentProviderEventDTO);

    /**
     * Get all the paymentProviderEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentProviderEventDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paymentProviderEvent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentProviderEventDTO> findOne(Long id);

    /**
     * Delete the "id" paymentProviderEvent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
