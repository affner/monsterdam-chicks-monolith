package com.monsterdam.app.repository;

import com.monsterdam.app.domain.CommentMention;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommentMention entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentMentionRepository extends LogicalDeletionRepository<CommentMention> {}
