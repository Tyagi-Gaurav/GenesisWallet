resource "null_resource" "ssh_key" {
  provisioner "local-exec" {
    command = "rm -rf /tmp/ssh_key*"
  }

  provisioner "local-exec" {
    command = "ssh-keygen -t ed25519 -q -N '' -f /tmp/ssh_key"
  }
}

data "local_file" "ssh_pub_key_file" {
  filename = "/tmp/ssh_key.pub"
  depends_on = [null_resource.ssh_key]
}

#data "template_file" "ssh_pub_key_file" {
#  depends_on = [null_resource.ssh_key]
#  template   = file("/tmp/ssh_key.pub")
#}

resource "aws_key_pair" "instance_access" {
  key_name   = "instance_access-key"
  public_key = data.local_file.ssh_pub_key_file.content
}
