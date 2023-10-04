package com.gw.common.http.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static com.gw.common.http.filter.AccessLogging.Direction.IN;
import static com.gw.common.http.filter.AccessLogging.Type.REQUEST;
import static com.gw.common.http.filter.AccessLoggingAssert.assertThat;
import static org.springframework.http.HttpMethod.POST;

class AccessLoggingParserTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void parseFullLog() throws JsonProcessingException {
        AccessLogging parsedLogging = AccessLoggingParser.parse("""
                Direction: IN
                RequestType: REQUEST
                Method: POST
                Path: /some/uri/path
                Headers: {Secret=*****, Content-Type=application/json}
                Body: {
                  "level1" : {
                    "user" : "abc",
                    "password" : "def",
                    "level2" : {
                      "secret" : "xyz",
                      "some-known-field" : "public-value"
                    }
                  }
                }
                """);

        assertThat(parsedLogging)
                .isNotNull()
                .hasDirection(IN)
                .hasHttpMethod(POST.name())
                .hasPath("/some/uri/path")
                .containsHeaders(Map.of("Secret", "*****", "Content-Type", "application/json"));

        Assertions.assertThat(objectMapper.readTree(parsedLogging.body()).toPrettyString()).isEqualTo(objectMapper.createObjectNode()
                .putPOJO("level1", objectMapper.createObjectNode()
                        .put("user", "abc")
                        .put("password", "def")
                        .putPOJO("level2", objectMapper.createObjectNode()
                                .put("secret", "xyz")
                                .put("some-known-field", "public-value"))).toPrettyString());
    }

    @Test
    void parsePartialLogWithDirection()  {
        AccessLogging parsedLogging = AccessLoggingParser.parse("""
                Direction: IN
                """);

        assertThat(parsedLogging)
                .isNotNull()
                .hasDirection(IN)
                .hasNoType()
                .hasNoHttpMethod()
                .containsNoHeaders()
                .hasNoPath()
                .hasNoBody();
    }

    @Test
    void parsePartialLogWithRequestType()  {
        AccessLogging parsedLogging = AccessLoggingParser.parse("""
                RequestType: REQUEST
                """);

        assertThat(parsedLogging)
                .isNotNull()
                .hasNoDirection()
                .hasType(REQUEST)
                .hasNoHttpMethod()
                .containsNoHeaders()
                .hasNoPath()
                .hasNoBody();
    }

    @Test
    void parsePartialLogWithMethod()  {
        AccessLogging parsedLogging = AccessLoggingParser.parse("""
                Method: POST
                """);

        assertThat(parsedLogging)
                .isNotNull()
                .hasNoDirection()
                .hasNoType()
                .hasHttpMethod(POST.name())
                .containsNoHeaders()
                .hasNoPath()
                .hasNoBody();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/some/uri/path", ""})
    void parsePartialLogWithPath(String pathValue)  {
        AccessLogging parsedLogging = AccessLoggingParser.parse("""
                Path: $$
                """.replace("$$", pathValue));

        assertThat(parsedLogging)
                .isNotNull()
                .hasNoDirection()
                .hasNoType()
                .hasNoHttpMethod()
                .hasPath(pathValue)
                .containsNoHeaders()
                .hasNoBody();
    }

    @Test
    void parsePartialLogWithHeaders()  {
        AccessLogging parsedLogging = AccessLoggingParser.parse("""
                Headers: {Secret=*****, Content-Type=application/json}
                """);

        assertThat(parsedLogging)
                .isNotNull()
                .hasNoDirection()
                .hasNoType()
                .hasNoHttpMethod()
                .hasNoPath()
                .containsHeaders(Map.of("Secret", "*****", "Content-Type", "application/json"))
                .hasNoBody();
    }

    @Test
    void parsePartialLogWithEmptyHeaders()  {
        AccessLogging parsedLogging = AccessLoggingParser.parse("""
                Headers: 
                """);

        assertThat(parsedLogging)
                .isNotNull()
                .hasNoDirection()
                .hasNoType()
                .hasNoHttpMethod()
                .hasNoPath()
                .containsNoHeaders()
                .hasNoBody();
    }

    @Test
    void parsePartialLogWithBody() throws JsonProcessingException {
        AccessLogging parsedLogging = AccessLoggingParser.parse("""
                Body: {
                  "level1" : {
                    "user" : "abc",
                    "password" : "def",
                    "level2" : {
                      "secret" : "xyz",
                      "some-known-field" : "public-value"
                    }
                  }
                }
                """);

        assertThat(parsedLogging)
                .isNotNull()
                .hasNoDirection()
                .hasNoType()
                .hasNoHttpMethod()
                .hasNoPath()
                .containsNoHeaders();

        Assertions.assertThat(objectMapper.readTree(parsedLogging.body()).toPrettyString()).isEqualTo(objectMapper.createObjectNode()
                .putPOJO("level1", objectMapper.createObjectNode()
                        .put("user", "abc")
                        .put("password", "def")
                        .putPOJO("level2", objectMapper.createObjectNode()
                                .put("secret", "xyz")
                                .put("some-known-field", "public-value"))).toPrettyString());
    }

    @Test
    void parseBlankLines()  {
        AccessLogging parsedLogging = AccessLoggingParser.parse("");

        assertThat(parsedLogging)
                .isNotNull()
                .hasNoDirection()
                .hasNoType()
                .hasNoHttpMethod()
                .hasNoPath()
                .containsNoHeaders()
                .hasNoBody();
    }
}