package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.TrialLinkAsserts.*;
import static com.monsterdam.app.domain.TrialLinkTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrialLinkMapperTest {

    private TrialLinkMapper trialLinkMapper;

    @BeforeEach
    void setUp() {
        trialLinkMapper = new TrialLinkMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrialLinkSample1();
        var actual = trialLinkMapper.toEntity(trialLinkMapper.toDto(expected));
        assertTrialLinkAllPropertiesEquals(expected, actual);
    }
}
