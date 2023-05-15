variable "API_GATEWAY" {
  default = "api-gateway"
}

module "api_gateway_alb" {
  ENV         = var.ENV
  source      = "../modules/alb"
  VPC_ID      = module.main-vpc.vpc_id
  ALB_NAME    = "${var.ENV}-${var.API_GATEWAY}-alb"
  TARGET_APPS = {
    group1 = {
      PORT             = 80
      TARGET_GROUP_ARN = module.api-gateway-ecs-service.target_group_arn
    }
  }
  DOMAIN            = "${var.ENV}.api-gateway.genesis"
  INTERNAL          = false
  ECS_SG            = module.dev-api-gateway-ecs-cluster.cluster_sg_id
  VPC_SUBNETS       = join(",", module.main-vpc.public_subnets)
  CERTIFICATE_ARN   = aws_acm_certificate.alb_cert.arn
  DELETE_PROTECTION = false #So that we can delete the alb
}

module "dev-api-gateway-ecs-cluster" {
  ENV                = var.ENV
  source             = "../modules/ecs-cluster"
  VPC_ID             = module.main-vpc.vpc_id
  CLUSTER_NAME       = "${var.ENV}-${var.API_GATEWAY}-ecs-cluster"
  INSTANCE_TYPE      = "t2.small"
  VPC_SUBNETS        = join(",", module.main-vpc.public_subnets) #For debug. Change to private subnet later.
  ENABLE_SSH         = true #Mark false for prod
  SSH_SECURITY_GROUP = module.allow_cluster_access.ssh_security_group_id
  LOG_GROUP          = "${var.ENV}-${var.API_GATEWAY}-log-group"
  AWS_ACCOUNT_ID     = data.aws_caller_identity.current.account_id
  AWS_REGION         = var.AWS_REGION
  ACCESS_KEY_NAME    = module.allow_cluster_access.ssh_key_pair_name
  ACCESSIBLE_PORTS = {
    #Required for ALB to be able to access the ECS cluster ports
    port1 = {
      #Api-Gateway
      FROM_PORT      = 80
      TO_PORT        = 80
      SECURITY_GROUP = module.api_gateway_alb.alb_security_group_id
    }
  }
}

module "api-gateway-ecs-service" {
  source           = "../modules/ecs-service"
  ENV              = var.ENV
  VPC_ID           = module.main-vpc.vpc_id
  APPLICATION_NAME = "api-gateway"
  APPLICATION_PORT = 80
  PORT_MAPPINGS    = {
    #Goes directly into task definition
    port1 = {
      HOST_PORT        = 80
      APPLICATION_PORT = 80
    }
  }
  APPLICATION_VERSION = "latest"
  CLUSTER_ARN         = module.dev-api-gateway-ecs-cluster.cluster_arn
  SERVICE_ROLE_ARN    = module.dev-api-gateway-ecs-cluster.service_role_arn
  AWS_REGION          = var.AWS_REGION
  CPU_RESERVATION     = "256"
  MEMORY_RESERVATION  = "128"
  LOG_GROUP           = "${var.ENV}-${var.API_GATEWAY}-log-group"
  DESIRED_COUNT       = 1
  ECR_REPO_URL        = "latest"
  HEALTH_CHECK_PATH = "/"
  HEALTH_CHECK_PORT = 80
}
