package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.UserReport;
import com.monsterdam.app.repository.UserReportRepository;
import com.monsterdam.app.service.UserReportService;
import com.monsterdam.app.service.dto.UserReportDTO;
import com.monsterdam.app.service.mapper.UserReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.UserReport}.
 */
@Service
@Transactional
public class UserReportServiceImpl implements UserReportService {

    private static final Logger LOG = LoggerFactory.getLogger(UserReportServiceImpl.class);

    private final UserReportRepository userReportRepository;

    private final UserReportMapper userReportMapper;

    public UserReportServiceImpl(UserReportRepository userReportRepository, UserReportMapper userReportMapper) {
        this.userReportRepository = userReportRepository;
        this.userReportMapper = userReportMapper;
    }

    @Override
    public UserReportDTO save(UserReportDTO userReportDTO) {
        LOG.debug("Request to save UserReport : {}", userReportDTO);
        UserReport userReport = userReportMapper.toEntity(userReportDTO);
        userReport = userReportRepository.save(userReport);
        return userReportMapper.toDto(userReport);
    }

    @Override
    public UserReportDTO update(UserReportDTO userReportDTO) {
        LOG.debug("Request to update UserReport : {}", userReportDTO);
        UserReport userReport = userReportMapper.toEntity(userReportDTO);
        userReport = userReportRepository.save(userReport);
        return userReportMapper.toDto(userReport);
    }

    @Override
    public Optional<UserReportDTO> partialUpdate(UserReportDTO userReportDTO) {
        LOG.debug("Request to partially update UserReport : {}", userReportDTO);

        return userReportRepository
            .findById(userReportDTO.getId())
            .map(existingUserReport -> {
                userReportMapper.partialUpdate(existingUserReport, userReportDTO);

                return existingUserReport;
            })
            .map(userReportRepository::save)
            .map(userReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserReportDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserReports");
        return userReportRepository.findAll(pageable).map(userReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserReportDTO> findOne(Long id) {
        LOG.debug("Request to get UserReport : {}", id);
        return userReportRepository.findById(id).map(userReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserReport : {}", id);
        userReportRepository.deleteById(id);
    }
}
