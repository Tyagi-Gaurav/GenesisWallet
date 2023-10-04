package com.gw.common.http.filter;

import com.gw.common.annotations.GenerateBuilder;

import java.util.Map;

@GenerateBuilder(builderMethod = "anAccessLog")
public record AccessLogging(
        Direction direction,
        Type type,
        String method,
        String path,
        Map<String, String> headers,
        String statusCode,
        String body
) {

    public static AccessLogging parse(String logAsText) {
        return AccessLoggingParser.parse(logAsText);
    }

    public enum Direction {
        IN, OUT
    }

    public enum Type {
        REQUEST, RESPONSE
    }

    @Override
    public String toString() {
        return  "\nDirection: " + direction + "\n" +
                "RequestType: " + type + "\n" +
                "Method: " + method + "\n" +
                "Path: " + path + "\n" +
                "Headers: " + headers + "\n" +
                "Body: " + body + "\n";
    }
}