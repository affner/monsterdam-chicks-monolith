package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.TrialLinkTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrialLinkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrialLink.class);
        TrialLink trialLink1 = getTrialLinkSample1();
        TrialLink trialLink2 = new TrialLink();
        assertThat(trialLink1).isNotEqualTo(trialLink2);

        trialLink2.setId(trialLink1.getId());
        assertThat(trialLink1).isEqualTo(trialLink2);

        trialLink2 = getTrialLinkSample2();
        assertThat(trialLink1).isNotEqualTo(trialLink2);
    }

    @Test
    void creatorTest() {
        TrialLink trialLink = getTrialLinkRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        trialLink.setCreator(userLiteBack);
        assertThat(trialLink.getCreator()).isEqualTo(userLiteBack);

        trialLink.creator(null);
        assertThat(trialLink.getCreator()).isNull();
    }
}
