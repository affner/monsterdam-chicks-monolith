package com.monsterdam.app.repository;

import com.monsterdam.app.domain.UserSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSettingsRepository extends LogicalDeletionRepository<UserSettings> {}
