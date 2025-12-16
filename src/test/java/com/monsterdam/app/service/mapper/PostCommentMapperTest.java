package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PostCommentAsserts.*;
import static com.monsterdam.app.domain.PostCommentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostCommentMapperTest {

    private PostCommentMapper postCommentMapper;

    @BeforeEach
    void setUp() {
        postCommentMapper = new PostCommentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPostCommentSample1();
        var actual = postCommentMapper.toEntity(postCommentMapper.toDto(expected));
        assertPostCommentAllPropertiesEquals(expected, actual);
    }
}
