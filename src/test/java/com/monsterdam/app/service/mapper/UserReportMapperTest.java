package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.UserReportAsserts.*;
import static com.monsterdam.app.domain.UserReportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserReportMapperTest {

    private UserReportMapper userReportMapper;

    @BeforeEach
    void setUp() {
        userReportMapper = new UserReportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserReportSample1();
        var actual = userReportMapper.toEntity(userReportMapper.toDto(expected));
        assertUserReportAllPropertiesEquals(expected, actual);
    }
}
