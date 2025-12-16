package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.UserSettingsDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.UserSettings}.
 */
public interface UserSettingsService {
    /**
     * Save a userSettings.
     *
     * @param userSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    UserSettingsDTO save(UserSettingsDTO userSettingsDTO);

    /**
     * Updates a userSettings.
     *
     * @param userSettingsDTO the entity to update.
     * @return the persisted entity.
     */
    UserSettingsDTO update(UserSettingsDTO userSettingsDTO);

    /**
     * Partially updates a userSettings.
     *
     * @param userSettingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserSettingsDTO> partialUpdate(UserSettingsDTO userSettingsDTO);

    /**
     * Get all the userSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserSettingsDTO> findAll(Pageable pageable);

    /**
     * Get all the UserSettingsDTO where User is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<UserSettingsDTO> findAllWhereUserIsNull();

    /**
     * Get the "id" userSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" userSettings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
