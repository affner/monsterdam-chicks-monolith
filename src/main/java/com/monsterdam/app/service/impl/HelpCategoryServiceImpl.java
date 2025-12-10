package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.HelpCategory;
import com.monsterdam.app.repository.HelpCategoryRepository;
import com.monsterdam.app.service.HelpCategoryService;
import com.monsterdam.app.service.dto.HelpCategoryDTO;
import com.monsterdam.app.service.mapper.HelpCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.HelpCategory}.
 */
@Service
@Transactional
public class HelpCategoryServiceImpl implements HelpCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(HelpCategoryServiceImpl.class);

    private final HelpCategoryRepository helpCategoryRepository;

    private final HelpCategoryMapper helpCategoryMapper;

    public HelpCategoryServiceImpl(HelpCategoryRepository helpCategoryRepository, HelpCategoryMapper helpCategoryMapper) {
        this.helpCategoryRepository = helpCategoryRepository;
        this.helpCategoryMapper = helpCategoryMapper;
    }

    @Override
    public HelpCategoryDTO save(HelpCategoryDTO helpCategoryDTO) {
        LOG.debug("Request to save HelpCategory : {}", helpCategoryDTO);
        HelpCategory helpCategory = helpCategoryMapper.toEntity(helpCategoryDTO);
        helpCategory = helpCategoryRepository.save(helpCategory);
        return helpCategoryMapper.toDto(helpCategory);
    }

    @Override
    public HelpCategoryDTO update(HelpCategoryDTO helpCategoryDTO) {
        LOG.debug("Request to update HelpCategory : {}", helpCategoryDTO);
        HelpCategory helpCategory = helpCategoryMapper.toEntity(helpCategoryDTO);
        helpCategory = helpCategoryRepository.save(helpCategory);
        return helpCategoryMapper.toDto(helpCategory);
    }

    @Override
    public Optional<HelpCategoryDTO> partialUpdate(HelpCategoryDTO helpCategoryDTO) {
        LOG.debug("Request to partially update HelpCategory : {}", helpCategoryDTO);

        return helpCategoryRepository
            .findById(helpCategoryDTO.getId())
            .map(existingHelpCategory -> {
                helpCategoryMapper.partialUpdate(existingHelpCategory, helpCategoryDTO);

                return existingHelpCategory;
            })
            .map(helpCategoryRepository::save)
            .map(helpCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HelpCategoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HelpCategories");
        return helpCategoryRepository.findAll(pageable).map(helpCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HelpCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get HelpCategory : {}", id);
        return helpCategoryRepository.findById(id).map(helpCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete HelpCategory : {}", id);
        helpCategoryRepository.deleteById(id);
    }
}
