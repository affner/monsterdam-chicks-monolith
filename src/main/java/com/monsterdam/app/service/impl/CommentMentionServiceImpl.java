package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.CommentMention;
import com.monsterdam.app.repository.CommentMentionRepository;
import com.monsterdam.app.service.CommentMentionService;
import com.monsterdam.app.service.dto.CommentMentionDTO;
import com.monsterdam.app.service.mapper.CommentMentionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.CommentMention}.
 */
@Service
@Transactional
public class CommentMentionServiceImpl implements CommentMentionService {

    private static final Logger LOG = LoggerFactory.getLogger(CommentMentionServiceImpl.class);

    private final CommentMentionRepository commentMentionRepository;

    private final CommentMentionMapper commentMentionMapper;

    public CommentMentionServiceImpl(CommentMentionRepository commentMentionRepository, CommentMentionMapper commentMentionMapper) {
        this.commentMentionRepository = commentMentionRepository;
        this.commentMentionMapper = commentMentionMapper;
    }

    @Override
    public CommentMentionDTO save(CommentMentionDTO commentMentionDTO) {
        LOG.debug("Request to save CommentMention : {}", commentMentionDTO);
        CommentMention commentMention = commentMentionMapper.toEntity(commentMentionDTO);
        commentMention = commentMentionRepository.save(commentMention);
        return commentMentionMapper.toDto(commentMention);
    }

    @Override
    public CommentMentionDTO update(CommentMentionDTO commentMentionDTO) {
        LOG.debug("Request to update CommentMention : {}", commentMentionDTO);
        CommentMention commentMention = commentMentionMapper.toEntity(commentMentionDTO);
        commentMention = commentMentionRepository.save(commentMention);
        return commentMentionMapper.toDto(commentMention);
    }

    @Override
    public Optional<CommentMentionDTO> partialUpdate(CommentMentionDTO commentMentionDTO) {
        LOG.debug("Request to partially update CommentMention : {}", commentMentionDTO);

        return commentMentionRepository
            .findById(commentMentionDTO.getId())
            .map(existingCommentMention -> {
                commentMentionMapper.partialUpdate(existingCommentMention, commentMentionDTO);

                return existingCommentMention;
            })
            .map(commentMentionRepository::save)
            .map(commentMentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentMentionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CommentMentions");
        return commentMentionRepository.findAll(pageable).map(commentMentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentMentionDTO> findOne(Long id) {
        LOG.debug("Request to get CommentMention : {}", id);
        return commentMentionRepository.findById(id).map(commentMentionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CommentMention : {}", id);
        commentMentionRepository.deleteById(id);
    }
}
