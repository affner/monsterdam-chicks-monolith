package com.monsterdam.app.repository;

import com.monsterdam.app.domain.PostMention;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostMention entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostMentionRepository extends JpaRepository<PostMention, Long> {}
