package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuctionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Auction getAuctionSample1() {
        return new Auction().id(1L).title("title1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Auction getAuctionSample2() {
        return new Auction().id(2L).title("title2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Auction getAuctionRandomSampleGenerator() {
        return new Auction()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
