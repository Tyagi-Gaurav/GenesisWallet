resource "aws_security_group" "security_group" {
  vpc_id = var.VPC_ID
  name   = var.SECURITY_GROUP_NAME
}

resource "aws_security_group_rule" "ingress_rule" {
  for_each                 = var.ALLOWED_SECURITY_GROUPS
  from_port                = each.value["FROM_PORT"]
  protocol                 = "tcp"
  security_group_id        = aws_security_group.security_group.id
  to_port                  = each.value["TO_PORT"]
  type                     = "ingress"
  source_security_group_id = each.value["SECURITY_GROUP"]
}

resource "aws_security_group_rule" "egress_rule" {
  from_port         = 0
  protocol          = "-1"
  security_group_id = aws_security_group.security_group.id
  to_port           = 0
  type              = "egress"
  cidr_blocks       = ["0.0.0.0/0"]
}

data "template_file" "ec2_setup_script" {
  template = file("${path.module}/setup.sh")

  vars = {
    AWS_REGION     = var.AWS_REGION
    AWS_ACCOUNT_ID = var.AWS_ACCOUNT_ID
    IMAGE_NAME     = var.APP_IMAGE_NAME
    IMAGE_TAG      = var.APP_IMAGE_TAG
    VAULT_HOST      = ""
    DB_NAME      = ""
  }
}

data "template_cloudinit_config" "ec2_init" {
  gzip          = false
  base64_encode = false

  part {
    content      = data.template_file.ec2_setup_script.rendered
    content_type = "text/x-shellscript"
  }
}

resource "aws_instance" "ec2_instance" {
  ami                  = "ami-06e0ce9d3339cb039"
  instance_type        = "t2.micro"
  subnet_id            = var.SUBNET_ID
  security_groups      = [aws_security_group.security_group.id]
  iam_instance_profile = aws_iam_instance_profile.ec2-role-instance-profile.id

  tags = {
    Name        = var.APP_NAME
    Environment = var.ENV
  }

  user_data                   = data.template_cloudinit_config.ec2_init.rendered
  associate_public_ip_address = true
  key_name                    = var.ACCESS_KEY_NAME
}

resource "aws_security_group_rule" "sg_rule_allow_ssh" {
  count             = var.ENABLE_SSH ? 1 : 0
  security_group_id = aws_security_group.security_group.id
  from_port         = 22
  protocol          = "tcp"
  to_port           = 22
  type              = "ingress"
  cidr_blocks       = ["0.0.0.0/0"]
}