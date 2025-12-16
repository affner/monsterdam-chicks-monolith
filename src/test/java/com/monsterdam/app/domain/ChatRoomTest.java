package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.ChatRoomTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ChatRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatRoom.class);
        ChatRoom chatRoom1 = getChatRoomSample1();
        ChatRoom chatRoom2 = new ChatRoom();
        assertThat(chatRoom1).isNotEqualTo(chatRoom2);

        chatRoom2.setId(chatRoom1.getId());
        assertThat(chatRoom1).isEqualTo(chatRoom2);

        chatRoom2 = getChatRoomSample2();
        assertThat(chatRoom1).isNotEqualTo(chatRoom2);
    }

    @Test
    void participantsTest() {
        ChatRoom chatRoom = getChatRoomRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        chatRoom.addParticipants(userLiteBack);
        assertThat(chatRoom.getParticipants()).containsOnly(userLiteBack);

        chatRoom.removeParticipants(userLiteBack);
        assertThat(chatRoom.getParticipants()).doesNotContain(userLiteBack);

        chatRoom.participants(new HashSet<>(Set.of(userLiteBack)));
        assertThat(chatRoom.getParticipants()).containsOnly(userLiteBack);

        chatRoom.setParticipants(new HashSet<>());
        assertThat(chatRoom.getParticipants()).doesNotContain(userLiteBack);
    }
}
