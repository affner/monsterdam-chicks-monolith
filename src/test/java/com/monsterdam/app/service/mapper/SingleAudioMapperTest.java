package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.SingleAudioAsserts.*;
import static com.monsterdam.app.domain.SingleAudioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SingleAudioMapperTest {

    private SingleAudioMapper singleAudioMapper;

    @BeforeEach
    void setUp() {
        singleAudioMapper = new SingleAudioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSingleAudioSample1();
        var actual = singleAudioMapper.toEntity(singleAudioMapper.toDto(expected));
        assertSingleAudioAllPropertiesEquals(expected, actual);
    }
}
