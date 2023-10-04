package com.gw.common.http.filter;

import com.gw.common.http.filter.AccessLogging.Direction;
import com.gw.common.http.filter.AccessLogging.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

public class AccessLoggingParser {
    private static final String[] ALL_FIELDS = {
            "Direction",
            "RequestType",
            "Method",
            "Path",
            "Headers",
            "Body"
    };

    private AccessLoggingParser() {}

    public static AccessLogging parse(String logAsText) {
        String[] lines = logAsText.split("\n");
        Map<String, String> allFields = getFields(lines);

        return new AccessLogging(
                ofNullable(allFields.get("direction")).map(Direction::valueOf).orElse(null),
                ofNullable(allFields.get("requesttype")).map(Type::valueOf).orElse(null),
                allFields.get("method"),
                allFields.get("path"),
                ofNullable(allFields.get("headers")).map(AccessLoggingParser::createHeadersFromString).orElse(null),
                allFields.get("statusCode"),
                allFields.get("body"));
    }

    private static Map<String, String> getFields(String[] lines) {
        Map<String, StringBuilder> fieldMap = new HashMap<>();

        String previousField = null;
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            String matchedField = Arrays.stream(ALL_FIELDS).filter(line::startsWith)
                    .findFirst()
                    .orElse(null);

            if (Objects.nonNull(matchedField)) {
                if (fieldMap.containsKey(matchedField)) {
                    throw new IllegalStateException("Duplicate field: " + matchedField);
                }
                fieldMap.put(matchedField.toLowerCase(), initialListWith(matchedField, line));
                previousField = matchedField;
            } else {
                fieldMap.computeIfPresent(previousField.toLowerCase(), (s, strings) -> {
                    strings.append(line);
                    return strings;
                });
            }
        }

        return fieldMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
    }

    private static StringBuilder initialListWith(String matchedField, String line) {
        StringBuilder valueLines = new StringBuilder();
        if (matchedField.length() + 2 < line.length()) {
            valueLines.append(line.substring(matchedField.length() + 2));
        }
        return valueLines;
    }

    private static Map<String, String> createHeadersFromString(String headers) {
        Pattern pattern = compile("\\{(.*)}$", CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(headers);
        if (matcher.matches() && matcher.groupCount() >= 1) {
            return Arrays.asList(matcher.group(1).split(" "))
                    .stream()
                    .filter(s -> !s.isBlank())
                    .map(token -> token.split("="))
                    .collect(Collectors.toMap(
                            str -> stripOffSpecialCharacters(str[0]),
                            str -> stripOffSpecialCharacters(str[1])));
        } else {
            return Map.of();
        }
    }

    private static String stripOffSpecialCharacters(String s) {
        return s.replaceAll(",", "");
    }
}