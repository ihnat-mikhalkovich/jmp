#!/bin/bash
echo "sleeping for 2 seconds"
sleep 2

echo mongo_setup.sh time now: `date +"%T" `

mongoimport --port 27017 --db main --collection tasks \
       --drop --file /scripts/data.json --jsonArray

mongo --port 27017 <<EOF
  use main
  db.tasks.createIndex({
    "description": "text",
    "$**": "text"
  })
EOF