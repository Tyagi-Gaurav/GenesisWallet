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

resource "aws_security_group_rule" "accessible_ports_sg" {
  for_each = var.ACCESSIBLE_PORTS
  security_group_id = aws_security_group.alb_security_group.id
  from_port         = each.value["FROM_PORT"]
  protocol          = "tcp"
  to_port           = each.value["TO_PORT"]
  type              = "ingress"
  cidr_blocks = ["0.0.0.0/0"]
}