data aws_caller_identity "current" {}

module "main-vpc" {
  source     = "../modules/vpc"
  ENV        = var.ENV
  AWS_REGION = var.AWS_REGION
}

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

module "allow_cluster_access" {
  source = "../modules/test-access"
  ENV    = var.ENV
  VPC_ID = module.main-vpc.vpc_id
}

output "ping_alb_dns" {
  value = module.ping-alb.dns_name
}