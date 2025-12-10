package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.AdminAnnouncement;
import com.monsterdam.app.repository.AdminAnnouncementRepository;
import com.monsterdam.app.service.AdminAnnouncementService;
import com.monsterdam.app.service.dto.AdminAnnouncementDTO;
import com.monsterdam.app.service.mapper.AdminAnnouncementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.AdminAnnouncement}.
 */
@Service
@Transactional
public class AdminAnnouncementServiceImpl implements AdminAnnouncementService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminAnnouncementServiceImpl.class);

    private final AdminAnnouncementRepository adminAnnouncementRepository;

    private final AdminAnnouncementMapper adminAnnouncementMapper;

    public AdminAnnouncementServiceImpl(
        AdminAnnouncementRepository adminAnnouncementRepository,
        AdminAnnouncementMapper adminAnnouncementMapper
    ) {
        this.adminAnnouncementRepository = adminAnnouncementRepository;
        this.adminAnnouncementMapper = adminAnnouncementMapper;
    }

    @Override
    public AdminAnnouncementDTO save(AdminAnnouncementDTO adminAnnouncementDTO) {
        LOG.debug("Request to save AdminAnnouncement : {}", adminAnnouncementDTO);
        AdminAnnouncement adminAnnouncement = adminAnnouncementMapper.toEntity(adminAnnouncementDTO);
        adminAnnouncement = adminAnnouncementRepository.save(adminAnnouncement);
        return adminAnnouncementMapper.toDto(adminAnnouncement);
    }

    @Override
    public AdminAnnouncementDTO update(AdminAnnouncementDTO adminAnnouncementDTO) {
        LOG.debug("Request to update AdminAnnouncement : {}", adminAnnouncementDTO);
        AdminAnnouncement adminAnnouncement = adminAnnouncementMapper.toEntity(adminAnnouncementDTO);
        adminAnnouncement = adminAnnouncementRepository.save(adminAnnouncement);
        return adminAnnouncementMapper.toDto(adminAnnouncement);
    }

    @Override
    public Optional<AdminAnnouncementDTO> partialUpdate(AdminAnnouncementDTO adminAnnouncementDTO) {
        LOG.debug("Request to partially update AdminAnnouncement : {}", adminAnnouncementDTO);

        return adminAnnouncementRepository
            .findById(adminAnnouncementDTO.getId())
            .map(existingAdminAnnouncement -> {
                adminAnnouncementMapper.partialUpdate(existingAdminAnnouncement, adminAnnouncementDTO);

                return existingAdminAnnouncement;
            })
            .map(adminAnnouncementRepository::save)
            .map(adminAnnouncementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminAnnouncementDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AdminAnnouncements");
        return adminAnnouncementRepository.findAll(pageable).map(adminAnnouncementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdminAnnouncementDTO> findOne(Long id) {
        LOG.debug("Request to get AdminAnnouncement : {}", id);
        return adminAnnouncementRepository.findById(id).map(adminAnnouncementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AdminAnnouncement : {}", id);
        adminAnnouncementRepository.deleteById(id);
    }
}
