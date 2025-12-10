package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PostPollAsserts.*;
import static com.monsterdam.app.domain.PostPollTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostPollMapperTest {

    private PostPollMapper postPollMapper;

    @BeforeEach
    void setUp() {
        postPollMapper = new PostPollMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPostPollSample1();
        var actual = postPollMapper.toEntity(postPollMapper.toDto(expected));
        assertPostPollAllPropertiesEquals(expected, actual);
    }
}
