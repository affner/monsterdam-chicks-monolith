package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.BookMarkAsserts.*;
import static com.monsterdam.app.domain.BookMarkTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookMarkMapperTest {

    private BookMarkMapper bookMarkMapper;

    @BeforeEach
    void setUp() {
        bookMarkMapper = new BookMarkMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookMarkSample1();
        var actual = bookMarkMapper.toEntity(bookMarkMapper.toDto(expected));
        assertBookMarkAllPropertiesEquals(expected, actual);
    }
}
