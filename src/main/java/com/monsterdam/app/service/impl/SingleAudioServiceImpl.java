package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.SingleAudio;
import com.monsterdam.app.repository.SingleAudioRepository;
import com.monsterdam.app.service.SingleAudioService;
import com.monsterdam.app.service.dto.SingleAudioDTO;
import com.monsterdam.app.service.mapper.SingleAudioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.SingleAudio}.
 */
@Service
@Transactional
public class SingleAudioServiceImpl implements SingleAudioService {

    private static final Logger LOG = LoggerFactory.getLogger(SingleAudioServiceImpl.class);

    private final SingleAudioRepository singleAudioRepository;

    private final SingleAudioMapper singleAudioMapper;

    public SingleAudioServiceImpl(SingleAudioRepository singleAudioRepository, SingleAudioMapper singleAudioMapper) {
        this.singleAudioRepository = singleAudioRepository;
        this.singleAudioMapper = singleAudioMapper;
    }

    @Override
    public SingleAudioDTO save(SingleAudioDTO singleAudioDTO) {
        LOG.debug("Request to save SingleAudio : {}", singleAudioDTO);
        SingleAudio singleAudio = singleAudioMapper.toEntity(singleAudioDTO);
        singleAudio = singleAudioRepository.save(singleAudio);
        return singleAudioMapper.toDto(singleAudio);
    }

    @Override
    public SingleAudioDTO update(SingleAudioDTO singleAudioDTO) {
        LOG.debug("Request to update SingleAudio : {}", singleAudioDTO);
        SingleAudio singleAudio = singleAudioMapper.toEntity(singleAudioDTO);
        singleAudio = singleAudioRepository.save(singleAudio);
        return singleAudioMapper.toDto(singleAudio);
    }

    @Override
    public Optional<SingleAudioDTO> partialUpdate(SingleAudioDTO singleAudioDTO) {
        LOG.debug("Request to partially update SingleAudio : {}", singleAudioDTO);

        return singleAudioRepository
            .findById(singleAudioDTO.getId())
            .map(existingSingleAudio -> {
                singleAudioMapper.partialUpdate(existingSingleAudio, singleAudioDTO);

                return existingSingleAudio;
            })
            .map(singleAudioRepository::save)
            .map(singleAudioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SingleAudioDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SingleAudios");
        return singleAudioRepository.findAll(pageable).map(singleAudioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SingleAudioDTO> findOne(Long id) {
        LOG.debug("Request to get SingleAudio : {}", id);
        return singleAudioRepository.findById(id).map(singleAudioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SingleAudio : {}", id);
        singleAudioRepository.deleteById(id);
    }
}
