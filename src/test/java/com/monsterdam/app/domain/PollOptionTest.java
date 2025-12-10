package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PollOptionTestSamples.*;
import static com.monsterdam.app.domain.PostPollTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PollOptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PollOption.class);
        PollOption pollOption1 = getPollOptionSample1();
        PollOption pollOption2 = new PollOption();
        assertThat(pollOption1).isNotEqualTo(pollOption2);

        pollOption2.setId(pollOption1.getId());
        assertThat(pollOption1).isEqualTo(pollOption2);

        pollOption2 = getPollOptionSample2();
        assertThat(pollOption1).isNotEqualTo(pollOption2);
    }

    @Test
    void pollTest() {
        PollOption pollOption = getPollOptionRandomSampleGenerator();
        PostPoll postPollBack = getPostPollRandomSampleGenerator();

        pollOption.setPoll(postPollBack);
        assertThat(pollOption.getPoll()).isEqualTo(postPollBack);

        pollOption.poll(null);
        assertThat(pollOption.getPoll()).isNull();
    }
}
