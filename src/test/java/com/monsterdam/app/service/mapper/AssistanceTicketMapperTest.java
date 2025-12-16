package com.monsterdam.app.service.mapper;

import static com.monsterdam.app.domain.AssistanceTicketAsserts.*;
import static com.monsterdam.app.domain.AssistanceTicketTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssistanceTicketMapperTest {

    private AssistanceTicketMapper assistanceTicketMapper;

    @BeforeEach
    void setUp() {
        assistanceTicketMapper = new AssistanceTicketMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssistanceTicketSample1();
        var actual = assistanceTicketMapper.toEntity(assistanceTicketMapper.toDto(expected));
        assertAssistanceTicketAllPropertiesEquals(expected, actual);
    }
}
