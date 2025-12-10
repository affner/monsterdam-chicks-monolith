package com.monsterdam.app.repository;

import com.monsterdam.app.domain.MoneyEarning;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MoneyEarning entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoneyEarningRepository extends JpaRepository<MoneyEarning, Long> {}
