variable "ENV" {}
variable "VPC_ID" {}
variable "SUBNET_ID" {}
variable "ALLOWED_SECURITY_GROUPS" {
  default = {}
}
variable "DB_HOST" {}
variable "DB_PORT" {}
variable "DB_USER" {}
variable "DB_PASSWORD" {}
variable "ACCESS_KEY_NAME" {} #To allow vault instance to be accessed