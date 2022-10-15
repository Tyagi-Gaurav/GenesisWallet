@Migrated
Feature: Validating inputs

  Scenario Outline: Return 400 status code when input does not satisfy constraints
    Given a user attempts to create a new account with following details
      | firstName | lastName | userName   | password   | dateOfBirth | gender | homeCountry   |
      | bcdsf     | def      | <username> | <password> | <dob>       | FEMALE | <homeCountry> |
    Then the response should be received with HTTP status code 400
    Examples:
      | username              | password                    | dob        | homeCountry |
      | Usr                   | ddfdsffdf                   | 19/03/1972 | IND         |
      | gdfgjszghjghsgdgsjdgj | ddfdsffdf                   | 19/03/1972 | AUS         |
      | user                  | hfhdsfhdsfkhjhsdfkddd345345 | 19/03/1972 | AUT         |
      | user                  | hsyy                        | 19/03/1972 | BWA         |
      | user                  | dsfdsf                      | 1/03/1972  | SRI         |
      | user                  | eeet                        | 01/3/1972  | PAK         |
      | user                  | rtert                       | 01/03/66   | BAN         |
      | user                  | rtryy                       | 01/03/1966 | IN          |