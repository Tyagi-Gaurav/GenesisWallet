package com.gw.functional.steps;

import com.gw.functional.config.ScenarioContext;
import com.gw.functional.domain.TestAccountCreateRequestDTO;
import com.gw.functional.domain.TestGender;
import com.gw.functional.resource.TestAccountCreateResource;
import com.gw.functional.util.ResponseHolder;
import io.cucumber.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.gw.functional.util.StringValueScenario.actualOrRandom;
import static org.assertj.core.api.Assertions.assertThat;

public class UserSteps implements En {
    @Autowired
    private TestAccountCreateResource testAccountCreateResource;

    @Autowired
    private ScenarioContext scenarioContext;

    @Autowired
    private ResponseHolder responseHolder;

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

                    //loginUsing(scenarioContext.getCredentials(role));
                    assertThat(responseHolder.getResponseCode()).isEqualTo(200);
                });

        And("^the userId for the user is recorded$", () -> {
            scenarioContext.setUserIdForRegularUser(responseHolder.getUserId());
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
    }
}
