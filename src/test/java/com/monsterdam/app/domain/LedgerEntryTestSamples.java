package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LedgerEntryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LedgerEntry getLedgerEntrySample1() {
        return new LedgerEntry()
            .id(1L)
            .currency("currency1")
            .accountOwnerId(1L)
            .referenceType("referenceType1")
            .referenceId(1L)
            .createdBy("createdBy1");
    }

    public static LedgerEntry getLedgerEntrySample2() {
        return new LedgerEntry()
            .id(2L)
            .currency("currency2")
            .accountOwnerId(2L)
            .referenceType("referenceType2")
            .referenceId(2L)
            .createdBy("createdBy2");
    }

    public static LedgerEntry getLedgerEntryRandomSampleGenerator() {
        return new LedgerEntry()
            .id(longCount.incrementAndGet())
            .currency(UUID.randomUUID().toString())
            .accountOwnerId(longCount.incrementAndGet())
            .referenceType(UUID.randomUUID().toString())
            .referenceId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
