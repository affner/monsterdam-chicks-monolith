package com.monsterdam.app.repository;

import com.monsterdam.app.domain.PostPoll;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostPoll entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostPollRepository extends JpaRepository<PostPoll, Long> {}
