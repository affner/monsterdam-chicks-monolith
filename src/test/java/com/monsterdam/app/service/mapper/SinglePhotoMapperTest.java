package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.SinglePhotoAsserts.*;
import static com.monsterdam.app.domain.SinglePhotoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SinglePhotoMapperTest {

    private SinglePhotoMapper singlePhotoMapper;

    @BeforeEach
    void setUp() {
        singlePhotoMapper = new SinglePhotoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSinglePhotoSample1();
        var actual = singlePhotoMapper.toEntity(singlePhotoMapper.toDto(expected));
        assertSinglePhotoAllPropertiesEquals(expected, actual);
    }
}
