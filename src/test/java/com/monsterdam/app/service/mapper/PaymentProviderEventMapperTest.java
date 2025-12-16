package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PaymentProviderEventAsserts.*;
import static com.monsterdam.app.domain.PaymentProviderEventTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentProviderEventMapperTest {

    private PaymentProviderEventMapper paymentProviderEventMapper;

    @BeforeEach
    void setUp() {
        paymentProviderEventMapper = new PaymentProviderEventMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentProviderEventSample1();
        var actual = paymentProviderEventMapper.toEntity(paymentProviderEventMapper.toDto(expected));
        assertPaymentProviderEventAllPropertiesEquals(expected, actual);
    }
}
