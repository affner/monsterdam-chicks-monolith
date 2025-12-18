package com.monsterdam.app.repository;

import com.monsterdam.app.domain.Auction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Auction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuctionRepository extends LogicalDeletionRepository<Auction> {}
