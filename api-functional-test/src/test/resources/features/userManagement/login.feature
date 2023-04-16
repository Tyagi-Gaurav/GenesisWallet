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

  # TODO
#  Scenario: User should be able to login only from single place
#    Given a user attempts to create a new account with following details
#      | firstName | lastName | userName | password | dateOfBirth | gender | homeCountry |
#      | bcssdf    | defdsfdf | <random> | <random> | 19/03/1972  | FEMALE | AUS         |
#    And the response should be received with HTTP status code 201
#    And the user attempts to login using the new credentials
#    And the response should be received with HTTP status code 200
#    And the user login response contains an authorisation token in the response
#    And the user token received in the response is recorded
#    And the user attempts to login using the new credentials
#    And the user login response contains an authorisation token in the response
#    And the token received in response is different from first response
#    When the user tries to use first token to read user details
#    Then the response should be received with HTTP status code 401
