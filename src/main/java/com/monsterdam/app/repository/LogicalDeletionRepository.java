package com.monsterdam.app.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface LogicalDeletionRepository<T> extends JpaRepository<T, Long> {
    Page<T> findAllByDeletedDateIsNull(Pageable pageable);

    Optional<T> findByIdAndDeletedDateIsNull(Long id);

    boolean existsByIdAndDeletedDateIsNull(Long id);
}
