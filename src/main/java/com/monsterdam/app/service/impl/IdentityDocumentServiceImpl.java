package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.IdentityDocument;
import com.monsterdam.app.repository.IdentityDocumentRepository;
import com.monsterdam.app.service.IdentityDocumentService;
import com.monsterdam.app.service.dto.IdentityDocumentDTO;
import com.monsterdam.app.service.mapper.IdentityDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.IdentityDocument}.
 */
@Service
@Transactional
public class IdentityDocumentServiceImpl implements IdentityDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityDocumentServiceImpl.class);

    private final IdentityDocumentRepository identityDocumentRepository;

    private final IdentityDocumentMapper identityDocumentMapper;

    public IdentityDocumentServiceImpl(
        IdentityDocumentRepository identityDocumentRepository,
        IdentityDocumentMapper identityDocumentMapper
    ) {
        this.identityDocumentRepository = identityDocumentRepository;
        this.identityDocumentMapper = identityDocumentMapper;
    }

    @Override
    public IdentityDocumentDTO save(IdentityDocumentDTO identityDocumentDTO) {
        LOG.debug("Request to save IdentityDocument : {}", identityDocumentDTO);
        IdentityDocument identityDocument = identityDocumentMapper.toEntity(identityDocumentDTO);
        identityDocument = identityDocumentRepository.save(identityDocument);
        return identityDocumentMapper.toDto(identityDocument);
    }

    @Override
    public IdentityDocumentDTO update(IdentityDocumentDTO identityDocumentDTO) {
        LOG.debug("Request to update IdentityDocument : {}", identityDocumentDTO);
        IdentityDocument identityDocument = identityDocumentMapper.toEntity(identityDocumentDTO);
        identityDocument = identityDocumentRepository.save(identityDocument);
        return identityDocumentMapper.toDto(identityDocument);
    }

    @Override
    public Optional<IdentityDocumentDTO> partialUpdate(IdentityDocumentDTO identityDocumentDTO) {
        LOG.debug("Request to partially update IdentityDocument : {}", identityDocumentDTO);

        return identityDocumentRepository
            .findById(identityDocumentDTO.getId())
            .map(existingIdentityDocument -> {
                identityDocumentMapper.partialUpdate(existingIdentityDocument, identityDocumentDTO);

                return existingIdentityDocument;
            })
            .map(identityDocumentRepository::save)
            .map(identityDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IdentityDocumentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all IdentityDocuments");
        return identityDocumentRepository.findAll(pageable).map(identityDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IdentityDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get IdentityDocument : {}", id);
        return identityDocumentRepository.findById(id).map(identityDocumentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete IdentityDocument : {}", id);
        identityDocumentRepository.deleteById(id);
    }
}
