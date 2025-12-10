package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrialLinkTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TrialLink getTrialLinkSample1() {
        return new TrialLink().id(1L).linkCode("linkCode1").freeDays(1).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static TrialLink getTrialLinkSample2() {
        return new TrialLink().id(2L).linkCode("linkCode2").freeDays(2).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static TrialLink getTrialLinkRandomSampleGenerator() {
        return new TrialLink()
            .id(longCount.incrementAndGet())
            .linkCode(UUID.randomUUID().toString())
            .freeDays(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
