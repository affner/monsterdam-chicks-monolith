package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.HelpRelatedArticle;
import com.monsterdam.app.repository.HelpRelatedArticleRepository;
import com.monsterdam.app.service.HelpRelatedArticleService;
import com.monsterdam.app.service.dto.HelpRelatedArticleDTO;
import com.monsterdam.app.service.mapper.HelpRelatedArticleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.HelpRelatedArticle}.
 */
@Service
@Transactional
public class HelpRelatedArticleServiceImpl
    extends AbstractLogicalDeletionService<HelpRelatedArticle, HelpRelatedArticleDTO>
    implements HelpRelatedArticleService {

    private static final Logger LOG = LoggerFactory.getLogger(HelpRelatedArticleServiceImpl.class);

    private final HelpRelatedArticleRepository helpRelatedArticleRepository;

    private final HelpRelatedArticleMapper helpRelatedArticleMapper;

    public HelpRelatedArticleServiceImpl(
        HelpRelatedArticleRepository helpRelatedArticleRepository,
        HelpRelatedArticleMapper helpRelatedArticleMapper
    ) {
        super(helpRelatedArticleRepository, helpRelatedArticleMapper, HelpRelatedArticle::setDeletedDate);
        this.helpRelatedArticleRepository = helpRelatedArticleRepository;
        this.helpRelatedArticleMapper = helpRelatedArticleMapper;
    }

    @Override
    public HelpRelatedArticleDTO save(HelpRelatedArticleDTO helpRelatedArticleDTO) {
        LOG.debug("Request to save HelpRelatedArticle : {}", helpRelatedArticleDTO);
        HelpRelatedArticle helpRelatedArticle = helpRelatedArticleMapper.toEntity(helpRelatedArticleDTO);
        helpRelatedArticle = helpRelatedArticleRepository.save(helpRelatedArticle);
        return helpRelatedArticleMapper.toDto(helpRelatedArticle);
    }

    @Override
    public HelpRelatedArticleDTO update(HelpRelatedArticleDTO helpRelatedArticleDTO) {
        LOG.debug("Request to update HelpRelatedArticle : {}", helpRelatedArticleDTO);
        HelpRelatedArticle helpRelatedArticle = helpRelatedArticleMapper.toEntity(helpRelatedArticleDTO);
        helpRelatedArticle = helpRelatedArticleRepository.save(helpRelatedArticle);
        return helpRelatedArticleMapper.toDto(helpRelatedArticle);
    }

    @Override
    public Optional<HelpRelatedArticleDTO> partialUpdate(HelpRelatedArticleDTO helpRelatedArticleDTO) {
        LOG.debug("Request to partially update HelpRelatedArticle : {}", helpRelatedArticleDTO);

        return helpRelatedArticleRepository
            .findById(helpRelatedArticleDTO.getId())
            .map(existingHelpRelatedArticle -> {
                helpRelatedArticleMapper.partialUpdate(existingHelpRelatedArticle, helpRelatedArticleDTO);

                return existingHelpRelatedArticle;
            })
            .map(helpRelatedArticleRepository::save)
            .map(helpRelatedArticleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HelpRelatedArticleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HelpRelatedArticles");
        return helpRelatedArticleRepository.findAll(pageable).map(helpRelatedArticleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HelpRelatedArticleDTO> findOne(Long id) {
        LOG.debug("Request to get HelpRelatedArticle : {}", id);
        return helpRelatedArticleRepository.findById(id).map(helpRelatedArticleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete HelpRelatedArticle : {}", id);
        helpRelatedArticleRepository.deleteById(id);
    }
}
