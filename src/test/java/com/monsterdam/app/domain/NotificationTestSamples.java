package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Notification getNotificationSample1() {
        return new Notification()
            .id(1L)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .commentedUserId(1L)
            .messagedUserId(1L)
            .mentionerIdInPost(1L)
            .mentionerIdInComment(1L);
    }

    public static Notification getNotificationSample2() {
        return new Notification()
            .id(2L)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .commentedUserId(2L)
            .messagedUserId(2L)
            .mentionerIdInPost(2L)
            .mentionerIdInComment(2L);
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .commentedUserId(longCount.incrementAndGet())
            .messagedUserId(longCount.incrementAndGet())
            .mentionerIdInPost(longCount.incrementAndGet())
            .mentionerIdInComment(longCount.incrementAndGet());
    }
}
