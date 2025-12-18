package com.monsterdam.app.service.impl;

import com.monsterdam.app.repository.LogicalDeletionRepository;
import com.monsterdam.app.service.LogicalDeletionService;
import com.monsterdam.app.service.mapper.EntityMapper;
import java.time.Instant;
import java.util.Optional;
import java.util.function.BiConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractLogicalDeletionService<E, D> implements LogicalDeletionService<D> {

    private final LogicalDeletionRepository<E> repository;
    private final EntityMapper<D, E> mapper;
    private final BiConsumer<E, Instant> deletedDateSetter;

    protected AbstractLogicalDeletionService(
        LogicalDeletionRepository<E> repository,
        EntityMapper<D, E> mapper,
        BiConsumer<E, Instant> deletedDateSetter
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.deletedDateSetter = deletedDateSetter;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<D> logicalFindAll(Pageable pageable) {
        return repository.findAllByDeletedDateIsNull(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<D> logicalGet(Long id) {
        return repository.findByIdAndDeletedDateIsNull(id).map(mapper::toDto);
    }

    @Override
    public void logicalDelete(Long id) {
        repository
            .findByIdAndDeletedDateIsNull(id)
            .ifPresent(entity -> {
                deletedDateSetter.accept(entity, Instant.now());
                repository.save(entity);
            });
    }

    @Override
    public void restore(Long id) {
        repository
            .findById(id)
            .ifPresent(entity -> {
                deletedDateSetter.accept(entity, null);
                repository.save(entity);
            });
    }

    protected LogicalDeletionRepository<E> getRepository() {
        return repository;
    }
}
