Feature: Access with HTTP should be redirected

  Scenario: When trying to access app with HTTP, then should get redirection
    Given a user attempts to create a new account with following details with HTTP
      | firstName | lastName | userName | password | dateOfBirth | gender | homeCountry |
      | bcssdf    | defdsfdf | <random> | <random> | 19/03/1972  | MALE   | AUS         |
    Then the response should be received with HTTP status code 201