Feature: Observability & Monitoring Scenarios - healthchecks, metrics, status.

  Scenario: User service should expose status endpoint
    When the user service is requested for status
    And the response should be received with HTTP status code 200
    Then the response should be a success status

  Scenario: UI service should expose status endpoint
    When the UI service is requested for status
    And the response should be received with HTTP status code 200
    Then the response should be a success status

  Scenario: User service should expose metrics endpoint
    When the user service is requested for metrics
    And the response should be received with HTTP status code 200
    Then the response should contain following metrics
      | jvm_memory_used_bytes                                  |
      | http_server_requests_seconds                           |
      | grpc_server_request_duration                           |