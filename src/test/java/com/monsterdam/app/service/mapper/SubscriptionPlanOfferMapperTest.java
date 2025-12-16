package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.SubscriptionPlanOfferAsserts.*;
import static com.monsterdam.app.domain.SubscriptionPlanOfferTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubscriptionPlanOfferMapperTest {

    private SubscriptionPlanOfferMapper subscriptionPlanOfferMapper;

    @BeforeEach
    void setUp() {
        subscriptionPlanOfferMapper = new SubscriptionPlanOfferMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSubscriptionPlanOfferSample1();
        var actual = subscriptionPlanOfferMapper.toEntity(subscriptionPlanOfferMapper.toDto(expected));
        assertSubscriptionPlanOfferAllPropertiesEquals(expected, actual);
    }
}
