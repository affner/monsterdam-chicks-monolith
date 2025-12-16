package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.ViewerWalletAsserts.*;
import static com.monsterdam.app.domain.ViewerWalletTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViewerWalletMapperTest {

    private ViewerWalletMapper viewerWalletMapper;

    @BeforeEach
    void setUp() {
        viewerWalletMapper = new ViewerWalletMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getViewerWalletSample1();
        var actual = viewerWalletMapper.toEntity(viewerWalletMapper.toDto(expected));
        assertViewerWalletAllPropertiesEquals(expected, actual);
    }
}
