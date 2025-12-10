package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.DocumentReviewObservation;
import com.monsterdam.app.repository.DocumentReviewObservationRepository;
import com.monsterdam.app.service.DocumentReviewObservationService;
import com.monsterdam.app.service.dto.DocumentReviewObservationDTO;
import com.monsterdam.app.service.mapper.DocumentReviewObservationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.DocumentReviewObservation}.
 */
@Service
@Transactional
public class DocumentReviewObservationServiceImpl implements DocumentReviewObservationService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentReviewObservationServiceImpl.class);

    private final DocumentReviewObservationRepository documentReviewObservationRepository;

    private final DocumentReviewObservationMapper documentReviewObservationMapper;

    public DocumentReviewObservationServiceImpl(
        DocumentReviewObservationRepository documentReviewObservationRepository,
        DocumentReviewObservationMapper documentReviewObservationMapper
    ) {
        this.documentReviewObservationRepository = documentReviewObservationRepository;
        this.documentReviewObservationMapper = documentReviewObservationMapper;
    }

    @Override
    public DocumentReviewObservationDTO save(DocumentReviewObservationDTO documentReviewObservationDTO) {
        LOG.debug("Request to save DocumentReviewObservation : {}", documentReviewObservationDTO);
        DocumentReviewObservation documentReviewObservation = documentReviewObservationMapper.toEntity(documentReviewObservationDTO);
        documentReviewObservation = documentReviewObservationRepository.save(documentReviewObservation);
        return documentReviewObservationMapper.toDto(documentReviewObservation);
    }

    @Override
    public DocumentReviewObservationDTO update(DocumentReviewObservationDTO documentReviewObservationDTO) {
        LOG.debug("Request to update DocumentReviewObservation : {}", documentReviewObservationDTO);
        DocumentReviewObservation documentReviewObservation = documentReviewObservationMapper.toEntity(documentReviewObservationDTO);
        documentReviewObservation = documentReviewObservationRepository.save(documentReviewObservation);
        return documentReviewObservationMapper.toDto(documentReviewObservation);
    }

    @Override
    public Optional<DocumentReviewObservationDTO> partialUpdate(DocumentReviewObservationDTO documentReviewObservationDTO) {
        LOG.debug("Request to partially update DocumentReviewObservation : {}", documentReviewObservationDTO);

        return documentReviewObservationRepository
            .findById(documentReviewObservationDTO.getId())
            .map(existingDocumentReviewObservation -> {
                documentReviewObservationMapper.partialUpdate(existingDocumentReviewObservation, documentReviewObservationDTO);

                return existingDocumentReviewObservation;
            })
            .map(documentReviewObservationRepository::save)
            .map(documentReviewObservationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentReviewObservationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DocumentReviewObservations");
        return documentReviewObservationRepository.findAll(pageable).map(documentReviewObservationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentReviewObservationDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentReviewObservation : {}", id);
        return documentReviewObservationRepository.findById(id).map(documentReviewObservationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentReviewObservation : {}", id);
        documentReviewObservationRepository.deleteById(id);
    }
}
