data aws_caller_identity "current" {}

data "aws_ecr_repository" "test_genesis_user_ecr" {
  name = "test_genesis/gw-user"
}

data "aws_ecr_repository" "test_genesis_ui_ecr" {
  name = "test_genesis/gw-ui"
}

data "aws_ecr_repository" "test_genesis_api_gateway_ecr" {
  name = "test_genesis/gw-api-gateway"
}

resource "aws_ecr_repository_policy" "test_genesis_user_ecr_policy" {
  repository = data.aws_ecr_repository.test_genesis_user_ecr.name

  policy = <<EOF
  {
      "Version": "2012-10-17",
      "Statement": [
          {
              "Sid": "AllowPushPullFromRepository",
              "Effect": "Allow",
              "Principal": {
                  "AWS" : [
                      "arn:aws:iam::${var.AWS_ACCOUNT_ID}:user/terraform-gt-user",
                      "arn:aws:iam::${var.AWS_ACCOUNT_ID}:root",
                      "arn:aws:iam::${var.AWS_ACCOUNT_ID}:role/dev-ecs-cluster-ec2-role",
                      "arn:aws:iam::${var.AWS_ACCOUNT_ID}:role/dev-ecs-cluster-ecs-role"
                  ]
              },
              "Action": [
                  "ecr:GetDownloadUrlForLayer",
                  "ecr:BatchGetImage",
                  "ecr:BatchCheckLayerAvailability",
                  "ecr:PutImage",
                  "ecr:InitiateLayerUpload",
                  "ecr:UploadLayerPart",
                  "ecr:CompleteLayerUpload",
                  "ecr:DescribeRepositories",
                  "ecr:GetRepositoryPolicy",
                  "ecr:ListImages",
                  "ecr:DeleteRepository",
                  "ecr:BatchDeleteImage",
                  "ecr:SetRepositoryPolicy",
                  "ecr:DeleteRepositoryPolicy",
                  "ecr:GetAuthorizationToken"
              ]
          }
      ]
  }
EOF
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
  ALLOWED_SECURITY_GROUP_IDS = [module.dev-ecs-cluster.cluster_sg_id]
}

module "vault_instance" {
  source                  = "../modules/vault"
  ENV                     = var.ENV
  SUBNET_ID               = element(module.main-vpc.public_subnets, 0)
  VPC_ID                  = module.main-vpc.vpc_id
  DB_HOST                 = module.single_db_instance.host
  DB_PORT                 = module.single_db_instance.port
  DB_USER                 = module.single_db_instance.username
  DB_PASSWORD             = module.single_db_instance.password
  ALLOWED_SECURITY_GROUPS = {
    group1 = module.allow_cluster_access.ssh_security_group_id,
    group2 = module.dev-ecs-cluster.cluster_sg_id
  }
  ACCESS_KEY_NAME = module.allow_cluster_access.ssh_key_pair_name
}

module "dev-ecs-cluster" {
  ENV                = var.ENV
  source             = "../modules/ecs-cluster"
  VPC_ID             = module.main-vpc.vpc_id
  CLUSTER_NAME       = "${var.ENV}-ecs-cluster"
  INSTANCE_TYPE      = "t2.small"
  VPC_SUBNETS        = join(",", module.main-vpc.public_subnets) #For debug. Change to private subnet later.
  ENABLE_SSH         = true #Disable for prod
  SSH_SECURITY_GROUP = module.allow_cluster_access.ssh_security_group_id
  LOG_GROUP          = "${var.ENV}-log-group"
  AWS_ACCOUNT_ID     = data.aws_caller_identity.current.account_id
  AWS_REGION         = var.AWS_REGION
  ACCESS_KEY_NAME    = module.allow_cluster_access.ssh_key_pair_name
  ECR_REPO_ARNS      = [
    data.aws_ecr_repository.test_genesis_user_ecr.arn,
    data.aws_ecr_repository.test_genesis_ui_ecr.arn
  ]
  ACCESSIBLE_PORTS = {
    #Required for ALB to be able to access the ECS cluster ports
    port1 = {
      FROM_PORT      = 9090
      TO_PORT        = 9090
      SECURITY_GROUP = module.alb.alb_security_group_id
    },
    port2 = {
      FROM_PORT      = 9091
      TO_PORT        = 9091
      SECURITY_GROUP = module.alb.alb_security_group_id
    },
    port3 = {
      FROM_PORT      = 19090
      TO_PORT        = 19090
      SECURITY_GROUP = module.alb.alb_security_group_id
    },
    port4 = {
      FROM_PORT      = 7070
      TO_PORT        = 7070
      SECURITY_GROUP = module.alb.alb_security_group_id
    },
    port5 = {
      FROM_PORT      = 7080
      TO_PORT        = 7080
      SECURITY_GROUP = module.alb.alb_security_group_id
    }
  }
}

module "user-ecs-service" {
  source           = "../modules/ecs-service"
  ENV              = var.ENV
  VPC_ID           = module.main-vpc.vpc_id
  APPLICATION_NAME = "gw-user"
  APPLICATION_PORT = 9090
  PORT_MAPPINGS    = {
    #Goes directly into task definition
    port1 = {
      HOST_PORT        = 9090
      APPLICATION_PORT = 9090
    },
    port2 = {
      HOST_PORT        = 9091
      APPLICATION_PORT = 9091
    },
    port3 = {
      HOST_PORT        = 19090
      APPLICATION_PORT = 19090
    },
  }
  APPLICATION_VERSION = "0.1.0"
  CLUSTER_ARN         = module.dev-ecs-cluster.cluster_arn
  SERVICE_ROLE_ARN    = module.dev-ecs-cluster.service_role_arn
  AWS_REGION          = var.AWS_REGION
  CPU_RESERVATION     = "256"
  MEMORY_RESERVATION  = "128"
  LOG_GROUP           = "${var.ENV}-log-group"
  DESIRED_COUNT       = 1
  ECR_REPO_URL        = data.aws_ecr_repository.test_genesis_user_ecr.repository_url
  APP_ENV             = tomap({
    VAULT_HOST = module.vault_instance.vault_ec2_private_ip
    DB_NAME    = module.single_db_instance.dbname
  })
  HEALTH_CHECK_PATH = "/actuator/healthcheck/status"
  HEALTH_CHECK_PORT = 9091
}

module "ui-ecs-service" {
  source           = "../modules/ecs-service"
  ENV              = var.ENV
  VPC_ID           = module.main-vpc.vpc_id
  APPLICATION_NAME = "gw-ui"
  APPLICATION_PORT = 8080
  PORT_MAPPINGS    = {
    #Goes directly into task definition
    port1 = {
      HOST_PORT        = 7070
      APPLICATION_PORT = 8080
    },
    port2 = {
      HOST_PORT        = 7080
      APPLICATION_PORT = 8081
    }
  }
  APPLICATION_VERSION = "0.1.0"
  CLUSTER_ARN         = module.dev-ecs-cluster.cluster_arn
  SERVICE_ROLE_ARN    = module.dev-ecs-cluster.service_role_arn
  AWS_REGION          = var.AWS_REGION
  CPU_RESERVATION     = "256"
  MEMORY_RESERVATION  = "128"
  LOG_GROUP           = "${var.ENV}-log-group"
  DESIRED_COUNT       = 1
  ECR_REPO_URL        = data.aws_ecr_repository.test_genesis_ui_ecr.repository_url
  APP_ENV             = tomap({
    USER_HOST = "localhost" #For dev, they would all be on same node
    USER_PORT = 19090
  })
  HEALTH_CHECK_PATH = "/actuator/healthcheck/status"
  HEALTH_CHECK_PORT = 9091
}

module "api-gateway-service" {
  source           = "../modules/ecs-service"
  ENV              = var.ENV
  VPC_ID           = module.main-vpc.vpc_id
  APPLICATION_NAME = "gw-api-gateway"
  APPLICATION_PORT = 80
  PORT_MAPPINGS    = {
    #Goes directly into task definition
    port1 = {
      HOST_PORT        = 80
      APPLICATION_PORT = 80
    }
  }
  APPLICATION_VERSION = "0.1.0"
  CLUSTER_ARN         = module.dev-ecs-cluster.cluster_arn
  SERVICE_ROLE_ARN    = module.dev-ecs-cluster.service_role_arn
  AWS_REGION          = var.AWS_REGION
  CPU_RESERVATION     = "256"
  MEMORY_RESERVATION  = "128"
  LOG_GROUP           = "${var.ENV}-log-group"
  DESIRED_COUNT       = 1
  ECR_REPO_URL        = data.aws_ecr_repository.test_genesis_api_gateway_ecr.repository_url
  APP_ENV             = tomap({
    USER_HOST = "localhost" #For dev, they would all be on same node
    USER_PORT = 19090
  })
  HEALTH_CHECK_PATH = "/actuator/healthcheck/status"
  HEALTH_CHECK_PORT = 9091
}

module "alb" {
  ENV         = var.ENV
  source      = "../modules/alb"
  VPC_ID      = module.main-vpc.vpc_id
  ALB_NAME    = "${var.ENV}-user-alb"
  TARGET_APPS = {
    group1 = {
      PORT             = 80
      TARGET_GROUP_ARN = module.ui-ecs-service.target_group_arn
    },
    group2 = {
      PORT             = 8080
      TARGET_GROUP_ARN = module.user-ecs-service.target_group_arn
    }
  }
  DOMAIN            = "${var.ENV}.user.genesis"
  INTERNAL          = false
  ECS_SG            = module.dev-ecs-cluster.cluster_sg_id
  VPC_SUBNETS       = join(",", module.main-vpc.public_subnets)
  CERTIFICATE_ARN   = aws_acm_certificate.alb_cert.arn
  DELETE_PROTECTION = false #So that we can delete the alb
}

output "vault_host" {
  value = module.vault_instance.vault_ec2_private_ip
}

output "alb_dns_host" {
  value = module.alb.dns_name
}

output "dev_ecr_arn" {
  value = data.aws_ecr_repository.test_genesis_user_ecr.arn
}

output "dev_ecr_repository_url" {
  value = data.aws_ecr_repository.test_genesis_user_ecr.repository_url
}