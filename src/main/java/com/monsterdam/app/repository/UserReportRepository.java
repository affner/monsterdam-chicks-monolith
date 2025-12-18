package com.monsterdam.app.repository;

import com.monsterdam.app.domain.UserReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserReportRepository extends LogicalDeletionRepository<UserReport> {}
