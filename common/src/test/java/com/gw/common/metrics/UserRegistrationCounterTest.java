package com.gw.common.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserRegistrationCounterTest {
    @Spy
    private MeterRegistry meterRegistry = new SimpleMeterRegistry();

    @InjectMocks
    private UserRegistrationCounter userRegistrationCounter;

    @Test
    void shouldIncrementExceptionMetricWhenItOccurs() {
        //when
        String source = "source";
        String type = "type";
        userRegistrationCounter.increment(source, type);

        //then
        assertThat(meterRegistry.counter("user_registration_count", "source", source, "type", type)
                .count()).isEqualTo(1.0);
    }
}