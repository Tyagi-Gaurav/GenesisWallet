variable "ENV" {}
variable "ALLOCATED_STORAGE" {}
variable "DB_NAME" {}
variable "DB_INSTANCE_CLASS" {}
variable "USERNAME" {}
variable "PASSWORD" {}
variable "SUBNET_IDS" {}
variable "ALLOWED_SECURITY_GROUP_IDS" {
  default = []
}
variable "VPC_ID" {
  default = ""
}