package com.gw.common.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationCounter {
    private static final String SOURCE_TAG = "source";
    private static final String TYPE_TAG = "type";
    private static final String USER_REGISTRATION_COUNT = "user_registration_count";

    private final MeterRegistry meterRegistry;

    public UserRegistrationCounter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void increment(String source, String type) {
        Counter requestCount = Counter.builder(USER_REGISTRATION_COUNT)
                .tags(SOURCE_TAG, source, TYPE_TAG, type)
                .register(meterRegistry);
        requestCount.increment();
    }
}
