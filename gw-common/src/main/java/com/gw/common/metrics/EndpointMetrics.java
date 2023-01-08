package com.gw.common.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
public class EndpointMetrics {
    private static final Logger LOG = LoggerFactory.getLogger(EndpointMetrics.class);
    private final MeterRegistry meterRegistry;

    @Autowired
    public EndpointMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public Histogram createHistogramFor(String metricName, String... tags) {
        return new Histogram(Timer.builder(metricName)
                .tags(tags)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry));
    }

    public static class Histogram {
        private final Timer timer;
        private ThreadLocal<Instant> threadLocal;

        public Histogram(Timer timer) {
            this.timer = timer;
        }

        public void start() {
            threadLocal = ThreadLocal.withInitial(Instant::now);
        }

        public void observe() {
            Instant startTime = threadLocal.get();
            long duration = Instant.now().toEpochMilli() - startTime.toEpochMilli();
            LOG.info("Duration of request for {} was : {} milli seconds", timer.getId(), duration);
            timer.record(duration, TimeUnit.MILLISECONDS);
            threadLocal.remove();
        }
    }
}
