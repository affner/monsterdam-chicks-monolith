package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.ContentPackageTestSamples.*;
import static com.monsterdam.app.domain.SinglePhotoTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SinglePhotoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SinglePhoto.class);
        SinglePhoto singlePhoto1 = getSinglePhotoSample1();
        SinglePhoto singlePhoto2 = new SinglePhoto();
        assertThat(singlePhoto1).isNotEqualTo(singlePhoto2);

        singlePhoto2.setId(singlePhoto1.getId());
        assertThat(singlePhoto1).isEqualTo(singlePhoto2);

        singlePhoto2 = getSinglePhotoSample2();
        assertThat(singlePhoto1).isNotEqualTo(singlePhoto2);
    }

    @Test
    void contentPackageTest() {
        SinglePhoto singlePhoto = getSinglePhotoRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        singlePhoto.setContentPackage(contentPackageBack);
        assertThat(singlePhoto.getContentPackage()).isEqualTo(contentPackageBack);

        singlePhoto.contentPackage(null);
        assertThat(singlePhoto.getContentPackage()).isNull();
    }

    @Test
    void creatorTest() {
        SinglePhoto singlePhoto = getSinglePhotoRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        singlePhoto.setCreator(userLiteBack);
        assertThat(singlePhoto.getCreator()).isEqualTo(userLiteBack);

        singlePhoto.creator(null);
        assertThat(singlePhoto.getCreator()).isNull();
    }
}
