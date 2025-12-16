package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PostFeedTestSamples.*;
import static com.monsterdam.app.domain.PostPollTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostPollTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostPoll.class);
        PostPoll postPoll1 = getPostPollSample1();
        PostPoll postPoll2 = new PostPoll();
        assertThat(postPoll1).isNotEqualTo(postPoll2);

        postPoll2.setId(postPoll1.getId());
        assertThat(postPoll1).isEqualTo(postPoll2);

        postPoll2 = getPostPollSample2();
        assertThat(postPoll1).isNotEqualTo(postPoll2);
    }

    @Test
    void postTest() {
        PostPoll postPoll = getPostPollRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        postPoll.setPost(postFeedBack);
        assertThat(postPoll.getPost()).isEqualTo(postFeedBack);

        postPoll.post(null);
        assertThat(postPoll.getPost()).isNull();
    }
}
