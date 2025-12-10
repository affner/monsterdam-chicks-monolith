package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.UserEventTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserEvent.class);
        UserEvent userEvent1 = getUserEventSample1();
        UserEvent userEvent2 = new UserEvent();
        assertThat(userEvent1).isNotEqualTo(userEvent2);

        userEvent2.setId(userEvent1.getId());
        assertThat(userEvent1).isEqualTo(userEvent2);

        userEvent2 = getUserEventSample2();
        assertThat(userEvent1).isNotEqualTo(userEvent2);
    }

    @Test
    void creatorTest() {
        UserEvent userEvent = getUserEventRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        userEvent.setCreator(userLiteBack);
        assertThat(userEvent.getCreator()).isEqualTo(userLiteBack);

        userEvent.creator(null);
        assertThat(userEvent.getCreator()).isNull();
    }
}
