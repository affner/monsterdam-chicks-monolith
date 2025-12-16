package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.PlatformAdminUserDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.PlatformAdminUser}.
 */
public interface PlatformAdminUserService {
    /**
     * Save a platformAdminUser.
     *
     * @param platformAdminUserDTO the entity to save.
     * @return the persisted entity.
     */
    PlatformAdminUserDTO save(PlatformAdminUserDTO platformAdminUserDTO);

    /**
     * Updates a platformAdminUser.
     *
     * @param platformAdminUserDTO the entity to update.
     * @return the persisted entity.
     */
    PlatformAdminUserDTO update(PlatformAdminUserDTO platformAdminUserDTO);

    /**
     * Partially updates a platformAdminUser.
     *
     * @param platformAdminUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlatformAdminUserDTO> partialUpdate(PlatformAdminUserDTO platformAdminUserDTO);

    /**
     * Get all the platformAdminUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlatformAdminUserDTO> findAll(Pageable pageable);

    /**
     * Get the "id" platformAdminUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlatformAdminUserDTO> findOne(Long id);

    /**
     * Delete the "id" platformAdminUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
