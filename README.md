[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Tyagi-Gaurav_GenesisWallet&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Tyagi-Gaurav_GenesisWallet)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Tyagi-Gaurav_GenesisWallet&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Tyagi-Gaurav_GenesisWallet)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Tyagi-Gaurav_GenesisWallet&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Tyagi-Gaurav_GenesisWallet)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Tyagi-Gaurav_GenesisWallet&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Tyagi-Gaurav_GenesisWallet)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Tyagi-Gaurav_GenesisWallet&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Tyagi-Gaurav_GenesisWallet)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Tyagi-Gaurav_GenesisWallet&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Tyagi-Gaurav_GenesisWallet)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Tyagi-Gaurav_GenesisWallet&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Tyagi-Gaurav_GenesisWallet)

# GenesisWallet

# How to Setup Dev environment (Automate as much as possible)?
  * Create AWS account and create a user which has programmatic admin access.
  * Get Access key and Secret
  * Install AWS CLI (Use the following link)
      * https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
  * run `aws configure`
      * Provide access keys and secret
      * Region: `eu-west-1`
  * Create S3 bucket in the EU region (To be automated)
    * Enable Encryption
    * Enable bucket versioning
  * From gw-infrastructure, execute
    * `terraform init`
    * `terraform plan`
    * `terraform apply`

# Local Setup

## Spin up stack locally
`docker-compose up -d --build --remove-orphans`

## Connecting to database
```
docker exec -it local.postgres bash
psql -h localhost -p 5432 -U user -d testUserDB
```
To List tables: `\dt USER_SCHEMA.*`

## How to create keystore for functional tests?

## How to create keystore for dev functional tests?

## How to create keypair using ssh-keygen?
```
ssh-keygen -t ed25519 -q -N "" -f ./ssh_key
```

# Services
* API Gateway (80)
* User (9090/19090)
* UI (8080)

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
https://swimlanes.io/u/mcghmm30h

# Notes
* Vault is structured as follows
  * Give the above role policy to access 
    * database
      * Define a path `database/*` which would contain database specific payloads in vault.
      * Create a policy that has access to above path.
      * Database path has postgres credentials on `database/postgres/user_service`.
  * A role for application to access vault `/auth/approle/role/<application_role>`
    * Give the above role access to database policy