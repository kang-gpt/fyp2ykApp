package com.yk.booking.service.mapper;

import static com.yk.booking.domain.CourtAsserts.*;
import static com.yk.booking.domain.CourtTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourtMapperTest {

    private CourtMapper courtMapper;

    @BeforeEach
    void setUp() {
        courtMapper = new CourtMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCourtSample1();
        var actual = courtMapper.toEntity(courtMapper.toDto(expected));
        assertCourtAllPropertiesEquals(expected, actual);
    }
}
