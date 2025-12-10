package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.ContentPackageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
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
}
