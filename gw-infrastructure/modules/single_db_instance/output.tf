output "username" {
  value = aws_db_instance.postgresql.username
}

output "password" {
  value = aws_db_instance.postgresql.password
}

output "host" {
  value = aws_db_instance.postgresql.address
}

output "port" {
  value = aws_db_instance.postgresql.port
}

output "dbname" {
  value = aws_db_instance.postgresql.db_name
}