package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.ViewerWalletDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.ViewerWallet}.
 */
public interface ViewerWalletService {
    /**
     * Save a viewerWallet.
     *
     * @param viewerWalletDTO the entity to save.
     * @return the persisted entity.
     */
    ViewerWalletDTO save(ViewerWalletDTO viewerWalletDTO);

    /**
     * Updates a viewerWallet.
     *
     * @param viewerWalletDTO the entity to update.
     * @return the persisted entity.
     */
    ViewerWalletDTO update(ViewerWalletDTO viewerWalletDTO);

    /**
     * Partially updates a viewerWallet.
     *
     * @param viewerWalletDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ViewerWalletDTO> partialUpdate(ViewerWalletDTO viewerWalletDTO);

    /**
     * Get all the viewerWallets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ViewerWalletDTO> findAll(Pageable pageable);

    /**
     * Get the "id" viewerWallet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ViewerWalletDTO> findOne(Long id);

    /**
     * Delete the "id" viewerWallet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
