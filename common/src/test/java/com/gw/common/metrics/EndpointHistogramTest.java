package com.gw.common.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class EndpointHistogramTest {
    private final MeterRegistry meterRegistry = new SimpleMeterRegistry();

    private final EndpointHistogram endpointHistogram = new EndpointHistogram(meterRegistry);

    @Test
    void observe() {
        //when
        endpointHistogram.observe(5);

        //then
        assertNotNull(meterRegistry.get("request_latency").meter());
    }
}