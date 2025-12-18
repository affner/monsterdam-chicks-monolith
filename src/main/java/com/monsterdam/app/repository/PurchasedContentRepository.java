package com.monsterdam.app.repository;

import com.monsterdam.app.domain.PurchasedContent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchasedContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchasedContentRepository extends LogicalDeletionRepository<PurchasedContent> {}
