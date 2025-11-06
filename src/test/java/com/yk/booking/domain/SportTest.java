package com.yk.booking.domain;

import static com.yk.booking.domain.SportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yk.booking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sport.class);
        Sport sport1 = getSportSample1();
        Sport sport2 = new Sport();
        assertThat(sport1).isNotEqualTo(sport2);

        sport2.setId(sport1.getId());
        assertThat(sport1).isEqualTo(sport2);

        sport2 = getSportSample2();
        assertThat(sport1).isNotEqualTo(sport2);
    }
    // Court relationship test removed - Sport does not have a OneToMany relationship with Court
}
