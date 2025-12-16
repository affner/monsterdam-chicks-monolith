package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.SocialNetwork;
import com.monsterdam.app.repository.SocialNetworkRepository;
import com.monsterdam.app.service.SocialNetworkService;
import com.monsterdam.app.service.dto.SocialNetworkDTO;
import com.monsterdam.app.service.mapper.SocialNetworkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.SocialNetwork}.
 */
@Service
@Transactional
public class SocialNetworkServiceImpl implements SocialNetworkService {

    private static final Logger LOG = LoggerFactory.getLogger(SocialNetworkServiceImpl.class);

    private final SocialNetworkRepository socialNetworkRepository;

    private final SocialNetworkMapper socialNetworkMapper;

    public SocialNetworkServiceImpl(SocialNetworkRepository socialNetworkRepository, SocialNetworkMapper socialNetworkMapper) {
        this.socialNetworkRepository = socialNetworkRepository;
        this.socialNetworkMapper = socialNetworkMapper;
    }

    @Override
    public SocialNetworkDTO save(SocialNetworkDTO socialNetworkDTO) {
        LOG.debug("Request to save SocialNetwork : {}", socialNetworkDTO);
        SocialNetwork socialNetwork = socialNetworkMapper.toEntity(socialNetworkDTO);
        socialNetwork = socialNetworkRepository.save(socialNetwork);
        return socialNetworkMapper.toDto(socialNetwork);
    }

    @Override
    public SocialNetworkDTO update(SocialNetworkDTO socialNetworkDTO) {
        LOG.debug("Request to update SocialNetwork : {}", socialNetworkDTO);
        SocialNetwork socialNetwork = socialNetworkMapper.toEntity(socialNetworkDTO);
        socialNetwork = socialNetworkRepository.save(socialNetwork);
        return socialNetworkMapper.toDto(socialNetwork);
    }

    @Override
    public Optional<SocialNetworkDTO> partialUpdate(SocialNetworkDTO socialNetworkDTO) {
        LOG.debug("Request to partially update SocialNetwork : {}", socialNetworkDTO);

        return socialNetworkRepository
            .findById(socialNetworkDTO.getId())
            .map(existingSocialNetwork -> {
                socialNetworkMapper.partialUpdate(existingSocialNetwork, socialNetworkDTO);

                return existingSocialNetwork;
            })
            .map(socialNetworkRepository::save)
            .map(socialNetworkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SocialNetworkDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SocialNetworks");
        return socialNetworkRepository.findAll(pageable).map(socialNetworkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SocialNetworkDTO> findOne(Long id) {
        LOG.debug("Request to get SocialNetwork : {}", id);
        return socialNetworkRepository.findById(id).map(socialNetworkMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SocialNetwork : {}", id);
        socialNetworkRepository.deleteById(id);
    }
}
