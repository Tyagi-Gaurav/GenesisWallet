#RDS
resource "aws_db_subnet_group" "database" {
  name = "postgres-db-subnet"
  subnet_ids = var.SUBNET_IDS

  tags = {
    Environment = var.ENV
  }
}

resource "aws_db_instance" "postgresql" {
  allocated_storage    = var.ALLOCATED_STORAGE
  instance_class       = var.DB_INSTANCE_CLASS #"db.t3.micro"
  engine               = "postgres"
  engine_version       = "15.2"
  db_name              = var.DB_NAME
  username             = var.USERNAME
  password             = var.PASSWORD
  parameter_group_name = "default.postgres15"
  db_subnet_group_name = aws_db_subnet_group.database.name
  skip_final_snapshot = true
  vpc_security_group_ids = [aws_security_group.postgres_sec_group.id]

  tags = {
    Environment = var.ENV
  }
}


resource "aws_security_group" postgres_sec_group {
  vpc_id = var.VPC_ID

  name = "postgres security group"

  ingress {
    from_port = 5432
    protocol  = "tcp"
    to_port   = 5432
    security_groups = var.ALLOWED_SECURITY_GROUP_IDS
  }
}
