#output "vault_ec2_public_ip" {
#  value = aws_instance.vault.public_ip
#}

output "vault_ec2_private_ip" {
  value = aws_instance.vault.private_ip
}