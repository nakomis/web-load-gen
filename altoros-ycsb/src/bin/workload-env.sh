#!/bin/bash
#M2_HOME=/usr/local/apache-maven
#JAVA_HOME=/usr/lib/jvm/jdk1.6.0_31
MAVEN_OPTS="-Xms2048m -Xmx4096m -XX:MaxDirectMemorySize=2048m -XX:+UseConcMarkSweepGC -XX:MaxGCPauseMillis=850"

#export M2_HOME="$M2_HOME"
#export JAVA_HOME="$JAVA_HOME"
export MAVEN_OPTS="$MAVEN_OPTS"