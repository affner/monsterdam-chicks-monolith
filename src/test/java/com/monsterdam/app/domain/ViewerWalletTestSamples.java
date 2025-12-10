package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ViewerWalletTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ViewerWallet getViewerWalletSample1() {
        return new ViewerWallet().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1").viewerId(1L);
    }

    public static ViewerWallet getViewerWalletSample2() {
        return new ViewerWallet().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2").viewerId(2L);
    }

    public static ViewerWallet getViewerWalletRandomSampleGenerator() {
        return new ViewerWallet()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .viewerId(longCount.incrementAndGet());
    }
}
