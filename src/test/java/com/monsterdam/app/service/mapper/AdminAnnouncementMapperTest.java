package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.AdminAnnouncementAsserts.*;
import static com.monsterdam.app.domain.AdminAnnouncementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdminAnnouncementMapperTest {

    private AdminAnnouncementMapper adminAnnouncementMapper;

    @BeforeEach
    void setUp() {
        adminAnnouncementMapper = new AdminAnnouncementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdminAnnouncementSample1();
        var actual = adminAnnouncementMapper.toEntity(adminAnnouncementMapper.toDto(expected));
        assertAdminAnnouncementAllPropertiesEquals(expected, actual);
    }
}
