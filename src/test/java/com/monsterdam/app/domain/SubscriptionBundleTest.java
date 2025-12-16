package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.SubscriptionBundleTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionBundleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionBundle.class);
        SubscriptionBundle subscriptionBundle1 = getSubscriptionBundleSample1();
        SubscriptionBundle subscriptionBundle2 = new SubscriptionBundle();
        assertThat(subscriptionBundle1).isNotEqualTo(subscriptionBundle2);

        subscriptionBundle2.setId(subscriptionBundle1.getId());
        assertThat(subscriptionBundle1).isEqualTo(subscriptionBundle2);

        subscriptionBundle2 = getSubscriptionBundleSample2();
        assertThat(subscriptionBundle1).isNotEqualTo(subscriptionBundle2);
    }

    @Test
    void creatorTest() {
        SubscriptionBundle subscriptionBundle = getSubscriptionBundleRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        subscriptionBundle.setCreator(userLiteBack);
        assertThat(subscriptionBundle.getCreator()).isEqualTo(userLiteBack);

        subscriptionBundle.creator(null);
        assertThat(subscriptionBundle.getCreator()).isNull();
    }
}
