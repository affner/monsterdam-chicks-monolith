package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.MoneyGift;
import com.monsterdam.app.repository.MoneyGiftRepository;
import com.monsterdam.app.service.MoneyGiftService;
import com.monsterdam.app.service.dto.MoneyGiftDTO;
import com.monsterdam.app.service.mapper.MoneyGiftMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.MoneyGift}.
 */
@Service
@Transactional
public class MoneyGiftServiceImpl implements MoneyGiftService {

    private static final Logger LOG = LoggerFactory.getLogger(MoneyGiftServiceImpl.class);

    private final MoneyGiftRepository moneyGiftRepository;

    private final MoneyGiftMapper moneyGiftMapper;

    public MoneyGiftServiceImpl(MoneyGiftRepository moneyGiftRepository, MoneyGiftMapper moneyGiftMapper) {
        this.moneyGiftRepository = moneyGiftRepository;
        this.moneyGiftMapper = moneyGiftMapper;
    }

    @Override
    public MoneyGiftDTO save(MoneyGiftDTO moneyGiftDTO) {
        LOG.debug("Request to save MoneyGift : {}", moneyGiftDTO);
        MoneyGift moneyGift = moneyGiftMapper.toEntity(moneyGiftDTO);
        moneyGift = moneyGiftRepository.save(moneyGift);
        return moneyGiftMapper.toDto(moneyGift);
    }

    @Override
    public MoneyGiftDTO update(MoneyGiftDTO moneyGiftDTO) {
        LOG.debug("Request to update MoneyGift : {}", moneyGiftDTO);
        MoneyGift moneyGift = moneyGiftMapper.toEntity(moneyGiftDTO);
        moneyGift = moneyGiftRepository.save(moneyGift);
        return moneyGiftMapper.toDto(moneyGift);
    }

    @Override
    public Optional<MoneyGiftDTO> partialUpdate(MoneyGiftDTO moneyGiftDTO) {
        LOG.debug("Request to partially update MoneyGift : {}", moneyGiftDTO);

        return moneyGiftRepository
            .findById(moneyGiftDTO.getId())
            .map(existingMoneyGift -> {
                moneyGiftMapper.partialUpdate(existingMoneyGift, moneyGiftDTO);

                return existingMoneyGift;
            })
            .map(moneyGiftRepository::save)
            .map(moneyGiftMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoneyGiftDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MoneyGifts");
        return moneyGiftRepository.findAll(pageable).map(moneyGiftMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoneyGiftDTO> findOne(Long id) {
        LOG.debug("Request to get MoneyGift : {}", id);
        return moneyGiftRepository.findById(id).map(moneyGiftMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MoneyGift : {}", id);
        moneyGiftRepository.deleteById(id);
    }
}
