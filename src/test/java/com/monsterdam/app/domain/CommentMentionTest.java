package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.CommentMentionTestSamples.*;
import static com.monsterdam.app.domain.PostCommentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentMentionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentMention.class);
        CommentMention commentMention1 = getCommentMentionSample1();
        CommentMention commentMention2 = new CommentMention();
        assertThat(commentMention1).isNotEqualTo(commentMention2);

        commentMention2.setId(commentMention1.getId());
        assertThat(commentMention1).isEqualTo(commentMention2);

        commentMention2 = getCommentMentionSample2();
        assertThat(commentMention1).isNotEqualTo(commentMention2);
    }

    @Test
    void originPostCommentTest() {
        CommentMention commentMention = getCommentMentionRandomSampleGenerator();
        PostComment postCommentBack = getPostCommentRandomSampleGenerator();

        commentMention.setOriginPostComment(postCommentBack);
        assertThat(commentMention.getOriginPostComment()).isEqualTo(postCommentBack);

        commentMention.originPostComment(null);
        assertThat(commentMention.getOriginPostComment()).isNull();
    }
}
