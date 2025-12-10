package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PlatformAdminUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlatformAdminUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlatformAdminUser.class);
        PlatformAdminUser platformAdminUser1 = getPlatformAdminUserSample1();
        PlatformAdminUser platformAdminUser2 = new PlatformAdminUser();
        assertThat(platformAdminUser1).isNotEqualTo(platformAdminUser2);

        platformAdminUser2.setId(platformAdminUser1.getId());
        assertThat(platformAdminUser1).isEqualTo(platformAdminUser2);

        platformAdminUser2 = getPlatformAdminUserSample2();
        assertThat(platformAdminUser1).isNotEqualTo(platformAdminUser2);
    }
}
