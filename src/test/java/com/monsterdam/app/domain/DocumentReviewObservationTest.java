package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.DocumentReviewObservationTestSamples.*;
import static com.monsterdam.app.domain.IdentityDocumentReviewTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentReviewObservationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentReviewObservation.class);
        DocumentReviewObservation documentReviewObservation1 = getDocumentReviewObservationSample1();
        DocumentReviewObservation documentReviewObservation2 = new DocumentReviewObservation();
        assertThat(documentReviewObservation1).isNotEqualTo(documentReviewObservation2);

        documentReviewObservation2.setId(documentReviewObservation1.getId());
        assertThat(documentReviewObservation1).isEqualTo(documentReviewObservation2);

        documentReviewObservation2 = getDocumentReviewObservationSample2();
        assertThat(documentReviewObservation1).isNotEqualTo(documentReviewObservation2);
    }

    @Test
    void reviewTest() {
        DocumentReviewObservation documentReviewObservation = getDocumentReviewObservationRandomSampleGenerator();
        IdentityDocumentReview identityDocumentReviewBack = getIdentityDocumentReviewRandomSampleGenerator();

        documentReviewObservation.setReview(identityDocumentReviewBack);
        assertThat(documentReviewObservation.getReview()).isEqualTo(identityDocumentReviewBack);

        documentReviewObservation.review(null);
        assertThat(documentReviewObservation.getReview()).isNull();
    }
}
