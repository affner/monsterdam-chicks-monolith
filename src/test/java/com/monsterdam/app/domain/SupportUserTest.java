package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.SupportUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupportUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupportUser.class);
        SupportUser supportUser1 = getSupportUserSample1();
        SupportUser supportUser2 = new SupportUser();
        assertThat(supportUser1).isNotEqualTo(supportUser2);

        supportUser2.setId(supportUser1.getId());
        assertThat(supportUser1).isEqualTo(supportUser2);

        supportUser2 = getSupportUserSample2();
        assertThat(supportUser1).isNotEqualTo(supportUser2);
    }
}
