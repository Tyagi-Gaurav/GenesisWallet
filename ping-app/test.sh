#!/bin/sh

echo off

docker build . -t ping-app:latest

docker stop `docker ps -q`

docker run -p 6060:6060 ping-app:latest & 

sleep 10

curl -I http://localhost:6060/api/ping

docker stop `docker ps -q`
