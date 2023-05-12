resource "aws_elasticache_subnet_group" "elasticache-subnet" {
  name       = "my-redis-subnet"
  subnet_ids = var.SUBNET_IDS

  tags = {
    Environment = var.ENV
  }
}

resource "aws_elasticache_cluster" "redis-cluster" {
  cluster_id           = "redis-cluster"
  engine               = "redis"
  node_type            = "cache.m4.large"
  num_cache_nodes      = 1
  engine_version       = "6.2"
  parameter_group_name = "default.redis6.x"
  port                 = 6379
  subnet_group_name = aws_elasticache_subnet_group.elasticache-subnet.name
  security_group_ids = [aws_security_group.elasticache_sec_group.id]

  tags = {
    Environment = var.ENV
  }
}

resource "aws_security_group" elasticache_sec_group {
  vpc_id = var.VPC_ID

  name = "elasticache security group"

  ingress {
    from_port = 6379
    to_port   = 6379
    protocol  = "tcp"
    security_groups = var.ALLOWED_SECURITY_GROUP_IDS
  }

  egress {
    from_port = 0
    protocol  = "-1"
    to_port   = 0
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Environment = var.ENV
  }
}
