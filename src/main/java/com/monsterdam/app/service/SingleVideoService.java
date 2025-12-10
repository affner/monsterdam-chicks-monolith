package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.SingleVideoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.SingleVideo}.
 */
public interface SingleVideoService {
    /**
     * Save a singleVideo.
     *
     * @param singleVideoDTO the entity to save.
     * @return the persisted entity.
     */
    SingleVideoDTO save(SingleVideoDTO singleVideoDTO);

    /**
     * Updates a singleVideo.
     *
     * @param singleVideoDTO the entity to update.
     * @return the persisted entity.
     */
    SingleVideoDTO update(SingleVideoDTO singleVideoDTO);

    /**
     * Partially updates a singleVideo.
     *
     * @param singleVideoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SingleVideoDTO> partialUpdate(SingleVideoDTO singleVideoDTO);

    /**
     * Get all the singleVideos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SingleVideoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" singleVideo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SingleVideoDTO> findOne(Long id);

    /**
     * Delete the "id" singleVideo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
