variable "AWS_ACCESS_KEY" {}
variable "AWS_SECRET_KEY" {}
variable "AWS_REGION" {
  default = "eu-west-1"
}
variable "AMIS" {
  type = map(string)
  default = {
    eu-west-1 = "ami-0f29c8402f8cce65c"
  }
}
variable "INSTANCE_USERNAME" {
  default = "ubuntu"
}