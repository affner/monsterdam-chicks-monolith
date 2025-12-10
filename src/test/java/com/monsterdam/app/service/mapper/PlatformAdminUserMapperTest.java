package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PlatformAdminUserAsserts.*;
import static com.monsterdam.app.domain.PlatformAdminUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlatformAdminUserMapperTest {

    private PlatformAdminUserMapper platformAdminUserMapper;

    @BeforeEach
    void setUp() {
        platformAdminUserMapper = new PlatformAdminUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlatformAdminUserSample1();
        var actual = platformAdminUserMapper.toEntity(platformAdminUserMapper.toDto(expected));
        assertPlatformAdminUserAllPropertiesEquals(expected, actual);
    }
}
