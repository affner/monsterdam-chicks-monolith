package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MoneyGiftTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MoneyGift getMoneyGiftSample1() {
        return new MoneyGift()
            .id(1L)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .messageId(1L)
            .postId(1L)
            .viewerId(1L)
            .creatorId(1L);
    }

    public static MoneyGift getMoneyGiftSample2() {
        return new MoneyGift()
            .id(2L)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .messageId(2L)
            .postId(2L)
            .viewerId(2L)
            .creatorId(2L);
    }

    public static MoneyGift getMoneyGiftRandomSampleGenerator() {
        return new MoneyGift()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .messageId(longCount.incrementAndGet())
            .postId(longCount.incrementAndGet())
            .viewerId(longCount.incrementAndGet())
            .creatorId(longCount.incrementAndGet());
    }
}
