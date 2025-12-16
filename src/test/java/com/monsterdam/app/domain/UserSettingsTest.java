package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static com.monsterdam.app.domain.UserSettingsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSettings.class);
        UserSettings userSettings1 = getUserSettingsSample1();
        UserSettings userSettings2 = new UserSettings();
        assertThat(userSettings1).isNotEqualTo(userSettings2);

        userSettings2.setId(userSettings1.getId());
        assertThat(userSettings1).isEqualTo(userSettings2);

        userSettings2 = getUserSettingsSample2();
        assertThat(userSettings1).isNotEqualTo(userSettings2);
    }

    @Test
    void userTest() {
        UserSettings userSettings = getUserSettingsRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        userSettings.setUser(userLiteBack);
        assertThat(userSettings.getUser()).isEqualTo(userLiteBack);
        assertThat(userLiteBack.getSettings()).isEqualTo(userSettings);

        userSettings.user(null);
        assertThat(userSettings.getUser()).isNull();
        assertThat(userLiteBack.getSettings()).isNull();
    }
}
