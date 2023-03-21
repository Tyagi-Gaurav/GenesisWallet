# IAM EC2 Role
resource "aws_iam_role" "ec2-iam-role" {
  name               = "${var.ENV}-${var.APP_NAME}-ec2-role"
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
resource "aws_iam_instance_profile" "ec2-role-instance-profile" {
  name = "${var.ENV}-${var.APP_NAME}-instance-profile"
  role = aws_iam_role.ec2-iam-role.name

  tags = {
    Environment = var.ENV
  }
}

resource "aws_iam_role_policy" "ec2-iam-role-policy" {
  for_each = toset(var.ECR_REPO_ARNS)
  name     = "${var.ENV}-${var.APP_NAME}-iam-ecr-specific-role-policy"
  role     = aws_iam_role.ec2-iam-role.id

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
        "ecr:GetRepositoryPolicy",
        "ecr:DescribeRepositories",
        "ecr:BatchGetImage",
        "ecr:DescribeImages",
        "ecr:ListImages"
      ],
      "Resource": [
        "${each.value}"
      ]
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "ec2-iam-role-all-resources-policy" {
  name = "${var.ENV}-${var.APP_NAME}-all-resources-iam-role-policy"
  role = aws_iam_role.ec2-iam-role.id

  policy = <<EOF
{
  "Version" : "2012-10-17",
  "Statement" : [
    {
      "Effect" : "Allow",
      "Action" : [
          "ecr:GetAuthorizationToken",
          "ecr:BatchCheckLayerAvailability",
          "ecr:GetDownloadUrlForLayer",
          "ecr:GetRepositoryPolicy",
          "ecr:DescribeRepositories",
          "ecr:BatchGetImage",
          "ecr:DescribeImages",
          "ecr:ListImages",
          "logs:CreateLogStream",
          "logs:PutLogEvents",
          "logs:CreateLogGroup",
          "logs:DescribeLogStreams"
      ],
      "Resource" : "*"
    }]
}
EOF
}