package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.SubscriptionBundleAsserts.*;
import static com.monsterdam.app.domain.SubscriptionBundleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubscriptionBundleMapperTest {

    private SubscriptionBundleMapper subscriptionBundleMapper;

    @BeforeEach
    void setUp() {
        subscriptionBundleMapper = new SubscriptionBundleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSubscriptionBundleSample1();
        var actual = subscriptionBundleMapper.toEntity(subscriptionBundleMapper.toDto(expected));
        assertSubscriptionBundleAllPropertiesEquals(expected, actual);
    }
}
