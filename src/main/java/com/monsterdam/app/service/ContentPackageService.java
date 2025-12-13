package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.ContentPackageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.ContentPackage}.
 */
public interface ContentPackageService {
    /**
     * Save a contentPackage.
     *
     * @param contentPackageDTO the entity to save.
     * @return the persisted entity.
     */
    ContentPackageDTO save(ContentPackageDTO contentPackageDTO);

    /**
     * Updates a contentPackage.
     *
     * @param contentPackageDTO the entity to update.
     * @return the persisted entity.
     */
    ContentPackageDTO update(ContentPackageDTO contentPackageDTO);

    /**
     * Partially updates a contentPackage.
     *
     * @param contentPackageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContentPackageDTO> partialUpdate(ContentPackageDTO contentPackageDTO);

    /**
     * Get all the contentPackages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContentPackageDTO> findAll(Pageable pageable);

    /**
     * Get all active (non-deleted) content packages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContentPackageDTO> findActive(Pageable pageable);

    /**
     * Get active content packages filtered by whether they are paid content.
     *
     * @param isPaidContent whether the package is paid.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContentPackageDTO> findActiveByPaymentType(Boolean isPaidContent, Pageable pageable);

    /**
     * Get the "id" contentPackage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContentPackageDTO> findOne(Long id);

    /**
     * Delete the "id" contentPackage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
