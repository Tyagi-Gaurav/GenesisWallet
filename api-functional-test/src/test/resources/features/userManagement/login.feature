Feature: Users should be able to Login & Logout

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

  Scenario: A new token should be generated when user tries to login again on the same session
    Given a user attempts to create a new account with following details
      | firstName | lastName | userName | password | dateOfBirth | gender | homeCountry |
      | bcssdf    | defdsfdf | <random> | <random> | 19/03/1972  | FEMALE | AUS         |
    And the response should be received with HTTP status code 201
    And the user attempts to login using the new credentials
    And the response should be received with HTTP status code 200
    And the user login response contains an authorisation token in the response
    And the user token received in the response is recorded as 'tokenA'
    And the user attempts to login using the new credentials
    And the user login response contains an authorisation token in the response
    And the user token received in the response is recorded as 'tokenB'
    And the token 'tokenB' is different from 'tokenA'

  Scenario: User should be able to login only from single session
    Given a user attempts to create a new account with following details
      | firstName | lastName | userName | password | dateOfBirth | gender | homeCountry |
      | bcssdf    | defdsfdf | <random> | <random> | 19/03/1972  | FEMALE | AUS         |
    And the response should be received with HTTP status code 201
    And the user attempts to login using the new credentials
    And the response should be received with HTTP status code 200
    And the user login response contains an authorisation token in the response
    And the user token received in the response is recorded as 'tokenA'
    And the user attempts to login using the new credentials
    And the user login response contains an authorisation token in the response
    And the user token received in the response is recorded as 'tokenB'
    And the token 'tokenA' is different from 'tokenB'
    When the user tries to use 'tokenA' to read user details
    Then the response should be received with HTTP status code 401

  Scenario: User should be able to logout and then not use same token for access
    Given a user attempts to create a new account with following details
      | firstName | lastName | userName | password | dateOfBirth | gender | homeCountry |
      | bcssdf    | defdsfdf | <random> | <random> | 19/03/1972  | FEMALE | AUS         |
    And the response should be received with HTTP status code 201
    And the user attempts to login using the new credentials
    And the response should be received with HTTP status code 200
    And the user login response contains an authorisation token in the response
    And the user token received in the response is recorded as 'tokenA'
    And the user attempts to logout using 'tokenA'
    And the response should be received with HTTP status code 200
    When the user tries to use 'tokenA' to read user details
    Then the response should be received with HTTP status code 401