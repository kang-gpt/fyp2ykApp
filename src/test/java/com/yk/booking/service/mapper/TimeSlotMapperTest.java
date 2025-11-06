package com.yk.booking.service.mapper;

import static com.yk.booking.domain.TimeSlotAsserts.*;
import static com.yk.booking.domain.TimeSlotTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeSlotMapperTest {

    private TimeSlotMapper timeSlotMapper;

    @BeforeEach
    void setUp() {
        timeSlotMapper = new TimeSlotMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTimeSlotSample1();
        var actual = timeSlotMapper.toEntity(timeSlotMapper.toDto(expected));
        assertTimeSlotAllPropertiesEquals(expected, actual);
    }
}
