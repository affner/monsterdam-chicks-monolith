package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.MoneyWithdrawAsserts.*;
import static com.monsterdam.app.domain.MoneyWithdrawTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoneyWithdrawMapperTest {

    private MoneyWithdrawMapper moneyWithdrawMapper;

    @BeforeEach
    void setUp() {
        moneyWithdrawMapper = new MoneyWithdrawMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMoneyWithdrawSample1();
        var actual = moneyWithdrawMapper.toEntity(moneyWithdrawMapper.toDto(expected));
        assertMoneyWithdrawAllPropertiesEquals(expected, actual);
    }
}
