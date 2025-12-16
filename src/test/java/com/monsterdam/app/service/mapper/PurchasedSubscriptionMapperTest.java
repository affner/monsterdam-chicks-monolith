package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PurchasedSubscriptionAsserts.*;
import static com.monsterdam.app.domain.PurchasedSubscriptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchasedSubscriptionMapperTest {

    private PurchasedSubscriptionMapper purchasedSubscriptionMapper;

    @BeforeEach
    void setUp() {
        purchasedSubscriptionMapper = new PurchasedSubscriptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPurchasedSubscriptionSample1();
        var actual = purchasedSubscriptionMapper.toEntity(purchasedSubscriptionMapper.toDto(expected));
        assertPurchasedSubscriptionAllPropertiesEquals(expected, actual);
    }
}
