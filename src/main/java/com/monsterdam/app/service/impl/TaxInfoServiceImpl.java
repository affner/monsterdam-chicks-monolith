package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.TaxInfo;
import com.monsterdam.app.repository.TaxInfoRepository;
import com.monsterdam.app.service.TaxInfoService;
import com.monsterdam.app.service.dto.TaxInfoDTO;
import com.monsterdam.app.service.mapper.TaxInfoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.TaxInfo}.
 */
@Service
@Transactional
public class TaxInfoServiceImpl implements TaxInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(TaxInfoServiceImpl.class);

    private final TaxInfoRepository taxInfoRepository;

    private final TaxInfoMapper taxInfoMapper;

    public TaxInfoServiceImpl(TaxInfoRepository taxInfoRepository, TaxInfoMapper taxInfoMapper) {
        this.taxInfoRepository = taxInfoRepository;
        this.taxInfoMapper = taxInfoMapper;
    }

    @Override
    public TaxInfoDTO save(TaxInfoDTO taxInfoDTO) {
        LOG.debug("Request to save TaxInfo : {}", taxInfoDTO);
        TaxInfo taxInfo = taxInfoMapper.toEntity(taxInfoDTO);
        taxInfo = taxInfoRepository.save(taxInfo);
        return taxInfoMapper.toDto(taxInfo);
    }

    @Override
    public TaxInfoDTO update(TaxInfoDTO taxInfoDTO) {
        LOG.debug("Request to update TaxInfo : {}", taxInfoDTO);
        TaxInfo taxInfo = taxInfoMapper.toEntity(taxInfoDTO);
        taxInfo = taxInfoRepository.save(taxInfo);
        return taxInfoMapper.toDto(taxInfo);
    }

    @Override
    public Optional<TaxInfoDTO> partialUpdate(TaxInfoDTO taxInfoDTO) {
        LOG.debug("Request to partially update TaxInfo : {}", taxInfoDTO);

        return taxInfoRepository
            .findById(taxInfoDTO.getId())
            .map(existingTaxInfo -> {
                taxInfoMapper.partialUpdate(existingTaxInfo, taxInfoDTO);

                return existingTaxInfo;
            })
            .map(taxInfoRepository::save)
            .map(taxInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaxInfoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TaxInfos");
        return taxInfoRepository.findAll(pageable).map(taxInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaxInfoDTO> findOne(Long id) {
        LOG.debug("Request to get TaxInfo : {}", id);
        return taxInfoRepository.findById(id).map(taxInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TaxInfo : {}", id);
        taxInfoRepository.deleteById(id);
    }
}
