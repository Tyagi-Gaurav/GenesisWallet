#!/usr/bin/env sh

set -a
docker-compose down --rmi all
./gradlew clean build

docker-compose down --rmi all
./gradlew clean build

docker-compose --env-file ~/.secret/env.file up -d --build --remove-orphans
WAIT_TIME=15
echo "Waiting for ${WAIT_TIME} seconds for container to come up"
sleep ${WAIT_TIME}

./gradlew localFunctional