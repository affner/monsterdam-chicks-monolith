package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.AuctionAsserts.*;
import static com.monsterdam.app.domain.AuctionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuctionMapperTest {

    private AuctionMapper auctionMapper;

    @BeforeEach
    void setUp() {
        auctionMapper = new AuctionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAuctionSample1();
        var actual = auctionMapper.toEntity(auctionMapper.toDto(expected));
        assertAuctionAllPropertiesEquals(expected, actual);
    }
}
