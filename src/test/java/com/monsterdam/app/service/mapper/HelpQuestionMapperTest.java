package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.HelpQuestionAsserts.*;
import static com.monsterdam.app.domain.HelpQuestionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpQuestionMapperTest {

    private HelpQuestionMapper helpQuestionMapper;

    @BeforeEach
    void setUp() {
        helpQuestionMapper = new HelpQuestionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHelpQuestionSample1();
        var actual = helpQuestionMapper.toEntity(helpQuestionMapper.toDto(expected));
        assertHelpQuestionAllPropertiesEquals(expected, actual);
    }
}
