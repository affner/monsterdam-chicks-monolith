package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.ContentPackageTestSamples.*;
import static com.monsterdam.app.domain.SingleVideoTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SingleVideoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SingleVideo.class);
        SingleVideo singleVideo1 = getSingleVideoSample1();
        SingleVideo singleVideo2 = new SingleVideo();
        assertThat(singleVideo1).isNotEqualTo(singleVideo2);

        singleVideo2.setId(singleVideo1.getId());
        assertThat(singleVideo1).isEqualTo(singleVideo2);

        singleVideo2 = getSingleVideoSample2();
        assertThat(singleVideo1).isNotEqualTo(singleVideo2);
    }

    @Test
    void contentPackageTest() {
        SingleVideo singleVideo = getSingleVideoRandomSampleGenerator();
        ContentPackage contentPackageBack = getContentPackageRandomSampleGenerator();

        singleVideo.setContentPackage(contentPackageBack);
        assertThat(singleVideo.getContentPackage()).isEqualTo(contentPackageBack);

        singleVideo.contentPackage(null);
        assertThat(singleVideo.getContentPackage()).isNull();
    }

    @Test
    void creatorTest() {
        SingleVideo singleVideo = getSingleVideoRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        singleVideo.setCreator(userLiteBack);
        assertThat(singleVideo.getCreator()).isEqualTo(userLiteBack);

        singleVideo.creator(null);
        assertThat(singleVideo.getCreator()).isNull();
    }
}
