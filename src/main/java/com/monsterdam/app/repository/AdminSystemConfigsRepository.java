package com.monsterdam.app.repository;

import com.monsterdam.app.domain.AdminSystemConfigs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AdminSystemConfigs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminSystemConfigsRepository extends JpaRepository<AdminSystemConfigs, Long> {}
