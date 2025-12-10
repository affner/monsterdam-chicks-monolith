package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.MoneyEarningAsserts.*;
import static com.monsterdam.app.domain.MoneyEarningTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoneyEarningMapperTest {

    private MoneyEarningMapper moneyEarningMapper;

    @BeforeEach
    void setUp() {
        moneyEarningMapper = new MoneyEarningMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMoneyEarningSample1();
        var actual = moneyEarningMapper.toEntity(moneyEarningMapper.toDto(expected));
        assertMoneyEarningAllPropertiesEquals(expected, actual);
    }
}
