package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.LedgerEntry;
import com.monsterdam.app.repository.LedgerEntryRepository;
import com.monsterdam.app.service.LedgerEntryService;
import com.monsterdam.app.service.dto.LedgerEntryDTO;
import com.monsterdam.app.service.mapper.LedgerEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.LedgerEntry}.
 */
@Service
@Transactional
public class LedgerEntryServiceImpl extends AbstractLogicalDeletionService<LedgerEntry, LedgerEntryDTO> implements LedgerEntryService {

    private static final Logger LOG = LoggerFactory.getLogger(LedgerEntryServiceImpl.class);

    private final LedgerEntryRepository ledgerEntryRepository;

    private final LedgerEntryMapper ledgerEntryMapper;

    public LedgerEntryServiceImpl(LedgerEntryRepository ledgerEntryRepository, LedgerEntryMapper ledgerEntryMapper) {
        super(ledgerEntryRepository, ledgerEntryMapper, LedgerEntry::setDeletedDate);
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.ledgerEntryMapper = ledgerEntryMapper;
    }

    @Override
    public LedgerEntryDTO save(LedgerEntryDTO ledgerEntryDTO) {
        LOG.debug("Request to save LedgerEntry : {}", ledgerEntryDTO);
        LedgerEntry ledgerEntry = ledgerEntryMapper.toEntity(ledgerEntryDTO);
        ledgerEntry = ledgerEntryRepository.save(ledgerEntry);
        return ledgerEntryMapper.toDto(ledgerEntry);
    }

    @Override
    public LedgerEntryDTO update(LedgerEntryDTO ledgerEntryDTO) {
        LOG.debug("Request to update LedgerEntry : {}", ledgerEntryDTO);
        LedgerEntry ledgerEntry = ledgerEntryMapper.toEntity(ledgerEntryDTO);
        ledgerEntry = ledgerEntryRepository.save(ledgerEntry);
        return ledgerEntryMapper.toDto(ledgerEntry);
    }

    @Override
    public Optional<LedgerEntryDTO> partialUpdate(LedgerEntryDTO ledgerEntryDTO) {
        LOG.debug("Request to partially update LedgerEntry : {}", ledgerEntryDTO);

        return ledgerEntryRepository
            .findById(ledgerEntryDTO.getId())
            .map(existingLedgerEntry -> {
                ledgerEntryMapper.partialUpdate(existingLedgerEntry, ledgerEntryDTO);

                return existingLedgerEntry;
            })
            .map(ledgerEntryRepository::save)
            .map(ledgerEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LedgerEntryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all LedgerEntries");
        return ledgerEntryRepository.findAll(pageable).map(ledgerEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LedgerEntryDTO> findOne(Long id) {
        LOG.debug("Request to get LedgerEntry : {}", id);
        return ledgerEntryRepository.findById(id).map(ledgerEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete LedgerEntry : {}", id);
        ledgerEntryRepository.deleteById(id);
    }
}
