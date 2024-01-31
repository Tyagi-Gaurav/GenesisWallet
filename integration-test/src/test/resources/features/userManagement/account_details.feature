@Disabled
Feature: Users should be able to retrieve their details from the account

  Scenario: User should be able to create a new account and NOT fetch user details without login
    Given that metrics are captured
    And a user attempts to create a new account with following details
      | firstName | lastName | userName | password | dateOfBirth |
      | bcssdf    | defdsfdf | <random> | <random> | 19/03/1972  |
    And the response should be received with HTTP status code 201
    When the user service is requested for user details without login
    Then the response should be received with HTTP status code 401

  Scenario: User should be able to create a new account and fetch user details after login
    Given that metrics are captured
    And a user attempts to create a new account with following details
      | firstName | lastName | userName | password | dateOfBirth |
      | bcssdf    | defdsfdf | <random> | <random> | 19/03/1972  |
    And the response should be received with HTTP status code 201
    And the user attempts to login using the new credentials
    And the response should be received with HTTP status code 200
    And the user login response contains an authorisation token in the response
    When the user service is requested for user details
    Then the following user details are returned in the response
      | firstName | lastName | dateOfBirth |
      | bcssdf    | defdsfdf | 19/03/1972  |