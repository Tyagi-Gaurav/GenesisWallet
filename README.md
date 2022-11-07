# GenesisWallet

# Functional Features
* User Creation
* User Authorization
    * MFA
        * SMS
        * Captcha
        * Puzzle
    * Username/Password
* Automated Session Logout

* Push Notifications
    * Push price changes of crypto currencies

* QR Code Scanner 
    * Automatic scanning of wallet address, and the public keys, or users will have to type long walleet address characters

* Multiple Cryptocurrencies
* Paper Wallet Import
* Blockchain based transactions
* Payment Gateways
    * Buy or Sell currencies

# Non-Functional Features
* Security
* Service Discovery
    * Self registration
    * Deployment environment provided service registry
* Observability
    * HealthCheck API
    * Ready
    * Status
    * Log Aggregation
    * Distributed Tracing
        * Asign each request a unique ID
    * Exception Tracking
        * Report exceptions to an exception tracking service, which deduplicates exceptions, alerts developers, tracks resolutions.
    * Application Metrics 
    * Audit Logging
* Testing
    * Consumer-driven contract testing
        * Service meets expectations of clients
    * Consumer-side contract test
        * Verify client of a service can communicate with the service
    * Service component testing
* API Gateway
    * Authentication
    * Loggin
    * Versioning
    * Throttling
    * Load balancing
* Resilience
    * Circuit Breaker
    * BulkHead
    * Timeouts
    * Rate Limiting on Client
* Documentation
  * Swagger for APIs (https://github.com/Tyagi-Gaurav/GenesisWallet/issues/44)
    * Local URL (Use Safari): https://localhost/api/user/swagger-ui/# 
  * Information dashboard for application
    * Current Version in prod
    * HealthCheck panel
    * Availability parameters 

# All Diagrams
https://app.diagrams.net/#G16jsKiOVyKhlBEANwm2DxsQpYRsK9gUgp

# Notes
* Vault is structured as follows
  * Give the above role policy to access 
    * database
      * Define a path `database/*` which would contain database specific payloads in vault.
      * Create a policy that has access to above path.
      * Database path has postgres credentials on `database/postgres/user_service`.
  * A role for application to access vault `/auth/approle/role/<application_role>`
    * Give the above role access to database policy