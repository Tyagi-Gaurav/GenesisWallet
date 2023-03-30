#!/bin/sh

export MALLOC_ARENA_MAX=2

set -e

MEM_FLAGS="-Xms512m -Xmx1024m"
GCFLAGS="-XX:+PrintGCDetails"

java $MEM_FLAGS -jar /data/ui.jar
