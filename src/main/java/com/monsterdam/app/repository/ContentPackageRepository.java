package com.monsterdam.app.repository;

import com.monsterdam.app.domain.ContentPackage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContentPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentPackageRepository extends JpaRepository<ContentPackage, Long> {}
