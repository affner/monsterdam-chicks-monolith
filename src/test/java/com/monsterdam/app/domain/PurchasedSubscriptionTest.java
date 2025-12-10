package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PaymentTestSamples.*;
import static com.monsterdam.app.domain.PurchasedSubscriptionTestSamples.*;
import static com.monsterdam.app.domain.SubscriptionPlanOfferTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedSubscription.class);
        PurchasedSubscription purchasedSubscription1 = getPurchasedSubscriptionSample1();
        PurchasedSubscription purchasedSubscription2 = new PurchasedSubscription();
        assertThat(purchasedSubscription1).isNotEqualTo(purchasedSubscription2);

        purchasedSubscription2.setId(purchasedSubscription1.getId());
        assertThat(purchasedSubscription1).isEqualTo(purchasedSubscription2);

        purchasedSubscription2 = getPurchasedSubscriptionSample2();
        assertThat(purchasedSubscription1).isNotEqualTo(purchasedSubscription2);
    }

    @Test
    void paymentTest() {
        PurchasedSubscription purchasedSubscription = getPurchasedSubscriptionRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        purchasedSubscription.setPayment(paymentBack);
        assertThat(purchasedSubscription.getPayment()).isEqualTo(paymentBack);

        purchasedSubscription.payment(null);
        assertThat(purchasedSubscription.getPayment()).isNull();
    }

    @Test
    void subscriptionPlanOfferTest() {
        PurchasedSubscription purchasedSubscription = getPurchasedSubscriptionRandomSampleGenerator();
        SubscriptionPlanOffer subscriptionPlanOfferBack = getSubscriptionPlanOfferRandomSampleGenerator();

        purchasedSubscription.setSubscriptionPlanOffer(subscriptionPlanOfferBack);
        assertThat(purchasedSubscription.getSubscriptionPlanOffer()).isEqualTo(subscriptionPlanOfferBack);

        purchasedSubscription.subscriptionPlanOffer(null);
        assertThat(purchasedSubscription.getSubscriptionPlanOffer()).isNull();
    }
}
