Feature: Users should be able to retrieve their details from the account

  Scenario: User should be able to create a new account
    Given that metrics are captured
    And a user attempts to create a new account with following details
      | firstName | lastName | userName | password | dateOfBirth | gender | homeCountry |
      | bcssdf    | defdsfdf | <random> | <random> | 19/03/1972  | MALE   | AUS         |
    And the response should be received with HTTP status code 201
    When the user service is requested for user details
    Then the following user details are returned in the response
      | firstName | lastName | dateOfBirth | gender | homeCountry |
      | bcssdf    | defdsfdf | 19/03/1972  | MALE   | AUS         |
