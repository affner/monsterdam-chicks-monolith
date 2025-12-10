package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Payment getPaymentSample1() {
        return new Payment().id(1L).paymentReference("paymentReference1").cloudTransactionId("cloudTransactionId1").viewerId(1L);
    }

    public static Payment getPaymentSample2() {
        return new Payment().id(2L).paymentReference("paymentReference2").cloudTransactionId("cloudTransactionId2").viewerId(2L);
    }

    public static Payment getPaymentRandomSampleGenerator() {
        return new Payment()
            .id(longCount.incrementAndGet())
            .paymentReference(UUID.randomUUID().toString())
            .cloudTransactionId(UUID.randomUUID().toString())
            .viewerId(longCount.incrementAndGet());
    }
}
