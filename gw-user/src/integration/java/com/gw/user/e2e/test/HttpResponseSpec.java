package com.gw.user.e2e.test;

import org.hamcrest.Matcher;
import org.springframework.test.web.reactive.server.WebTestClient;

public class HttpResponseSpec<V> implements TestResponse<V> {
    private final String responseKey;
    private final WebTestClient.ResponseSpec responseSpec;
    private V response;

    public HttpResponseSpec(Class<V> payloadClass, WebTestClient.ResponseSpec responseSpec) {
        this.responseKey = payloadClass.getSimpleName();
        response = responseSpec.returnResult(payloadClass).getResponseBody().blockFirst();
        this.responseSpec = responseSpec;
    }

    @Override
    public String responseKey() {
        return responseKey;
    }

    @Override
    public V responseValue() {
        return response;
    }

    public void matchStatus(Matcher<Integer> statusMatcher) {
        responseSpec.expectStatus().value(statusMatcher);
    }
}
