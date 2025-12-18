package com.monsterdam.app.repository;

import com.monsterdam.app.domain.PostFeed;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostFeed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostFeedRepository extends LogicalDeletionRepository<PostFeed> {}
