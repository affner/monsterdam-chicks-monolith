package com.monsterdam.app.repository;

import com.monsterdam.app.domain.SubscriptionPlanOffer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubscriptionPlanOffer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionPlanOfferRepository extends JpaRepository<SubscriptionPlanOffer, Long> {}
