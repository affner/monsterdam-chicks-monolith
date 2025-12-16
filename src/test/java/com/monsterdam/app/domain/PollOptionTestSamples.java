package com.monsterdam.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PollOptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PollOption getPollOptionSample1() {
        return new PollOption().id(1L);
    }

    public static PollOption getPollOptionSample2() {
        return new PollOption().id(2L);
    }

    public static PollOption getPollOptionRandomSampleGenerator() {
        return new PollOption().id(longCount.incrementAndGet());
    }
}
