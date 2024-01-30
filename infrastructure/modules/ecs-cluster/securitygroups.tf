resource "aws_security_group" "cluster_sg" {
  name        = var.CLUSTER_NAME
  vpc_id      = var.VPC_ID
  description = var.CLUSTER_NAME

  tags = {
    Environment = var.ENV
  }
}

resource "aws_security_group_rule" "cluster_sg_rule_allow_ssh" {
  count             = var.ENABLE_SSH ? 1 : 0
  security_group_id = aws_security_group.cluster_sg.id
  from_port         = 22
  protocol          = "tcp"
  to_port           = 22
  type              = "ingress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "cluster_sg_egress" {
  from_port         = 0
  protocol          = "-1"
  security_group_id = aws_security_group.cluster_sg.id
  to_port           = 0
  type              = "egress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "accessible_ports_sg" {
  for_each = var.ACCESSIBLE_PORTS
  security_group_id = aws_security_group.cluster_sg.id
  from_port         = each.value["FROM_PORT"]
  protocol          = "tcp"
  to_port           = each.value["TO_PORT"]
  type              = "ingress"
  source_security_group_id = each.value["SECURITY_GROUP"]
}