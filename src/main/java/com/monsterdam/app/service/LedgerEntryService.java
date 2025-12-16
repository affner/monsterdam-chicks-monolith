package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.LedgerEntryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.LedgerEntry}.
 */
public interface LedgerEntryService {
    /**
     * Save a ledgerEntry.
     *
     * @param ledgerEntryDTO the entity to save.
     * @return the persisted entity.
     */
    LedgerEntryDTO save(LedgerEntryDTO ledgerEntryDTO);

    /**
     * Updates a ledgerEntry.
     *
     * @param ledgerEntryDTO the entity to update.
     * @return the persisted entity.
     */
    LedgerEntryDTO update(LedgerEntryDTO ledgerEntryDTO);

    /**
     * Partially updates a ledgerEntry.
     *
     * @param ledgerEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LedgerEntryDTO> partialUpdate(LedgerEntryDTO ledgerEntryDTO);

    /**
     * Get all the ledgerEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LedgerEntryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ledgerEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LedgerEntryDTO> findOne(Long id);

    /**
     * Delete the "id" ledgerEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
