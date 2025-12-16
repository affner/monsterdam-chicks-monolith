package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentProviderEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentProviderEvent getPaymentProviderEventSample1() {
        return new PaymentProviderEvent()
            .id(1L)
            .providerName("providerName1")
            .eventType("eventType1")
            .eventId("eventId1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static PaymentProviderEvent getPaymentProviderEventSample2() {
        return new PaymentProviderEvent()
            .id(2L)
            .providerName("providerName2")
            .eventType("eventType2")
            .eventId("eventId2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static PaymentProviderEvent getPaymentProviderEventRandomSampleGenerator() {
        return new PaymentProviderEvent()
            .id(longCount.incrementAndGet())
            .providerName(UUID.randomUUID().toString())
            .eventType(UUID.randomUUID().toString())
            .eventId(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
