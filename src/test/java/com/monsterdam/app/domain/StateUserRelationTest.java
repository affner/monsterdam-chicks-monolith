package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.StateTestSamples.*;
import static com.monsterdam.app.domain.StateUserRelationTestSamples.*;
import static com.monsterdam.app.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StateUserRelationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateUserRelation.class);
        StateUserRelation stateUserRelation1 = getStateUserRelationSample1();
        StateUserRelation stateUserRelation2 = new StateUserRelation();
        assertThat(stateUserRelation1).isNotEqualTo(stateUserRelation2);

        stateUserRelation2.setId(stateUserRelation1.getId());
        assertThat(stateUserRelation1).isEqualTo(stateUserRelation2);

        stateUserRelation2 = getStateUserRelationSample2();
        assertThat(stateUserRelation1).isNotEqualTo(stateUserRelation2);
    }

    @Test
    void stateTest() {
        StateUserRelation stateUserRelation = getStateUserRelationRandomSampleGenerator();
        State stateBack = getStateRandomSampleGenerator();

        stateUserRelation.setState(stateBack);
        assertThat(stateUserRelation.getState()).isEqualTo(stateBack);

        stateUserRelation.state(null);
        assertThat(stateUserRelation.getState()).isNull();
    }

    @Test
    void userProfileTest() {
        StateUserRelation stateUserRelation = getStateUserRelationRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        stateUserRelation.setUserProfile(userProfileBack);
        assertThat(stateUserRelation.getUserProfile()).isEqualTo(userProfileBack);

        stateUserRelation.userProfile(null);
        assertThat(stateUserRelation.getUserProfile()).isNull();
    }
}
