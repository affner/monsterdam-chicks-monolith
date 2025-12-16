package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.UserSettings;
import com.monsterdam.app.repository.UserSettingsRepository;
import com.monsterdam.app.service.UserSettingsService;
import com.monsterdam.app.service.dto.UserSettingsDTO;
import com.monsterdam.app.service.mapper.UserSettingsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.UserSettings}.
 */
@Service
@Transactional
public class UserSettingsServiceImpl implements UserSettingsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserSettingsServiceImpl.class);

    private final UserSettingsRepository userSettingsRepository;

    private final UserSettingsMapper userSettingsMapper;

    public UserSettingsServiceImpl(UserSettingsRepository userSettingsRepository, UserSettingsMapper userSettingsMapper) {
        this.userSettingsRepository = userSettingsRepository;
        this.userSettingsMapper = userSettingsMapper;
    }

    @Override
    public UserSettingsDTO save(UserSettingsDTO userSettingsDTO) {
        LOG.debug("Request to save UserSettings : {}", userSettingsDTO);
        UserSettings userSettings = userSettingsMapper.toEntity(userSettingsDTO);
        userSettings = userSettingsRepository.save(userSettings);
        return userSettingsMapper.toDto(userSettings);
    }

    @Override
    public UserSettingsDTO update(UserSettingsDTO userSettingsDTO) {
        LOG.debug("Request to update UserSettings : {}", userSettingsDTO);
        UserSettings userSettings = userSettingsMapper.toEntity(userSettingsDTO);
        userSettings = userSettingsRepository.save(userSettings);
        return userSettingsMapper.toDto(userSettings);
    }

    @Override
    public Optional<UserSettingsDTO> partialUpdate(UserSettingsDTO userSettingsDTO) {
        LOG.debug("Request to partially update UserSettings : {}", userSettingsDTO);

        return userSettingsRepository
            .findById(userSettingsDTO.getId())
            .map(existingUserSettings -> {
                userSettingsMapper.partialUpdate(existingUserSettings, userSettingsDTO);

                return existingUserSettings;
            })
            .map(userSettingsRepository::save)
            .map(userSettingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSettingsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserSettings");
        return userSettingsRepository.findAll(pageable).map(userSettingsMapper::toDto);
    }

    /**
     *  Get all the userSettings where User is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserSettingsDTO> findAllWhereUserIsNull() {
        LOG.debug("Request to get all userSettings where User is null");
        return StreamSupport.stream(userSettingsRepository.findAll().spliterator(), false)
            .filter(userSettings -> userSettings.getUser() == null)
            .map(userSettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserSettingsDTO> findOne(Long id) {
        LOG.debug("Request to get UserSettings : {}", id);
        return userSettingsRepository.findById(id).map(userSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserSettings : {}", id);
        userSettingsRepository.deleteById(id);
    }
}
