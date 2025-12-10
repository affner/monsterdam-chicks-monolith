package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.UserEvent;
import com.monsterdam.app.repository.UserEventRepository;
import com.monsterdam.app.service.UserEventService;
import com.monsterdam.app.service.dto.UserEventDTO;
import com.monsterdam.app.service.mapper.UserEventMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.UserEvent}.
 */
@Service
@Transactional
public class UserEventServiceImpl implements UserEventService {

    private static final Logger LOG = LoggerFactory.getLogger(UserEventServiceImpl.class);

    private final UserEventRepository userEventRepository;

    private final UserEventMapper userEventMapper;

    public UserEventServiceImpl(UserEventRepository userEventRepository, UserEventMapper userEventMapper) {
        this.userEventRepository = userEventRepository;
        this.userEventMapper = userEventMapper;
    }

    @Override
    public UserEventDTO save(UserEventDTO userEventDTO) {
        LOG.debug("Request to save UserEvent : {}", userEventDTO);
        UserEvent userEvent = userEventMapper.toEntity(userEventDTO);
        userEvent = userEventRepository.save(userEvent);
        return userEventMapper.toDto(userEvent);
    }

    @Override
    public UserEventDTO update(UserEventDTO userEventDTO) {
        LOG.debug("Request to update UserEvent : {}", userEventDTO);
        UserEvent userEvent = userEventMapper.toEntity(userEventDTO);
        userEvent = userEventRepository.save(userEvent);
        return userEventMapper.toDto(userEvent);
    }

    @Override
    public Optional<UserEventDTO> partialUpdate(UserEventDTO userEventDTO) {
        LOG.debug("Request to partially update UserEvent : {}", userEventDTO);

        return userEventRepository
            .findById(userEventDTO.getId())
            .map(existingUserEvent -> {
                userEventMapper.partialUpdate(existingUserEvent, userEventDTO);

                return existingUserEvent;
            })
            .map(userEventRepository::save)
            .map(userEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserEventDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserEvents");
        return userEventRepository.findAll(pageable).map(userEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEventDTO> findOne(Long id) {
        LOG.debug("Request to get UserEvent : {}", id);
        return userEventRepository.findById(id).map(userEventMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserEvent : {}", id);
        userEventRepository.deleteById(id);
    }
}
