package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.RefundTransactionAsserts.*;
import static com.monsterdam.app.domain.RefundTransactionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RefundTransactionMapperTest {

    private RefundTransactionMapper refundTransactionMapper;

    @BeforeEach
    void setUp() {
        refundTransactionMapper = new RefundTransactionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRefundTransactionSample1();
        var actual = refundTransactionMapper.toEntity(refundTransactionMapper.toDto(expected));
        assertRefundTransactionAllPropertiesEquals(expected, actual);
    }
}
