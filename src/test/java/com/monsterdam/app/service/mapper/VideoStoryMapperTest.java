package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.VideoStoryAsserts.*;
import static com.monsterdam.app.domain.VideoStoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VideoStoryMapperTest {

    private VideoStoryMapper videoStoryMapper;

    @BeforeEach
    void setUp() {
        videoStoryMapper = new VideoStoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVideoStorySample1();
        var actual = videoStoryMapper.toEntity(videoStoryMapper.toDto(expected));
        assertVideoStoryAllPropertiesEquals(expected, actual);
    }
}
