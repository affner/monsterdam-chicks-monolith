package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.SubscriptionBundle;
import com.monsterdam.app.repository.SubscriptionBundleRepository;
import com.monsterdam.app.service.SubscriptionBundleService;
import com.monsterdam.app.service.dto.SubscriptionBundleDTO;
import com.monsterdam.app.service.mapper.SubscriptionBundleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.SubscriptionBundle}.
 */
@Service
@Transactional
public class SubscriptionBundleServiceImpl
    extends AbstractLogicalDeletionService<SubscriptionBundle, SubscriptionBundleDTO>
    implements SubscriptionBundleService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionBundleServiceImpl.class);

    private final SubscriptionBundleRepository subscriptionBundleRepository;

    private final SubscriptionBundleMapper subscriptionBundleMapper;

    public SubscriptionBundleServiceImpl(
        SubscriptionBundleRepository subscriptionBundleRepository,
        SubscriptionBundleMapper subscriptionBundleMapper
    ) {
        super(subscriptionBundleRepository, subscriptionBundleMapper, SubscriptionBundle::setDeletedDate);
        this.subscriptionBundleRepository = subscriptionBundleRepository;
        this.subscriptionBundleMapper = subscriptionBundleMapper;
    }

    @Override
    public SubscriptionBundleDTO save(SubscriptionBundleDTO subscriptionBundleDTO) {
        LOG.debug("Request to save SubscriptionBundle : {}", subscriptionBundleDTO);
        SubscriptionBundle subscriptionBundle = subscriptionBundleMapper.toEntity(subscriptionBundleDTO);
        subscriptionBundle = subscriptionBundleRepository.save(subscriptionBundle);
        return subscriptionBundleMapper.toDto(subscriptionBundle);
    }

    @Override
    public SubscriptionBundleDTO update(SubscriptionBundleDTO subscriptionBundleDTO) {
        LOG.debug("Request to update SubscriptionBundle : {}", subscriptionBundleDTO);
        SubscriptionBundle subscriptionBundle = subscriptionBundleMapper.toEntity(subscriptionBundleDTO);
        subscriptionBundle = subscriptionBundleRepository.save(subscriptionBundle);
        return subscriptionBundleMapper.toDto(subscriptionBundle);
    }

    @Override
    public Optional<SubscriptionBundleDTO> partialUpdate(SubscriptionBundleDTO subscriptionBundleDTO) {
        LOG.debug("Request to partially update SubscriptionBundle : {}", subscriptionBundleDTO);

        return subscriptionBundleRepository
            .findById(subscriptionBundleDTO.getId())
            .map(existingSubscriptionBundle -> {
                subscriptionBundleMapper.partialUpdate(existingSubscriptionBundle, subscriptionBundleDTO);

                return existingSubscriptionBundle;
            })
            .map(subscriptionBundleRepository::save)
            .map(subscriptionBundleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscriptionBundleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SubscriptionBundles");
        return subscriptionBundleRepository.findAll(pageable).map(subscriptionBundleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionBundleDTO> findOne(Long id) {
        LOG.debug("Request to get SubscriptionBundle : {}", id);
        return subscriptionBundleRepository.findById(id).map(subscriptionBundleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SubscriptionBundle : {}", id);
        subscriptionBundleRepository.deleteById(id);
    }
}
