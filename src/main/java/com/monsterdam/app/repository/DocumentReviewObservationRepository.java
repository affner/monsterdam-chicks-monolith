package com.monsterdam.app.repository;

import com.monsterdam.app.domain.DocumentReviewObservation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentReviewObservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentReviewObservationRepository extends JpaRepository<DocumentReviewObservation, Long> {}
