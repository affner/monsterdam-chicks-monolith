package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.PaymentProviderEvent;
import com.monsterdam.app.repository.PaymentProviderEventRepository;
import com.monsterdam.app.service.PaymentProviderEventService;
import com.monsterdam.app.service.dto.PaymentProviderEventDTO;
import com.monsterdam.app.service.mapper.PaymentProviderEventMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.PaymentProviderEvent}.
 */
@Service
@Transactional
public class PaymentProviderEventServiceImpl
    extends AbstractLogicalDeletionService<PaymentProviderEvent, PaymentProviderEventDTO>
    implements PaymentProviderEventService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentProviderEventServiceImpl.class);

    private final PaymentProviderEventRepository paymentProviderEventRepository;

    private final PaymentProviderEventMapper paymentProviderEventMapper;

    public PaymentProviderEventServiceImpl(
        PaymentProviderEventRepository paymentProviderEventRepository,
        PaymentProviderEventMapper paymentProviderEventMapper
    ) {
        super(paymentProviderEventRepository, paymentProviderEventMapper, PaymentProviderEvent::setDeletedDate);
        this.paymentProviderEventRepository = paymentProviderEventRepository;
        this.paymentProviderEventMapper = paymentProviderEventMapper;
    }

    @Override
    public PaymentProviderEventDTO save(PaymentProviderEventDTO paymentProviderEventDTO) {
        LOG.debug("Request to save PaymentProviderEvent : {}", paymentProviderEventDTO);
        PaymentProviderEvent paymentProviderEvent = paymentProviderEventMapper.toEntity(paymentProviderEventDTO);
        paymentProviderEvent = paymentProviderEventRepository.save(paymentProviderEvent);
        return paymentProviderEventMapper.toDto(paymentProviderEvent);
    }

    @Override
    public PaymentProviderEventDTO update(PaymentProviderEventDTO paymentProviderEventDTO) {
        LOG.debug("Request to update PaymentProviderEvent : {}", paymentProviderEventDTO);
        PaymentProviderEvent paymentProviderEvent = paymentProviderEventMapper.toEntity(paymentProviderEventDTO);
        paymentProviderEvent = paymentProviderEventRepository.save(paymentProviderEvent);
        return paymentProviderEventMapper.toDto(paymentProviderEvent);
    }

    @Override
    public Optional<PaymentProviderEventDTO> partialUpdate(PaymentProviderEventDTO paymentProviderEventDTO) {
        LOG.debug("Request to partially update PaymentProviderEvent : {}", paymentProviderEventDTO);

        return paymentProviderEventRepository
            .findById(paymentProviderEventDTO.getId())
            .map(existingPaymentProviderEvent -> {
                paymentProviderEventMapper.partialUpdate(existingPaymentProviderEvent, paymentProviderEventDTO);

                return existingPaymentProviderEvent;
            })
            .map(paymentProviderEventRepository::save)
            .map(paymentProviderEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentProviderEventDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PaymentProviderEvents");
        return paymentProviderEventRepository.findAll(pageable).map(paymentProviderEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentProviderEventDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentProviderEvent : {}", id);
        return paymentProviderEventRepository.findById(id).map(paymentProviderEventMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentProviderEvent : {}", id);
        paymentProviderEventRepository.deleteById(id);
    }
}
