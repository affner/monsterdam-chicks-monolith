package com.monsterdam.app.repository;

import com.monsterdam.app.domain.PlatformAdminUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlatformAdminUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlatformAdminUserRepository extends JpaRepository<PlatformAdminUser, Long> {}
