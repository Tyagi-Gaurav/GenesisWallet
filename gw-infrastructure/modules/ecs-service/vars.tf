variable "ENV" {}
variable "AWS_REGION" {}
variable "APPLICATION_NAME" {}
variable "APPLICATION_PORT" {}
variable "PORT_MAPPINGS" {
  type = map(object({
    HOST_PORT        = string
    APPLICATION_PORT = string
  }))
}
variable "APPLICATION_VERSION" {}
variable "CLUSTER_ARN" {}
variable "SERVICE_ROLE_ARN" {}
variable "DESIRED_COUNT" {}
variable "ECR_REPO_URL" {}
variable "VPC_ID" {}
variable "APP_ENV" {
  type = map(string)
  default = {}
}

variable "DEPLOYMENT_MIN_HEALTHY_PERCENTAGE" {
  default = 100
}

variable "DEPLOYMENT_MAX_PERCENTAGE" {
  default = 200
}

variable "DEREGISTRATION_DELAY" {
  default = 30
}

variable "HEALTHCHECK_MATCHER" {
  default = "200"
}

variable "CPU_RESERVATION" {}
variable "MEMORY_RESERVATION" {}
variable "LOG_GROUP" {}

variable "TASK_ROLE_ARN" {
  default = ""
}

variable "HEALTH_CHECK_PATH" {}
variable "HEALTH_CHECK_PORT" {}