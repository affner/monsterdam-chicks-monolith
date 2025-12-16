package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.ContentPackageAsserts.*;
import static com.monsterdam.app.domain.ContentPackageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContentPackageMapperTest {

    private ContentPackageMapper contentPackageMapper;

    @BeforeEach
    void setUp() {
        contentPackageMapper = new ContentPackageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContentPackageSample1();
        var actual = contentPackageMapper.toEntity(contentPackageMapper.toDto(expected));
        assertContentPackageAllPropertiesEquals(expected, actual);
    }
}
