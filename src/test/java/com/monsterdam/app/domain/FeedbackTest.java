package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.FeedbackTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feedback.class);
        Feedback feedback1 = getFeedbackSample1();
        Feedback feedback2 = new Feedback();
        assertThat(feedback1).isNotEqualTo(feedback2);

        feedback2.setId(feedback1.getId());
        assertThat(feedback1).isEqualTo(feedback2);

        feedback2 = getFeedbackSample2();
        assertThat(feedback1).isNotEqualTo(feedback2);
    }

    @Test
    void creatorTest() {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        feedback.setCreator(userLiteBack);
        assertThat(feedback.getCreator()).isEqualTo(userLiteBack);

        feedback.creator(null);
        assertThat(feedback.getCreator()).isNull();
    }
}
