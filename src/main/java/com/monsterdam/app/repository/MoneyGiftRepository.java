package com.monsterdam.app.repository;

import com.monsterdam.app.domain.MoneyGift;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MoneyGift entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoneyGiftRepository extends JpaRepository<MoneyGift, Long> {}
