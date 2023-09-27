package com.gw.api.functional.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class AbstractResource {
    private static final Logger APP_LOG = LogManager.getLogger("APP");
    private static final Logger ACCESS_LOG = LogManager.getLogger("ACCESS");

    protected HttpEntity EMPTY_HTTP_ENTITY = new HttpEntity(HttpHeaders.EMPTY);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected RestTemplate restTemplate;

    protected String getUrl(String hostName, String contextPath, String path, int port) {
        return getFullUrlWithScheme("http", hostName, contextPath, path, port);
    }

    protected String getFullUrlWithScheme(String scheme, String hostName, String contextPath, String path, int port) {
        StringBuilder fullUrl = new StringBuilder(scheme + "://" + hostName);

        if (port != 0) {
            fullUrl.append(":").append(port);
        }

        if (StringUtils.isNotBlank(contextPath)) {
            fullUrl.append(contextPath);
        }

        fullUrl.append(path);

        return fullUrl.toString();
    }

    protected <T> ResponseEntity get(String fullUrl, HttpEntity requestObject, Class<T> responseType) {
        try {
            log(fullUrl, requestObject);
            return restTemplate.exchange(fullUrl, HttpMethod.GET, requestObject, responseType);
        } catch (Exception e) {
            return handleError(fullUrl, e);
        }
    }

    protected <T> ResponseEntity delete(String fullUrl, HttpEntity requestObject, Class<T> responseType) {
        try {
            log(fullUrl, requestObject);
            return restTemplate.exchange(fullUrl, HttpMethod.DELETE, requestObject, responseType);
        } catch (Exception e) {
            return handleError(fullUrl, e);
        }
    }

    protected <T> ResponseEntity post(String fullUrl, Object request, Class<T> responseType) {
        try {
            log(fullUrl, request);
            return restTemplate.postForEntity(fullUrl, request, responseType);
        } catch (Exception e) {
            return handleError(fullUrl, e);
        }
    }

    protected ResponseEntity put(String fullUrl, HttpEntity requestObject, Class<String> responseType) {
        try {
            log(fullUrl, requestObject);
            return restTemplate.exchange(fullUrl, HttpMethod.PUT, requestObject, responseType);
        } catch (Exception e) {
            return handleError(fullUrl, e);
        }
    }

    private ResponseEntity handleError(String fullUrl, Exception e) {
        APP_LOG.error("Error Occurred while invoking: " + fullUrl, e);
        if (e instanceof RestClientResponseException) {
            RestClientResponseException exception = (RestClientResponseException) e;
            return ResponseEntity.status(exception.getRawStatusCode())
                    .headers(exception.getResponseHeaders())
                    .body(exception.getResponseBodyAsString());
        } else if (e instanceof HttpMessageConversionException) {
            HttpMessageConversionException exception = (HttpMessageConversionException) e;
            exception.printStackTrace();
            throw new RuntimeException(exception);
        } else if (e instanceof ResourceAccessException) {
            ResourceAccessException resourceAccessException = (ResourceAccessException) e;
            return ResponseEntity.status(404)
                    .body(null);
        }
        else {
            throw new RuntimeException(e);
        }
    }

    private void log(String url, Object body) {
        try {
            ACCESS_LOG.info(String.format("\n============ Payload Start ======= \n" +
                            "\tFull URL: %s ,\n " +
                            "\tBody: \n\t%s \n" +
                            "============ Payload End ======= \n", url,
                    objectMapper.writeValueAsString(body)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
