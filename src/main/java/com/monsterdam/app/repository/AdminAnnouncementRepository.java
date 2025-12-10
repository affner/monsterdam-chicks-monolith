package com.monsterdam.app.repository;

import com.monsterdam.app.domain.AdminAnnouncement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AdminAnnouncement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminAnnouncementRepository extends JpaRepository<AdminAnnouncement, Long> {}
