# Cloudwatch logs
resource "aws_cloudwatch_log_group" "cluster" {
  name = var.LOG_GROUP
  retention_in_days = 1

  tags = {
    Environment = var.ENV
  }
}
