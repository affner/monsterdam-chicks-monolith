package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PollOptionAsserts.*;
import static com.monsterdam.app.domain.PollOptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PollOptionMapperTest {

    private PollOptionMapper pollOptionMapper;

    @BeforeEach
    void setUp() {
        pollOptionMapper = new PollOptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPollOptionSample1();
        var actual = pollOptionMapper.toEntity(pollOptionMapper.toDto(expected));
        assertPollOptionAllPropertiesEquals(expected, actual);
    }
}
