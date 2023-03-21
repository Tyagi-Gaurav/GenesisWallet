resource "aws_alb_target_group" "alb_ecs_target" {
  name = "${var.APPLICATION_NAME}-${substr(md5(format("%s%s%s", var.APPLICATION_PORT, var.DEREGISTRATION_DELAY, var.HEALTHCHECK_MATCHER)), 0, 12)}"
  port = var.APPLICATION_PORT
  protocol = "HTTP"
  vpc_id = var.VPC_ID
  deregistration_delay = var.DEREGISTRATION_DELAY

  health_check {
    healthy_threshold = 3
    unhealthy_threshold = 3
    protocol = "HTTP"
    path = var.HEALTH_CHECK_PATH
    port = var.HEALTH_CHECK_PORT
    interval = 30
    matcher = var.HEALTHCHECK_MATCHER
  }
}