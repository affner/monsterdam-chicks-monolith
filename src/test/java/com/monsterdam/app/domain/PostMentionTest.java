package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PostFeedTestSamples.*;
import static com.monsterdam.app.domain.PostMentionTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostMentionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostMention.class);
        PostMention postMention1 = getPostMentionSample1();
        PostMention postMention2 = new PostMention();
        assertThat(postMention1).isNotEqualTo(postMention2);

        postMention2.setId(postMention1.getId());
        assertThat(postMention1).isEqualTo(postMention2);

        postMention2 = getPostMentionSample2();
        assertThat(postMention1).isNotEqualTo(postMention2);
    }

    @Test
    void originPostTest() {
        PostMention postMention = getPostMentionRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        postMention.setOriginPost(postFeedBack);
        assertThat(postMention.getOriginPost()).isEqualTo(postFeedBack);

        postMention.originPost(null);
        assertThat(postMention.getOriginPost()).isNull();
    }

    @Test
    void mentionedUserTest() {
        PostMention postMention = getPostMentionRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        postMention.setMentionedUser(userLiteBack);
        assertThat(postMention.getMentionedUser()).isEqualTo(userLiteBack);

        postMention.mentionedUser(null);
        assertThat(postMention.getMentionedUser()).isNull();
    }
}
