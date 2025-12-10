package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.AdminAnnouncementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.AdminAnnouncement}.
 */
public interface AdminAnnouncementService {
    /**
     * Save a adminAnnouncement.
     *
     * @param adminAnnouncementDTO the entity to save.
     * @return the persisted entity.
     */
    AdminAnnouncementDTO save(AdminAnnouncementDTO adminAnnouncementDTO);

    /**
     * Updates a adminAnnouncement.
     *
     * @param adminAnnouncementDTO the entity to update.
     * @return the persisted entity.
     */
    AdminAnnouncementDTO update(AdminAnnouncementDTO adminAnnouncementDTO);

    /**
     * Partially updates a adminAnnouncement.
     *
     * @param adminAnnouncementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdminAnnouncementDTO> partialUpdate(AdminAnnouncementDTO adminAnnouncementDTO);

    /**
     * Get all the adminAnnouncements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AdminAnnouncementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" adminAnnouncement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdminAnnouncementDTO> findOne(Long id);

    /**
     * Delete the "id" adminAnnouncement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
