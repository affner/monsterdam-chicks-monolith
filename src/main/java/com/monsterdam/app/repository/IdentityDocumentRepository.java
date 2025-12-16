package com.monsterdam.app.repository;

import com.monsterdam.app.domain.IdentityDocument;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IdentityDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, Long> {}
