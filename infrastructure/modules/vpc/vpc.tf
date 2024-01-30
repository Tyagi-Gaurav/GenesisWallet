variable "ENV" {}
variable "AWS_REGION" {}

module "main-vpc" {
  source = "terraform-aws-modules/vpc/aws"
  version = "3.19.0"
  cidr = "10.0.0.0/16"

  name="vpc-${var.ENV}"
  azs = ["${var.AWS_REGION}a", "${var.AWS_REGION}b", "${var.AWS_REGION}c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  public_subnets = ["10.0.101.0/24", "10.0.102.0/24", "10.0.103.0/24"]

  enable_nat_gateway = true #Should be true for private subnets to be accessible
  enable_vpn_gateway = false
  enable_dns_hostnames = true
  enable_dns_support = true
  map_public_ip_on_launch = true
  propagate_public_route_tables_vgw = true

  tags = {
    Environment = var.ENV
  }
}

output "vpc_id" {
  value = module.main-vpc.vpc_id
}

output "private_subnets" {
  value = module.main-vpc.private_subnets
}

output "public_subnets" {
  value = module.main-vpc.public_subnets
}