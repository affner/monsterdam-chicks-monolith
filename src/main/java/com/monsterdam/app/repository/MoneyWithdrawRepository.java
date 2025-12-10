package com.monsterdam.app.repository;

import com.monsterdam.app.domain.MoneyWithdraw;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MoneyWithdraw entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoneyWithdrawRepository extends JpaRepository<MoneyWithdraw, Long> {}
