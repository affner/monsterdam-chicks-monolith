package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.SinglePhoto;
import com.monsterdam.app.repository.SinglePhotoRepository;
import com.monsterdam.app.service.SinglePhotoService;
import com.monsterdam.app.service.dto.SinglePhotoDTO;
import com.monsterdam.app.service.mapper.SinglePhotoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.SinglePhoto}.
 */
@Service
@Transactional
public class SinglePhotoServiceImpl extends AbstractLogicalDeletionService<SinglePhoto, SinglePhotoDTO> implements SinglePhotoService {

    private static final Logger LOG = LoggerFactory.getLogger(SinglePhotoServiceImpl.class);

    private final SinglePhotoRepository singlePhotoRepository;

    private final SinglePhotoMapper singlePhotoMapper;

    public SinglePhotoServiceImpl(SinglePhotoRepository singlePhotoRepository, SinglePhotoMapper singlePhotoMapper) {
        super(singlePhotoRepository, singlePhotoMapper, SinglePhoto::setDeletedDate);
        this.singlePhotoRepository = singlePhotoRepository;
        this.singlePhotoMapper = singlePhotoMapper;
    }

    @Override
    public SinglePhotoDTO save(SinglePhotoDTO singlePhotoDTO) {
        LOG.debug("Request to save SinglePhoto : {}", singlePhotoDTO);
        SinglePhoto singlePhoto = singlePhotoMapper.toEntity(singlePhotoDTO);
        singlePhoto = singlePhotoRepository.save(singlePhoto);
        return singlePhotoMapper.toDto(singlePhoto);
    }

    @Override
    public SinglePhotoDTO update(SinglePhotoDTO singlePhotoDTO) {
        LOG.debug("Request to update SinglePhoto : {}", singlePhotoDTO);
        SinglePhoto singlePhoto = singlePhotoMapper.toEntity(singlePhotoDTO);
        singlePhoto = singlePhotoRepository.save(singlePhoto);
        return singlePhotoMapper.toDto(singlePhoto);
    }

    @Override
    public Optional<SinglePhotoDTO> partialUpdate(SinglePhotoDTO singlePhotoDTO) {
        LOG.debug("Request to partially update SinglePhoto : {}", singlePhotoDTO);

        return singlePhotoRepository
            .findById(singlePhotoDTO.getId())
            .map(existingSinglePhoto -> {
                singlePhotoMapper.partialUpdate(existingSinglePhoto, singlePhotoDTO);

                return existingSinglePhoto;
            })
            .map(singlePhotoRepository::save)
            .map(singlePhotoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SinglePhotoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SinglePhotos");
        return singlePhotoRepository.findAll(pageable).map(singlePhotoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SinglePhotoDTO> findOne(Long id) {
        LOG.debug("Request to get SinglePhoto : {}", id);
        return singlePhotoRepository.findById(id).map(singlePhotoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SinglePhoto : {}", id);
        singlePhotoRepository.deleteById(id);
    }
}
