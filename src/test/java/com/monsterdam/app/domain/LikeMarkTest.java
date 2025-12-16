package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.LikeMarkTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeMarkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeMark.class);
        LikeMark likeMark1 = getLikeMarkSample1();
        LikeMark likeMark2 = new LikeMark();
        assertThat(likeMark1).isNotEqualTo(likeMark2);

        likeMark2.setId(likeMark1.getId());
        assertThat(likeMark1).isEqualTo(likeMark2);

        likeMark2 = getLikeMarkSample2();
        assertThat(likeMark1).isNotEqualTo(likeMark2);
    }

    @Test
    void likerTest() {
        LikeMark likeMark = getLikeMarkRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        likeMark.setLiker(userLiteBack);
        assertThat(likeMark.getLiker()).isEqualTo(userLiteBack);

        likeMark.liker(null);
        assertThat(likeMark.getLiker()).isNull();
    }
}
