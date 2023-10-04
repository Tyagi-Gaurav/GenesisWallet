package com.gw.common.http.filter;

import org.assertj.core.api.AbstractAssert;

import java.util.Map;

public class AccessLoggingAssert extends AbstractAssert<AccessLoggingAssert, AccessLogging> {

    public AccessLoggingAssert(AccessLogging actual) {
        super(actual, AccessLoggingAssert.class);
    }

    public static AccessLoggingAssert assertThat(AccessLogging actual) {
        return new AccessLoggingAssert(actual);
    }

    public AccessLoggingAssert hasHttpMethod(String httpMethod) {
        isNotNull();
        if (!actual.method().equals(httpMethod)) {
            failWithMessage("Expected method: %s, Actual method: %s",
                    httpMethod, actual.method());
        }
        return this;
    }

    public AccessLoggingAssert hasDirection(AccessLogging.Direction direction) {
        isNotNull();
        if (!actual.direction().equals(direction)) {
            failWithMessage("Expected direction: %s, Actual direction: %s",
                    direction, actual.direction());
        }
        return this;
    }

    public AccessLoggingAssert hasBody(String body) {
        isNotNull();
        if (!actual.body().equals(body)) {
            failWithMessage("Expected body: %s, Actual body: %s",
                    body, actual.body());
        }
        return this;
    }

    public AccessLoggingAssert hasPath(String path) {
        isNotNull();
        if (!actual.path().equals(path)) {
            failWithMessage("Expected path: %s, Actual path: %s",
                    path, actual.path());
        }
        return this;
    }

    public AccessLoggingAssert hasType(AccessLogging.Type type) {
        isNotNull();
        if (!actual.type().equals(type)) {
            failWithMessage("Expected type: %s, Actual type: %s",
                    type, actual.type());
        }
        return this;
    }

    public AccessLoggingAssert containsHeader(String key, String value) {
        isNotNull();
        if (!(actual.headers().containsKey(key) && actual.headers().get(key).equals(value))) {
            failWithMessage("Unknown header (key, Value): (%s, %s) in Headers: %s",
                    key, value, actual.headers());
        }
        return this;
    }

    public AccessLoggingAssert containsNoHeaders() {
        isNotNull();
        if (actual.headers() != null && !actual.headers().isEmpty()) {
            failWithMessage("Expected Header to be empty: %s", actual.headers());
        }
        return this;
    }

    public AccessLoggingAssert containsHeaders(Map<String, String> headers) {
        isNotNull();
        if (actual.headers() != null && !actual.headers().equals(headers)) {
            failWithMessage("Expected Header : %s, Actual Headers: %s", headers ,actual.headers());
        }
        return this;
    }

    public AccessLoggingAssert hasNoHttpMethod() {
        isNotNull();
        if (actual.method() != null) {
            failWithMessage("Expected HttpMethod to be empty: %s", actual.method());
        }
        return this;
    }

    public AccessLoggingAssert hasNoPath() {
        isNotNull();
        if (actual.path() != null) {
            failWithMessage("Expected Path to be empty: %s", actual.path());
        }
        return this;
    }

    public AccessLoggingAssert hasNoBody() {
        isNotNull();
        if (actual.body() != null) {
            failWithMessage("Expected Body to be empty: %s", actual.body());
        }
        return this;
    }

    public AccessLoggingAssert hasNoDirection() {
        isNotNull();
        if (actual.direction() != null) {
            failWithMessage("Expected Direction to be empty: %s", actual.direction());
        }
        return this;
    }

    public AccessLoggingAssert hasNoType() {
        isNotNull();
        if (actual.type() != null) {
            failWithMessage("Expected Type to be empty: %s", actual.type());
        }
        return this;
    }
}
