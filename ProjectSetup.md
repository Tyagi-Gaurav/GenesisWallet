# How to Setup Dev environment (Automate as much as possible)?

* Create AWS account and create a user which has programmatic admin access.
* Get Access key and Secret
* Install AWS CLI (Use the following link)
    * https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
* Install GraphQL playground
  * brew install --cask graphql-playground
* run `aws configure`
    * Provide access keys and secret
    * Region: `eu-west-1`
* Create S3 bucket in the EU region (To be automated)
    * Enable Encryption
    * Enable bucket versioning
* From infrastructure, execute
    * `terraform init`
    * `terraform plan`
    * `terraform apply`
* Install
