package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.AdminEmailConfigsAsserts.*;
import static com.monsterdam.app.domain.AdminEmailConfigsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdminEmailConfigsMapperTest {

    private AdminEmailConfigsMapper adminEmailConfigsMapper;

    @BeforeEach
    void setUp() {
        adminEmailConfigsMapper = new AdminEmailConfigsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdminEmailConfigsSample1();
        var actual = adminEmailConfigsMapper.toEntity(adminEmailConfigsMapper.toDto(expected));
        assertAdminEmailConfigsAllPropertiesEquals(expected, actual);
    }
}
