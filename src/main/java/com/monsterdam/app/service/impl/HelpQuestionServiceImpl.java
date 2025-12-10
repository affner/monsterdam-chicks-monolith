package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.HelpQuestion;
import com.monsterdam.app.repository.HelpQuestionRepository;
import com.monsterdam.app.service.HelpQuestionService;
import com.monsterdam.app.service.dto.HelpQuestionDTO;
import com.monsterdam.app.service.mapper.HelpQuestionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.HelpQuestion}.
 */
@Service
@Transactional
public class HelpQuestionServiceImpl implements HelpQuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(HelpQuestionServiceImpl.class);

    private final HelpQuestionRepository helpQuestionRepository;

    private final HelpQuestionMapper helpQuestionMapper;

    public HelpQuestionServiceImpl(HelpQuestionRepository helpQuestionRepository, HelpQuestionMapper helpQuestionMapper) {
        this.helpQuestionRepository = helpQuestionRepository;
        this.helpQuestionMapper = helpQuestionMapper;
    }

    @Override
    public HelpQuestionDTO save(HelpQuestionDTO helpQuestionDTO) {
        LOG.debug("Request to save HelpQuestion : {}", helpQuestionDTO);
        HelpQuestion helpQuestion = helpQuestionMapper.toEntity(helpQuestionDTO);
        helpQuestion = helpQuestionRepository.save(helpQuestion);
        return helpQuestionMapper.toDto(helpQuestion);
    }

    @Override
    public HelpQuestionDTO update(HelpQuestionDTO helpQuestionDTO) {
        LOG.debug("Request to update HelpQuestion : {}", helpQuestionDTO);
        HelpQuestion helpQuestion = helpQuestionMapper.toEntity(helpQuestionDTO);
        helpQuestion = helpQuestionRepository.save(helpQuestion);
        return helpQuestionMapper.toDto(helpQuestion);
    }

    @Override
    public Optional<HelpQuestionDTO> partialUpdate(HelpQuestionDTO helpQuestionDTO) {
        LOG.debug("Request to partially update HelpQuestion : {}", helpQuestionDTO);

        return helpQuestionRepository
            .findById(helpQuestionDTO.getId())
            .map(existingHelpQuestion -> {
                helpQuestionMapper.partialUpdate(existingHelpQuestion, helpQuestionDTO);

                return existingHelpQuestion;
            })
            .map(helpQuestionRepository::save)
            .map(helpQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HelpQuestionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HelpQuestions");
        return helpQuestionRepository.findAll(pageable).map(helpQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HelpQuestionDTO> findOne(Long id) {
        LOG.debug("Request to get HelpQuestion : {}", id);
        return helpQuestionRepository.findById(id).map(helpQuestionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete HelpQuestion : {}", id);
        helpQuestionRepository.deleteById(id);
    }
}
