package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.DocumentReviewObservationAsserts.*;
import static com.monsterdam.app.domain.DocumentReviewObservationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentReviewObservationMapperTest {

    private DocumentReviewObservationMapper documentReviewObservationMapper;

    @BeforeEach
    void setUp() {
        documentReviewObservationMapper = new DocumentReviewObservationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentReviewObservationSample1();
        var actual = documentReviewObservationMapper.toEntity(documentReviewObservationMapper.toDto(expected));
        assertDocumentReviewObservationAllPropertiesEquals(expected, actual);
    }
}
