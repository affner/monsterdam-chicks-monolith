package com.monsterdam.app.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface LogicalDeletionRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    // Spec base: no borrados (deletedDate IS NULL)
    default Specification<T> notDeletedSpec() {
        return (root, query, cb) -> cb.isNull(root.get("deletedDate"));
    }

    //  igual que findAll(spec,pageable) pero SIEMPRE filtra deletedDate IS NULL ===
    default Page<T> findAllByDeletedDateIsNull(Specification<T> spec, Pageable pageable) {
        Specification<T> finalSpec = Specification.where(notDeletedSpec());
        if (spec != null) {
            finalSpec = finalSpec.and(spec);
        }
        return findAll(finalSpec, pageable);
    }

    // opcional: mantener consistencia con Pageable-only
    default Page<T> findAllByDeletedDateIsNull(Pageable pageable) {
        return findAll(notDeletedSpec(), pageable);
    }

    Optional<T> findByIdAndDeletedDateIsNull(Long id);

    boolean existsByIdAndDeletedDateIsNull(Long id);
}
