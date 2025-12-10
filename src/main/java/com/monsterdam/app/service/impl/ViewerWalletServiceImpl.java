package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.ViewerWallet;
import com.monsterdam.app.repository.ViewerWalletRepository;
import com.monsterdam.app.service.ViewerWalletService;
import com.monsterdam.app.service.dto.ViewerWalletDTO;
import com.monsterdam.app.service.mapper.ViewerWalletMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.ViewerWallet}.
 */
@Service
@Transactional
public class ViewerWalletServiceImpl implements ViewerWalletService {

    private static final Logger LOG = LoggerFactory.getLogger(ViewerWalletServiceImpl.class);

    private final ViewerWalletRepository viewerWalletRepository;

    private final ViewerWalletMapper viewerWalletMapper;

    public ViewerWalletServiceImpl(ViewerWalletRepository viewerWalletRepository, ViewerWalletMapper viewerWalletMapper) {
        this.viewerWalletRepository = viewerWalletRepository;
        this.viewerWalletMapper = viewerWalletMapper;
    }

    @Override
    public ViewerWalletDTO save(ViewerWalletDTO viewerWalletDTO) {
        LOG.debug("Request to save ViewerWallet : {}", viewerWalletDTO);
        ViewerWallet viewerWallet = viewerWalletMapper.toEntity(viewerWalletDTO);
        viewerWallet = viewerWalletRepository.save(viewerWallet);
        return viewerWalletMapper.toDto(viewerWallet);
    }

    @Override
    public ViewerWalletDTO update(ViewerWalletDTO viewerWalletDTO) {
        LOG.debug("Request to update ViewerWallet : {}", viewerWalletDTO);
        ViewerWallet viewerWallet = viewerWalletMapper.toEntity(viewerWalletDTO);
        viewerWallet = viewerWalletRepository.save(viewerWallet);
        return viewerWalletMapper.toDto(viewerWallet);
    }

    @Override
    public Optional<ViewerWalletDTO> partialUpdate(ViewerWalletDTO viewerWalletDTO) {
        LOG.debug("Request to partially update ViewerWallet : {}", viewerWalletDTO);

        return viewerWalletRepository
            .findById(viewerWalletDTO.getId())
            .map(existingViewerWallet -> {
                viewerWalletMapper.partialUpdate(existingViewerWallet, viewerWalletDTO);

                return existingViewerWallet;
            })
            .map(viewerWalletRepository::save)
            .map(viewerWalletMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ViewerWalletDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ViewerWallets");
        return viewerWalletRepository.findAll(pageable).map(viewerWalletMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ViewerWalletDTO> findOne(Long id) {
        LOG.debug("Request to get ViewerWallet : {}", id);
        return viewerWalletRepository.findById(id).map(viewerWalletMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ViewerWallet : {}", id);
        viewerWalletRepository.deleteById(id);
    }
}
