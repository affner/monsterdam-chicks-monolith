package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.IdentityDocumentReviewTestSamples.*;
import static com.monsterdam.app.domain.IdentityDocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdentityDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdentityDocument.class);
        IdentityDocument identityDocument1 = getIdentityDocumentSample1();
        IdentityDocument identityDocument2 = new IdentityDocument();
        assertThat(identityDocument1).isNotEqualTo(identityDocument2);

        identityDocument2.setId(identityDocument1.getId());
        assertThat(identityDocument1).isEqualTo(identityDocument2);

        identityDocument2 = getIdentityDocumentSample2();
        assertThat(identityDocument1).isNotEqualTo(identityDocument2);
    }

    @Test
    void reviewTest() {
        IdentityDocument identityDocument = getIdentityDocumentRandomSampleGenerator();
        IdentityDocumentReview identityDocumentReviewBack = getIdentityDocumentReviewRandomSampleGenerator();

        identityDocument.setReview(identityDocumentReviewBack);
        assertThat(identityDocument.getReview()).isEqualTo(identityDocumentReviewBack);

        identityDocument.review(null);
        assertThat(identityDocument.getReview()).isNull();
    }
}
