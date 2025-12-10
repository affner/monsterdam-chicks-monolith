package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MoneyWithdrawTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MoneyWithdraw getMoneyWithdrawSample1() {
        return new MoneyWithdraw().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static MoneyWithdraw getMoneyWithdrawSample2() {
        return new MoneyWithdraw().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static MoneyWithdraw getMoneyWithdrawRandomSampleGenerator() {
        return new MoneyWithdraw()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
