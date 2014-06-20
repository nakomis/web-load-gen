#!/bin/bash
BIN_HOME=./../../bin

. $BIN_HOME/utils.sh
. ./workload-env.sh

workloads=("profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=100 operationcount=10000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=100 operationcount=10000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=100 operationcount=10000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=100 operationcount=10000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=100 operationcount=10000"

           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=300 operationcount=30000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=300 operationcount=30000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=300 operationcount=30000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=300 operationcount=30000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=300 operationcount=30000"

           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=500 operationcount=50000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=500 operationcount=50000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=500 operationcount=50000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=500 operationcount=50000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=500 operationcount=50000"

           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=1000 operationcount=100000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=1000 operationcount=100000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=1000 operationcount=100000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=1000 operationcount=100000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=1000 operationcount=100000"

           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=1500 operationcount=100000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=1500 operationcount=100000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=1500 operationcount=100000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=1500 operationcount=100000"
           "profile=mysql-sharded-${MYSQL_SHARDED_VERSION}-b.workload target=1500 operationcount=100000")

workloads=$(array_join "${workloads[@]}")

$BIN_HOME/run-workload.sh -w "$workloads" -i 1