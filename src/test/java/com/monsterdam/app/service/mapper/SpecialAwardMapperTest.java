package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.SpecialAwardAsserts.*;
import static com.monsterdam.app.domain.SpecialAwardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpecialAwardMapperTest {

    private SpecialAwardMapper specialAwardMapper;

    @BeforeEach
    void setUp() {
        specialAwardMapper = new SpecialAwardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSpecialAwardSample1();
        var actual = specialAwardMapper.toEntity(specialAwardMapper.toDto(expected));
        assertSpecialAwardAllPropertiesEquals(expected, actual);
    }
}
