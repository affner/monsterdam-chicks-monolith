package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.PlatformAdminUser;
import com.monsterdam.app.repository.PlatformAdminUserRepository;
import com.monsterdam.app.service.PlatformAdminUserService;
import com.monsterdam.app.service.dto.PlatformAdminUserDTO;
import com.monsterdam.app.service.mapper.PlatformAdminUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.PlatformAdminUser}.
 */
@Service
@Transactional
public class PlatformAdminUserServiceImpl
    extends AbstractLogicalDeletionService<PlatformAdminUser, PlatformAdminUserDTO>
    implements PlatformAdminUserService {

    private static final Logger LOG = LoggerFactory.getLogger(PlatformAdminUserServiceImpl.class);

    private final PlatformAdminUserRepository platformAdminUserRepository;

    private final PlatformAdminUserMapper platformAdminUserMapper;

    public PlatformAdminUserServiceImpl(
        PlatformAdminUserRepository platformAdminUserRepository,
        PlatformAdminUserMapper platformAdminUserMapper
    ) {
        super(platformAdminUserRepository, platformAdminUserMapper, PlatformAdminUser::setDeletedDate);
        this.platformAdminUserRepository = platformAdminUserRepository;
        this.platformAdminUserMapper = platformAdminUserMapper;
    }

    @Override
    public PlatformAdminUserDTO save(PlatformAdminUserDTO platformAdminUserDTO) {
        LOG.debug("Request to save PlatformAdminUser : {}", platformAdminUserDTO);
        PlatformAdminUser platformAdminUser = platformAdminUserMapper.toEntity(platformAdminUserDTO);
        platformAdminUser = platformAdminUserRepository.save(platformAdminUser);
        return platformAdminUserMapper.toDto(platformAdminUser);
    }

    @Override
    public PlatformAdminUserDTO update(PlatformAdminUserDTO platformAdminUserDTO) {
        LOG.debug("Request to update PlatformAdminUser : {}", platformAdminUserDTO);
        PlatformAdminUser platformAdminUser = platformAdminUserMapper.toEntity(platformAdminUserDTO);
        platformAdminUser = platformAdminUserRepository.save(platformAdminUser);
        return platformAdminUserMapper.toDto(platformAdminUser);
    }

    @Override
    public Optional<PlatformAdminUserDTO> partialUpdate(PlatformAdminUserDTO platformAdminUserDTO) {
        LOG.debug("Request to partially update PlatformAdminUser : {}", platformAdminUserDTO);

        return platformAdminUserRepository
            .findById(platformAdminUserDTO.getId())
            .map(existingPlatformAdminUser -> {
                platformAdminUserMapper.partialUpdate(existingPlatformAdminUser, platformAdminUserDTO);

                return existingPlatformAdminUser;
            })
            .map(platformAdminUserRepository::save)
            .map(platformAdminUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlatformAdminUserDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlatformAdminUsers");
        return platformAdminUserRepository.findAll(pageable).map(platformAdminUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlatformAdminUserDTO> findOne(Long id) {
        LOG.debug("Request to get PlatformAdminUser : {}", id);
        return platformAdminUserRepository.findById(id).map(platformAdminUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PlatformAdminUser : {}", id);
        platformAdminUserRepository.deleteById(id);
    }
}
