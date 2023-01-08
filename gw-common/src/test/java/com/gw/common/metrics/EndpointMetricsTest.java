package com.gw.common.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EndpointMetricsTest {
    private final MeterRegistry meterRegistry = new SimpleMeterRegistry();

    private final EndpointMetrics endpointMetrics = new EndpointMetrics(meterRegistry);

    @Test
    void observe() throws InterruptedException {
        //given
        EndpointMetrics.Histogram request_latency = endpointMetrics.createHistogramFor("request_latency");

        //when
        request_latency.start();
        await().atLeast(100, TimeUnit.MILLISECONDS).untilAsserted(request_latency::observe);
//        request_latency.observe();

        //then
        assertNotNull(meterRegistry.get("request_latency").meter());
    }
}