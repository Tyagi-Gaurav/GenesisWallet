output "ssh_security_group_id" {
  value = aws_security_group.allow_ssh.id
}

output "ssh_key_pair_name" {
  value = aws_key_pair.instance_access.key_name
}