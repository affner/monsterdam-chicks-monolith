package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.AdminSystemConfigs;
import com.monsterdam.app.repository.AdminSystemConfigsRepository;
import com.monsterdam.app.service.AdminSystemConfigsService;
import com.monsterdam.app.service.dto.AdminSystemConfigsDTO;
import com.monsterdam.app.service.mapper.AdminSystemConfigsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.AdminSystemConfigs}.
 */
@Service
@Transactional
public class AdminSystemConfigsServiceImpl
    extends AbstractLogicalDeletionService<AdminSystemConfigs, AdminSystemConfigsDTO>
    implements AdminSystemConfigsService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminSystemConfigsServiceImpl.class);

    private final AdminSystemConfigsRepository adminSystemConfigsRepository;

    private final AdminSystemConfigsMapper adminSystemConfigsMapper;

    public AdminSystemConfigsServiceImpl(
        AdminSystemConfigsRepository adminSystemConfigsRepository,
        AdminSystemConfigsMapper adminSystemConfigsMapper
    ) {
        super(adminSystemConfigsRepository, adminSystemConfigsMapper, AdminSystemConfigs::setDeletedDate);
        this.adminSystemConfigsRepository = adminSystemConfigsRepository;
        this.adminSystemConfigsMapper = adminSystemConfigsMapper;
    }

    @Override
    public AdminSystemConfigsDTO save(AdminSystemConfigsDTO adminSystemConfigsDTO) {
        LOG.debug("Request to save AdminSystemConfigs : {}", adminSystemConfigsDTO);
        AdminSystemConfigs adminSystemConfigs = adminSystemConfigsMapper.toEntity(adminSystemConfigsDTO);
        adminSystemConfigs = adminSystemConfigsRepository.save(adminSystemConfigs);
        return adminSystemConfigsMapper.toDto(adminSystemConfigs);
    }

    @Override
    public AdminSystemConfigsDTO update(AdminSystemConfigsDTO adminSystemConfigsDTO) {
        LOG.debug("Request to update AdminSystemConfigs : {}", adminSystemConfigsDTO);
        AdminSystemConfigs adminSystemConfigs = adminSystemConfigsMapper.toEntity(adminSystemConfigsDTO);
        adminSystemConfigs = adminSystemConfigsRepository.save(adminSystemConfigs);
        return adminSystemConfigsMapper.toDto(adminSystemConfigs);
    }

    @Override
    public Optional<AdminSystemConfigsDTO> partialUpdate(AdminSystemConfigsDTO adminSystemConfigsDTO) {
        LOG.debug("Request to partially update AdminSystemConfigs : {}", adminSystemConfigsDTO);

        return adminSystemConfigsRepository
            .findById(adminSystemConfigsDTO.getId())
            .map(existingAdminSystemConfigs -> {
                adminSystemConfigsMapper.partialUpdate(existingAdminSystemConfigs, adminSystemConfigsDTO);

                return existingAdminSystemConfigs;
            })
            .map(adminSystemConfigsRepository::save)
            .map(adminSystemConfigsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminSystemConfigsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AdminSystemConfigs");
        return adminSystemConfigsRepository.findAll(pageable).map(adminSystemConfigsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdminSystemConfigsDTO> findOne(Long id) {
        LOG.debug("Request to get AdminSystemConfigs : {}", id);
        return adminSystemConfigsRepository.findById(id).map(adminSystemConfigsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AdminSystemConfigs : {}", id);
        adminSystemConfigsRepository.deleteById(id);
    }
}
