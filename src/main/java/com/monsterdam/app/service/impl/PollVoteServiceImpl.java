package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.PollVote;
import com.monsterdam.app.repository.PollVoteRepository;
import com.monsterdam.app.service.PollVoteService;
import com.monsterdam.app.service.dto.PollVoteDTO;
import com.monsterdam.app.service.mapper.PollVoteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.PollVote}.
 */
@Service
@Transactional
public class PollVoteServiceImpl implements PollVoteService {

    private static final Logger LOG = LoggerFactory.getLogger(PollVoteServiceImpl.class);

    private final PollVoteRepository pollVoteRepository;

    private final PollVoteMapper pollVoteMapper;

    public PollVoteServiceImpl(PollVoteRepository pollVoteRepository, PollVoteMapper pollVoteMapper) {
        this.pollVoteRepository = pollVoteRepository;
        this.pollVoteMapper = pollVoteMapper;
    }

    @Override
    public PollVoteDTO save(PollVoteDTO pollVoteDTO) {
        LOG.debug("Request to save PollVote : {}", pollVoteDTO);
        PollVote pollVote = pollVoteMapper.toEntity(pollVoteDTO);
        pollVote = pollVoteRepository.save(pollVote);
        return pollVoteMapper.toDto(pollVote);
    }

    @Override
    public PollVoteDTO update(PollVoteDTO pollVoteDTO) {
        LOG.debug("Request to update PollVote : {}", pollVoteDTO);
        PollVote pollVote = pollVoteMapper.toEntity(pollVoteDTO);
        pollVote = pollVoteRepository.save(pollVote);
        return pollVoteMapper.toDto(pollVote);
    }

    @Override
    public Optional<PollVoteDTO> partialUpdate(PollVoteDTO pollVoteDTO) {
        LOG.debug("Request to partially update PollVote : {}", pollVoteDTO);

        return pollVoteRepository
            .findById(pollVoteDTO.getId())
            .map(existingPollVote -> {
                pollVoteMapper.partialUpdate(existingPollVote, pollVoteDTO);

                return existingPollVote;
            })
            .map(pollVoteRepository::save)
            .map(pollVoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PollVoteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PollVotes");
        return pollVoteRepository.findAll(pageable).map(pollVoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PollVoteDTO> findOne(Long id) {
        LOG.debug("Request to get PollVote : {}", id);
        return pollVoteRepository.findById(id).map(pollVoteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PollVote : {}", id);
        pollVoteRepository.deleteById(id);
    }
}
