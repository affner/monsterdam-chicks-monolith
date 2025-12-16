package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.HelpCategoryAsserts.*;
import static com.monsterdam.app.domain.HelpCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpCategoryMapperTest {

    private HelpCategoryMapper helpCategoryMapper;

    @BeforeEach
    void setUp() {
        helpCategoryMapper = new HelpCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHelpCategorySample1();
        var actual = helpCategoryMapper.toEntity(helpCategoryMapper.toDto(expected));
        assertHelpCategoryAllPropertiesEquals(expected, actual);
    }
}
