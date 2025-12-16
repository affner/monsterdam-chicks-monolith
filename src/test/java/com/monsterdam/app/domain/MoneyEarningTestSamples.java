package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MoneyEarningTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MoneyEarning getMoneyEarningSample1() {
        return new MoneyEarning()
            .id(1L)
            .currency("currency1")
            .sourceType("sourceType1")
            .sourceId(1L)
            .payoutId(1L)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static MoneyEarning getMoneyEarningSample2() {
        return new MoneyEarning()
            .id(2L)
            .currency("currency2")
            .sourceType("sourceType2")
            .sourceId(2L)
            .payoutId(2L)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static MoneyEarning getMoneyEarningRandomSampleGenerator() {
        return new MoneyEarning()
            .id(longCount.incrementAndGet())
            .currency(UUID.randomUUID().toString())
            .sourceType(UUID.randomUUID().toString())
            .sourceId(longCount.incrementAndGet())
            .payoutId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
