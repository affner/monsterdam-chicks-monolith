package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.HelpRelatedArticleAsserts.*;
import static com.monsterdam.app.domain.HelpRelatedArticleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpRelatedArticleMapperTest {

    private HelpRelatedArticleMapper helpRelatedArticleMapper;

    @BeforeEach
    void setUp() {
        helpRelatedArticleMapper = new HelpRelatedArticleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHelpRelatedArticleSample1();
        var actual = helpRelatedArticleMapper.toEntity(helpRelatedArticleMapper.toDto(expected));
        assertHelpRelatedArticleAllPropertiesEquals(expected, actual);
    }
}
