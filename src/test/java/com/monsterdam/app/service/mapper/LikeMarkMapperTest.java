package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.LikeMarkAsserts.*;
import static com.monsterdam.app.domain.LikeMarkTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LikeMarkMapperTest {

    private LikeMarkMapper likeMarkMapper;

    @BeforeEach
    void setUp() {
        likeMarkMapper = new LikeMarkMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLikeMarkSample1();
        var actual = likeMarkMapper.toEntity(likeMarkMapper.toDto(expected));
        assertLikeMarkAllPropertiesEquals(expected, actual);
    }
}
