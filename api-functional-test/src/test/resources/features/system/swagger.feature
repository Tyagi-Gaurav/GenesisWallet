Feature: Swagger should be accessible

  @Disabled
  Scenario: When trying to access swagger with HTTP, then should get success
    Given a client attempts to access swagger
    Then the response should be received with HTTP status code 200