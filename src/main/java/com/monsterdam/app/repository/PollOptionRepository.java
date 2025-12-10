package com.monsterdam.app.repository;

import com.monsterdam.app.domain.PollOption;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PollOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollOptionRepository extends JpaRepository<PollOption, Long> {}
