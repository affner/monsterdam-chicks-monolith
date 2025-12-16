package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.SubscriptionPlanOfferTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionPlanOfferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionPlanOffer.class);
        SubscriptionPlanOffer subscriptionPlanOffer1 = getSubscriptionPlanOfferSample1();
        SubscriptionPlanOffer subscriptionPlanOffer2 = new SubscriptionPlanOffer();
        assertThat(subscriptionPlanOffer1).isNotEqualTo(subscriptionPlanOffer2);

        subscriptionPlanOffer2.setId(subscriptionPlanOffer1.getId());
        assertThat(subscriptionPlanOffer1).isEqualTo(subscriptionPlanOffer2);

        subscriptionPlanOffer2 = getSubscriptionPlanOfferSample2();
        assertThat(subscriptionPlanOffer1).isNotEqualTo(subscriptionPlanOffer2);
    }

    @Test
    void creatorTest() {
        SubscriptionPlanOffer subscriptionPlanOffer = getSubscriptionPlanOfferRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        subscriptionPlanOffer.setCreator(userLiteBack);
        assertThat(subscriptionPlanOffer.getCreator()).isEqualTo(userLiteBack);

        subscriptionPlanOffer.creator(null);
        assertThat(subscriptionPlanOffer.getCreator()).isNull();
    }
}
