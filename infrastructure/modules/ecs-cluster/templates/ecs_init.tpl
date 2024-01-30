#!/bin/bash

sudo mkdir -p /etc/ecs
sudo /bin/su -c "echo ECS_CLUSTER=${CLUSTER_NAME} >> /etc/ecs/ecs.config"
start ecs