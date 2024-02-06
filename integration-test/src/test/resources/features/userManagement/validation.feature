@Disabled
Feature: Validating inputs

  Scenario Outline: Return 400 status code when input does not satisfy constraints
    Given a user attempts to create a new account with following details
      | firstName | lastName | userName   | password   | dateOfBirth |
      | bcdsf     | def      | <username> | <password> | <dob>       |
    Then the response should be received with HTTP status code 400
    Examples:
      | username              | password                    | dob        |
      | Usr                   | ddfdsffdf                   | 19/03/1972 |
      | gdfgjszghjghsgdgsjdgj | ddfdsffdf                   | 19/03/1972 |
      | user                  | hfhdsfhdsfkhjhsdfkddd345345 | 19/03/1972 |
      | user                  | eeet                        | 01/3/1972  |
      | user                  | rtert                       | 01/03/66   |
      | user                  | rtryy                       | 01/03/1966 |