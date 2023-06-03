variable "ENV" {}
variable "AWS_ACCOUNT_ID" {}
variable "AWS_REGION" {}
variable "LOG_GROUP" {}
variable "VPC_ID" {}
variable "CLUSTER_NAME" {}
variable "VPC_SUBNETS" {}
variable "INSTANCE_TYPE" {}
variable "ACCESS_KEY_NAME" {}
variable "ECR_REPO_ARNS" {
  default = []
}
variable "ACCESSIBLE_PORTS" {
  type = map(object({
    FROM_PORT      = string
    TO_PORT        = string
    SECURITY_GROUP = string
  }))
}


variable "ECS_TERMINATION_POLICIES" {
  default = "OldestLaunchConfiguration,default"
}

variable "ECS_MINSIZE" {
  default = 1
}

variable "ECS_MAXSIZE" {
  default = 1
}

variable "ECS_DESIRED_CAPACITY" {
  default = 1
}

variable "ENABLE_SSH" {
  default = false
}

variable SSH_SECURITY_GROUP {
  default = ""
}

variable "ASSOCIATE_PUBLIC_IP_ADDRESS" {
  default = true
}