package com.monsterdam.app.repository;

import com.monsterdam.app.domain.ModerationAction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ModerationAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModerationActionRepository extends JpaRepository<ModerationAction, Long> {}
