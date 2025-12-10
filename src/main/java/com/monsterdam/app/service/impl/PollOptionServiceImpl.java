package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.PollOption;
import com.monsterdam.app.repository.PollOptionRepository;
import com.monsterdam.app.service.PollOptionService;
import com.monsterdam.app.service.dto.PollOptionDTO;
import com.monsterdam.app.service.mapper.PollOptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.PollOption}.
 */
@Service
@Transactional
public class PollOptionServiceImpl implements PollOptionService {

    private static final Logger LOG = LoggerFactory.getLogger(PollOptionServiceImpl.class);

    private final PollOptionRepository pollOptionRepository;

    private final PollOptionMapper pollOptionMapper;

    public PollOptionServiceImpl(PollOptionRepository pollOptionRepository, PollOptionMapper pollOptionMapper) {
        this.pollOptionRepository = pollOptionRepository;
        this.pollOptionMapper = pollOptionMapper;
    }

    @Override
    public PollOptionDTO save(PollOptionDTO pollOptionDTO) {
        LOG.debug("Request to save PollOption : {}", pollOptionDTO);
        PollOption pollOption = pollOptionMapper.toEntity(pollOptionDTO);
        pollOption = pollOptionRepository.save(pollOption);
        return pollOptionMapper.toDto(pollOption);
    }

    @Override
    public PollOptionDTO update(PollOptionDTO pollOptionDTO) {
        LOG.debug("Request to update PollOption : {}", pollOptionDTO);
        PollOption pollOption = pollOptionMapper.toEntity(pollOptionDTO);
        pollOption = pollOptionRepository.save(pollOption);
        return pollOptionMapper.toDto(pollOption);
    }

    @Override
    public Optional<PollOptionDTO> partialUpdate(PollOptionDTO pollOptionDTO) {
        LOG.debug("Request to partially update PollOption : {}", pollOptionDTO);

        return pollOptionRepository
            .findById(pollOptionDTO.getId())
            .map(existingPollOption -> {
                pollOptionMapper.partialUpdate(existingPollOption, pollOptionDTO);

                return existingPollOption;
            })
            .map(pollOptionRepository::save)
            .map(pollOptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PollOptionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PollOptions");
        return pollOptionRepository.findAll(pageable).map(pollOptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PollOptionDTO> findOne(Long id) {
        LOG.debug("Request to get PollOption : {}", id);
        return pollOptionRepository.findById(id).map(pollOptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PollOption : {}", id);
        pollOptionRepository.deleteById(id);
    }
}
