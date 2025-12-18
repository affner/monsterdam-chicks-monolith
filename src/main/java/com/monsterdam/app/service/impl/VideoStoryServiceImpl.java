package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.VideoStory;
import com.monsterdam.app.repository.VideoStoryRepository;
import com.monsterdam.app.service.VideoStoryService;
import com.monsterdam.app.service.dto.VideoStoryDTO;
import com.monsterdam.app.service.mapper.VideoStoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.VideoStory}.
 */
@Service
@Transactional
public class VideoStoryServiceImpl extends AbstractLogicalDeletionService<VideoStory, VideoStoryDTO> implements VideoStoryService {

    private static final Logger LOG = LoggerFactory.getLogger(VideoStoryServiceImpl.class);

    private final VideoStoryRepository videoStoryRepository;

    private final VideoStoryMapper videoStoryMapper;

    public VideoStoryServiceImpl(VideoStoryRepository videoStoryRepository, VideoStoryMapper videoStoryMapper) {
        super(videoStoryRepository, videoStoryMapper, VideoStory::setDeletedDate);
        this.videoStoryRepository = videoStoryRepository;
        this.videoStoryMapper = videoStoryMapper;
    }

    @Override
    public VideoStoryDTO save(VideoStoryDTO videoStoryDTO) {
        LOG.debug("Request to save VideoStory : {}", videoStoryDTO);
        VideoStory videoStory = videoStoryMapper.toEntity(videoStoryDTO);
        videoStory = videoStoryRepository.save(videoStory);
        return videoStoryMapper.toDto(videoStory);
    }

    @Override
    public VideoStoryDTO update(VideoStoryDTO videoStoryDTO) {
        LOG.debug("Request to update VideoStory : {}", videoStoryDTO);
        VideoStory videoStory = videoStoryMapper.toEntity(videoStoryDTO);
        videoStory = videoStoryRepository.save(videoStory);
        return videoStoryMapper.toDto(videoStory);
    }

    @Override
    public Optional<VideoStoryDTO> partialUpdate(VideoStoryDTO videoStoryDTO) {
        LOG.debug("Request to partially update VideoStory : {}", videoStoryDTO);

        return videoStoryRepository
            .findById(videoStoryDTO.getId())
            .map(existingVideoStory -> {
                videoStoryMapper.partialUpdate(existingVideoStory, videoStoryDTO);

                return existingVideoStory;
            })
            .map(videoStoryRepository::save)
            .map(videoStoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VideoStoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all VideoStories");
        return videoStoryRepository.findAll(pageable).map(videoStoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VideoStoryDTO> findOne(Long id) {
        LOG.debug("Request to get VideoStory : {}", id);
        return videoStoryRepository.findById(id).map(videoStoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete VideoStory : {}", id);
        videoStoryRepository.deleteById(id);
    }
}
