package com.yk.booking.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTierTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ClientTier getClientTierSample1() {
        return new ClientTier().id(1L).tierName("tierName1");
    }

    public static ClientTier getClientTierSample2() {
        return new ClientTier().id(2L).tierName("tierName2");
    }

    public static ClientTier getClientTierRandomSampleGenerator() {
        return new ClientTier().id(longCount.incrementAndGet()).tierName(UUID.randomUUID().toString());
    }
}
