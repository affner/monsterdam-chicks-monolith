package com.monsterdam.app.repository;

import com.monsterdam.app.domain.PollVote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PollVote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollVoteRepository extends JpaRepository<PollVote, Long> {}
