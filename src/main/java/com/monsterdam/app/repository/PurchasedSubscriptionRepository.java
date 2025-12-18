package com.monsterdam.app.repository;

import com.monsterdam.app.domain.PurchasedSubscription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchasedSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchasedSubscriptionRepository extends LogicalDeletionRepository<PurchasedSubscription> {}
