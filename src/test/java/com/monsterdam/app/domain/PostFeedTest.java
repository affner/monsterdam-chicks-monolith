package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PostFeedTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
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
}
