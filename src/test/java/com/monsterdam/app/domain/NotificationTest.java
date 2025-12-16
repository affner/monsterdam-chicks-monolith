package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.DirectMessageTestSamples.*;
import static com.monsterdam.app.domain.NotificationTestSamples.*;
import static com.monsterdam.app.domain.PostCommentTestSamples.*;
import static com.monsterdam.app.domain.PostFeedTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void commentTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        PostComment postCommentBack = getPostCommentRandomSampleGenerator();

        notification.setComment(postCommentBack);
        assertThat(notification.getComment()).isEqualTo(postCommentBack);

        notification.comment(null);
        assertThat(notification.getComment()).isNull();
    }

    @Test
    void postTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        notification.setPost(postFeedBack);
        assertThat(notification.getPost()).isEqualTo(postFeedBack);

        notification.post(null);
        assertThat(notification.getPost()).isNull();
    }

    @Test
    void messageTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        notification.setMessage(directMessageBack);
        assertThat(notification.getMessage()).isEqualTo(directMessageBack);

        notification.message(null);
        assertThat(notification.getMessage()).isNull();
    }

    @Test
    void recipientTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        notification.setRecipient(userLiteBack);
        assertThat(notification.getRecipient()).isEqualTo(userLiteBack);

        notification.recipient(null);
        assertThat(notification.getRecipient()).isNull();
    }

    @Test
    void actorTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        notification.setActor(userLiteBack);
        assertThat(notification.getActor()).isEqualTo(userLiteBack);

        notification.actor(null);
        assertThat(notification.getActor()).isNull();
    }
}
