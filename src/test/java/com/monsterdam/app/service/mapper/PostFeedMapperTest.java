package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PostFeedAsserts.*;
import static com.monsterdam.app.domain.PostFeedTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostFeedMapperTest {

    private PostFeedMapper postFeedMapper;

    @BeforeEach
    void setUp() {
        postFeedMapper = new PostFeedMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPostFeedSample1();
        var actual = postFeedMapper.toEntity(postFeedMapper.toDto(expected));
        assertPostFeedAllPropertiesEquals(expected, actual);
    }
}
