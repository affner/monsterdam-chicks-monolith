package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.PostComment;
import com.monsterdam.app.repository.PostCommentRepository;
import com.monsterdam.app.service.PostCommentService;
import com.monsterdam.app.service.dto.PostCommentDTO;
import com.monsterdam.app.service.mapper.PostCommentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.PostComment}.
 */
@Service
@Transactional
public class PostCommentServiceImpl extends AbstractLogicalDeletionService<PostComment, PostCommentDTO> implements PostCommentService {

    private static final Logger LOG = LoggerFactory.getLogger(PostCommentServiceImpl.class);

    private final PostCommentRepository postCommentRepository;

    private final PostCommentMapper postCommentMapper;

    public PostCommentServiceImpl(PostCommentRepository postCommentRepository, PostCommentMapper postCommentMapper) {
        super(postCommentRepository, postCommentMapper, PostComment::setDeletedDate);
        this.postCommentRepository = postCommentRepository;
        this.postCommentMapper = postCommentMapper;
    }

    @Override
    public PostCommentDTO save(PostCommentDTO postCommentDTO) {
        LOG.debug("Request to save PostComment : {}", postCommentDTO);
        PostComment postComment = postCommentMapper.toEntity(postCommentDTO);
        postComment = postCommentRepository.save(postComment);
        return postCommentMapper.toDto(postComment);
    }

    @Override
    public PostCommentDTO update(PostCommentDTO postCommentDTO) {
        LOG.debug("Request to update PostComment : {}", postCommentDTO);
        PostComment postComment = postCommentMapper.toEntity(postCommentDTO);
        postComment = postCommentRepository.save(postComment);
        return postCommentMapper.toDto(postComment);
    }

    @Override
    public Optional<PostCommentDTO> partialUpdate(PostCommentDTO postCommentDTO) {
        LOG.debug("Request to partially update PostComment : {}", postCommentDTO);

        return postCommentRepository
            .findById(postCommentDTO.getId())
            .map(existingPostComment -> {
                postCommentMapper.partialUpdate(existingPostComment, postCommentDTO);

                return existingPostComment;
            })
            .map(postCommentRepository::save)
            .map(postCommentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostCommentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PostComments");
        return postCommentRepository.findAll(pageable).map(postCommentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostCommentDTO> findOne(Long id) {
        LOG.debug("Request to get PostComment : {}", id);
        return postCommentRepository.findById(id).map(postCommentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PostComment : {}", id);
        postCommentRepository.deleteById(id);
    }
}
