package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.ModerationActionAsserts.*;
import static com.monsterdam.app.domain.ModerationActionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModerationActionMapperTest {

    private ModerationActionMapper moderationActionMapper;

    @BeforeEach
    void setUp() {
        moderationActionMapper = new ModerationActionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getModerationActionSample1();
        var actual = moderationActionMapper.toEntity(moderationActionMapper.toDto(expected));
        assertModerationActionAllPropertiesEquals(expected, actual);
    }
}
