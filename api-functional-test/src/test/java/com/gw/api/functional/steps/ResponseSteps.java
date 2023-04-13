package com.gw.api.functional.steps;

import com.gw.api.functional.domain.TestLoginResponseDTO;
import com.gw.api.functional.util.ResponseHolder;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseSteps implements En {
    @Autowired
    private ResponseHolder responseHolder;

    public ResponseSteps() {
        Then("^the response should be received with HTTP status code (\\d+)$", (Integer responseCode) -> {
            assertThat(responseHolder.getResponseCode()).isEqualTo(responseCode);
        });

        And("^the user login response contains an authorisation token in the response", () -> {
            TestLoginResponseDTO testLoginResponseDTO = responseHolder.readResponse(TestLoginResponseDTO.class);
            assertThat(testLoginResponseDTO.token()).isNotEmpty();
        });

        And("^the response contains the (.*) header in response$", (String headerName) -> {
            assertThat(responseHolder.getHeaders()).containsKey(headerName);
        });

        Then("^the response should be a success status$", () -> {
            String response = responseHolder.readResponse(String.class);
            assertThat(response).isEqualTo("{\"status\":\"UP\"}");
        });
    }
}
