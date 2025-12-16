package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.ContentPackageTestSamples.*;
import static com.monsterdam.app.domain.PaymentTestSamples.*;
import static com.monsterdam.app.domain.PurchasedContentTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
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
    void contentPackageTest() {
        PurchasedContent purchasedContent = getPurchasedContentRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        purchasedContent.setContentPackage(contentPackageBack);
        assertThat(purchasedContent.getContentPackage()).isEqualTo(contentPackageBack);

        purchasedContent.contentPackage(null);
        assertThat(purchasedContent.getContentPackage()).isNull();
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

    @Test
    void viewerTest() {
        PurchasedContent purchasedContent = getPurchasedContentRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        purchasedContent.setViewer(userLiteBack);
        assertThat(purchasedContent.getViewer()).isEqualTo(userLiteBack);

        purchasedContent.viewer(null);
        assertThat(purchasedContent.getViewer()).isNull();
    }

    @Test
    void creatorTest() {
        PurchasedContent purchasedContent = getPurchasedContentRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        purchasedContent.setCreator(userLiteBack);
        assertThat(purchasedContent.getCreator()).isEqualTo(userLiteBack);

        purchasedContent.creator(null);
        assertThat(purchasedContent.getCreator()).isNull();
    }
}
