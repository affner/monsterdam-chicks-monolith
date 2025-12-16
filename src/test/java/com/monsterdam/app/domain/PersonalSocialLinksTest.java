package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PersonalSocialLinksTestSamples.*;
import static com.monsterdam.app.domain.SocialNetworkTestSamples.*;
import static com.monsterdam.app.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalSocialLinksTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalSocialLinks.class);
        PersonalSocialLinks personalSocialLinks1 = getPersonalSocialLinksSample1();
        PersonalSocialLinks personalSocialLinks2 = new PersonalSocialLinks();
        assertThat(personalSocialLinks1).isNotEqualTo(personalSocialLinks2);

        personalSocialLinks2.setId(personalSocialLinks1.getId());
        assertThat(personalSocialLinks1).isEqualTo(personalSocialLinks2);

        personalSocialLinks2 = getPersonalSocialLinksSample2();
        assertThat(personalSocialLinks1).isNotEqualTo(personalSocialLinks2);
    }

    @Test
    void socialNetworkTest() {
        PersonalSocialLinks personalSocialLinks = getPersonalSocialLinksRandomSampleGenerator();
        SocialNetwork socialNetworkBack = getSocialNetworkRandomSampleGenerator();

        personalSocialLinks.setSocialNetwork(socialNetworkBack);
        assertThat(personalSocialLinks.getSocialNetwork()).isEqualTo(socialNetworkBack);

        personalSocialLinks.socialNetwork(null);
        assertThat(personalSocialLinks.getSocialNetwork()).isNull();
    }

    @Test
    void userProfileTest() {
        PersonalSocialLinks personalSocialLinks = getPersonalSocialLinksRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        personalSocialLinks.setUserProfile(userProfileBack);
        assertThat(personalSocialLinks.getUserProfile()).isEqualTo(userProfileBack);

        personalSocialLinks.userProfile(null);
        assertThat(personalSocialLinks.getUserProfile()).isNull();
    }
}
