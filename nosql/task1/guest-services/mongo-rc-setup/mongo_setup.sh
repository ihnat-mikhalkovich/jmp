#!/bin/bash
echo "sleeping for 2 seconds"
sleep 2

echo mongo_setup.sh time now: `date +"%T" `

mongod --bind_ip_all --replSet rs0 --port 9042

mongo --port 9042 <<EOF
  rs.initiate( {
   _id : "rs0",
   members: [
      { _id: 0, host: "mongo1:9042" },
      { _id: 1, host: "mongo2:9142" },
      { _id: 2, host: "mongo3:9242" }
   ]
  })
EOF