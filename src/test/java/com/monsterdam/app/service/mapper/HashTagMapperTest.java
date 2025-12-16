package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.HashTagAsserts.*;
import static com.monsterdam.app.domain.HashTagTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashTagMapperTest {

    private HashTagMapper hashTagMapper;

    @BeforeEach
    void setUp() {
        hashTagMapper = new HashTagMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHashTagSample1();
        var actual = hashTagMapper.toEntity(hashTagMapper.toDto(expected));
        assertHashTagAllPropertiesEquals(expected, actual);
    }
}
