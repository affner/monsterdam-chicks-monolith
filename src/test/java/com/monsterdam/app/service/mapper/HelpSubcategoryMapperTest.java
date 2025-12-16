package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.HelpSubcategoryAsserts.*;
import static com.monsterdam.app.domain.HelpSubcategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpSubcategoryMapperTest {

    private HelpSubcategoryMapper helpSubcategoryMapper;

    @BeforeEach
    void setUp() {
        helpSubcategoryMapper = new HelpSubcategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHelpSubcategorySample1();
        var actual = helpSubcategoryMapper.toEntity(helpSubcategoryMapper.toDto(expected));
        assertHelpSubcategoryAllPropertiesEquals(expected, actual);
    }
}
