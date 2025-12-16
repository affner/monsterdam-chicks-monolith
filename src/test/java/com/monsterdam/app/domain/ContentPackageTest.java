package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.ContentPackageTestSamples.*;
import static com.monsterdam.app.domain.PurchasedContentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContentPackageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentPackage.class);
        ContentPackage contentPackage1 = getContentPackageSample1();
        ContentPackage contentPackage2 = new ContentPackage();
        assertThat(contentPackage1).isNotEqualTo(contentPackage2);

        contentPackage2.setId(contentPackage1.getId());
        assertThat(contentPackage1).isEqualTo(contentPackage2);

        contentPackage2 = getContentPackageSample2();
        assertThat(contentPackage1).isNotEqualTo(contentPackage2);
    }

    @Test
    void purchasesTest() {
        ContentPackage contentPackage = getContentPackageRandomSampleGenerator();
        PurchasedContent purchasedContentBack = getPurchasedContentRandomSampleGenerator();

        contentPackage.addPurchases(purchasedContentBack);
        assertThat(contentPackage.getPurchases()).containsOnly(purchasedContentBack);
        assertThat(purchasedContentBack.getContentPackage()).isEqualTo(contentPackage);

        contentPackage.removePurchases(purchasedContentBack);
        assertThat(contentPackage.getPurchases()).doesNotContain(purchasedContentBack);
        assertThat(purchasedContentBack.getContentPackage()).isNull();

        contentPackage.purchases(new HashSet<>(Set.of(purchasedContentBack)));
        assertThat(contentPackage.getPurchases()).containsOnly(purchasedContentBack);
        assertThat(purchasedContentBack.getContentPackage()).isEqualTo(contentPackage);

        contentPackage.setPurchases(new HashSet<>());
        assertThat(contentPackage.getPurchases()).doesNotContain(purchasedContentBack);
        assertThat(purchasedContentBack.getContentPackage()).isNull();
    }
}
