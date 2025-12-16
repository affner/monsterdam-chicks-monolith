package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.PurchasedContentAsserts.*;
import static com.monsterdam.app.domain.PurchasedContentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchasedContentMapperTest {

    private PurchasedContentMapper purchasedContentMapper;

    @BeforeEach
    void setUp() {
        purchasedContentMapper = new PurchasedContentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPurchasedContentSample1();
        var actual = purchasedContentMapper.toEntity(purchasedContentMapper.toDto(expected));
        assertPurchasedContentAllPropertiesEquals(expected, actual);
    }
}
