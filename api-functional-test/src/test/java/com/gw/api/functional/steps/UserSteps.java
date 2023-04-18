package com.gw.api.functional.steps;

import com.gw.api.functional.config.ScenarioContext;
import com.gw.api.functional.domain.*;
import com.gw.api.functional.resource.TestAccountCreateResource;
import com.gw.api.functional.resource.TestAccountDetailsRequestResource;
import com.gw.api.functional.resource.TestLoginResource;
import com.gw.api.functional.util.ResponseHolder;
import io.cucumber.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.gw.api.functional.util.StringValueScenario.actualOrRandom;
import static org.assertj.core.api.Assertions.assertThat;

public class UserSteps implements En {
    @Autowired
    private TestAccountCreateResource testAccountCreateResource;
    @Autowired
    private TestAccountDetailsRequestResource testAccountDetailsRequestResource;
    @Autowired
    private ScenarioContext scenarioContext;
    @Autowired
    private ResponseHolder responseHolder;
    @Autowired
    private TestLoginResource testLoginResource;

    public UserSteps() {
        Given("^a user attempts to create a new account with following details$",
                (TestAccountCreateRequestDTO testAccountCreateRequestDTO) -> {
                    String userNameValue = testAccountCreateRequestDTO.userName();

                    if ("<captured>".equals(userNameValue)) {
                        userNameValue = scenarioContext.getLastUserName();
                    }

                    testAccountCreateRequestDTO = new TestAccountCreateRequestDTO(
                            actualOrRandom(userNameValue, 6),
                            actualOrRandom(testAccountCreateRequestDTO.password(), 6),
                            testAccountCreateRequestDTO.firstName(),
                            testAccountCreateRequestDTO.lastName(),
                            testAccountCreateRequestDTO.dateOfBirth(),
                            testAccountCreateRequestDTO.gender(),
                            testAccountCreateRequestDTO.homeCountry()
                    );

                    scenarioContext.storeCredentialsRequest(testAccountCreateRequestDTO);
                    testAccountCreateResource.create(testAccountCreateRequestDTO);
                });

        Given("^a user creates a new account and performs login with user name '(.*)' and role '(.*)'$",
                (String userName, String role) -> {
                    TestAccountCreateRequestDTO testAccountCreateRequestDTO = new TestAccountCreateRequestDTO(
                            actualOrRandom(userName, 6),
                            RandomStringUtils.randomAlphabetic(6),
                            RandomStringUtils.randomAlphabetic(6),
                            RandomStringUtils.randomAlphabetic(6),
                            "12/04/1990",
                            TestGender.MALE,
                            "AUS");

                    scenarioContext.storeCredentialsRequest(testAccountCreateRequestDTO);
                    testAccountCreateResource.create(testAccountCreateRequestDTO);

                    loginUsing(scenarioContext.getCredentials());
                    assertThat(responseHolder.getResponseCode()).isEqualTo(200);
                });

        When("the authenticated admin user creates another user with user name {string} and role {string}",
                (String userName, String role) -> {
                    TestAccountCreateRequestDTO testAccountCreateRequestDTO = new TestAccountCreateRequestDTO(
                            actualOrRandom(userName, 6),
                            RandomStringUtils.randomAlphabetic(6),
                            RandomStringUtils.randomAlphabetic(6),
                            RandomStringUtils.randomAlphabetic(6),
                            "12/04/1990",
                            TestGender.MALE,
                            "AUS");

                    scenarioContext.storeCredentialsRequest(testAccountCreateRequestDTO);
                    //userManagementResource.create(testAccountCreateRequestDTO);
                });

        And("^the userName is captured$", () -> {
            scenarioContext.setLastUserName(scenarioContext.getUserCredentialsRequest().userName());
        });

        When("^the user attempts to login using the new credentials$", () -> {
            loginUsing(scenarioContext.getUserCredentialsRequest());
        });
        Given("^a user attempts to create a new account with following details with HTTP$",
                (TestAccountCreateRequestDTO testAccountCreateRequestDTO) -> {
            String userNameValue = testAccountCreateRequestDTO.userName();

            if ("<captured>".equals(userNameValue)) {
                userNameValue = scenarioContext.getLastUserName();
            }

            testAccountCreateRequestDTO = new TestAccountCreateRequestDTO(
                    actualOrRandom(userNameValue, 6),
                    actualOrRandom(testAccountCreateRequestDTO.password(), 6),
                    testAccountCreateRequestDTO.firstName(),
                    testAccountCreateRequestDTO.lastName(),
                    testAccountCreateRequestDTO.dateOfBirth(),
                    testAccountCreateRequestDTO.gender(),
                    testAccountCreateRequestDTO.homeCountry()
            );

            scenarioContext.storeCredentialsRequest(testAccountCreateRequestDTO);
            testAccountCreateResource.createWithHttp(testAccountCreateRequestDTO);
        });

        When("^the user service is requested for user details$", () -> {
            var accountCreateResponseDTO = responseHolder.getResponse(TestAccountCreateResponseDTO.class);
            var loginResponseDTO = responseHolder.getResponse(TestLoginResponseDTO.class);
            testAccountDetailsRequestResource.getUserDetails(accountCreateResponseDTO.userId(),
                    loginResponseDTO.token());
        });

        Then("^the following user details are returned in the response$", (TestUserDetailsFetchResponseDTO expectedDetails) -> {
            var response = responseHolder.getResponse(TestUserDetailsFetchResponseDTO.class);
            assertThat(response).isEqualTo(expectedDetails);
        });
        When("^the user service is requested for user details without login$", () -> {
            var accountCreateResponseDTO = responseHolder.getResponse(TestAccountCreateResponseDTO.class);
            testAccountDetailsRequestResource.getUserDetails(accountCreateResponseDTO.userId(), "");
        });

        And("^the token '(.*)' is different from '(.*)'$", (String tokenA, String tokenB) -> {
            String token1 = responseHolder.getToken(tokenB);
            String token2 = responseHolder.getToken(tokenA);
            assertThat(token2).isNotEmpty();
            assertThat(token1).isNotEmpty();
            assertThat(token2).isNotEqualTo(token1);
        });

        When("^the user tries to use '(.*)' to read user details$", (String token) -> {
            var accountCreateResponseDTO = responseHolder.getResponse(TestAccountCreateResponseDTO.class);
            testAccountDetailsRequestResource.getUserDetails(accountCreateResponseDTO.userId(),
                    token);
        });
    }

    private void loginUsing(TestAccountCreateRequestDTO userCredentialsRequest) {
        TestLoginRequestDTO testLoginRequestDTO = new TestLoginRequestDTO(
                userCredentialsRequest.userName(),
                userCredentialsRequest.password());
        testLoginResource.doLogin(testLoginRequestDTO);
    }
}
