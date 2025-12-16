package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.AdminSystemConfigsAsserts.*;
import static com.monsterdam.app.domain.AdminSystemConfigsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdminSystemConfigsMapperTest {

    private AdminSystemConfigsMapper adminSystemConfigsMapper;

    @BeforeEach
    void setUp() {
        adminSystemConfigsMapper = new AdminSystemConfigsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdminSystemConfigsSample1();
        var actual = adminSystemConfigsMapper.toEntity(adminSystemConfigsMapper.toDto(expected));
        assertAdminSystemConfigsAllPropertiesEquals(expected, actual);
    }
}
