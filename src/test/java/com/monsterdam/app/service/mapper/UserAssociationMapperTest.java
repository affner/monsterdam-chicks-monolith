package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.UserAssociationAsserts.*;
import static com.monsterdam.app.domain.UserAssociationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAssociationMapperTest {

    private UserAssociationMapper userAssociationMapper;

    @BeforeEach
    void setUp() {
        userAssociationMapper = new UserAssociationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserAssociationSample1();
        var actual = userAssociationMapper.toEntity(userAssociationMapper.toDto(expected));
        assertUserAssociationAllPropertiesEquals(expected, actual);
    }
}
