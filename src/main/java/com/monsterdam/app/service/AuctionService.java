package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.AuctionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.Auction}.
 */
public interface AuctionService {
    /**
     * Save a auction.
     *
     * @param auctionDTO the entity to save.
     * @return the persisted entity.
     */
    AuctionDTO save(AuctionDTO auctionDTO);

    /**
     * Updates a auction.
     *
     * @param auctionDTO the entity to update.
     * @return the persisted entity.
     */
    AuctionDTO update(AuctionDTO auctionDTO);

    /**
     * Partially updates a auction.
     *
     * @param auctionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AuctionDTO> partialUpdate(AuctionDTO auctionDTO);

    /**
     * Get all the auctions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AuctionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" auction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AuctionDTO> findOne(Long id);

    /**
     * Delete the "id" auction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
