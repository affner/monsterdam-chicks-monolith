package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.PostFeedDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.PostFeed}.
 */
public interface PostFeedService {
    /**
     * Save a postFeed.
     *
     * @param postFeedDTO the entity to save.
     * @return the persisted entity.
     */
    PostFeedDTO save(PostFeedDTO postFeedDTO);

    /**
     * Updates a postFeed.
     *
     * @param postFeedDTO the entity to update.
     * @return the persisted entity.
     */
    PostFeedDTO update(PostFeedDTO postFeedDTO);

    /**
     * Partially updates a postFeed.
     *
     * @param postFeedDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PostFeedDTO> partialUpdate(PostFeedDTO postFeedDTO);

    /**
     * Get all the postFeeds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostFeedDTO> findAll(Pageable pageable);

    /**
     * Get all public post feeds ready for public browse surfaces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostFeedDTO> findPublicFeed(Pageable pageable);

    /**
     * Search across public post feeds.
     *
     * @param query the free text query.
     * @param pageable the pagination information.
     * @return matching public posts.
     */
    Page<PostFeedDTO> searchPublicFeed(String query, Pageable pageable);

    /**
     * Get the "id" postFeed.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostFeedDTO> findOne(Long id);

    /**
     * Delete the "id" postFeed.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
