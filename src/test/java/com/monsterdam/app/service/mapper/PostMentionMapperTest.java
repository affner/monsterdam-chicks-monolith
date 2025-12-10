package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PostMentionAsserts.*;
import static com.monsterdam.app.domain.PostMentionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostMentionMapperTest {

    private PostMentionMapper postMentionMapper;

    @BeforeEach
    void setUp() {
        postMentionMapper = new PostMentionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPostMentionSample1();
        var actual = postMentionMapper.toEntity(postMentionMapper.toDto(expected));
        assertPostMentionAllPropertiesEquals(expected, actual);
    }
}
