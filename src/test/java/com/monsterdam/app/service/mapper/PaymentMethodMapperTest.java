package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PaymentMethodAsserts.*;
import static com.monsterdam.app.domain.PaymentMethodTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentMethodMapperTest {

    private PaymentMethodMapper paymentMethodMapper;

    @BeforeEach
    void setUp() {
        paymentMethodMapper = new PaymentMethodMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentMethodSample1();
        var actual = paymentMethodMapper.toEntity(paymentMethodMapper.toDto(expected));
        assertPaymentMethodAllPropertiesEquals(expected, actual);
    }
}
