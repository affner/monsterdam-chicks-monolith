package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.IdentityDocumentAsserts.*;
import static com.monsterdam.app.domain.IdentityDocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IdentityDocumentMapperTest {

    private IdentityDocumentMapper identityDocumentMapper;

    @BeforeEach
    void setUp() {
        identityDocumentMapper = new IdentityDocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIdentityDocumentSample1();
        var actual = identityDocumentMapper.toEntity(identityDocumentMapper.toDto(expected));
        assertIdentityDocumentAllPropertiesEquals(expected, actual);
    }
}
