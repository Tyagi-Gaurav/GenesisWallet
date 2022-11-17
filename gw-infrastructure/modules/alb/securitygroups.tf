resource "aws_security_group" "alb_security_group" {
  name        = var.ALB_NAME
  vpc_id      = var.VPC_ID
  description = var.ALB_NAME

  ingress {
    from_port = 80
    to_port   = 80
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 443
    to_port   = 443
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

resource "aws_security_group_rule" "cluster-allow-alb" {
  from_port         = 32768
  protocol          = "tcp"
  security_group_id = var.ECS_SG
  to_port           = 61000
  type              = "ingress"
  source_security_group_id = aws_security_group.alb_security_group.id
}