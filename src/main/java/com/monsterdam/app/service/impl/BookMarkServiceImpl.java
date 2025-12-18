package com.monsterdam.app.service.impl;

import com.monsterdam.app.domain.BookMark;
import com.monsterdam.app.repository.BookMarkRepository;
import com.monsterdam.app.service.BookMarkService;
import com.monsterdam.app.service.dto.BookMarkDTO;
import com.monsterdam.app.service.mapper.BookMarkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.BookMark}.
 */
@Service
@Transactional
public class BookMarkServiceImpl extends AbstractLogicalDeletionService<BookMark, BookMarkDTO> implements BookMarkService {

    private static final Logger LOG = LoggerFactory.getLogger(BookMarkServiceImpl.class);

    private final BookMarkRepository bookMarkRepository;

    private final BookMarkMapper bookMarkMapper;

    public BookMarkServiceImpl(BookMarkRepository bookMarkRepository, BookMarkMapper bookMarkMapper) {
        super(bookMarkRepository, bookMarkMapper, BookMark::setDeletedDate);
        this.bookMarkRepository = bookMarkRepository;
        this.bookMarkMapper = bookMarkMapper;
    }

    @Override
    public BookMarkDTO save(BookMarkDTO bookMarkDTO) {
        LOG.debug("Request to save BookMark : {}", bookMarkDTO);
        BookMark bookMark = bookMarkMapper.toEntity(bookMarkDTO);
        bookMark = bookMarkRepository.save(bookMark);
        return bookMarkMapper.toDto(bookMark);
    }

    @Override
    public BookMarkDTO update(BookMarkDTO bookMarkDTO) {
        LOG.debug("Request to update BookMark : {}", bookMarkDTO);
        BookMark bookMark = bookMarkMapper.toEntity(bookMarkDTO);
        bookMark = bookMarkRepository.save(bookMark);
        return bookMarkMapper.toDto(bookMark);
    }

    @Override
    public Optional<BookMarkDTO> partialUpdate(BookMarkDTO bookMarkDTO) {
        LOG.debug("Request to partially update BookMark : {}", bookMarkDTO);

        return bookMarkRepository
            .findById(bookMarkDTO.getId())
            .map(existingBookMark -> {
                bookMarkMapper.partialUpdate(existingBookMark, bookMarkDTO);

                return existingBookMark;
            })
            .map(bookMarkRepository::save)
            .map(bookMarkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookMarkDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookMarks");
        return bookMarkRepository.findAll(pageable).map(bookMarkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookMarkDTO> findOne(Long id) {
        LOG.debug("Request to get BookMark : {}", id);
        return bookMarkRepository.findById(id).map(bookMarkMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete BookMark : {}", id);
        bookMarkRepository.deleteById(id);
    }
}
