package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.TaxInfoAsserts.*;
import static com.monsterdam.app.domain.TaxInfoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaxInfoMapperTest {

    private TaxInfoMapper taxInfoMapper;

    @BeforeEach
    void setUp() {
        taxInfoMapper = new TaxInfoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTaxInfoSample1();
        var actual = taxInfoMapper.toEntity(taxInfoMapper.toDto(expected));
        assertTaxInfoAllPropertiesEquals(expected, actual);
    }
}
