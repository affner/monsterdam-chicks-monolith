package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.MoneyGiftAsserts.*;
import static com.monsterdam.app.domain.MoneyGiftTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoneyGiftMapperTest {

    private MoneyGiftMapper moneyGiftMapper;

    @BeforeEach
    void setUp() {
        moneyGiftMapper = new MoneyGiftMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMoneyGiftSample1();
        var actual = moneyGiftMapper.toEntity(moneyGiftMapper.toDto(expected));
        assertMoneyGiftAllPropertiesEquals(expected, actual);
    }
}
