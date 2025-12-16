package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MoneyWithdrawTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MoneyWithdraw getMoneyWithdrawSample1() {
        return new MoneyWithdraw()
            .id(1L)
            .currency("currency1")
            .payoutProviderName("payoutProviderName1")
            .payoutReferenceId("payoutReferenceId1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static MoneyWithdraw getMoneyWithdrawSample2() {
        return new MoneyWithdraw()
            .id(2L)
            .currency("currency2")
            .payoutProviderName("payoutProviderName2")
            .payoutReferenceId("payoutReferenceId2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static MoneyWithdraw getMoneyWithdrawRandomSampleGenerator() {
        return new MoneyWithdraw()
            .id(longCount.incrementAndGet())
            .currency(UUID.randomUUID().toString())
            .payoutProviderName(UUID.randomUUID().toString())
            .payoutReferenceId(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
