package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PostCommentTestSamples.*;
import static com.monsterdam.app.domain.PostCommentTestSamples.*;
import static com.monsterdam.app.domain.PostFeedTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostCommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostComment.class);
        PostComment postComment1 = getPostCommentSample1();
        PostComment postComment2 = new PostComment();
        assertThat(postComment1).isNotEqualTo(postComment2);

        postComment2.setId(postComment1.getId());
        assertThat(postComment1).isEqualTo(postComment2);

        postComment2 = getPostCommentSample2();
        assertThat(postComment1).isNotEqualTo(postComment2);
    }

    @Test
    void postTest() {
        PostComment postComment = getPostCommentRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        postComment.setPost(postFeedBack);
        assertThat(postComment.getPost()).isEqualTo(postFeedBack);

        postComment.post(null);
        assertThat(postComment.getPost()).isNull();
    }

    @Test
    void responseToTest() {
        PostComment postComment = getPostCommentRandomSampleGenerator();
        PostComment postCommentBack = getPostCommentRandomSampleGenerator();

        postComment.setResponseTo(postCommentBack);
        assertThat(postComment.getResponseTo()).isEqualTo(postCommentBack);

        postComment.responseTo(null);
        assertThat(postComment.getResponseTo()).isNull();
    }

    @Test
    void commenterTest() {
        PostComment postComment = getPostCommentRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        postComment.setCommenter(userLiteBack);
        assertThat(postComment.getCommenter()).isEqualTo(userLiteBack);

        postComment.commenter(null);
        assertThat(postComment.getCommenter()).isNull();
    }
}
