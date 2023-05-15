variable "USER_APP" {
  default = "user"
}

module "user-alb" {
  ENV         = var.ENV
  source      = "../modules/alb"
  VPC_ID      = module.main-vpc.vpc_id
  ALB_NAME    = "${var.ENV}-user-alb"
  TARGET_APPS = {
    group2 = {
      PORT             = 9090
      TARGET_GROUP_ARN = module.user-ecs-service.target_group_arn
    }
  }
  DOMAIN            = "${var.ENV}.user.genesis"
  INTERNAL          = true
  ECS_SG            = module.dev-user-ecs-cluster.cluster_sg_id
  VPC_SUBNETS       = join(",", module.main-vpc.public_subnets)
  CERTIFICATE_ARN   = aws_acm_certificate.alb_cert.arn
  DELETE_PROTECTION = false #So that we can delete the alb
}

module "dev-user-ecs-cluster" {
  ENV                = var.ENV
  source             = "../modules/ecs-cluster"
  VPC_ID             = module.main-vpc.vpc_id
  CLUSTER_NAME       = "${var.ENV}-${var.USER_APP}-ecs-cluster"
  INSTANCE_TYPE      = "t2.small"
  VPC_SUBNETS        = join(",", module.main-vpc.public_subnets) #For debug. Change to private subnet later.
  ENABLE_SSH         = false #Disable for prod
  SSH_SECURITY_GROUP = module.allow_cluster_access.ssh_security_group_id
  LOG_GROUP          = "${var.ENV}-${var.USER_APP}-log-group"
  AWS_ACCOUNT_ID     = data.aws_caller_identity.current.account_id
  AWS_REGION         = var.AWS_REGION
  ACCESS_KEY_NAME    = module.allow_cluster_access.ssh_key_pair_name
  ECR_REPO_ARNS      = [
    data.aws_ecr_repository.test_genesis_user_ecr.arn
  ]
  ACCESSIBLE_PORTS = {
    #Required for ALB to be able to access the ECS cluster ports
    port1 = {
      #User-App
      FROM_PORT      = 9090
      TO_PORT        = 9091
      SECURITY_GROUP = module.user-alb.alb_security_group_id
    },
    port2 = {
      #UI->User GRPC
      FROM_PORT      = 19090
      TO_PORT        = 19090
      SECURITY_GROUP = module.user-alb.alb_security_group_id
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
  CLUSTER_ARN         = module.dev-user-ecs-cluster.cluster_arn
  SERVICE_ROLE_ARN    = module.dev-user-ecs-cluster.service_role_arn
  AWS_REGION          = var.AWS_REGION
  CPU_RESERVATION     = "256"
  MEMORY_RESERVATION  = "128"
  LOG_GROUP           = "${var.ENV}-${var.USER_APP}-log-group"
  DESIRED_COUNT       = 1
  ECR_REPO_URL        = data.aws_ecr_repository.test_genesis_user_ecr.repository_url
  APP_ENV             = tomap({
    VAULT_HOST = module.vault_instance.vault_ec2_private_ip
    DB_NAME    = module.single_db_instance.dbname
    CACHE_HOST = module.elasticache_instance.elasticache_cluster_cache_nodes
  })
  HEALTH_CHECK_PATH = "/actuator/healthcheck/status"
  HEALTH_CHECK_PORT = 9091
}

data "aws_ecr_repository" "test_genesis_user_ecr" {
  name = "test_genesis/gw-user"
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
                      "arn:aws:iam::${var.AWS_ACCOUNT_ID}:role/${var.ENV}-${var.USER_APP}-ecs-cluster-ecs-role",
                      "arn:aws:iam::${var.AWS_ACCOUNT_ID}:role/${var.ENV}-${var.USER_APP}-ecs-cluster-ec2-role"
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
