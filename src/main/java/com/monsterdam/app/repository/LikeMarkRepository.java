package com.monsterdam.app.repository;

import com.monsterdam.app.domain.LikeMark;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LikeMark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikeMarkRepository extends LogicalDeletionRepository<LikeMark> {}
