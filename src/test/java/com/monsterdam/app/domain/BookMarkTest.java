package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.BookMarkTestSamples.*;
import static com.monsterdam.app.domain.DirectMessageTestSamples.*;
import static com.monsterdam.app.domain.PostFeedTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookMarkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookMark.class);
        BookMark bookMark1 = getBookMarkSample1();
        BookMark bookMark2 = new BookMark();
        assertThat(bookMark1).isNotEqualTo(bookMark2);

        bookMark2.setId(bookMark1.getId());
        assertThat(bookMark1).isEqualTo(bookMark2);

        bookMark2 = getBookMarkSample2();
        assertThat(bookMark1).isNotEqualTo(bookMark2);
    }

    @Test
    void userTest() {
        BookMark bookMark = getBookMarkRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        bookMark.setUser(userLiteBack);
        assertThat(bookMark.getUser()).isEqualTo(userLiteBack);

        bookMark.user(null);
        assertThat(bookMark.getUser()).isNull();
    }

    @Test
    void postTest() {
        BookMark bookMark = getBookMarkRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        bookMark.setPost(postFeedBack);
        assertThat(bookMark.getPost()).isEqualTo(postFeedBack);

        bookMark.post(null);
        assertThat(bookMark.getPost()).isNull();
    }

    @Test
    void messageTest() {
        BookMark bookMark = getBookMarkRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        bookMark.setMessage(directMessageBack);
        assertThat(bookMark.getMessage()).isEqualTo(directMessageBack);

        bookMark.message(null);
        assertThat(bookMark.getMessage()).isNull();
    }
}
