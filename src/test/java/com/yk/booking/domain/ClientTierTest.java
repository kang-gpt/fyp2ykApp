package com.yk.booking.domain;

import static com.yk.booking.domain.ClientTierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yk.booking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientTierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientTier.class);
        ClientTier clientTier1 = getClientTierSample1();
        ClientTier clientTier2 = new ClientTier();
        assertThat(clientTier1).isNotEqualTo(clientTier2);

        clientTier2.setId(clientTier1.getId());
        assertThat(clientTier1).isEqualTo(clientTier2);

        clientTier2 = getClientTierSample2();
        assertThat(clientTier1).isNotEqualTo(clientTier2);
    }
}
