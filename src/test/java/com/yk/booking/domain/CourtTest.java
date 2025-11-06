package com.yk.booking.domain;

import static com.yk.booking.domain.CourtTestSamples.*;
import static com.yk.booking.domain.SportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yk.booking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourtTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Court.class);
        Court court1 = getCourtSample1();
        Court court2 = new Court();
        assertThat(court1).isNotEqualTo(court2);

        court2.setId(court1.getId());
        assertThat(court1).isEqualTo(court2);

        court2 = getCourtSample2();
        assertThat(court1).isNotEqualTo(court2);
    }

    // TimeSlot relationship test removed - Court does not have a OneToMany relationship with TimeSlot

    @Test
    void sportTest() {
        Court court = getCourtRandomSampleGenerator();
        Sport sportBack = getSportRandomSampleGenerator();

        court.setSport(sportBack);
        assertThat(court.getSport()).isEqualTo(sportBack);

        court.sport(null);
        assertThat(court.getSport()).isNull();
    }
}
