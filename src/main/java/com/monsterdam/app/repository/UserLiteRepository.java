package com.monsterdam.app.repository;

import com.monsterdam.app.domain.UserLite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserLite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserLiteRepository extends LogicalDeletionRepository<UserLite> {}
