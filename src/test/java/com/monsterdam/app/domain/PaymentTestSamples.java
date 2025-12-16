package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Payment getPaymentSample1() {
        return new Payment()
            .id(1L)
            .currency("currency1")
            .paymentReference("paymentReference1")
            .cloudTransactionId("cloudTransactionId1")
            .providerPaymentIntentId("providerPaymentIntentId1")
            .providerChargeId("providerChargeId1")
            .providerCustomerId("providerCustomerId1")
            .providerPaymentMethodId("providerPaymentMethodId1")
            .providerEventLastId("providerEventLastId1")
            .countryCode("countryCode1");
    }

    public static Payment getPaymentSample2() {
        return new Payment()
            .id(2L)
            .currency("currency2")
            .paymentReference("paymentReference2")
            .cloudTransactionId("cloudTransactionId2")
            .providerPaymentIntentId("providerPaymentIntentId2")
            .providerChargeId("providerChargeId2")
            .providerCustomerId("providerCustomerId2")
            .providerPaymentMethodId("providerPaymentMethodId2")
            .providerEventLastId("providerEventLastId2")
            .countryCode("countryCode2");
    }

    public static Payment getPaymentRandomSampleGenerator() {
        return new Payment()
            .id(longCount.incrementAndGet())
            .currency(UUID.randomUUID().toString())
            .paymentReference(UUID.randomUUID().toString())
            .cloudTransactionId(UUID.randomUUID().toString())
            .providerPaymentIntentId(UUID.randomUUID().toString())
            .providerChargeId(UUID.randomUUID().toString())
            .providerCustomerId(UUID.randomUUID().toString())
            .providerPaymentMethodId(UUID.randomUUID().toString())
            .providerEventLastId(UUID.randomUUID().toString())
            .countryCode(UUID.randomUUID().toString());
    }
}
