package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PaymentTestSamples.*;
import static com.monsterdam.app.domain.PurchasedContentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchasedContentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasedContent.class);
        PurchasedContent purchasedContent1 = getPurchasedContentSample1();
        PurchasedContent purchasedContent2 = new PurchasedContent();
        assertThat(purchasedContent1).isNotEqualTo(purchasedContent2);

        purchasedContent2.setId(purchasedContent1.getId());
        assertThat(purchasedContent1).isEqualTo(purchasedContent2);

        purchasedContent2 = getPurchasedContentSample2();
        assertThat(purchasedContent1).isNotEqualTo(purchasedContent2);
    }

    @Test
    void paymentTest() {
        PurchasedContent purchasedContent = getPurchasedContentRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        purchasedContent.setPayment(paymentBack);
        assertThat(purchasedContent.getPayment()).isEqualTo(paymentBack);

        purchasedContent.payment(null);
        assertThat(purchasedContent.getPayment()).isNull();
    }
}
