package com.gw.functional.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ResponseHolder {
    private final List<ResponseEntity> previousResponses = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String token;
    private String userId;
    private String metrics;

    public void setResponse(ResponseEntity entity) {
        previousResponses.add(0, entity);
    }

    public int getResponseCode() {
        return Optional.ofNullable(getLastResponse()).map(resp -> resp.getStatusCode().value()).orElse(-1);
    }

    public <T> T readResponse(Class<T> clazz) {
        return read(getLastResponse(), clazz);
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
        return getLastResponse().getHeaders();
    }

    public <T> T getPreviousResponse(int index, Class<T> clazz) {
        Optional<ResponseEntity> previousResponseEntity = Optional.ofNullable(previousResponses.get(index));
        ResponseEntity responseEntity = previousResponseEntity.orElseThrow(IllegalStateException::new);
        return read(responseEntity, clazz);
    }

    private ResponseEntity getLastResponse() {
        return previousResponses.size() > 0 ? previousResponses.get(0) : null;
    }

    @PreDestroy
    public void clearResponses() {
        previousResponses.clear();
    }

    public void storeUserToken(String token) {
        this.token = token;
    }

    public void storeUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void storeMetrics(String readResponse) {
        this.metrics = readResponse;
    }

    public String getMetrics() {
        return metrics;
    }
}
