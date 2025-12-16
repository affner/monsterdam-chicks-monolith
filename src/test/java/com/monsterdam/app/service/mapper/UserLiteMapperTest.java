package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.UserLiteAsserts.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserLiteMapperTest {

    private UserLiteMapper userLiteMapper;

    @BeforeEach
    void setUp() {
        userLiteMapper = new UserLiteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserLiteSample1();
        var actual = userLiteMapper.toEntity(userLiteMapper.toDto(expected));
        assertUserLiteAllPropertiesEquals(expected, actual);
    }
}
