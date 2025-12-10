package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.ChatRoomTestSamples.*;
import static com.monsterdam.app.domain.DirectMessageTestSamples.*;
import static com.monsterdam.app.domain.DirectMessageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DirectMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DirectMessage.class);
        DirectMessage directMessage1 = getDirectMessageSample1();
        DirectMessage directMessage2 = new DirectMessage();
        assertThat(directMessage1).isNotEqualTo(directMessage2);

        directMessage2.setId(directMessage1.getId());
        assertThat(directMessage1).isEqualTo(directMessage2);

        directMessage2 = getDirectMessageSample2();
        assertThat(directMessage1).isNotEqualTo(directMessage2);
    }

    @Test
    void responseToTest() {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        directMessage.setResponseTo(directMessageBack);
        assertThat(directMessage.getResponseTo()).isEqualTo(directMessageBack);

        directMessage.responseTo(null);
        assertThat(directMessage.getResponseTo()).isNull();
    }

    @Test
    void chatRoomTest() {
        DirectMessage directMessage = getDirectMessageRandomSampleGenerator();
        ChatRoom chatRoomBack = getChatRoomRandomSampleGenerator();

        directMessage.setChatRoom(chatRoomBack);
        assertThat(directMessage.getChatRoom()).isEqualTo(chatRoomBack);

        directMessage.chatRoom(null);
        assertThat(directMessage.getChatRoom()).isNull();
    }
}
