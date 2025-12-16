package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.TrialLink;
import com.monsterdam.app.repository.TrialLinkRepository;
import com.monsterdam.app.service.TrialLinkService;
import com.monsterdam.app.service.dto.TrialLinkDTO;
import com.monsterdam.app.service.mapper.TrialLinkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.TrialLink}.
 */
@Service
@Transactional
public class TrialLinkServiceImpl implements TrialLinkService {

    private static final Logger LOG = LoggerFactory.getLogger(TrialLinkServiceImpl.class);

    private final TrialLinkRepository trialLinkRepository;

    private final TrialLinkMapper trialLinkMapper;

    public TrialLinkServiceImpl(TrialLinkRepository trialLinkRepository, TrialLinkMapper trialLinkMapper) {
        this.trialLinkRepository = trialLinkRepository;
        this.trialLinkMapper = trialLinkMapper;
    }

    @Override
    public TrialLinkDTO save(TrialLinkDTO trialLinkDTO) {
        LOG.debug("Request to save TrialLink : {}", trialLinkDTO);
        TrialLink trialLink = trialLinkMapper.toEntity(trialLinkDTO);
        trialLink = trialLinkRepository.save(trialLink);
        return trialLinkMapper.toDto(trialLink);
    }

    @Override
    public TrialLinkDTO update(TrialLinkDTO trialLinkDTO) {
        LOG.debug("Request to update TrialLink : {}", trialLinkDTO);
        TrialLink trialLink = trialLinkMapper.toEntity(trialLinkDTO);
        trialLink = trialLinkRepository.save(trialLink);
        return trialLinkMapper.toDto(trialLink);
    }

    @Override
    public Optional<TrialLinkDTO> partialUpdate(TrialLinkDTO trialLinkDTO) {
        LOG.debug("Request to partially update TrialLink : {}", trialLinkDTO);

        return trialLinkRepository
            .findById(trialLinkDTO.getId())
            .map(existingTrialLink -> {
                trialLinkMapper.partialUpdate(existingTrialLink, trialLinkDTO);

                return existingTrialLink;
            })
            .map(trialLinkRepository::save)
            .map(trialLinkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrialLinkDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TrialLinks");
        return trialLinkRepository.findAll(pageable).map(trialLinkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrialLinkDTO> findOne(Long id) {
        LOG.debug("Request to get TrialLink : {}", id);
        return trialLinkRepository.findById(id).map(trialLinkMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TrialLink : {}", id);
        trialLinkRepository.deleteById(id);
    }
}
