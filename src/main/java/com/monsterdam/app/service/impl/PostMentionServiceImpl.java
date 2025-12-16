package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.PostMention;
import com.monsterdam.app.repository.PostMentionRepository;
import com.monsterdam.app.service.PostMentionService;
import com.monsterdam.app.service.dto.PostMentionDTO;
import com.monsterdam.app.service.mapper.PostMentionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.PostMention}.
 */
@Service
@Transactional
public class PostMentionServiceImpl implements PostMentionService {

    private static final Logger LOG = LoggerFactory.getLogger(PostMentionServiceImpl.class);

    private final PostMentionRepository postMentionRepository;

    private final PostMentionMapper postMentionMapper;

    public PostMentionServiceImpl(PostMentionRepository postMentionRepository, PostMentionMapper postMentionMapper) {
        this.postMentionRepository = postMentionRepository;
        this.postMentionMapper = postMentionMapper;
    }

    @Override
    public PostMentionDTO save(PostMentionDTO postMentionDTO) {
        LOG.debug("Request to save PostMention : {}", postMentionDTO);
        PostMention postMention = postMentionMapper.toEntity(postMentionDTO);
        postMention = postMentionRepository.save(postMention);
        return postMentionMapper.toDto(postMention);
    }

    @Override
    public PostMentionDTO update(PostMentionDTO postMentionDTO) {
        LOG.debug("Request to update PostMention : {}", postMentionDTO);
        PostMention postMention = postMentionMapper.toEntity(postMentionDTO);
        postMention = postMentionRepository.save(postMention);
        return postMentionMapper.toDto(postMention);
    }

    @Override
    public Optional<PostMentionDTO> partialUpdate(PostMentionDTO postMentionDTO) {
        LOG.debug("Request to partially update PostMention : {}", postMentionDTO);

        return postMentionRepository
            .findById(postMentionDTO.getId())
            .map(existingPostMention -> {
                postMentionMapper.partialUpdate(existingPostMention, postMentionDTO);

                return existingPostMention;
            })
            .map(postMentionRepository::save)
            .map(postMentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostMentionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PostMentions");
        return postMentionRepository.findAll(pageable).map(postMentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostMentionDTO> findOne(Long id) {
        LOG.debug("Request to get PostMention : {}", id);
        return postMentionRepository.findById(id).map(postMentionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PostMention : {}", id);
        postMentionRepository.deleteById(id);
    }
}
