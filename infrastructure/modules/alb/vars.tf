variable "ENV" {}
#Environment where we deploy - dev, int, etc.
variable "ALB_NAME" {}
variable "INTERNAL" {}
variable "VPC_ID" {}
#Location of VPC to host this ELB
variable "VPC_SUBNETS" {}
variable "DOMAIN" {}
variable "TARGET_APPS" {
  type = map(object({
    PORT             = number
    TARGET_GROUP_ARN = string
  }))
}
variable "CERTIFICATE_ARN" {}
variable "ECS_SG" {
  default = ""
}
variable "DELETE_PROTECTION" {
  default = true
}
variable "ACCESSIBLE_PORTS" {
  type = map(object({
    FROM_PORT      = string
    TO_PORT        = string
  }))
  default = {}
}