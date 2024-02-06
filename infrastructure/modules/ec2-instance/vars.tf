variable "ENV" {}
variable "SUBNET_ID" {}
variable "VPC_ID" {}
variable "APP_NAME" {}
variable "ALLOWED_SECURITY_GROUPS" {
  type = map(object({
    FROM_PORT      = string
    TO_PORT        = string
    SECURITY_GROUP = string
  }))
}
variable "SECURITY_GROUP_NAME" {}
variable "ENABLE_SSH" {
  default = false
}
variable "ACCESS_KEY_NAME" {}
variable "ECR_REPO_ARNS" {}
variable "APP_ENV" {}
variable "AWS_REGION" {}
variable "AWS_ACCOUNT_ID" {}
variable "APP_IMAGE_NAME" {}
variable "APP_IMAGE_TAG" {}