package com.monsterdam.app.repository;

import com.monsterdam.app.domain.TrialLink;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrialLink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrialLinkRepository extends LogicalDeletionRepository<TrialLink> {}
