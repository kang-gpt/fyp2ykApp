package com.yk.booking.service.mapper;

import static com.yk.booking.domain.ClientTierAsserts.*;
import static com.yk.booking.domain.ClientTierTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientTierMapperTest {

    private ClientTierMapper clientTierMapper;

    @BeforeEach
    void setUp() {
        clientTierMapper = new ClientTierMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClientTierSample1();
        var actual = clientTierMapper.toEntity(clientTierMapper.toDto(expected));
        assertClientTierAllPropertiesEquals(expected, actual);
    }
}
