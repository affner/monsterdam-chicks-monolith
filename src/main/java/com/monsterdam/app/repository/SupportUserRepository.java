package com.monsterdam.app.repository;

import com.monsterdam.app.domain.SupportUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SupportUser entity.
 */
@Repository
public interface SupportUserRepository extends JpaRepository<SupportUser, Long> {
    Optional<SupportUser> findOneByUserId(Long userId);

    Optional<SupportUser> findOneByUser_LoginIgnoreCase(String login);

    Optional<SupportUser> findOneByUser_EmailIgnoreCase(String email);
}
