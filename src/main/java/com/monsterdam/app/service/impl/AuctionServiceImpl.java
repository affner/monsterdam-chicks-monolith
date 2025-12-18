package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.Auction;
import com.monsterdam.app.repository.AuctionRepository;
import com.monsterdam.app.service.AuctionService;
import com.monsterdam.app.service.dto.AuctionDTO;
import com.monsterdam.app.service.mapper.AuctionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.Auction}.
 */
@Service
@Transactional
public class AuctionServiceImpl extends AbstractLogicalDeletionService<Auction, AuctionDTO> implements AuctionService {

    private static final Logger LOG = LoggerFactory.getLogger(AuctionServiceImpl.class);

    private final AuctionRepository auctionRepository;

    private final AuctionMapper auctionMapper;

    public AuctionServiceImpl(AuctionRepository auctionRepository, AuctionMapper auctionMapper) {
        super(auctionRepository, auctionMapper, Auction::setDeletedDate);
        this.auctionRepository = auctionRepository;
        this.auctionMapper = auctionMapper;
    }

    @Override
    public AuctionDTO save(AuctionDTO auctionDTO) {
        LOG.debug("Request to save Auction : {}", auctionDTO);
        Auction auction = auctionMapper.toEntity(auctionDTO);
        auction = auctionRepository.save(auction);
        return auctionMapper.toDto(auction);
    }

    @Override
    public AuctionDTO update(AuctionDTO auctionDTO) {
        LOG.debug("Request to update Auction : {}", auctionDTO);
        Auction auction = auctionMapper.toEntity(auctionDTO);
        auction = auctionRepository.save(auction);
        return auctionMapper.toDto(auction);
    }

    @Override
    public Optional<AuctionDTO> partialUpdate(AuctionDTO auctionDTO) {
        LOG.debug("Request to partially update Auction : {}", auctionDTO);

        return auctionRepository
            .findById(auctionDTO.getId())
            .map(existingAuction -> {
                auctionMapper.partialUpdate(existingAuction, auctionDTO);

                return existingAuction;
            })
            .map(auctionRepository::save)
            .map(auctionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuctionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Auctions");
        return auctionRepository.findAll(pageable).map(auctionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuctionDTO> findOne(Long id) {
        LOG.debug("Request to get Auction : {}", id);
        return auctionRepository.findById(id).map(auctionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Auction : {}", id);
        auctionRepository.deleteById(id);
    }
}
