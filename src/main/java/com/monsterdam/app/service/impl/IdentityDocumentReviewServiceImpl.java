package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.IdentityDocumentReview;
import com.monsterdam.app.repository.IdentityDocumentReviewRepository;
import com.monsterdam.app.service.IdentityDocumentReviewService;
import com.monsterdam.app.service.dto.IdentityDocumentReviewDTO;
import com.monsterdam.app.service.mapper.IdentityDocumentReviewMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.IdentityDocumentReview}.
 */
@Service
@Transactional
public class IdentityDocumentReviewServiceImpl implements IdentityDocumentReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityDocumentReviewServiceImpl.class);

    private final IdentityDocumentReviewRepository identityDocumentReviewRepository;

    private final IdentityDocumentReviewMapper identityDocumentReviewMapper;

    public IdentityDocumentReviewServiceImpl(
        IdentityDocumentReviewRepository identityDocumentReviewRepository,
        IdentityDocumentReviewMapper identityDocumentReviewMapper
    ) {
        this.identityDocumentReviewRepository = identityDocumentReviewRepository;
        this.identityDocumentReviewMapper = identityDocumentReviewMapper;
    }

    @Override
    public IdentityDocumentReviewDTO save(IdentityDocumentReviewDTO identityDocumentReviewDTO) {
        LOG.debug("Request to save IdentityDocumentReview : {}", identityDocumentReviewDTO);
        IdentityDocumentReview identityDocumentReview = identityDocumentReviewMapper.toEntity(identityDocumentReviewDTO);
        identityDocumentReview = identityDocumentReviewRepository.save(identityDocumentReview);
        return identityDocumentReviewMapper.toDto(identityDocumentReview);
    }

    @Override
    public IdentityDocumentReviewDTO update(IdentityDocumentReviewDTO identityDocumentReviewDTO) {
        LOG.debug("Request to update IdentityDocumentReview : {}", identityDocumentReviewDTO);
        IdentityDocumentReview identityDocumentReview = identityDocumentReviewMapper.toEntity(identityDocumentReviewDTO);
        identityDocumentReview = identityDocumentReviewRepository.save(identityDocumentReview);
        return identityDocumentReviewMapper.toDto(identityDocumentReview);
    }

    @Override
    public Optional<IdentityDocumentReviewDTO> partialUpdate(IdentityDocumentReviewDTO identityDocumentReviewDTO) {
        LOG.debug("Request to partially update IdentityDocumentReview : {}", identityDocumentReviewDTO);

        return identityDocumentReviewRepository
            .findById(identityDocumentReviewDTO.getId())
            .map(existingIdentityDocumentReview -> {
                identityDocumentReviewMapper.partialUpdate(existingIdentityDocumentReview, identityDocumentReviewDTO);

                return existingIdentityDocumentReview;
            })
            .map(identityDocumentReviewRepository::save)
            .map(identityDocumentReviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IdentityDocumentReviewDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all IdentityDocumentReviews");
        return identityDocumentReviewRepository.findAll(pageable).map(identityDocumentReviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IdentityDocumentReviewDTO> findOne(Long id) {
        LOG.debug("Request to get IdentityDocumentReview : {}", id);
        return identityDocumentReviewRepository.findById(id).map(identityDocumentReviewMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete IdentityDocumentReview : {}", id);
        identityDocumentReviewRepository.deleteById(id);
    }
}
