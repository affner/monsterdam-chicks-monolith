package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.SpecialAwardTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialAwardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialAward.class);
        SpecialAward specialAward1 = getSpecialAwardSample1();
        SpecialAward specialAward2 = new SpecialAward();
        assertThat(specialAward1).isNotEqualTo(specialAward2);

        specialAward2.setId(specialAward1.getId());
        assertThat(specialAward1).isEqualTo(specialAward2);

        specialAward2 = getSpecialAwardSample2();
        assertThat(specialAward1).isNotEqualTo(specialAward2);
    }

    @Test
    void creatorTest() {
        SpecialAward specialAward = getSpecialAwardRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        specialAward.setCreator(userLiteBack);
        assertThat(specialAward.getCreator()).isEqualTo(userLiteBack);

        specialAward.creator(null);
        assertThat(specialAward.getCreator()).isNull();
    }

    @Test
    void viewerTest() {
        SpecialAward specialAward = getSpecialAwardRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        specialAward.setViewer(userLiteBack);
        assertThat(specialAward.getViewer()).isEqualTo(userLiteBack);

        specialAward.viewer(null);
        assertThat(specialAward.getViewer()).isNull();
    }
}
