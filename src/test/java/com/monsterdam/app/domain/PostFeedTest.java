package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PostFeedTestSamples.*;
import static com.monsterdam.app.domain.PostPollTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PostFeedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostFeed.class);
        PostFeed postFeed1 = getPostFeedSample1();
        PostFeed postFeed2 = new PostFeed();
        assertThat(postFeed1).isNotEqualTo(postFeed2);

        postFeed2.setId(postFeed1.getId());
        assertThat(postFeed1).isEqualTo(postFeed2);

        postFeed2 = getPostFeedSample2();
        assertThat(postFeed1).isNotEqualTo(postFeed2);
    }

    @Test
    void creatorTest() {
        PostFeed postFeed = getPostFeedRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        postFeed.setCreator(userLiteBack);
        assertThat(postFeed.getCreator()).isEqualTo(userLiteBack);

        postFeed.creator(null);
        assertThat(postFeed.getCreator()).isNull();
    }

    @Test
    void pollsTest() {
        PostFeed postFeed = getPostFeedRandomSampleGenerator();
        PostPoll postPollBack = getPostPollRandomSampleGenerator();

        postFeed.addPolls(postPollBack);
        assertThat(postFeed.getPolls()).containsOnly(postPollBack);
        assertThat(postPollBack.getPost()).isEqualTo(postFeed);

        postFeed.removePolls(postPollBack);
        assertThat(postFeed.getPolls()).doesNotContain(postPollBack);
        assertThat(postPollBack.getPost()).isNull();

        postFeed.polls(new HashSet<>(Set.of(postPollBack)));
        assertThat(postFeed.getPolls()).containsOnly(postPollBack);
        assertThat(postPollBack.getPost()).isEqualTo(postFeed);

        postFeed.setPolls(new HashSet<>());
        assertThat(postFeed.getPolls()).doesNotContain(postPollBack);
        assertThat(postPollBack.getPost()).isNull();
    }
}
