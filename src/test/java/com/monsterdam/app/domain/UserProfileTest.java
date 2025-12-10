package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.StateTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static com.monsterdam.app.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void stateOfResidenceTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        State stateBack = getStateRandomSampleGenerator();

        userProfile.setStateOfResidence(stateBack);
        assertThat(userProfile.getStateOfResidence()).isEqualTo(stateBack);

        userProfile.stateOfResidence(null);
        assertThat(userProfile.getStateOfResidence()).isNull();
    }

    @Test
    void userTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        userProfile.setUser(userLiteBack);
        assertThat(userProfile.getUser()).isEqualTo(userLiteBack);
        assertThat(userLiteBack.getProfile()).isEqualTo(userProfile);

        userProfile.user(null);
        assertThat(userProfile.getUser()).isNull();
        assertThat(userLiteBack.getProfile()).isNull();
    }
}
