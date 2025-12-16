package com.monsterdam.app.repository;

import com.monsterdam.app.domain.UserAssociation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserAssociation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAssociationRepository extends JpaRepository<UserAssociation, Long> {}
