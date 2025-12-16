package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RefundTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RefundTransaction getRefundTransactionSample1() {
        return new RefundTransaction()
            .id(1L)
            .currency("currency1")
            .reason("reason1")
            .paymentReference("paymentReference1")
            .providerChargeId("providerChargeId1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static RefundTransaction getRefundTransactionSample2() {
        return new RefundTransaction()
            .id(2L)
            .currency("currency2")
            .reason("reason2")
            .paymentReference("paymentReference2")
            .providerChargeId("providerChargeId2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static RefundTransaction getRefundTransactionRandomSampleGenerator() {
        return new RefundTransaction()
            .id(longCount.incrementAndGet())
            .currency(UUID.randomUUID().toString())
            .reason(UUID.randomUUID().toString())
            .paymentReference(UUID.randomUUID().toString())
            .providerChargeId(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
