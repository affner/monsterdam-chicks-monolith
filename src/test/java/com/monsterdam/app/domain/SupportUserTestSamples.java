package com.monsterdam.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SupportUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SupportUser getSupportUserSample1() {
        return new SupportUser().id(1L);
    }

    public static SupportUser getSupportUserSample2() {
        return new SupportUser().id(2L);
    }

    public static SupportUser getSupportUserRandomSampleGenerator() {
        return new SupportUser().id(longCount.incrementAndGet());
    }
}
