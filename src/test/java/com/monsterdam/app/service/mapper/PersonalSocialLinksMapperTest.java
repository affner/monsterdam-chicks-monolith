package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PersonalSocialLinksAsserts.*;
import static com.monsterdam.app.domain.PersonalSocialLinksTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonalSocialLinksMapperTest {

    private PersonalSocialLinksMapper personalSocialLinksMapper;

    @BeforeEach
    void setUp() {
        personalSocialLinksMapper = new PersonalSocialLinksMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPersonalSocialLinksSample1();
        var actual = personalSocialLinksMapper.toEntity(personalSocialLinksMapper.toDto(expected));
        assertPersonalSocialLinksAllPropertiesEquals(expected, actual);
    }
}
