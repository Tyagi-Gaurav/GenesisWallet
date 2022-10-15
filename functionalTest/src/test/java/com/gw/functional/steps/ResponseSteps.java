package com.gw.functional.steps;

import com.gw.functional.util.ResponseHolder;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResponseSteps implements En {
    @Autowired
    private ResponseHolder responseHolder;

    public ResponseSteps() {
        Then("^the response should be received with HTTP status code (\\d+)$", (Integer responseCode) -> {
            assertThat(responseHolder.getResponseCode()).isEqualTo(responseCode);
        });

        And("^the response should be a success status response$", () -> {
            String response = responseHolder.readResponse(String.class);
            assertThat(response).isEqualTo("OK");
        });

        And("^the user login response contains an authorisation token in the header$", () -> {
            final HttpHeaders headers = responseHolder.getHeaders();
            assertTrue(headers.containsKey("X-AUTH-TOKEN"));
        });

        And("^the response contains the (.*) header in response$", (String headerName) -> {
            assertThat(responseHolder.getHeaders()).containsKey(headerName);
        });
    }
}
