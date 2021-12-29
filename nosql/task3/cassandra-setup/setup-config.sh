#!/bin/bash
set -e  #fail fast

CASSANDRA_VERSION=`docker-compose config | grep 'image:.*cassandra:' | head -1 | awk -F":" '{ print $NF}'`
docker image pull cassandra:latest

docker run --rm -d --name tmp cassandra:latest
docker cp tmp:/etc/cassandra/ etc_cassandra_latest_vanilla/
docker stop tmp

etc_volumes=`docker-compose config | grep '/etc/cassandra' | awk -F ":" '{ print $1}' | awk '{ print $NF}'`
for v in ${etc_volumes}; do
   mkdir -p ${v}
   cp -r etc_cassandra_latest_vanilla/*.* ${v}/
done
