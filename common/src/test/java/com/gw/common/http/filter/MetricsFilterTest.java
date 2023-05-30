package com.gw.common.http.filter;

import com.gw.common.metrics.EndpointMetrics;
import com.gw.common.metrics.EndpointRequestCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetricsFilterTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ServerWebExchange serverWebExchange;
    @Mock
    private WebFilterChain webFilterChain;
    @Mock
    private EndpointRequestCounter endpointRequestCounter;
    @Mock
    EndpointMetrics endpointMetrics;
    @Mock
    private EndpointMetrics.Histogram endpointRequestLatency;
    private MetricsFilter metricsFilter;

    @BeforeEach
    void setUp() {
        when(endpointMetrics.createHistogramFor("request_latency")).thenReturn(endpointRequestLatency);
        metricsFilter = new MetricsFilter(endpointRequestCounter, endpointMetrics);
        when(webFilterChain.filter(serverWebExchange)).thenReturn(Mono.empty().then());
    }

    @Test
    void filter() {
        doNothing().when(endpointRequestLatency).start();
        StepVerifier.create(metricsFilter.filter(serverWebExchange, webFilterChain))
                .verifyComplete();
    }
}