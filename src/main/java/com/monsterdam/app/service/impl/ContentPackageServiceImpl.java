package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.repository.ContentPackageRepository;
import com.monsterdam.app.service.ContentPackageService;
import com.monsterdam.app.service.dto.ContentPackageDTO;
import com.monsterdam.app.service.mapper.ContentPackageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.ContentPackage}.
 */
@Service
@Transactional
public class ContentPackageServiceImpl
    extends AbstractLogicalDeletionService<ContentPackage, ContentPackageDTO>
    implements ContentPackageService {

    private static final Logger LOG = LoggerFactory.getLogger(ContentPackageServiceImpl.class);

    private final ContentPackageRepository contentPackageRepository;

    private final ContentPackageMapper contentPackageMapper;

    public ContentPackageServiceImpl(ContentPackageRepository contentPackageRepository, ContentPackageMapper contentPackageMapper) {
        super(contentPackageRepository, contentPackageMapper, ContentPackage::setDeletedDate);
        this.contentPackageRepository = contentPackageRepository;
        this.contentPackageMapper = contentPackageMapper;
    }

    @Override
    public ContentPackageDTO save(ContentPackageDTO contentPackageDTO) {
        LOG.debug("Request to save ContentPackage : {}", contentPackageDTO);
        ContentPackage contentPackage = contentPackageMapper.toEntity(contentPackageDTO);
        contentPackage = contentPackageRepository.save(contentPackage);
        return contentPackageMapper.toDto(contentPackage);
    }

    @Override
    public ContentPackageDTO update(ContentPackageDTO contentPackageDTO) {
        LOG.debug("Request to update ContentPackage : {}", contentPackageDTO);
        ContentPackage contentPackage = contentPackageMapper.toEntity(contentPackageDTO);
        contentPackage = contentPackageRepository.save(contentPackage);
        return contentPackageMapper.toDto(contentPackage);
    }

    @Override
    public Optional<ContentPackageDTO> partialUpdate(ContentPackageDTO contentPackageDTO) {
        LOG.debug("Request to partially update ContentPackage : {}", contentPackageDTO);

        return contentPackageRepository
            .findById(contentPackageDTO.getId())
            .map(existingContentPackage -> {
                contentPackageMapper.partialUpdate(existingContentPackage, contentPackageDTO);

                return existingContentPackage;
            })
            .map(contentPackageRepository::save)
            .map(contentPackageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContentPackageDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ContentPackages");
        return contentPackageRepository.findAll(pageable).map(contentPackageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContentPackageDTO> findOne(Long id) {
        LOG.debug("Request to get ContentPackage : {}", id);
        return contentPackageRepository.findById(id).map(contentPackageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ContentPackage : {}", id);
        contentPackageRepository.deleteById(id);
    }
}
