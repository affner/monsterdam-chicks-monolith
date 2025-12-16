package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static com.monsterdam.app.domain.VideoStoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VideoStoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VideoStory.class);
        VideoStory videoStory1 = getVideoStorySample1();
        VideoStory videoStory2 = new VideoStory();
        assertThat(videoStory1).isNotEqualTo(videoStory2);

        videoStory2.setId(videoStory1.getId());
        assertThat(videoStory1).isEqualTo(videoStory2);

        videoStory2 = getVideoStorySample2();
        assertThat(videoStory1).isNotEqualTo(videoStory2);
    }

    @Test
    void creatorTest() {
        VideoStory videoStory = getVideoStoryRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        videoStory.setCreator(userLiteBack);
        assertThat(videoStory.getCreator()).isEqualTo(userLiteBack);

        videoStory.creator(null);
        assertThat(videoStory.getCreator()).isNull();
    }
}
