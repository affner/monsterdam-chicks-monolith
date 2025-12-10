package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.DirectMessageAsserts.*;
import static com.monsterdam.app.domain.DirectMessageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DirectMessageMapperTest {

    private DirectMessageMapper directMessageMapper;

    @BeforeEach
    void setUp() {
        directMessageMapper = new DirectMessageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDirectMessageSample1();
        var actual = directMessageMapper.toEntity(directMessageMapper.toDto(expected));
        assertDirectMessageAllPropertiesEquals(expected, actual);
    }
}
