package com.yk.booking.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TimeSlotTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TimeSlot getTimeSlotSample1() {
        return new TimeSlot().id(1L);
    }

    public static TimeSlot getTimeSlotSample2() {
        return new TimeSlot().id(2L);
    }

    public static TimeSlot getTimeSlotRandomSampleGenerator() {
        return new TimeSlot().id(longCount.incrementAndGet());
    }
}
