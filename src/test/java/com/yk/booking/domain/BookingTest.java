package com.yk.booking.domain;

import static com.yk.booking.domain.BookingTestSamples.*;
import static com.yk.booking.domain.TimeSlotTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yk.booking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Booking.class);
        Booking booking1 = getBookingSample1();
        Booking booking2 = new Booking();
        assertThat(booking1).isNotEqualTo(booking2);

        booking2.setId(booking1.getId());
        assertThat(booking1).isEqualTo(booking2);

        booking2 = getBookingSample2();
        assertThat(booking1).isNotEqualTo(booking2);
    }

    @Test
    void timeSlotTest() {
        Booking booking = getBookingRandomSampleGenerator();
        TimeSlot timeSlotBack = getTimeSlotRandomSampleGenerator();

        booking.setTimeSlot(timeSlotBack);
        assertThat(booking.getTimeSlot()).isEqualTo(timeSlotBack);

        booking.timeSlot(null);
        assertThat(booking.getTimeSlot()).isNull();
    }
}
