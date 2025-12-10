package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.HashTagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HashTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HashTag.class);
        HashTag hashTag1 = getHashTagSample1();
        HashTag hashTag2 = new HashTag();
        assertThat(hashTag1).isNotEqualTo(hashTag2);

        hashTag2.setId(hashTag1.getId());
        assertThat(hashTag1).isEqualTo(hashTag2);

        hashTag2 = getHashTagSample2();
        assertThat(hashTag1).isNotEqualTo(hashTag2);
    }
}
