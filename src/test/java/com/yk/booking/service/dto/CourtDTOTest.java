package com.yk.booking.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yk.booking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourtDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourtDTO.class);
        CourtDTO courtDTO1 = new CourtDTO();
        courtDTO1.setId(1L);
        CourtDTO courtDTO2 = new CourtDTO();
        assertThat(courtDTO1).isNotEqualTo(courtDTO2);
        courtDTO2.setId(courtDTO1.getId());
        assertThat(courtDTO1).isEqualTo(courtDTO2);
        courtDTO2.setId(2L);
        assertThat(courtDTO1).isNotEqualTo(courtDTO2);
        courtDTO1.setId(null);
        assertThat(courtDTO1).isNotEqualTo(courtDTO2);
    }
}
