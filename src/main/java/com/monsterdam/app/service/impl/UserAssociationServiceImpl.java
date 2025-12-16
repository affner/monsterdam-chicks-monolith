package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.UserAssociation;
import com.monsterdam.app.repository.UserAssociationRepository;
import com.monsterdam.app.service.UserAssociationService;
import com.monsterdam.app.service.dto.UserAssociationDTO;
import com.monsterdam.app.service.mapper.UserAssociationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.UserAssociation}.
 */
@Service
@Transactional
public class UserAssociationServiceImpl implements UserAssociationService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAssociationServiceImpl.class);

    private final UserAssociationRepository userAssociationRepository;

    private final UserAssociationMapper userAssociationMapper;

    public UserAssociationServiceImpl(UserAssociationRepository userAssociationRepository, UserAssociationMapper userAssociationMapper) {
        this.userAssociationRepository = userAssociationRepository;
        this.userAssociationMapper = userAssociationMapper;
    }

    @Override
    public UserAssociationDTO save(UserAssociationDTO userAssociationDTO) {
        LOG.debug("Request to save UserAssociation : {}", userAssociationDTO);
        UserAssociation userAssociation = userAssociationMapper.toEntity(userAssociationDTO);
        userAssociation = userAssociationRepository.save(userAssociation);
        return userAssociationMapper.toDto(userAssociation);
    }

    @Override
    public UserAssociationDTO update(UserAssociationDTO userAssociationDTO) {
        LOG.debug("Request to update UserAssociation : {}", userAssociationDTO);
        UserAssociation userAssociation = userAssociationMapper.toEntity(userAssociationDTO);
        userAssociation = userAssociationRepository.save(userAssociation);
        return userAssociationMapper.toDto(userAssociation);
    }

    @Override
    public Optional<UserAssociationDTO> partialUpdate(UserAssociationDTO userAssociationDTO) {
        LOG.debug("Request to partially update UserAssociation : {}", userAssociationDTO);

        return userAssociationRepository
            .findById(userAssociationDTO.getId())
            .map(existingUserAssociation -> {
                userAssociationMapper.partialUpdate(existingUserAssociation, userAssociationDTO);

                return existingUserAssociation;
            })
            .map(userAssociationRepository::save)
            .map(userAssociationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAssociationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserAssociations");
        return userAssociationRepository.findAll(pageable).map(userAssociationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAssociationDTO> findOne(Long id) {
        LOG.debug("Request to get UserAssociation : {}", id);
        return userAssociationRepository.findById(id).map(userAssociationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserAssociation : {}", id);
        userAssociationRepository.deleteById(id);
    }
}
