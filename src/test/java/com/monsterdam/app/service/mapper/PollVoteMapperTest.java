package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PollVoteAsserts.*;
import static com.monsterdam.app.domain.PollVoteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PollVoteMapperTest {

    private PollVoteMapper pollVoteMapper;

    @BeforeEach
    void setUp() {
        pollVoteMapper = new PollVoteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPollVoteSample1();
        var actual = pollVoteMapper.toEntity(pollVoteMapper.toDto(expected));
        assertPollVoteAllPropertiesEquals(expected, actual);
    }
}
