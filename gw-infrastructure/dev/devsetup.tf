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

#module "user_ec2" {
#  source                  = "../modules/ec2-instance"
#  ALLOWED_SECURITY_GROUPS = {
#    port1 = {
#      #EC2-Cluster->UserApp (HTTP)
#      FROM_PORT      = 9090
#      TO_PORT        = 9091
#      SECURITY_GROUP = module.dev-ecs-cluster.cluster_sg_id
#    },
#    port2 = {
#      #EC2-Cluster->UserApp (GRPC)
#      FROM_PORT      = 19090
#      TO_PORT        = 19090
#      SECURITY_GROUP = module.dev-ecs-cluster.cluster_sg_id
#    }
#  }
#  ECR_REPO_ARNS = [data.aws_ecr_repository.test_genesis_user_ecr.arn]
#  APP_NAME            = "user"
#  ENV                 = var.ENV
#  SECURITY_GROUP_NAME = "user-sg"
#  SUBNET_ID           = element(module.main-vpc.public_subnets, 0)
#  VPC_ID              = module.main-vpc.vpc_id
#  ENABLE_SSH          = true
#  ACCESS_KEY_NAME     = module.allow_cluster_access.ssh_key_pair_name
#  APP_ENV             = tomap({
#    VAULT_HOST   = module.vault_instance.vault_ec2_public_ip
#    ECR_REPO_URL = data.aws_ecr_repository.test_genesis_user_ecr.arn
#  })
#  APP_IMAGE_NAME = data.aws_ecr_repository.test_genesis_user_ecr.repository_url
#  APP_IMAGE_TAG  = "0.1.0"
#  AWS_ACCOUNT_ID = var.AWS_ACCOUNT_ID
#  AWS_REGION     = var.AWS_REGION
#}

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