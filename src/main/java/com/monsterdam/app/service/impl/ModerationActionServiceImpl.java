package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.ModerationAction;
import com.monsterdam.app.repository.ModerationActionRepository;
import com.monsterdam.app.service.ModerationActionService;
import com.monsterdam.app.service.dto.ModerationActionDTO;
import com.monsterdam.app.service.mapper.ModerationActionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.ModerationAction}.
 */
@Service
@Transactional
public class ModerationActionServiceImpl implements ModerationActionService {

    private static final Logger LOG = LoggerFactory.getLogger(ModerationActionServiceImpl.class);

    private final ModerationActionRepository moderationActionRepository;

    private final ModerationActionMapper moderationActionMapper;

    public ModerationActionServiceImpl(
        ModerationActionRepository moderationActionRepository,
        ModerationActionMapper moderationActionMapper
    ) {
        this.moderationActionRepository = moderationActionRepository;
        this.moderationActionMapper = moderationActionMapper;
    }

    @Override
    public ModerationActionDTO save(ModerationActionDTO moderationActionDTO) {
        LOG.debug("Request to save ModerationAction : {}", moderationActionDTO);
        ModerationAction moderationAction = moderationActionMapper.toEntity(moderationActionDTO);
        moderationAction = moderationActionRepository.save(moderationAction);
        return moderationActionMapper.toDto(moderationAction);
    }

    @Override
    public ModerationActionDTO update(ModerationActionDTO moderationActionDTO) {
        LOG.debug("Request to update ModerationAction : {}", moderationActionDTO);
        ModerationAction moderationAction = moderationActionMapper.toEntity(moderationActionDTO);
        moderationAction = moderationActionRepository.save(moderationAction);
        return moderationActionMapper.toDto(moderationAction);
    }

    @Override
    public Optional<ModerationActionDTO> partialUpdate(ModerationActionDTO moderationActionDTO) {
        LOG.debug("Request to partially update ModerationAction : {}", moderationActionDTO);

        return moderationActionRepository
            .findById(moderationActionDTO.getId())
            .map(existingModerationAction -> {
                moderationActionMapper.partialUpdate(existingModerationAction, moderationActionDTO);

                return existingModerationAction;
            })
            .map(moderationActionRepository::save)
            .map(moderationActionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ModerationActionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ModerationActions");
        return moderationActionRepository.findAll(pageable).map(moderationActionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ModerationActionDTO> findOne(Long id) {
        LOG.debug("Request to get ModerationAction : {}", id);
        return moderationActionRepository.findById(id).map(moderationActionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ModerationAction : {}", id);
        moderationActionRepository.deleteById(id);
    }
}
