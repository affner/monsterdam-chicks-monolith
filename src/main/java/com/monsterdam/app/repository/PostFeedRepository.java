package com.monsterdam.app.repository;

import com.monsterdam.app.domain.PostFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostFeed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostFeedRepository extends JpaRepository<PostFeed, Long> {
    @Query(
        "select post from PostFeed post where (post.isHidden is null or post.isHidden = false) and post.isDeleted = false order by post.pinnedPost desc, post.createdDate desc"
    )
    Page<PostFeed> findVisibleFeed(Pageable pageable);

    Page<PostFeed> findByPostContentContainingIgnoreCaseAndIsDeletedFalse(String query, Pageable pageable);
}
