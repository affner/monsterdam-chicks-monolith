package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubscriptionPlanOfferTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubscriptionPlanOffer getSubscriptionPlanOfferSample1() {
        return new SubscriptionPlanOffer().id(1L).subscriptionsLimit(1).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static SubscriptionPlanOffer getSubscriptionPlanOfferSample2() {
        return new SubscriptionPlanOffer().id(2L).subscriptionsLimit(2).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static SubscriptionPlanOffer getSubscriptionPlanOfferRandomSampleGenerator() {
        return new SubscriptionPlanOffer()
            .id(longCount.incrementAndGet())
            .subscriptionsLimit(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
