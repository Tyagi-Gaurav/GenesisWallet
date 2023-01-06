Feature: Scenarios for healthchecks, metrics, status.

  Scenario: User service should expose status endpoint
    When the user service is requested for metrics endpoint
    And the response should be received with HTTP status code 200
    Then the response should be a success status
