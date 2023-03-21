data "template_file" "vault_setup_script" {
  template = file("${path.module}/vault_setup.sh")

  vars = {
    DB_HOST     = var.DB_HOST
    DB_PORT     = var.DB_PORT
    DB_USER     = var.DB_USER
    DB_PASSWORD = var.DB_PASSWORD
  }
}

data "template_cloudinit_config" "vault_init" {
  gzip          = false
  base64_encode = false

  part {
    content      = data.template_file.vault_setup_script.rendered
    content_type = "text/x-shellscript"
  }
}

#Create EC2 instance where we install Vault
resource "aws_instance" "vault" {
  ami             = "ami-06e0ce9d3339cb039"
  instance_type   = "t2.micro"
  subnet_id       = var.SUBNET_ID
  security_groups = [aws_security_group.vault_security_group.id]

  tags = {
    Name        = "Vault"
    Environment = var.ENV
  }

  user_data                   = data.template_cloudinit_config.vault_init.rendered
  associate_public_ip_address = true #For debugging only
  key_name                    = var.ACCESS_KEY_NAME #Should be allowed for dev only
}

resource "aws_security_group" "vault_security_group" {
  vpc_id = var.VPC_ID
  name   = "vault-security-group"
}

resource "aws_security_group_rule" "ingress_rule" {
  for_each                 = var.ALLOWED_SECURITY_GROUPS
  from_port                = 8200
  protocol                 = "tcp"
  security_group_id        = aws_security_group.vault_security_group.id
  to_port                  = 8200
  type                     = "ingress"
  source_security_group_id = each.value
}

resource "aws_security_group_rule" "egress_rule" {
  from_port         = 0
  protocol          = "-1"
  security_group_id = aws_security_group.vault_security_group.id
  to_port           = 0
  type              = "egress"
  cidr_blocks       = ["0.0.0.0/0"]
}