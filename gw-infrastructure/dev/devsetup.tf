data aws_caller_identity "current" {}

resource "tls_private_key" "dev_private_key" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "tls_self_signed_cert" "dev_self_signed_cert" {
  private_key_pem = tls_private_key.dev_private_key.private_key_pem

  subject {
    common_name  = "${var.ENV}.genesis"
    organization = "Genesis Inc"
  }

  validity_period_hours = 1

  allowed_uses = [
    "key_encipherment",
    "digital_signature",
    "server_auth",
  ]
}

resource "aws_acm_certificate" "alb_cert" {
  private_key      = tls_private_key.dev_private_key.private_key_pem
  certificate_body = tls_self_signed_cert.dev_self_signed_cert.cert_pem
}

module "main-vpc" {
  source     = "../modules/vpc"
  ENV        = var.ENV
  AWS_REGION = var.AWS_REGION
}

module "allow_cluster_access" {
  source = "../modules/test-access"
  ENV    = var.ENV
  VPC_ID = module.main-vpc.vpc_id
}

module "single_db_instance" {
  source                     = "../modules/single_db_instance"
  ALLOCATED_STORAGE          = "20"
  DB_INSTANCE_CLASS          = "db.t3.micro"
  DB_NAME                    = "devDB"
  ENV                        = var.ENV
  PASSWORD                   = "dev-password"
  SUBNET_IDS                 = module.main-vpc.private_subnets
  USERNAME                   = "devuser"
  VPC_ID                     = module.main-vpc.vpc_id
  ALLOWED_SECURITY_GROUP_IDS = [module.dev-user-ecs-cluster.cluster_sg_id]
}

output "vault_host" {
  value = module.vault_instance.vault_ec2_private_ip
}

output "api_gateway_alb_dns_host" {
  value = module.api_gateway_alb.dns_name
}

output "dev_ecr_arn" {
  value = data.aws_ecr_repository.test_genesis_user_ecr.arn
}

output "dev_ecr_repository_url" {
  value = data.aws_ecr_repository.test_genesis_user_ecr.repository_url
}

output "user_public_dns" {
  value = module.user-alb.dns_name
}
