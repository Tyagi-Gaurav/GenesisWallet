output "cluster_arn" {
  value = aws_ecs_cluster.ecs_cluster.id
}

output "service_role_arn" {
  value = aws_iam_role.cluster-service-role.arn
}

output "cluster_sg_id" {
  value = aws_security_group.cluster_sg.id
}
