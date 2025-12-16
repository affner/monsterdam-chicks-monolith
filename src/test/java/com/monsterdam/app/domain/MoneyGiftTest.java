package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.MoneyGiftTestSamples.*;
import static com.monsterdam.app.domain.PaymentTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyGiftTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyGift.class);
        MoneyGift moneyGift1 = getMoneyGiftSample1();
        MoneyGift moneyGift2 = new MoneyGift();
        assertThat(moneyGift1).isNotEqualTo(moneyGift2);

        moneyGift2.setId(moneyGift1.getId());
        assertThat(moneyGift1).isEqualTo(moneyGift2);

        moneyGift2 = getMoneyGiftSample2();
        assertThat(moneyGift1).isNotEqualTo(moneyGift2);
    }

    @Test
    void paymentTest() {
        MoneyGift moneyGift = getMoneyGiftRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        moneyGift.setPayment(paymentBack);
        assertThat(moneyGift.getPayment()).isEqualTo(paymentBack);

        moneyGift.payment(null);
        assertThat(moneyGift.getPayment()).isNull();
    }

    @Test
    void viewerTest() {
        MoneyGift moneyGift = getMoneyGiftRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        moneyGift.setViewer(userLiteBack);
        assertThat(moneyGift.getViewer()).isEqualTo(userLiteBack);

        moneyGift.viewer(null);
        assertThat(moneyGift.getViewer()).isNull();
    }

    @Test
    void creatorTest() {
        MoneyGift moneyGift = getMoneyGiftRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        moneyGift.setCreator(userLiteBack);
        assertThat(moneyGift.getCreator()).isEqualTo(userLiteBack);

        moneyGift.creator(null);
        assertThat(moneyGift.getCreator()).isNull();
    }
}
