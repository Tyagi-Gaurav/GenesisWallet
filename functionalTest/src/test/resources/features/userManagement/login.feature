Feature: Users should be able to Login

  Scenario Outline: User should be able to create a new account and login
    Given a user attempts to create a new account with following details
      | firstName | lastName | userName | password | dateOfBirth | gender        | homeCountry |
      | bcssdf    | defdsfdf | <random> | <random> | 19/03/1972  | <GenderValue> | AUS         |
    Then the response should be received with HTTP status code 201
    When the user attempts to login using the new credentials
    Then the response should be received with HTTP status code 200
    And the user login response contains an authorisation token in the response
    Examples:
      | GenderValue |
      | FEMALE      |
      | MALE        |