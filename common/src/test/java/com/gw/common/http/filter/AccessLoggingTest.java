package com.gw.common.http.filter;

import org.junit.jupiter.api.Test;

import static com.gw.common.http.filter.AccessLogging.Direction.OUT;
import static com.gw.common.http.filter.AccessLogging.Type.RESPONSE;

class AccessLoggingTest {
    @Test
    void parseAccessLogFromString() {
        final var actualLog = AccessLogging.parse("""
                Direction: OUT
                RequestType: RESPONSE
                Method: POST
                Path: /some/uri/path
                Headers: {Secret=*****, Content-Type=application/json}
                Body: { "user" : "abc", "some-field" : "some-value"}
                """);

        AccessLoggingAssert.assertThat(actualLog)
                .hasHttpMethod("POST")
                .hasType(RESPONSE)
                .hasDirection(OUT)
                .hasBody("{ \"user\" : \"abc\", \"some-field\" : \"some-value\"}")
                .hasPath("/some/uri/path")
                .containsHeader("Secret", "*****")
                .containsHeader("Content-Type", "application/json");
    }

    @Test
    void parseAccessLogFromStringWithEmptyFieldsAndBlankLines() {
        final var actualLog = AccessLogging.parse("""
                
                Direction: OUT
                RequestType: RESPONSE
                Method: POST
                Path: /some/uri/path
                Headers: {}
                Body:
                """);

        AccessLoggingAssert.assertThat(actualLog)
                .hasHttpMethod("POST")
                .hasType(RESPONSE)
                .hasDirection(OUT)
                .hasBody("")
                .hasPath("/some/uri/path")
                .containsNoHeaders();
    }
}
