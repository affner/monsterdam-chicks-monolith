package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.CountryTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static com.monsterdam.app.domain.UserProfileTestSamples.*;
import static com.monsterdam.app.domain.UserSettingsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserLiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserLite.class);
        UserLite userLite1 = getUserLiteSample1();
        UserLite userLite2 = new UserLite();
        assertThat(userLite1).isNotEqualTo(userLite2);

        userLite2.setId(userLite1.getId());
        assertThat(userLite1).isEqualTo(userLite2);

        userLite2 = getUserLiteSample2();
        assertThat(userLite1).isNotEqualTo(userLite2);
    }

    @Test
    void profileTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userLite.setProfile(userProfileBack);
        assertThat(userLite.getProfile()).isEqualTo(userProfileBack);

        userLite.profile(null);
        assertThat(userLite.getProfile()).isNull();
    }

    @Test
    void settingsTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        UserSettings userSettingsBack = getUserSettingsRandomSampleGenerator();

        userLite.setSettings(userSettingsBack);
        assertThat(userLite.getSettings()).isEqualTo(userSettingsBack);

        userLite.settings(null);
        assertThat(userLite.getSettings()).isNull();
    }

    @Test
    void countryOfBirthTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        userLite.setCountryOfBirth(countryBack);
        assertThat(userLite.getCountryOfBirth()).isEqualTo(countryBack);

        userLite.countryOfBirth(null);
        assertThat(userLite.getCountryOfBirth()).isNull();
    }
}
