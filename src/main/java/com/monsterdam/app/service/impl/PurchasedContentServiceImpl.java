package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.PurchasedContent;
import com.monsterdam.app.repository.PurchasedContentRepository;
import com.monsterdam.app.service.PurchasedContentService;
import com.monsterdam.app.service.dto.PurchasedContentDTO;
import com.monsterdam.app.service.mapper.PurchasedContentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.PurchasedContent}.
 */
@Service
@Transactional
public class PurchasedContentServiceImpl implements PurchasedContentService {

    private static final Logger LOG = LoggerFactory.getLogger(PurchasedContentServiceImpl.class);

    private final PurchasedContentRepository purchasedContentRepository;

    private final PurchasedContentMapper purchasedContentMapper;

    public PurchasedContentServiceImpl(
        PurchasedContentRepository purchasedContentRepository,
        PurchasedContentMapper purchasedContentMapper
    ) {
        this.purchasedContentRepository = purchasedContentRepository;
        this.purchasedContentMapper = purchasedContentMapper;
    }

    @Override
    public PurchasedContentDTO save(PurchasedContentDTO purchasedContentDTO) {
        LOG.debug("Request to save PurchasedContent : {}", purchasedContentDTO);
        PurchasedContent purchasedContent = purchasedContentMapper.toEntity(purchasedContentDTO);
        purchasedContent = purchasedContentRepository.save(purchasedContent);
        return purchasedContentMapper.toDto(purchasedContent);
    }

    @Override
    public PurchasedContentDTO update(PurchasedContentDTO purchasedContentDTO) {
        LOG.debug("Request to update PurchasedContent : {}", purchasedContentDTO);
        PurchasedContent purchasedContent = purchasedContentMapper.toEntity(purchasedContentDTO);
        purchasedContent = purchasedContentRepository.save(purchasedContent);
        return purchasedContentMapper.toDto(purchasedContent);
    }

    @Override
    public Optional<PurchasedContentDTO> partialUpdate(PurchasedContentDTO purchasedContentDTO) {
        LOG.debug("Request to partially update PurchasedContent : {}", purchasedContentDTO);

        return purchasedContentRepository
            .findById(purchasedContentDTO.getId())
            .map(existingPurchasedContent -> {
                purchasedContentMapper.partialUpdate(existingPurchasedContent, purchasedContentDTO);

                return existingPurchasedContent;
            })
            .map(purchasedContentRepository::save)
            .map(purchasedContentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchasedContentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PurchasedContents");
        return purchasedContentRepository.findAll(pageable).map(purchasedContentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchasedContentDTO> findOne(Long id) {
        LOG.debug("Request to get PurchasedContent : {}", id);
        return purchasedContentRepository.findById(id).map(purchasedContentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PurchasedContent : {}", id);
        purchasedContentRepository.deleteById(id);
    }
}
