Feature: Users should be able to Login

  Scenario Outline: User should be able to create a new account and login
    Given a user attempts to create a new account with following details
      | firstName | lastName | userName | role | password | dateOfBirth | gender        | homeCountry |
      | bcssdf    | defdsfdf | <random> | USER | <random> | 19/03/1972  | <GenderValue> | AUS         |
    Then the response should be received with HTTP status code 201
    Examples:
      | GenderValue |
      | FEMALE      |
      | MALE        |