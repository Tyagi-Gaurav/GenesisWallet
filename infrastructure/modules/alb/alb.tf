#ECS ALB
resource "aws_alb" "alb" {
  name            = var.ALB_NAME
  internal        = var.INTERNAL
  security_groups = [aws_security_group.alb_security_group.id]
  subnets         = split(",", var.VPC_SUBNETS)
  enable_deletion_protection = var.DELETE_PROTECTION

  tags = {
    Environment = var.ENV
  }
}

#ALB Listener (HTTP)
resource "aws_alb_listener" "alb_http" {
  for_each = var.TARGET_APPS
  load_balancer_arn = aws_alb.alb.arn
  port              = each.value["PORT"]
  protocol          = "HTTP"

  default_action {
    target_group_arn = each.value["TARGET_GROUP_ARN"]
    type             = "forward"
  }

  tags = {
    Environment = var.ENV
  }
}
