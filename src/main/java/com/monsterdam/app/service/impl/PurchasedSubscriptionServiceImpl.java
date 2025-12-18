package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.PurchasedSubscription;
import com.monsterdam.app.repository.PurchasedSubscriptionRepository;
import com.monsterdam.app.service.PurchasedSubscriptionService;
import com.monsterdam.app.service.dto.PurchasedSubscriptionDTO;
import com.monsterdam.app.service.mapper.PurchasedSubscriptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.PurchasedSubscription}.
 */
@Service
@Transactional
public class PurchasedSubscriptionServiceImpl
    extends AbstractLogicalDeletionService<PurchasedSubscription, PurchasedSubscriptionDTO>
    implements PurchasedSubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(PurchasedSubscriptionServiceImpl.class);

    private final PurchasedSubscriptionRepository purchasedSubscriptionRepository;

    private final PurchasedSubscriptionMapper purchasedSubscriptionMapper;

    public PurchasedSubscriptionServiceImpl(
        PurchasedSubscriptionRepository purchasedSubscriptionRepository,
        PurchasedSubscriptionMapper purchasedSubscriptionMapper
    ) {
        super(purchasedSubscriptionRepository, purchasedSubscriptionMapper, PurchasedSubscription::setDeletedDate);
        this.purchasedSubscriptionRepository = purchasedSubscriptionRepository;
        this.purchasedSubscriptionMapper = purchasedSubscriptionMapper;
    }

    @Override
    public PurchasedSubscriptionDTO save(PurchasedSubscriptionDTO purchasedSubscriptionDTO) {
        LOG.debug("Request to save PurchasedSubscription : {}", purchasedSubscriptionDTO);
        PurchasedSubscription purchasedSubscription = purchasedSubscriptionMapper.toEntity(purchasedSubscriptionDTO);
        purchasedSubscription = purchasedSubscriptionRepository.save(purchasedSubscription);
        return purchasedSubscriptionMapper.toDto(purchasedSubscription);
    }

    @Override
    public PurchasedSubscriptionDTO update(PurchasedSubscriptionDTO purchasedSubscriptionDTO) {
        LOG.debug("Request to update PurchasedSubscription : {}", purchasedSubscriptionDTO);
        PurchasedSubscription purchasedSubscription = purchasedSubscriptionMapper.toEntity(purchasedSubscriptionDTO);
        purchasedSubscription = purchasedSubscriptionRepository.save(purchasedSubscription);
        return purchasedSubscriptionMapper.toDto(purchasedSubscription);
    }

    @Override
    public Optional<PurchasedSubscriptionDTO> partialUpdate(PurchasedSubscriptionDTO purchasedSubscriptionDTO) {
        LOG.debug("Request to partially update PurchasedSubscription : {}", purchasedSubscriptionDTO);

        return purchasedSubscriptionRepository
            .findById(purchasedSubscriptionDTO.getId())
            .map(existingPurchasedSubscription -> {
                purchasedSubscriptionMapper.partialUpdate(existingPurchasedSubscription, purchasedSubscriptionDTO);

                return existingPurchasedSubscription;
            })
            .map(purchasedSubscriptionRepository::save)
            .map(purchasedSubscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchasedSubscriptionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PurchasedSubscriptions");
        return purchasedSubscriptionRepository.findAll(pageable).map(purchasedSubscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchasedSubscriptionDTO> findOne(Long id) {
        LOG.debug("Request to get PurchasedSubscription : {}", id);
        return purchasedSubscriptionRepository.findById(id).map(purchasedSubscriptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PurchasedSubscription : {}", id);
        purchasedSubscriptionRepository.deleteById(id);
    }
}
