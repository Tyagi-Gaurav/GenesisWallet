variable "PING-APP" {
  default = "ping-app"
}

module "dev-ui-ecs-cluster" {
  ENV                = var.ENV
  source             = "../modules/ecs-cluster"
  VPC_ID             = module.main-vpc.vpc_id
  CLUSTER_NAME       = "${var.ENV}-${var.PING-APP}-ecs-cluster"
  INSTANCE_TYPE      = "t2.small"
  VPC_SUBNETS        = join(",", module.main-vpc.public_subnets) #For debug. Change to private subnet later.
  ENABLE_SSH         = false #Disable for prod
  SSH_SECURITY_GROUP = module.allow_cluster_access.ssh_security_group_id
  LOG_GROUP          = "${var.ENV}-${var.PING-APP}-log-group"
  AWS_ACCOUNT_ID     = data.aws_caller_identity.current.account_id
  AWS_REGION         = var.AWS_REGION
  ACCESS_KEY_NAME    = module.allow_cluster_access.ssh_key_pair_name
  ECR_REPO_ARNS      = [
    data.aws_ecr_repository.test_genesis_ui_ecr.arn
  ]
  ACCESSIBLE_PORTS = {
    #Required for ALB to be able to access the ECS cluster ports
    port1 = {
      #UI_App
      FROM_PORT      = 6060
      TO_PORT        = 6061
      SECURITY_GROUP = module.ui-alb.alb_security_group_id
    }
  }
}

module "ui-alb" {
  ENV         = var.ENV
  source      = "../modules/alb"
  VPC_ID      = module.main-vpc.vpc_id
  ALB_NAME    = "${var.ENV}-${var.PING-APP}-alb"
  TARGET_APPS = {
    group1 = {
      PORT             = 8080
      TARGET_GROUP_ARN = module.ui-ecs-service.target_group_arn
    }
  }
  DOMAIN            = "${var.ENV}.${var.PING-APP}.genesis"
  INTERNAL          = false
  ECS_SG            = module.dev-ui-ecs-cluster.cluster_sg_id
  VPC_SUBNETS       = join(",", module.main-vpc.public_subnets)
  CERTIFICATE_ARN   = aws_acm_certificate.alb_cert.arn
  DELETE_PROTECTION = false #So that we can delete the alb
}

module "ping-ecs-service" {
  source           = "../modules/ecs-service"
  ENV              = var.ENV
  VPC_ID           = module.main-vpc.vpc_id
  APPLICATION_NAME = "gw-ui"
  APPLICATION_PORT = 6060
  PORT_MAPPINGS    = {
    #Goes directly into task definition
    port1 = {
      HOST_PORT        = 6060
      APPLICATION_PORT = 6060
    },
    port2 = {
      HOST_PORT        = 6061
      APPLICATION_PORT = 6061
    }
  }
  APPLICATION_VERSION = "v0.1.6"
  CLUSTER_ARN         = module.dev-ui-ecs-cluster.cluster_arn
  SERVICE_ROLE_ARN    = module.dev-ui-ecs-cluster.service_role_arn
  AWS_REGION          = var.AWS_REGION
  CPU_RESERVATION     = "256"
  MEMORY_RESERVATION  = "128"
  LOG_GROUP           = "${var.ENV}-${var.PING-APP}-log-group"
  DESIRED_COUNT       = 1
  ECR_REPO_URL        = data.aws_ecr_repository.test_genesis_ui_ecr.repository_url
  HEALTH_CHECK_PATH = "/actuator/healthcheck/status"
  HEALTH_CHECK_PORT = 8081
}

data "aws_ecr_repository" "test_genesis_ui_ecr" {
  name = "test_genesis/gw-ui"
}

resource "aws_ecr_repository_policy" "test_genesis_ui_ecr_policy" {
  repository = data.aws_ecr_repository.test_genesis_ui_ecr.name

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
                      "arn:aws:iam::${var.AWS_ACCOUNT_ID}:role/${var.ENV}-${var.PING-APP}-ecs-cluster-ecs-role",
                      "arn:aws:iam::${var.AWS_ACCOUNT_ID}:role/${var.ENV}-${var.PING-APP}-ecs-cluster-ec2-role"
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
