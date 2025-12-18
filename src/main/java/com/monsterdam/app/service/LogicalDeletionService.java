package com.monsterdam.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogicalDeletionService<D> {
    Page<D> logicalFindAll(Pageable pageable);

    Optional<D> logicalGet(Long id);

    void logicalDelete(Long id);

    void restore(Long id);
}
