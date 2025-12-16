package com.monsterdam.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChatRoomTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChatRoom getChatRoomSample1() {
        return new ChatRoom().id(1L).title("title1").lastAction("lastAction1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static ChatRoom getChatRoomSample2() {
        return new ChatRoom().id(2L).title("title2").lastAction("lastAction2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static ChatRoom getChatRoomRandomSampleGenerator() {
        return new ChatRoom()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .lastAction(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
