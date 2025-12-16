package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.DirectMessage;
import com.monsterdam.app.repository.DirectMessageRepository;
import com.monsterdam.app.service.DirectMessageService;
import com.monsterdam.app.service.dto.DirectMessageDTO;
import com.monsterdam.app.service.mapper.DirectMessageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.DirectMessage}.
 */
@Service
@Transactional
public class DirectMessageServiceImpl implements DirectMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(DirectMessageServiceImpl.class);

    private final DirectMessageRepository directMessageRepository;

    private final DirectMessageMapper directMessageMapper;

    public DirectMessageServiceImpl(DirectMessageRepository directMessageRepository, DirectMessageMapper directMessageMapper) {
        this.directMessageRepository = directMessageRepository;
        this.directMessageMapper = directMessageMapper;
    }

    @Override
    public DirectMessageDTO save(DirectMessageDTO directMessageDTO) {
        LOG.debug("Request to save DirectMessage : {}", directMessageDTO);
        DirectMessage directMessage = directMessageMapper.toEntity(directMessageDTO);
        directMessage = directMessageRepository.save(directMessage);
        return directMessageMapper.toDto(directMessage);
    }

    @Override
    public DirectMessageDTO update(DirectMessageDTO directMessageDTO) {
        LOG.debug("Request to update DirectMessage : {}", directMessageDTO);
        DirectMessage directMessage = directMessageMapper.toEntity(directMessageDTO);
        directMessage = directMessageRepository.save(directMessage);
        return directMessageMapper.toDto(directMessage);
    }

    @Override
    public Optional<DirectMessageDTO> partialUpdate(DirectMessageDTO directMessageDTO) {
        LOG.debug("Request to partially update DirectMessage : {}", directMessageDTO);

        return directMessageRepository
            .findById(directMessageDTO.getId())
            .map(existingDirectMessage -> {
                directMessageMapper.partialUpdate(existingDirectMessage, directMessageDTO);

                return existingDirectMessage;
            })
            .map(directMessageRepository::save)
            .map(directMessageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DirectMessageDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DirectMessages");
        return directMessageRepository.findAll(pageable).map(directMessageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DirectMessageDTO> findOne(Long id) {
        LOG.debug("Request to get DirectMessage : {}", id);
        return directMessageRepository.findById(id).map(directMessageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DirectMessage : {}", id);
        directMessageRepository.deleteById(id);
    }
}
