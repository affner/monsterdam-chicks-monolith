package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.SingleAudioDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.SingleAudio}.
 */
public interface SingleAudioService {
    /**
     * Save a singleAudio.
     *
     * @param singleAudioDTO the entity to save.
     * @return the persisted entity.
     */
    SingleAudioDTO save(SingleAudioDTO singleAudioDTO);

    /**
     * Updates a singleAudio.
     *
     * @param singleAudioDTO the entity to update.
     * @return the persisted entity.
     */
    SingleAudioDTO update(SingleAudioDTO singleAudioDTO);

    /**
     * Partially updates a singleAudio.
     *
     * @param singleAudioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SingleAudioDTO> partialUpdate(SingleAudioDTO singleAudioDTO);

    /**
     * Get all the singleAudios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SingleAudioDTO> findAll(Pageable pageable);

    /**
     * Get the "id" singleAudio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SingleAudioDTO> findOne(Long id);

    /**
     * Delete the "id" singleAudio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
