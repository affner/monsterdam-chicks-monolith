package com.monsterdam.app.repository;

import com.monsterdam.app.domain.DirectMessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DirectMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {}
