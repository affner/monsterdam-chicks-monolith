package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.CommentMentionAsserts.*;
import static com.monsterdam.app.domain.CommentMentionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentMentionMapperTest {

    private CommentMentionMapper commentMentionMapper;

    @BeforeEach
    void setUp() {
        commentMentionMapper = new CommentMentionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCommentMentionSample1();
        var actual = commentMentionMapper.toEntity(commentMentionMapper.toDto(expected));
        assertCommentMentionAllPropertiesEquals(expected, actual);
    }
}
