Feature: Users should be able to create an account

  Scenario: User should be able to create a new account
    Given that metrics are captured
    And a user attempts to create a new account with following details
      | firstName | lastName | userName | password | dateOfBirth |
      | bcssdf    | defdsfdf | <random> | <random> | 19/03/1972  |
    Then the response should be received with HTTP status code 201
    And the userId is received in the response
    And the user service is requested for metrics
    Then total registration metrics is incremented by 1
