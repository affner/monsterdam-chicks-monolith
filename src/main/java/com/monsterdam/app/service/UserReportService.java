package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.UserReportDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.UserReport}.
 */
public interface UserReportService {
    /**
     * Save a userReport.
     *
     * @param userReportDTO the entity to save.
     * @return the persisted entity.
     */
    UserReportDTO save(UserReportDTO userReportDTO);

    /**
     * Updates a userReport.
     *
     * @param userReportDTO the entity to update.
     * @return the persisted entity.
     */
    UserReportDTO update(UserReportDTO userReportDTO);

    /**
     * Partially updates a userReport.
     *
     * @param userReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserReportDTO> partialUpdate(UserReportDTO userReportDTO);

    /**
     * Get all the userReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserReportDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserReportDTO> findOne(Long id);

    /**
     * Delete the "id" userReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
