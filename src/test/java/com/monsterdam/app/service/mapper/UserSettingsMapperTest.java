package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.UserSettingsAsserts.*;
import static com.monsterdam.app.domain.UserSettingsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserSettingsMapperTest {

    private UserSettingsMapper userSettingsMapper;

    @BeforeEach
    void setUp() {
        userSettingsMapper = new UserSettingsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserSettingsSample1();
        var actual = userSettingsMapper.toEntity(userSettingsMapper.toDto(expected));
        assertUserSettingsAllPropertiesEquals(expected, actual);
    }
}
