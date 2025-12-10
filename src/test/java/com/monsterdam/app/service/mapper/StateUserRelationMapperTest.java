package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.StateUserRelationAsserts.*;
import static com.monsterdam.app.domain.StateUserRelationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StateUserRelationMapperTest {

    private StateUserRelationMapper stateUserRelationMapper;

    @BeforeEach
    void setUp() {
        stateUserRelationMapper = new StateUserRelationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStateUserRelationSample1();
        var actual = stateUserRelationMapper.toEntity(stateUserRelationMapper.toDto(expected));
        assertStateUserRelationAllPropertiesEquals(expected, actual);
    }
}
