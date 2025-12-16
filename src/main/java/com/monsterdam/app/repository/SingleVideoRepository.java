package com.monsterdam.app.repository;

import com.monsterdam.app.domain.SingleVideo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SingleVideo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SingleVideoRepository extends JpaRepository<SingleVideo, Long> {}
