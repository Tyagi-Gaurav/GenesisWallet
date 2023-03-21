#!/bin/bash

sudo yum update -y
sudo yum install -y jq yum-utils curl docker
sudo systemctl start docker
sudo curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
sudo unzip awscliv2.zip
sudo ./aws/install

#export VAULT_HOST=${VAULT_HOST} #module.vault_instance.vault_ec2_private_ip
#export DB_NAME=${DB_NAME} #module.single_db_instance.dbname

PWD=$(aws ecr get-login-password --region "${AWS_REGION}")
sudo docker login -u AWS -p "$PWD" "${AWS_ACCOUNT_ID}".dkr.ecr."${AWS_REGION}".amazonaws.com
sudo docker run -p 19090:19090 -p 9090:9090 -p 9091:9091 "${IMAGE_NAME}":"${IMAGE_TAG}"
