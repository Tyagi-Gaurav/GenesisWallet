package com.gw.common.http.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gw.common.http.filter.AccessLogging.Direction.IN;
import static com.gw.common.http.filter.AccessLogging.Direction.OUT;
import static com.gw.common.http.filter.AccessLogging.Type.REQUEST;
import static com.gw.common.http.filter.AccessLogging.Type.RESPONSE;
import static com.gw.common.http.filter.AccessLoggingBuilder.anAccessLog;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

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

    private final ObjectMapper mapper = new ObjectMapper();

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
                        .build(),
                anAccessLog()
                        .withDirection(OUT)
                        .withType(RESPONSE)
                        .withMethod(GET.name())
                        .withHeaders(Map.of())
                        .withBody("")
                        .withPath("/some/uri/path")
                        .withStatusCode("200")
                        .build());
    }

    @Test
    void hideSensitiveFields() {
        List<LogEvent> logs = new ArrayList<>();
        when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        when(serverWebExchange.getResponse()).thenReturn(serverHttpResponse);
        when(serverHttpResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
        when(serverHttpResponse.getHeaders()).thenReturn(HttpHeaders.EMPTY);
        when(serverHttpRequest.getMethod()).thenReturn(POST);

        final var originalRequestBody = mapper.createObjectNode()
                .putPOJO("level1", mapper.createObjectNode()
                        .put("user", "abc")
                        .put("password", "def")
                        .putPOJO("level2", mapper.createObjectNode()
                                .put("secret", "xyz")
                                .put("password", "private-secret")));

        DefaultDataBuffer dataBuffer = new DefaultDataBufferFactory().allocateBuffer(100)
                .write(originalRequestBody.toPrettyString().getBytes(StandardCharsets.UTF_8));
        when(serverHttpRequest.getBody()).thenReturn(Flux.just(dataBuffer));
        when(serverHttpRequest.getURI().getPath()).thenReturn("/some/uri/path");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("Secret", "xyz");
        when(serverHttpRequest.getHeaders()).thenReturn(httpHeaders);

        givenLogsAreCapturedFor(mockAppender, logs);

        when(webFilterChain.filter(any(ServerWebExchangeDecorator.class))).thenAnswer(invocationOnMock -> {
            ServerWebExchangeDecorator decorator = invocationOnMock.getArgument(0);
            StepVerifier.create(decorator.getRequest().getBody())
                    .expectNext(dataBuffer)
                    .verifyComplete();
            return Mono.empty().then();
        });

        StepVerifier.create(loggingFilter.filter(serverWebExchange, webFilterChain))
                .verifyComplete();


        final var expectedRequestBody = mapper.createObjectNode()
                .putPOJO("level1", mapper.createObjectNode()
                        .put("user", "abc")
                        .put("password", "*****")
                        .putPOJO("level2", mapper.createObjectNode()
                                .put("secret", "*****")
                                .put("password", "*****")));

        verifyMessages(logs,
                anAccessLog()
                        .withDirection(IN)
                        .withType(REQUEST)
                        .withMethod(POST.name())
                        .withPath("/some/uri/path")
                        .withHeaders(Map.of(
                                "Content-Type", "application/json",
                                "Secret", "*****"
                        ))
                        .withBody(expectedRequestBody.toString())
                        .build(),
                anAccessLog()
                        .withDirection(OUT)
                        .withType(RESPONSE)
                        .withMethod(POST.name())
                        .withHeaders(Map.of())
                        .withBody("")
                        .withPath("/some/uri/path")
                        .withStatusCode("200")
                        .build());
    }

    private void givenLogsAreCapturedFor(Appender appender, List<LogEvent> logs) {
        doAnswer(invocationOnMock -> {
            logs.add(((LogEvent) invocationOnMock.getArgument(0)).toImmutable());
            return null;
        }).when(appender).append(captorLoggingEvent.capture());
    }

    private void verifyMessages(List<LogEvent> logs, AccessLogging... messages) {
        for (int i = 0; i < logs.size(); i++) {
            AccessLoggingAssert.assertThat(AccessLogging.parse(logs.get(i).getMessage().getFormattedMessage()))
                    .hasBody(messages[i].body())
                    .hasPath(messages[i].path())
                    .hasHttpMethod(messages[i].method())
                    .hasDirection(messages[i].direction())
                    .containsHeaders(messages[i].headers());
        }
    }

    @AfterEach
    public void tearDown() {
        // the appender we added will sit in the singleton logger forever
        // slowing future things down - so remove it
        audit.removeAppender(mockAppender);
    }
}