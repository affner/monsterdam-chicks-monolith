package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.repository.UserLiteRepository;
import com.monsterdam.app.service.UserLiteService;
import com.monsterdam.app.service.dto.UserLiteDTO;
import com.monsterdam.app.service.mapper.UserLiteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.UserLite}.
 */
@Service
@Transactional
public class UserLiteServiceImpl extends AbstractLogicalDeletionService<UserLite, UserLiteDTO> implements UserLiteService {

    private static final Logger LOG = LoggerFactory.getLogger(UserLiteServiceImpl.class);

    private final UserLiteRepository userLiteRepository;

    private final UserLiteMapper userLiteMapper;

    public UserLiteServiceImpl(UserLiteRepository userLiteRepository, UserLiteMapper userLiteMapper) {
        super(userLiteRepository, userLiteMapper, UserLite::setDeletedDate);
        this.userLiteRepository = userLiteRepository;
        this.userLiteMapper = userLiteMapper;
    }

    @Override
    public UserLiteDTO save(UserLiteDTO userLiteDTO) {
        LOG.debug("Request to save UserLite : {}", userLiteDTO);
        UserLite userLite = userLiteMapper.toEntity(userLiteDTO);
        userLite = userLiteRepository.save(userLite);
        return userLiteMapper.toDto(userLite);
    }

    @Override
    public UserLiteDTO update(UserLiteDTO userLiteDTO) {
        LOG.debug("Request to update UserLite : {}", userLiteDTO);
        UserLite userLite = userLiteMapper.toEntity(userLiteDTO);
        userLite = userLiteRepository.save(userLite);
        return userLiteMapper.toDto(userLite);
    }

    @Override
    public Optional<UserLiteDTO> partialUpdate(UserLiteDTO userLiteDTO) {
        LOG.debug("Request to partially update UserLite : {}", userLiteDTO);

        return userLiteRepository
            .findById(userLiteDTO.getId())
            .map(existingUserLite -> {
                userLiteMapper.partialUpdate(existingUserLite, userLiteDTO);

                return existingUserLite;
            })
            .map(userLiteRepository::save)
            .map(userLiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserLiteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserLites");
        return userLiteRepository.findAll(pageable).map(userLiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserLiteDTO> findOne(Long id) {
        LOG.debug("Request to get UserLite : {}", id);
        return userLiteRepository.findById(id).map(userLiteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserLite : {}", id);
        userLiteRepository.deleteById(id);
    }
}
