package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.MoneyWithdraw;
import com.monsterdam.app.repository.MoneyWithdrawRepository;
import com.monsterdam.app.service.MoneyWithdrawService;
import com.monsterdam.app.service.dto.MoneyWithdrawDTO;
import com.monsterdam.app.service.mapper.MoneyWithdrawMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.MoneyWithdraw}.
 */
@Service
@Transactional
public class MoneyWithdrawServiceImpl implements MoneyWithdrawService {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyWithdrawServiceImpl.class);

    private final MoneyWithdrawRepository moneyWithdrawRepository;

    private final MoneyWithdrawMapper moneyWithdrawMapper;

    public MoneyWithdrawServiceImpl(MoneyWithdrawRepository moneyWithdrawRepository, MoneyWithdrawMapper moneyWithdrawMapper) {
        this.moneyWithdrawRepository = moneyWithdrawRepository;
        this.moneyWithdrawMapper = moneyWithdrawMapper;
    }

    @Override
    public MoneyWithdrawDTO save(MoneyWithdrawDTO moneyWithdrawDTO) {
        LOG.debug("Request to save MoneyWithdraw : {}", moneyWithdrawDTO);
        MoneyWithdraw moneyWithdraw = moneyWithdrawMapper.toEntity(moneyWithdrawDTO);
        moneyWithdraw = moneyWithdrawRepository.save(moneyWithdraw);
        return moneyWithdrawMapper.toDto(moneyWithdraw);
    }

    @Override
    public MoneyWithdrawDTO update(MoneyWithdrawDTO moneyWithdrawDTO) {
        LOG.debug("Request to update MoneyWithdraw : {}", moneyWithdrawDTO);
        MoneyWithdraw moneyWithdraw = moneyWithdrawMapper.toEntity(moneyWithdrawDTO);
        moneyWithdraw = moneyWithdrawRepository.save(moneyWithdraw);
        return moneyWithdrawMapper.toDto(moneyWithdraw);
    }

    @Override
    public Optional<MoneyWithdrawDTO> partialUpdate(MoneyWithdrawDTO moneyWithdrawDTO) {
        LOG.debug("Request to partially update MoneyWithdraw : {}", moneyWithdrawDTO);

        return moneyWithdrawRepository
            .findById(moneyWithdrawDTO.getId())
            .map(existingMoneyWithdraw -> {
                moneyWithdrawMapper.partialUpdate(existingMoneyWithdraw, moneyWithdrawDTO);

                return existingMoneyWithdraw;
            })
            .map(moneyWithdrawRepository::save)
            .map(moneyWithdrawMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoneyWithdrawDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MoneyWithdraws");
        return moneyWithdrawRepository.findAll(pageable).map(moneyWithdrawMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoneyWithdrawDTO> findOne(Long id) {
        LOG.debug("Request to get MoneyWithdraw : {}", id);
        return moneyWithdrawRepository.findById(id).map(moneyWithdrawMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MoneyWithdraw : {}", id);
        moneyWithdrawRepository.deleteById(id);
    }
}
