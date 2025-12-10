package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.MoneyEarning;
import com.monsterdam.app.repository.MoneyEarningRepository;
import com.monsterdam.app.service.MoneyEarningService;
import com.monsterdam.app.service.dto.MoneyEarningDTO;
import com.monsterdam.app.service.mapper.MoneyEarningMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.MoneyEarning}.
 */
@Service
@Transactional
public class MoneyEarningServiceImpl implements MoneyEarningService {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyEarningServiceImpl.class);

    private final MoneyEarningRepository moneyEarningRepository;

    private final MoneyEarningMapper moneyEarningMapper;

    public MoneyEarningServiceImpl(MoneyEarningRepository moneyEarningRepository, MoneyEarningMapper moneyEarningMapper) {
        this.moneyEarningRepository = moneyEarningRepository;
        this.moneyEarningMapper = moneyEarningMapper;
    }

    @Override
    public MoneyEarningDTO save(MoneyEarningDTO moneyEarningDTO) {
        LOG.debug("Request to save MoneyEarning : {}", moneyEarningDTO);
        MoneyEarning moneyEarning = moneyEarningMapper.toEntity(moneyEarningDTO);
        moneyEarning = moneyEarningRepository.save(moneyEarning);
        return moneyEarningMapper.toDto(moneyEarning);
    }

    @Override
    public MoneyEarningDTO update(MoneyEarningDTO moneyEarningDTO) {
        LOG.debug("Request to update MoneyEarning : {}", moneyEarningDTO);
        MoneyEarning moneyEarning = moneyEarningMapper.toEntity(moneyEarningDTO);
        moneyEarning = moneyEarningRepository.save(moneyEarning);
        return moneyEarningMapper.toDto(moneyEarning);
    }

    @Override
    public Optional<MoneyEarningDTO> partialUpdate(MoneyEarningDTO moneyEarningDTO) {
        LOG.debug("Request to partially update MoneyEarning : {}", moneyEarningDTO);

        return moneyEarningRepository
            .findById(moneyEarningDTO.getId())
            .map(existingMoneyEarning -> {
                moneyEarningMapper.partialUpdate(existingMoneyEarning, moneyEarningDTO);

                return existingMoneyEarning;
            })
            .map(moneyEarningRepository::save)
            .map(moneyEarningMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoneyEarningDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MoneyEarnings");
        return moneyEarningRepository.findAll(pageable).map(moneyEarningMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoneyEarningDTO> findOne(Long id) {
        LOG.debug("Request to get MoneyEarning : {}", id);
        return moneyEarningRepository.findById(id).map(moneyEarningMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MoneyEarning : {}", id);
        moneyEarningRepository.deleteById(id);
    }
}
