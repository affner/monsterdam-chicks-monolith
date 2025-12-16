package com.monsterdam.app.repository;

import com.monsterdam.app.domain.HelpQuestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HelpQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpQuestionRepository extends JpaRepository<HelpQuestion, Long> {}
