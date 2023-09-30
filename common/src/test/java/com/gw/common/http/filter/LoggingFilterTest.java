package com.gw.common.http.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gw.common.http.filter.AccessLogging.Direction.IN;
import static com.gw.common.http.filter.AccessLogging.Direction.OUT;
import static com.gw.common.http.filter.AccessLogging.Type.REQUEST;
import static com.gw.common.http.filter.AccessLogging.Type.RESPONSE;
import static com.gw.common.http.filter.AccessLoggingBuilder.anAccessLog;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;

@ExtendWith(MockitoExtension.class)
class LoggingFilterTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ServerWebExchange serverWebExchange;
    @Mock
    private WebFilterChain webFilterChain;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ServerHttpRequest serverHttpRequest;

    @Mock
    private Appender mockAppender;

    @Mock
    private ServerHttpResponse serverHttpResponse;

    @InjectMocks
    private LoggingFilter loggingFilter;
    private Logger audit;

    @Captor
    private ArgumentCaptor<LogEvent> captorLoggingEvent;

    @BeforeEach
    void setUp() {
        when(mockAppender.getName()).thenReturn("MockAppender");
        when(mockAppender.isStarted()).thenReturn(true);

        audit = (Logger) LogManager.getLogger("ACCESS");
        audit.addAppender(mockAppender);
        audit.setLevel(Level.INFO);
    }

    @Test
    void filter() {
        List<LogEvent> logs = new ArrayList<>();
        when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        when(serverWebExchange.getResponse()).thenReturn(serverHttpResponse);
        when(serverHttpResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
        when(serverHttpResponse.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(serverHttpRequest.getMethod()).thenReturn(GET);
        when(serverHttpRequest.getURI().getPath()).thenReturn("/some/uri/path");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        when(serverHttpRequest.getHeaders()).thenReturn(httpHeaders);

        givenLogsAreCapturedFor(mockAppender, logs);

        when(webFilterChain.filter(any(ServerWebExchangeDecorator.class))).thenReturn(Mono.empty().then());

        StepVerifier.create(loggingFilter.filter(serverWebExchange, webFilterChain))
                .verifyComplete();

        verifyMessages(logs,
                anAccessLog()
                        .withDirection(IN)
                        .withType(REQUEST)
                        .withMethod(GET.name())
                        .withPath("/some/uri/path")
                        .withHeaders(Map.of("Content-Type", "application/json"))
                        .withBody("")
                        .build().toString(),
                anAccessLog()
                        .withDirection(OUT)
                        .withType(RESPONSE)
                        .withMethod(GET.name())
                        .withHeaders(Map.of())
                        .withBody("")
                        .withPath("/some/uri/path")
                        .withStatusCode("200")
                        .build().toString());
    }

    private void givenLogsAreCapturedFor(Appender appender, List<LogEvent> logs) {
        doAnswer(invocationOnMock -> {
            logs.add(((LogEvent) invocationOnMock.getArgument(0)).toImmutable());
            return null;
        }).when(appender).append(captorLoggingEvent.capture());
    }

    private void verifyMessages(List<LogEvent> logs, String... messages) {
        for (int i = 0; i < logs.size(); i++) {
            assertThat(logs.get(i).getMessage().getFormattedMessage()).isEqualTo(messages[i]);
        }
    }

    @AfterEach
    public void tearDown() {
        // the appender we added will sit in the singleton logger forever
        // slowing future things down - so remove it
        audit.removeAppender(mockAppender);
    }
}