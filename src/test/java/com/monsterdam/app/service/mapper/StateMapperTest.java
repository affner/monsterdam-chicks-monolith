package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.StateAsserts.*;
import static com.monsterdam.app.domain.StateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StateMapperTest {

    private StateMapper stateMapper;

    @BeforeEach
    void setUp() {
        stateMapper = new StateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStateSample1();
        var actual = stateMapper.toEntity(stateMapper.toDto(expected));
        assertStateAllPropertiesEquals(expected, actual);
    }
}
