#!/bin/bash

export MALLOC_ARENA_MAX=2

set -e

cmd="$1"

until $cmd; do
  >&2 echo "Dependency is unavailable - sleeping"
  sleep 10
done

>&2 echo "Dependency is up - executing command $cmd"

MEM_FLAGS="-Xms512m -Xmx1024m"

java $MEM_FLAGS -jar /data/application.jar
