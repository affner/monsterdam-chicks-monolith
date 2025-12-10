package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.PostPoll;
import com.monsterdam.app.repository.PostPollRepository;
import com.monsterdam.app.service.PostPollService;
import com.monsterdam.app.service.dto.PostPollDTO;
import com.monsterdam.app.service.mapper.PostPollMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.PostPoll}.
 */
@Service
@Transactional
public class PostPollServiceImpl implements PostPollService {

    private static final Logger LOG = LoggerFactory.getLogger(PostPollServiceImpl.class);

    private final PostPollRepository postPollRepository;

    private final PostPollMapper postPollMapper;

    public PostPollServiceImpl(PostPollRepository postPollRepository, PostPollMapper postPollMapper) {
        this.postPollRepository = postPollRepository;
        this.postPollMapper = postPollMapper;
    }

    @Override
    public PostPollDTO save(PostPollDTO postPollDTO) {
        LOG.debug("Request to save PostPoll : {}", postPollDTO);
        PostPoll postPoll = postPollMapper.toEntity(postPollDTO);
        postPoll = postPollRepository.save(postPoll);
        return postPollMapper.toDto(postPoll);
    }

    @Override
    public PostPollDTO update(PostPollDTO postPollDTO) {
        LOG.debug("Request to update PostPoll : {}", postPollDTO);
        PostPoll postPoll = postPollMapper.toEntity(postPollDTO);
        postPoll = postPollRepository.save(postPoll);
        return postPollMapper.toDto(postPoll);
    }

    @Override
    public Optional<PostPollDTO> partialUpdate(PostPollDTO postPollDTO) {
        LOG.debug("Request to partially update PostPoll : {}", postPollDTO);

        return postPollRepository
            .findById(postPollDTO.getId())
            .map(existingPostPoll -> {
                postPollMapper.partialUpdate(existingPostPoll, postPollDTO);

                return existingPostPoll;
            })
            .map(postPollRepository::save)
            .map(postPollMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostPollDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PostPolls");
        return postPollRepository.findAll(pageable).map(postPollMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostPollDTO> findOne(Long id) {
        LOG.debug("Request to get PostPoll : {}", id);
        return postPollRepository.findById(id).map(postPollMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PostPoll : {}", id);
        postPollRepository.deleteById(id);
    }
}
