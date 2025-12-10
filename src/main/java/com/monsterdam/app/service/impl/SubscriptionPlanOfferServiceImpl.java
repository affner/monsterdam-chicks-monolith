package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.SubscriptionPlanOffer;
import com.monsterdam.app.repository.SubscriptionPlanOfferRepository;
import com.monsterdam.app.service.SubscriptionPlanOfferService;
import com.monsterdam.app.service.dto.SubscriptionPlanOfferDTO;
import com.monsterdam.app.service.mapper.SubscriptionPlanOfferMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.SubscriptionPlanOffer}.
 */
@Service
@Transactional
public class SubscriptionPlanOfferServiceImpl implements SubscriptionPlanOfferService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionPlanOfferServiceImpl.class);

    private final SubscriptionPlanOfferRepository subscriptionPlanOfferRepository;

    private final SubscriptionPlanOfferMapper subscriptionPlanOfferMapper;

    public SubscriptionPlanOfferServiceImpl(
        SubscriptionPlanOfferRepository subscriptionPlanOfferRepository,
        SubscriptionPlanOfferMapper subscriptionPlanOfferMapper
    ) {
        this.subscriptionPlanOfferRepository = subscriptionPlanOfferRepository;
        this.subscriptionPlanOfferMapper = subscriptionPlanOfferMapper;
    }

    @Override
    public SubscriptionPlanOfferDTO save(SubscriptionPlanOfferDTO subscriptionPlanOfferDTO) {
        LOG.debug("Request to save SubscriptionPlanOffer : {}", subscriptionPlanOfferDTO);
        SubscriptionPlanOffer subscriptionPlanOffer = subscriptionPlanOfferMapper.toEntity(subscriptionPlanOfferDTO);
        subscriptionPlanOffer = subscriptionPlanOfferRepository.save(subscriptionPlanOffer);
        return subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);
    }

    @Override
    public SubscriptionPlanOfferDTO update(SubscriptionPlanOfferDTO subscriptionPlanOfferDTO) {
        LOG.debug("Request to update SubscriptionPlanOffer : {}", subscriptionPlanOfferDTO);
        SubscriptionPlanOffer subscriptionPlanOffer = subscriptionPlanOfferMapper.toEntity(subscriptionPlanOfferDTO);
        subscriptionPlanOffer = subscriptionPlanOfferRepository.save(subscriptionPlanOffer);
        return subscriptionPlanOfferMapper.toDto(subscriptionPlanOffer);
    }

    @Override
    public Optional<SubscriptionPlanOfferDTO> partialUpdate(SubscriptionPlanOfferDTO subscriptionPlanOfferDTO) {
        LOG.debug("Request to partially update SubscriptionPlanOffer : {}", subscriptionPlanOfferDTO);

        return subscriptionPlanOfferRepository
            .findById(subscriptionPlanOfferDTO.getId())
            .map(existingSubscriptionPlanOffer -> {
                subscriptionPlanOfferMapper.partialUpdate(existingSubscriptionPlanOffer, subscriptionPlanOfferDTO);

                return existingSubscriptionPlanOffer;
            })
            .map(subscriptionPlanOfferRepository::save)
            .map(subscriptionPlanOfferMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscriptionPlanOfferDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SubscriptionPlanOffers");
        return subscriptionPlanOfferRepository.findAll(pageable).map(subscriptionPlanOfferMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionPlanOfferDTO> findOne(Long id) {
        LOG.debug("Request to get SubscriptionPlanOffer : {}", id);
        return subscriptionPlanOfferRepository.findById(id).map(subscriptionPlanOfferMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SubscriptionPlanOffer : {}", id);
        subscriptionPlanOfferRepository.deleteById(id);
    }
}
