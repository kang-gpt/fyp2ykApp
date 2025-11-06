package com.yk.booking.domain;

import static com.yk.booking.domain.BookingTestSamples.*;
import static com.yk.booking.domain.CourtTestSamples.*;
import static com.yk.booking.domain.TimeSlotTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yk.booking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeSlotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeSlot.class);
        TimeSlot timeSlot1 = getTimeSlotSample1();
        TimeSlot timeSlot2 = new TimeSlot();
        assertThat(timeSlot1).isNotEqualTo(timeSlot2);

        timeSlot2.setId(timeSlot1.getId());
        assertThat(timeSlot1).isEqualTo(timeSlot2);

        timeSlot2 = getTimeSlotSample2();
        assertThat(timeSlot1).isNotEqualTo(timeSlot2);
    }

    @Test
    void bookingTest() {
        TimeSlot timeSlot = getTimeSlotRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        timeSlot.setBooking(bookingBack);
        assertThat(timeSlot.getBooking()).isEqualTo(bookingBack);
        assertThat(bookingBack.getTimeSlot()).isEqualTo(timeSlot);

        timeSlot.booking(null);
        assertThat(timeSlot.getBooking()).isNull();
        assertThat(bookingBack.getTimeSlot()).isNull();
    }

    @Test
    void courtTest() {
        TimeSlot timeSlot = getTimeSlotRandomSampleGenerator();
        Court courtBack = getCourtRandomSampleGenerator();

        timeSlot.setCourt(courtBack);
        assertThat(timeSlot.getCourt()).isEqualTo(courtBack);

        timeSlot.court(null);
        assertThat(timeSlot.getCourt()).isNull();
    }
}
