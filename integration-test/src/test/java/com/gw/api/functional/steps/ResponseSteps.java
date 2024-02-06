package com.gw.api.functional.steps;

import com.gw.api.functional.domain.TestAccountCreateResponseDTO;
import com.gw.api.functional.domain.TestLoginResponseDTO;
import com.gw.api.functional.util.ResponseHolder;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

public class ResponseSteps implements En {
    @Autowired
    private ResponseHolder responseHolder;

    public ResponseSteps() {
        Then("^the response should be received with HTTP status code (\\d+)$", (Integer responseCode) -> {
            assertThat(responseHolder.getResponseCode().value()).isEqualTo(responseCode);
        });

        And("^the user login response contains an authorisation token in the response", () -> {
            TestLoginResponseDTO testLoginResponseDTO = responseHolder.getResponse(TestLoginResponseDTO.class);
            assertThat(testLoginResponseDTO.token()).isNotEmpty();
        });

        And("^the response contains the (.*) header in response$", (String headerName) -> {
            assertThat(responseHolder.getHeaders()).containsKey(headerName);
        });

        Then("^the response should be a success status$", () -> {
            String response = responseHolder.getResponse(String.class);
                assertThat(response).isEqualTo("{\"status\":\"UP\"}");
        });

        And("^the userId is received in the response$", () -> {
            TestAccountCreateResponseDTO response = responseHolder.getResponse(TestAccountCreateResponseDTO.class);
            assertThat(response.userId()).isNotNull();
            assertThatNoException().isThrownBy(() -> fromString(response.userId()));
        });

        And("^the user token received in the response is recorded as '(.*)'$", (String tokenKey) -> {
            var loginResponseDTO = responseHolder.getResponse(TestLoginResponseDTO.class);
            responseHolder.storeUserToken(tokenKey, loginResponseDTO.token());
        });
    }
}
