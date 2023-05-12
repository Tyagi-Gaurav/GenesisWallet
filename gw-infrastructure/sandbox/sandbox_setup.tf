data aws_caller_identity "current" {}

module "main-vpc" {
  source     = "../modules/vpc"
  ENV        = var.ENV
  AWS_REGION = var.AWS_REGION
}

resource "aws_security_group" "default_vpc_security_group" {
  vpc_id      = module.main-vpc.vpc_id
  description = "All security group"

  ingress {
    from_port = 6379
    to_port   = 6379
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    protocol  = "-1"
    to_port   = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

data "aws_ecr_repository" "test_genesis_ui_ecr" {
  name = "test_genesis/gw-ui"
}

data "aws_iam_policy_document" "test_genesis_ui_ecr_policy" {
  statement {
    sid    = "AllowPushPullFromRepository"
    effect = "Allow"

    principals {
      type        = "AWS"
      identifiers = [
        "arn:aws:iam::${var.AWS_ACCOUNT_ID}:user/terraform-gt-user",
        "arn:aws:iam::${var.AWS_ACCOUNT_ID}:root",
        "arn:aws:iam::${var.AWS_ACCOUNT_ID}:role/dev-ecs-cluster-ec2-role"
#        "arn:aws:iam::${var.AWS_ACCOUNT_ID}:role/dev-ecs-cluster-ecs-role"
      ]
    }

    actions = [
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
}

resource "aws_ecr_repository_policy" "test_genesis_ui_ecr_policy" {
  repository = data.aws_ecr_repository.test_genesis_ui_ecr.name
  policy = data.aws_iam_policy_document.test_genesis_ui_ecr_policy.json
}

output "vpc_id" {
  value = module.main-vpc.vpc_id
}

#module "elasticache_instance" {
#  source = "../modules/elasticache"
#  ENV = var.ENV
#  VPC_ID = module.main-vpc.vpc_id
#  ALLOWED_SECURITY_GROUP_IDS = [aws_security_group.default_vpc_security_group.id]
#  SUBNET_IDS = module.main-vpc.public_subnets
#}

#output "elasticache_host" {
#  value = module.elasticache_instance.elasticache_cluster_cache_nodes[0].address
#}
#
#output "elasticache_host_config_endpoint" {
#  value = module.elasticache_instance.elasticache_cluster_configuration_endpoint
#}

