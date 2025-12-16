package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.IdentityDocumentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.IdentityDocument}.
 */
public interface IdentityDocumentService {
    /**
     * Save a identityDocument.
     *
     * @param identityDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    IdentityDocumentDTO save(IdentityDocumentDTO identityDocumentDTO);

    /**
     * Updates a identityDocument.
     *
     * @param identityDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    IdentityDocumentDTO update(IdentityDocumentDTO identityDocumentDTO);

    /**
     * Partially updates a identityDocument.
     *
     * @param identityDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IdentityDocumentDTO> partialUpdate(IdentityDocumentDTO identityDocumentDTO);

    /**
     * Get all the identityDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IdentityDocumentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" identityDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IdentityDocumentDTO> findOne(Long id);

    /**
     * Delete the "id" identityDocument.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
