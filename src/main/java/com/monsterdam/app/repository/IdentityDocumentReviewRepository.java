package com.monsterdam.app.repository;

import com.monsterdam.app.domain.IdentityDocumentReview;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IdentityDocumentReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdentityDocumentReviewRepository extends JpaRepository<IdentityDocumentReview, Long> {}
