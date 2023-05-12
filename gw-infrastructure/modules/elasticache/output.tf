output "elasticache_cluster_cache_nodes" {
  value = aws_elasticache_cluster.redis-cluster.cache_nodes[0].address
}

output "elasticache_cluster_configuration_endpoint" {
  value = aws_elasticache_cluster.redis-cluster.configuration_endpoint
}