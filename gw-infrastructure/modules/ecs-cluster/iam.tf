# ECS Service role
resource "aws_iam_role" "cluster-service-role" {
  name = "${var.CLUSTER_NAME}-ecs-role"

  assume_role_policy = <<EOF
  {
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Action" : "sts:AssumeRole",
        "Principal" : {
          "Service" : "ecs.amazonaws.com"
        },
        "Effect" : "Allow",
        "Sid" : ""
      }
    ]
  }
EOF

  tags = {
    Environment = var.ENV
  }
}

resource "aws_iam_role_policy" "cluster_service_role_policy" {
  name = "${var.CLUSTER_NAME}-policy"
  role = aws_iam_role.cluster-service-role.id

  policy = <<EOF
{
  "Version" : "2012-10-17",
  "Statement" : [
    {
      "Effect" : "Allow",
      "Action" : [
          "ec2:AuthorizeSecurityGroupIngress",
          "ec2:Describe*",
          "ecr:GetAuthorizationToken",
          "elasticloadbalancing:DeregisterInstancesFromLoadBalancer",
          "elasticloadbalancing:DeregisterTargets",
          "elasticloadbalancing:Describe*",
          "elasticloadbalancing:RegisterInstancesWithLoadBalancer",
          "elasticloadbalancing:RegisterTargets"
      ],
      "Resource" : "*"
    }
  ]
}
EOF
}

# IAM EC2 Role
resource "aws_iam_role" "cluster-iam-role" {
  name               = "${var.CLUSTER_NAME}-ec2-role"
  assume_role_policy = <<EOF
  {
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Action" : "sts:AssumeRole",
        "Principal" : {
          "Service" : "ec2.amazonaws.com"
        },
        "Effect" : "Allow",
        "Sid" : ""
      }
    ]
  }
EOF

  tags = {
    Environment = var.ENV
  }
}

#Used to pass role to an EC2 instance
resource "aws_iam_instance_profile" "cluster-ec2-role-instance-profile" {
  name = "${var.CLUSTER_NAME}-instance-profile"
  role = aws_iam_role.cluster-iam-role.name

  tags = {
    Environment = var.ENV
  }
}

resource "aws_iam_role_policy" "cluster-ecs-iam-role-policy" {
  name = "${var.CLUSTER_NAME}-ecs-iam-role-policy"
  role = aws_iam_role.cluster-iam-role.id

  policy = <<EOF
{
  "Version" : "2012-10-17",
  "Statement" : [
    {
      "Effect" : "Allow",
      "Action" : [
          "ecr:GetAuthorizationToken",
          "application-autoscaling:*",
          "ecs:CreateCluster",
          "ecs:DeregisterContainerInstance",
          "ecs:DiscoverPollEndpoint",
          "ecs:Poll",
          "ecs:RegisterContainerInstance",
          "ecs:StartTelemetrySession",
          "ecs:Submit*",
          "ecs:StartTask",
          "ecs:GetAuthorizationToken",
          "ecs:BatchCheckLayerAvailability",
          "ecs:GetDownloadUrlForLayer",
          "ecs:BatchGetImage",
          "logs:CreateLogStream",
          "logs:PutLogEvents",
          "logs:CreateLogGroup",
          "logs:DescribeLogStreams"
      ],
      "Resource" : "*"
    },
    {
      "Effect" : "Allow",
      "Action" : [
        "logs:*"
      ],
      "Resource" :  [
        "arn:aws:logs:${var.AWS_REGION}:${var.AWS_ACCOUNT_ID}:log-group:${var.LOG_GROUP}:*"
      ]
    }
  ]
}
EOF
}


resource "aws_iam_role_policy" "cluster-iam-role-policy" {
  for_each = toset(var.ECR_REPO_ARNS)
  name = "${var.CLUSTER_NAME}-iam-role-policy"
  role = aws_iam_role.cluster-iam-role.id

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowDescribeRepoImage",
      "Effect": "Allow",
      "Action": [
        "ecr:GetAuthorizationToken",
        "ecr:BatchCheckLayerAvailability",
        "ecr:GetDownloadUrlForLayer",
        "ecr:BatchGetImage"
      ],
      "Resource": [
        "${each.value}"
      ]
    }
  ]
}
EOF
}