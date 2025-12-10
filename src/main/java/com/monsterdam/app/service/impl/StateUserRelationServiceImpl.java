package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.StateUserRelation;
import com.monsterdam.app.repository.StateUserRelationRepository;
import com.monsterdam.app.service.StateUserRelationService;
import com.monsterdam.app.service.dto.StateUserRelationDTO;
import com.monsterdam.app.service.mapper.StateUserRelationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.StateUserRelation}.
 */
@Service
@Transactional
public class StateUserRelationServiceImpl implements StateUserRelationService {

    private static final Logger LOG = LoggerFactory.getLogger(StateUserRelationServiceImpl.class);

    private final StateUserRelationRepository stateUserRelationRepository;

    private final StateUserRelationMapper stateUserRelationMapper;

    public StateUserRelationServiceImpl(
        StateUserRelationRepository stateUserRelationRepository,
        StateUserRelationMapper stateUserRelationMapper
    ) {
        this.stateUserRelationRepository = stateUserRelationRepository;
        this.stateUserRelationMapper = stateUserRelationMapper;
    }

    @Override
    public StateUserRelationDTO save(StateUserRelationDTO stateUserRelationDTO) {
        LOG.debug("Request to save StateUserRelation : {}", stateUserRelationDTO);
        StateUserRelation stateUserRelation = stateUserRelationMapper.toEntity(stateUserRelationDTO);
        stateUserRelation = stateUserRelationRepository.save(stateUserRelation);
        return stateUserRelationMapper.toDto(stateUserRelation);
    }

    @Override
    public StateUserRelationDTO update(StateUserRelationDTO stateUserRelationDTO) {
        LOG.debug("Request to update StateUserRelation : {}", stateUserRelationDTO);
        StateUserRelation stateUserRelation = stateUserRelationMapper.toEntity(stateUserRelationDTO);
        stateUserRelation = stateUserRelationRepository.save(stateUserRelation);
        return stateUserRelationMapper.toDto(stateUserRelation);
    }

    @Override
    public Optional<StateUserRelationDTO> partialUpdate(StateUserRelationDTO stateUserRelationDTO) {
        LOG.debug("Request to partially update StateUserRelation : {}", stateUserRelationDTO);

        return stateUserRelationRepository
            .findById(stateUserRelationDTO.getId())
            .map(existingStateUserRelation -> {
                stateUserRelationMapper.partialUpdate(existingStateUserRelation, stateUserRelationDTO);

                return existingStateUserRelation;
            })
            .map(stateUserRelationRepository::save)
            .map(stateUserRelationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StateUserRelationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all StateUserRelations");
        return stateUserRelationRepository.findAll(pageable).map(stateUserRelationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StateUserRelationDTO> findOne(Long id) {
        LOG.debug("Request to get StateUserRelation : {}", id);
        return stateUserRelationRepository.findById(id).map(stateUserRelationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete StateUserRelation : {}", id);
        stateUserRelationRepository.deleteById(id);
    }
}
