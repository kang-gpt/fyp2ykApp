package com.yk.booking.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yk.booking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientTierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientTierDTO.class);
        ClientTierDTO clientTierDTO1 = new ClientTierDTO();
        clientTierDTO1.setId(1L);
        ClientTierDTO clientTierDTO2 = new ClientTierDTO();
        assertThat(clientTierDTO1).isNotEqualTo(clientTierDTO2);
        clientTierDTO2.setId(clientTierDTO1.getId());
        assertThat(clientTierDTO1).isEqualTo(clientTierDTO2);
        clientTierDTO2.setId(2L);
        assertThat(clientTierDTO1).isNotEqualTo(clientTierDTO2);
        clientTierDTO1.setId(null);
        assertThat(clientTierDTO1).isNotEqualTo(clientTierDTO2);
    }
}
