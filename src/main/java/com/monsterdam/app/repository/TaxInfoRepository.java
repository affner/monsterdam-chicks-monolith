package com.monsterdam.app.repository;

import com.monsterdam.app.domain.TaxInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TaxInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxInfoRepository extends JpaRepository<TaxInfo, Long> {}
