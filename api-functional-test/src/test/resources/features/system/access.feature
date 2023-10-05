Feature: System access

  Scenario: When trying to access a non-existent endpoint without login then should get 401
    Given the API is accessed using a non-existent endpoint
    Then the response should be received with HTTP status code 401

  Scenario: When trying to login with bad media type should get 404
    Given a user attempts to create a new account with following details
      | firstName | lastName | userName | password | dateOfBirth |
      | bcssdf    | defdsfdf | <random> | <random> | 19/03/1972  |
    Then the response should be received with HTTP status code 201
    When the user attempts to login using a bad media type
    Then the response should be received with HTTP status code 404
