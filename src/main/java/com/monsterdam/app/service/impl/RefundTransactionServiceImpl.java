package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.RefundTransaction;
import com.monsterdam.app.repository.RefundTransactionRepository;
import com.monsterdam.app.service.RefundTransactionService;
import com.monsterdam.app.service.dto.RefundTransactionDTO;
import com.monsterdam.app.service.mapper.RefundTransactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.RefundTransaction}.
 */
@Service
@Transactional
public class RefundTransactionServiceImpl
    extends AbstractLogicalDeletionService<RefundTransaction, RefundTransactionDTO>
    implements RefundTransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(RefundTransactionServiceImpl.class);

    private final RefundTransactionRepository refundTransactionRepository;

    private final RefundTransactionMapper refundTransactionMapper;

    public RefundTransactionServiceImpl(
        RefundTransactionRepository refundTransactionRepository,
        RefundTransactionMapper refundTransactionMapper
    ) {
        super(refundTransactionRepository, refundTransactionMapper, RefundTransaction::setDeletedDate);
        this.refundTransactionRepository = refundTransactionRepository;
        this.refundTransactionMapper = refundTransactionMapper;
    }

    @Override
    public RefundTransactionDTO save(RefundTransactionDTO refundTransactionDTO) {
        LOG.debug("Request to save RefundTransaction : {}", refundTransactionDTO);
        RefundTransaction refundTransaction = refundTransactionMapper.toEntity(refundTransactionDTO);
        refundTransaction = refundTransactionRepository.save(refundTransaction);
        return refundTransactionMapper.toDto(refundTransaction);
    }

    @Override
    public RefundTransactionDTO update(RefundTransactionDTO refundTransactionDTO) {
        LOG.debug("Request to update RefundTransaction : {}", refundTransactionDTO);
        RefundTransaction refundTransaction = refundTransactionMapper.toEntity(refundTransactionDTO);
        refundTransaction = refundTransactionRepository.save(refundTransaction);
        return refundTransactionMapper.toDto(refundTransaction);
    }

    @Override
    public Optional<RefundTransactionDTO> partialUpdate(RefundTransactionDTO refundTransactionDTO) {
        LOG.debug("Request to partially update RefundTransaction : {}", refundTransactionDTO);

        return refundTransactionRepository
            .findById(refundTransactionDTO.getId())
            .map(existingRefundTransaction -> {
                refundTransactionMapper.partialUpdate(existingRefundTransaction, refundTransactionDTO);

                return existingRefundTransaction;
            })
            .map(refundTransactionRepository::save)
            .map(refundTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RefundTransactionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RefundTransactions");
        return refundTransactionRepository.findAll(pageable).map(refundTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefundTransactionDTO> findOne(Long id) {
        LOG.debug("Request to get RefundTransaction : {}", id);
        return refundTransactionRepository.findById(id).map(refundTransactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete RefundTransaction : {}", id);
        refundTransactionRepository.deleteById(id);
    }
}
