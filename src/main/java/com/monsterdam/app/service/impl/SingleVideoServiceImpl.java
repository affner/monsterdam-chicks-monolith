package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.SingleVideo;
import com.monsterdam.app.repository.SingleVideoRepository;
import com.monsterdam.app.service.SingleVideoService;
import com.monsterdam.app.service.dto.SingleVideoDTO;
import com.monsterdam.app.service.mapper.SingleVideoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.SingleVideo}.
 */
@Service
@Transactional
public class SingleVideoServiceImpl implements SingleVideoService {

    private static final Logger LOG = LoggerFactory.getLogger(SingleVideoServiceImpl.class);

    private final SingleVideoRepository singleVideoRepository;

    private final SingleVideoMapper singleVideoMapper;

    public SingleVideoServiceImpl(SingleVideoRepository singleVideoRepository, SingleVideoMapper singleVideoMapper) {
        this.singleVideoRepository = singleVideoRepository;
        this.singleVideoMapper = singleVideoMapper;
    }

    @Override
    public SingleVideoDTO save(SingleVideoDTO singleVideoDTO) {
        LOG.debug("Request to save SingleVideo : {}", singleVideoDTO);
        SingleVideo singleVideo = singleVideoMapper.toEntity(singleVideoDTO);
        singleVideo = singleVideoRepository.save(singleVideo);
        return singleVideoMapper.toDto(singleVideo);
    }

    @Override
    public SingleVideoDTO update(SingleVideoDTO singleVideoDTO) {
        LOG.debug("Request to update SingleVideo : {}", singleVideoDTO);
        SingleVideo singleVideo = singleVideoMapper.toEntity(singleVideoDTO);
        singleVideo = singleVideoRepository.save(singleVideo);
        return singleVideoMapper.toDto(singleVideo);
    }

    @Override
    public Optional<SingleVideoDTO> partialUpdate(SingleVideoDTO singleVideoDTO) {
        LOG.debug("Request to partially update SingleVideo : {}", singleVideoDTO);

        return singleVideoRepository
            .findById(singleVideoDTO.getId())
            .map(existingSingleVideo -> {
                singleVideoMapper.partialUpdate(existingSingleVideo, singleVideoDTO);

                return existingSingleVideo;
            })
            .map(singleVideoRepository::save)
            .map(singleVideoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SingleVideoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SingleVideos");
        return singleVideoRepository.findAll(pageable).map(singleVideoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SingleVideoDTO> findOne(Long id) {
        LOG.debug("Request to get SingleVideo : {}", id);
        return singleVideoRepository.findById(id).map(singleVideoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SingleVideo : {}", id);
        singleVideoRepository.deleteById(id);
    }
}
