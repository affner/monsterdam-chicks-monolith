package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.ChatRoomAsserts.*;
import static com.monsterdam.app.domain.ChatRoomTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatRoomMapperTest {

    private ChatRoomMapper chatRoomMapper;

    @BeforeEach
    void setUp() {
        chatRoomMapper = new ChatRoomMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChatRoomSample1();
        var actual = chatRoomMapper.toEntity(chatRoomMapper.toDto(expected));
        assertChatRoomAllPropertiesEquals(expected, actual);
    }
}
