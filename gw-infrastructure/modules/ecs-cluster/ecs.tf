#
# ECS AMI
data "aws_ami" "ecs" {
  most_recent = true

  filter {
    name   = "name"
    values = ["amzn-ami-*-amazon-ecs-optimized"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["591542846629"] #AWS
}

#ECS Cluster
resource "aws_ecs_cluster" "ecs_cluster" {
  name = var.CLUSTER_NAME

  tags = {
    Environment = var.ENV
  }
}

data "template_file" "ecs_init" {
  template = file("${path.module}/templates/ecs_init.tpl") #This associates the AWS instance to cluster
  vars = {
    CLUSTER_NAME = var.CLUSTER_NAME
  }
}

#Launch Config
resource "aws_launch_configuration" "ecs_cluster_launch_config" {
  image_id      = data.aws_ami.ecs.id
  instance_type = var.INSTANCE_TYPE
  name_prefix = "ecs-${var.CLUSTER_NAME}-launch-config"
  iam_instance_profile = aws_iam_instance_profile.cluster-ec2-role-instance-profile.id
  security_groups = [aws_security_group.cluster_sg.id]
  user_data = data.template_file.ecs_init.rendered
  associate_public_ip_address = var.ASSOCIATE_PUBLIC_IP_ADDRESS #Disabling this fails to bring up ECS cluster.
  key_name = var.ACCESS_KEY_NAME

  lifecycle {
    create_before_destroy = true
  }
}

#Autoscaling
resource "aws_autoscaling_group" "ecs_cluster_autoscaling" {
  name = "ecs-${var.CLUSTER_NAME}-autoscaling"
  vpc_zone_identifier = split(",", var.VPC_SUBNETS)
  launch_configuration = aws_launch_configuration.ecs_cluster_launch_config.name
  termination_policies = split(",", var.ECS_TERMINATION_POLICIES)
  min_size = var.ECS_MINSIZE
  max_size = var.ECS_MAXSIZE
  desired_capacity = var.ECS_DESIRED_CAPACITY

  tag {
    key                 = "Name"
    propagate_at_launch = true
    value               = "${var.CLUSTER_NAME}-ecs"
  }

  tag {
    key                 = "Environment"
    propagate_at_launch = true
    value               = var.ENV
  }
}
