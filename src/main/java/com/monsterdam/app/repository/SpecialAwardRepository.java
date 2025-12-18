package com.monsterdam.app.repository;

import com.monsterdam.app.domain.SpecialAward;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SpecialAward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialAwardRepository extends LogicalDeletionRepository<SpecialAward> {}
