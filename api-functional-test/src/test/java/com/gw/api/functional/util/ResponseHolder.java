package com.gw.api.functional.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ResponseHolder {

    private ResponseEntity responseEntity;
    private final Map<String, Responses> responseMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, String> tokenMap = new HashMap<>();
    private String userId;
    private String metrics;

    public <T> void addResponse(ResponseEntity entity, Class<T> clazz) {
        this.responseEntity = entity;
        if (entity.getStatusCode().is2xxSuccessful()) {
            T response = read(entity, clazz);
            responseMap.put(clazz.getSimpleName(), new Responses(entity.getHeaders(), response));
        }
    }

    public void addResponse(ResponseEntity entity) {
        responseEntity = entity;
    }

    public HttpStatusCode getResponseCode() {
        return Optional.ofNullable(responseEntity).map(resp -> resp.getStatusCode()).orElseThrow(IllegalStateException::new);
    }

    public <T> T getResponse(Class<T> clazz) {
        var responses = responseMap.get(clazz.getSimpleName());
        return (T) responses.bodyObject();
    }

    private <T> T read(ResponseEntity responseEntity, Class<T> clazz) {
        Object body = responseEntity.getBody();

        if (body != null && clazz.isAssignableFrom(body.getClass())) {
            return (T) body;
        }

        try {
            return objectMapper.readValue(body.toString(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpHeaders getHeaders() {
        return responseEntity.getHeaders();
    }

    public void storeUserToken(String tokenKey, String token) {
        this.tokenMap.put(tokenKey, token);
    }

    public String getToken(String tokenKey) {
        return tokenMap.get(tokenKey);
    }

    public void storeMetrics(String readResponse) {
        this.metrics = readResponse;
    }

    public String getMetrics() {
        return metrics;
    }

    private record Responses(HttpHeaders httpHeaders, Object bodyObject) {}
}
