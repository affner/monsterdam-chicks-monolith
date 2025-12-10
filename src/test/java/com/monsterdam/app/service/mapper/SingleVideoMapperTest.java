package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.SingleVideoAsserts.*;
import static com.monsterdam.app.domain.SingleVideoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SingleVideoMapperTest {

    private SingleVideoMapper singleVideoMapper;

    @BeforeEach
    void setUp() {
        singleVideoMapper = new SingleVideoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSingleVideoSample1();
        var actual = singleVideoMapper.toEntity(singleVideoMapper.toDto(expected));
        assertSingleVideoAllPropertiesEquals(expected, actual);
    }
}
