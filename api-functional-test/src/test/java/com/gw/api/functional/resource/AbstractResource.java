package com.gw.api.functional.resource;

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
    protected HttpEntity EMPTY_HTTP_ENTITY = new HttpEntity(HttpHeaders.EMPTY);
    private static final Logger LOG = LogManager.getLogger("APP");

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
            return restTemplate.exchange(fullUrl, HttpMethod.GET, requestObject, responseType);
        } catch (Exception e) {
            return handleError(fullUrl, e);
        }
    }

    protected <T> ResponseEntity delete(String fullUrl, HttpEntity requestObject, Class<T> responseType) {
        try {
            return restTemplate.exchange(fullUrl, HttpMethod.DELETE, requestObject, responseType);
        } catch (Exception e) {
            return handleError(fullUrl, e);
        }
    }

    protected <T> ResponseEntity post(String fullUrl, Object request, Class<T> responseType) {
        try {
            return restTemplate.postForEntity(fullUrl, request, responseType);
        } catch (Exception e) {
            return handleError(fullUrl, e);
        }
    }

    protected ResponseEntity put(String fullUrl, HttpEntity requestObject, Class<String> responseType) {
        try {
            return restTemplate.exchange(fullUrl, HttpMethod.PUT, requestObject, responseType);
        } catch (Exception e) {
            return handleError(fullUrl, e);
        }
    }

    private ResponseEntity handleError(String fullUrl, Exception e) {
        LOG.error("Error occured while accessing: {}", fullUrl);
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
            return ResponseEntity.status(404)
                    .body(null);
        }
        else {
            throw new RuntimeException(e);
        }
    }
}
