package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlatformAdminUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PlatformAdminUser getPlatformAdminUserSample1() {
        return new PlatformAdminUser()
            .id(1L)
            .fullName("fullName1")
            .emailAddress("emailAddress1")
            .nickName("nickName1")
            .mobilePhone("mobilePhone1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static PlatformAdminUser getPlatformAdminUserSample2() {
        return new PlatformAdminUser()
            .id(2L)
            .fullName("fullName2")
            .emailAddress("emailAddress2")
            .nickName("nickName2")
            .mobilePhone("mobilePhone2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static PlatformAdminUser getPlatformAdminUserRandomSampleGenerator() {
        return new PlatformAdminUser()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .emailAddress(UUID.randomUUID().toString())
            .nickName(UUID.randomUUID().toString())
            .mobilePhone(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
