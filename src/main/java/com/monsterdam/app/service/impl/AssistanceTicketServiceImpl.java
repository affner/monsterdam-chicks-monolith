package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.AssistanceTicket;
import com.monsterdam.app.repository.AssistanceTicketRepository;
import com.monsterdam.app.service.AssistanceTicketService;
import com.monsterdam.app.service.dto.AssistanceTicketDTO;
import com.monsterdam.app.service.mapper.AssistanceTicketMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.AssistanceTicket}.
 */
@Service
@Transactional
public class AssistanceTicketServiceImpl implements AssistanceTicketService {

    private static final Logger LOG = LoggerFactory.getLogger(AssistanceTicketServiceImpl.class);

    private final AssistanceTicketRepository assistanceTicketRepository;

    private final AssistanceTicketMapper assistanceTicketMapper;

    public AssistanceTicketServiceImpl(
        AssistanceTicketRepository assistanceTicketRepository,
        AssistanceTicketMapper assistanceTicketMapper
    ) {
        this.assistanceTicketRepository = assistanceTicketRepository;
        this.assistanceTicketMapper = assistanceTicketMapper;
    }

    @Override
    public AssistanceTicketDTO save(AssistanceTicketDTO assistanceTicketDTO) {
        LOG.debug("Request to save AssistanceTicket : {}", assistanceTicketDTO);
        AssistanceTicket assistanceTicket = assistanceTicketMapper.toEntity(assistanceTicketDTO);
        assistanceTicket = assistanceTicketRepository.save(assistanceTicket);
        return assistanceTicketMapper.toDto(assistanceTicket);
    }

    @Override
    public AssistanceTicketDTO update(AssistanceTicketDTO assistanceTicketDTO) {
        LOG.debug("Request to update AssistanceTicket : {}", assistanceTicketDTO);
        AssistanceTicket assistanceTicket = assistanceTicketMapper.toEntity(assistanceTicketDTO);
        assistanceTicket = assistanceTicketRepository.save(assistanceTicket);
        return assistanceTicketMapper.toDto(assistanceTicket);
    }

    @Override
    public Optional<AssistanceTicketDTO> partialUpdate(AssistanceTicketDTO assistanceTicketDTO) {
        LOG.debug("Request to partially update AssistanceTicket : {}", assistanceTicketDTO);

        return assistanceTicketRepository
            .findById(assistanceTicketDTO.getId())
            .map(existingAssistanceTicket -> {
                assistanceTicketMapper.partialUpdate(existingAssistanceTicket, assistanceTicketDTO);

                return existingAssistanceTicket;
            })
            .map(assistanceTicketRepository::save)
            .map(assistanceTicketMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssistanceTicketDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AssistanceTickets");
        return assistanceTicketRepository.findAll(pageable).map(assistanceTicketMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssistanceTicketDTO> findOne(Long id) {
        LOG.debug("Request to get AssistanceTicket : {}", id);
        return assistanceTicketRepository.findById(id).map(assistanceTicketMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AssistanceTicket : {}", id);
        assistanceTicketRepository.deleteById(id);
    }
}
