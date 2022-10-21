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
class EndpointRequestCounterTest {
    @Spy
    private MeterRegistry meterRegistry = new SimpleMeterRegistry();

    @InjectMocks
    private EndpointRequestCounter endpointRequestCounter;

    @Test
    void shouldIncrementExceptionMetricWhenItOccurs() {
        //when
        String methodName = "methodName";
        String path = "path";
        endpointRequestCounter.increment(methodName, path);

        //then
        assertThat(meterRegistry.counter("request_count", "method", methodName, "path", path)
                .count()).isEqualTo(1.0);
    }
}