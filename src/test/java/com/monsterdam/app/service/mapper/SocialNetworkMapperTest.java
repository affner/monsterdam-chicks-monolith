package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.SocialNetworkAsserts.*;
import static com.monsterdam.app.domain.SocialNetworkTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SocialNetworkMapperTest {

    private SocialNetworkMapper socialNetworkMapper;

    @BeforeEach
    void setUp() {
        socialNetworkMapper = new SocialNetworkMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSocialNetworkSample1();
        var actual = socialNetworkMapper.toEntity(socialNetworkMapper.toDto(expected));
        assertSocialNetworkAllPropertiesEquals(expected, actual);
    }
}
