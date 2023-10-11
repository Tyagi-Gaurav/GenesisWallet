#!/bin/sh

IMAGE_TYPE=$1

echo "Creating image of type : ${IMAGE_TYPE}"

rm -rf ./tmp
mkdir ./tmp

cp -R gw-grpc-schema/src/main/proto ./tmp
cp baseImages/${IMAGE_TYPE}BaseImageDockerfile ./tmp/Dockerfile

NEW_TAG=$(git describe --tags --abbrev=0 | awk -F. -v OFS=. 'NF==1{print ++$NF}; NF>1{if(length($NF+1)>length($NF))$(NF-1)++; $NF=sprintf("%0*d", length($NF), ($NF+1)%(10^length($NF))); print}')

echo "Creating images with tag: $NEW_TAG"

docker build -f ./tmp/Dockerfile tmp -t chonku/${IMAGE_TYPE}:${NEW_TAG} -t chonku/${IMAGE_TYPE}:latest

docker push chonku/${IMAGE_TYPE}:${NEW_TAG}