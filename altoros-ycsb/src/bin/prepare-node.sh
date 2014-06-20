#!/bin/sh
# Open /etc/sudoers and comment out #Default requiretty
sudo chmod 666 /etc/hosts
sudo cat <<EOF> /etc/hosts
127.0.0.1       localhost localhost.localdomain
10.66.202.255   ycsb-client
10.190.170.123  ycsb-client1
10.68.178.148   ycsb-client2
10.188.118.125  ycsb-node-router
10.191.74.245   ycsb-node1
10.6.151.96     ycsb-node2
10.68.90.152	ycsb-node3
10.140.14.155   ycsb-node4
EOF

sudo chmod 666 /etc/security/limits.conf
sudo cat <<EOF> /etc/security/limits.conf
ec2-user         soft   nproc          8192
ec2-user         soft   nofile         65536
ec2-user         hard   nproc          8192
ec2-user         hard   nofile         65536
EOF

cat <<EOF> ~/mongo-bin/mongo-env.sh
MONGO_HOME="/usr/lib/mongodb-2.2.0-rc0/"
MONDO_DB_DIR="/var/lib/mongodb"
MONGO_LOG_DIR="/var/log/mongodb"
MONGO_ROUTER="ycsb-node1:27020"
MONGO_CONFIGDB="ycsb-node1:27019"
EOF

cat <<EOF> ./run-competition-load-warmup-access.sh
./run-competition-load.sh
./run-competition-warmup.sh
./run-competition-access.sh
EOF
