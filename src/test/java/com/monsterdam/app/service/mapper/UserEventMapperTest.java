package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.UserEventAsserts.*;
import static com.monsterdam.app.domain.UserEventTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserEventMapperTest {

    private UserEventMapper userEventMapper;

    @BeforeEach
    void setUp() {
        userEventMapper = new UserEventMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserEventSample1();
        var actual = userEventMapper.toEntity(userEventMapper.toDto(expected));
        assertUserEventAllPropertiesEquals(expected, actual);
    }
}
