package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.repository.PostFeedRepository;
import com.monsterdam.app.service.PostFeedService;
import com.monsterdam.app.service.dto.PostFeedDTO;
import com.monsterdam.app.service.mapper.PostFeedMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.PostFeed}.
 */
@Service
@Transactional
public class PostFeedServiceImpl implements PostFeedService {

    private static final Logger LOG = LoggerFactory.getLogger(PostFeedServiceImpl.class);

    private final PostFeedRepository postFeedRepository;

    private final PostFeedMapper postFeedMapper;

    public PostFeedServiceImpl(PostFeedRepository postFeedRepository, PostFeedMapper postFeedMapper) {
        this.postFeedRepository = postFeedRepository;
        this.postFeedMapper = postFeedMapper;
    }

    @Override
    public PostFeedDTO save(PostFeedDTO postFeedDTO) {
        LOG.debug("Request to save PostFeed : {}", postFeedDTO);
        PostFeed postFeed = postFeedMapper.toEntity(postFeedDTO);
        postFeed = postFeedRepository.save(postFeed);
        return postFeedMapper.toDto(postFeed);
    }

    @Override
    public PostFeedDTO update(PostFeedDTO postFeedDTO) {
        LOG.debug("Request to update PostFeed : {}", postFeedDTO);
        PostFeed postFeed = postFeedMapper.toEntity(postFeedDTO);
        postFeed = postFeedRepository.save(postFeed);
        return postFeedMapper.toDto(postFeed);
    }

    @Override
    public Optional<PostFeedDTO> partialUpdate(PostFeedDTO postFeedDTO) {
        LOG.debug("Request to partially update PostFeed : {}", postFeedDTO);

        return postFeedRepository
            .findById(postFeedDTO.getId())
            .map(existingPostFeed -> {
                postFeedMapper.partialUpdate(existingPostFeed, postFeedDTO);

                return existingPostFeed;
            })
            .map(postFeedRepository::save)
            .map(postFeedMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostFeedDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PostFeeds");
        return postFeedRepository.findAll(pageable).map(postFeedMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostFeedDTO> findPublicFeed(Pageable pageable) {
        LOG.debug("Request to get public PostFeeds for browse surfaces");
        return postFeedRepository.findVisibleFeed(pageable).map(postFeedMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostFeedDTO> searchPublicFeed(String query, Pageable pageable) {
        LOG.debug("Request to search public PostFeeds for query: {}", query);
        return postFeedRepository.findByPostContentContainingIgnoreCaseAndIsDeletedFalse(query, pageable).map(postFeedMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostFeedDTO> findOne(Long id) {
        LOG.debug("Request to get PostFeed : {}", id);
        return postFeedRepository.findById(id).map(postFeedMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PostFeed : {}", id);
        postFeedRepository.deleteById(id);
    }
}
