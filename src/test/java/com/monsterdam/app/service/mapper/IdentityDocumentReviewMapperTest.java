package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.IdentityDocumentReviewAsserts.*;
import static com.monsterdam.app.domain.IdentityDocumentReviewTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IdentityDocumentReviewMapperTest {

    private IdentityDocumentReviewMapper identityDocumentReviewMapper;

    @BeforeEach
    void setUp() {
        identityDocumentReviewMapper = new IdentityDocumentReviewMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIdentityDocumentReviewSample1();
        var actual = identityDocumentReviewMapper.toEntity(identityDocumentReviewMapper.toDto(expected));
        assertIdentityDocumentReviewAllPropertiesEquals(expected, actual);
    }
}
