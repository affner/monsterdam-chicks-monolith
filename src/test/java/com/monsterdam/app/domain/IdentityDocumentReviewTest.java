package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.IdentityDocumentReviewTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdentityDocumentReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdentityDocumentReview.class);
        IdentityDocumentReview identityDocumentReview1 = getIdentityDocumentReviewSample1();
        IdentityDocumentReview identityDocumentReview2 = new IdentityDocumentReview();
        assertThat(identityDocumentReview1).isNotEqualTo(identityDocumentReview2);

        identityDocumentReview2.setId(identityDocumentReview1.getId());
        assertThat(identityDocumentReview1).isEqualTo(identityDocumentReview2);

        identityDocumentReview2 = getIdentityDocumentReviewSample2();
        assertThat(identityDocumentReview1).isNotEqualTo(identityDocumentReview2);
    }
}
