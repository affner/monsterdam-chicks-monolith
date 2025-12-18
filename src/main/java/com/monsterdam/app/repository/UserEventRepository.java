package com.monsterdam.app.repository;

import com.monsterdam.app.domain.UserEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserEventRepository extends LogicalDeletionRepository<UserEvent> {}
