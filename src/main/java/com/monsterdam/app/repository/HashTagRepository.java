package com.monsterdam.app.repository;

import com.monsterdam.app.domain.HashTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HashTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HashTagRepository extends LogicalDeletionRepository<HashTag> {}
