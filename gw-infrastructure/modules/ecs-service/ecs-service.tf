#task definition template
data "template_file" "task_def_template" {
  template = file("${path.module}/ecs-service.json")
  vars     = {
    APPLICATION_NAME    = var.APPLICATION_NAME
    ENV_NAME            = var.ENV
    APPLICATION_VERSION = var.APPLICATION_VERSION
    ECR_REPO_ARN        = var.ECR_REPO_URL
    AWS_REGION          = var.AWS_REGION
    CPU_RESERVATION     = var.CPU_RESERVATION
    MEMORY_RESERVATION  = var.MEMORY_RESERVATION
    LOG_GROUP           = var.LOG_GROUP
    APP_ENV_VAR         = join(",", [for key, value in var.APP_ENV : "{ \"name\" : \"${key}\", \"value\" : \"${value}\" }"])
    PORT_MAPPINGS       = join(",", [for key, value in var.PORT_MAPPINGS : "{ \"containerPort\" : ${value["APPLICATION_PORT"]}, \"hostPort\" : ${value["HOST_PORT"]}, \"protocol\" : \"http\" }"])
  }
}

#ECS Task definition
resource "aws_ecs_task_definition" "ecs_service_taskdef" {
  container_definitions = data.template_file.task_def_template.rendered
  family                = var.APPLICATION_NAME
  task_role_arn         = var.TASK_ROLE_ARN

  tags = {
    Environment = var.ENV
  }
}

#ECS Service
resource "aws_ecs_service" "ecs-service" {
  name                               = var.APPLICATION_NAME
  cluster                            = var.CLUSTER_ARN
  task_definition                    = "${aws_ecs_task_definition.ecs_service_taskdef.family}:${aws_ecs_task_definition.ecs_service_taskdef.revision}"
  iam_role                           = var.SERVICE_ROLE_ARN
  desired_count                      = var.DESIRED_COUNT
  deployment_minimum_healthy_percent = var.DEPLOYMENT_MIN_HEALTHY_PERCENTAGE
  deployment_maximum_percent         = var.DEPLOYMENT_MAX_PERCENTAGE

  load_balancer {
    container_name   = var.APPLICATION_NAME
    container_port   = var.APPLICATION_PORT
    target_group_arn = aws_alb_target_group.alb_ecs_target.id
  }

  tags = {
    Environment = var.ENV
  }
}