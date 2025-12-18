package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.State;
import com.monsterdam.app.repository.StateRepository;
import com.monsterdam.app.service.StateService;
import com.monsterdam.app.service.dto.StateDTO;
import com.monsterdam.app.service.mapper.StateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.State}.
 */
@Service
@Transactional
public class StateServiceImpl extends AbstractLogicalDeletionService<State, StateDTO> implements StateService {

    private static final Logger LOG = LoggerFactory.getLogger(StateServiceImpl.class);

    private final StateRepository stateRepository;

    private final StateMapper stateMapper;

    public StateServiceImpl(StateRepository stateRepository, StateMapper stateMapper) {
        super(stateRepository, stateMapper, State::setDeletedDate);
        this.stateRepository = stateRepository;
        this.stateMapper = stateMapper;
    }

    @Override
    public StateDTO save(StateDTO stateDTO) {
        LOG.debug("Request to save State : {}", stateDTO);
        State state = stateMapper.toEntity(stateDTO);
        state = stateRepository.save(state);
        return stateMapper.toDto(state);
    }

    @Override
    public StateDTO update(StateDTO stateDTO) {
        LOG.debug("Request to update State : {}", stateDTO);
        State state = stateMapper.toEntity(stateDTO);
        state = stateRepository.save(state);
        return stateMapper.toDto(state);
    }

    @Override
    public Optional<StateDTO> partialUpdate(StateDTO stateDTO) {
        LOG.debug("Request to partially update State : {}", stateDTO);

        return stateRepository
            .findById(stateDTO.getId())
            .map(existingState -> {
                stateMapper.partialUpdate(existingState, stateDTO);

                return existingState;
            })
            .map(stateRepository::save)
            .map(stateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StateDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all States");
        return stateRepository.findAll(pageable).map(stateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StateDTO> findOne(Long id) {
        LOG.debug("Request to get State : {}", id);
        return stateRepository.findById(id).map(stateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete State : {}", id);
        stateRepository.deleteById(id);
    }
}
