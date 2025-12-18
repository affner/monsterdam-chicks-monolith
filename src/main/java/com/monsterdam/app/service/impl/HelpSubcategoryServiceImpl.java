package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.HelpSubcategory;
import com.monsterdam.app.repository.HelpSubcategoryRepository;
import com.monsterdam.app.service.HelpSubcategoryService;
import com.monsterdam.app.service.dto.HelpSubcategoryDTO;
import com.monsterdam.app.service.mapper.HelpSubcategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.HelpSubcategory}.
 */
@Service
@Transactional
public class HelpSubcategoryServiceImpl
    extends AbstractLogicalDeletionService<HelpSubcategory, HelpSubcategoryDTO>
    implements HelpSubcategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(HelpSubcategoryServiceImpl.class);

    private final HelpSubcategoryRepository helpSubcategoryRepository;

    private final HelpSubcategoryMapper helpSubcategoryMapper;

    public HelpSubcategoryServiceImpl(HelpSubcategoryRepository helpSubcategoryRepository, HelpSubcategoryMapper helpSubcategoryMapper) {
        super(helpSubcategoryRepository, helpSubcategoryMapper, HelpSubcategory::setDeletedDate);
        this.helpSubcategoryRepository = helpSubcategoryRepository;
        this.helpSubcategoryMapper = helpSubcategoryMapper;
    }

    @Override
    public HelpSubcategoryDTO save(HelpSubcategoryDTO helpSubcategoryDTO) {
        LOG.debug("Request to save HelpSubcategory : {}", helpSubcategoryDTO);
        HelpSubcategory helpSubcategory = helpSubcategoryMapper.toEntity(helpSubcategoryDTO);
        helpSubcategory = helpSubcategoryRepository.save(helpSubcategory);
        return helpSubcategoryMapper.toDto(helpSubcategory);
    }

    @Override
    public HelpSubcategoryDTO update(HelpSubcategoryDTO helpSubcategoryDTO) {
        LOG.debug("Request to update HelpSubcategory : {}", helpSubcategoryDTO);
        HelpSubcategory helpSubcategory = helpSubcategoryMapper.toEntity(helpSubcategoryDTO);
        helpSubcategory = helpSubcategoryRepository.save(helpSubcategory);
        return helpSubcategoryMapper.toDto(helpSubcategory);
    }

    @Override
    public Optional<HelpSubcategoryDTO> partialUpdate(HelpSubcategoryDTO helpSubcategoryDTO) {
        LOG.debug("Request to partially update HelpSubcategory : {}", helpSubcategoryDTO);

        return helpSubcategoryRepository
            .findById(helpSubcategoryDTO.getId())
            .map(existingHelpSubcategory -> {
                helpSubcategoryMapper.partialUpdate(existingHelpSubcategory, helpSubcategoryDTO);

                return existingHelpSubcategory;
            })
            .map(helpSubcategoryRepository::save)
            .map(helpSubcategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HelpSubcategoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HelpSubcategories");
        return helpSubcategoryRepository.findAll(pageable).map(helpSubcategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HelpSubcategoryDTO> findOne(Long id) {
        LOG.debug("Request to get HelpSubcategory : {}", id);
        return helpSubcategoryRepository.findById(id).map(helpSubcategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete HelpSubcategory : {}", id);
        helpSubcategoryRepository.deleteById(id);
    }
}
