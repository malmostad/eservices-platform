#!/bin/bash
mvn clean install

pushd inherit-portal
  mvn -P dist
popd

pushd /opt/inherit-platform/BOS-5.9-Tomcat-6.0.35/bin
  ./shutdown.sh
  sleep 5
popd

rm  -fr /opt/inherit-platform/BOS-5.9-Tomcat-6.0.35/webapps/inherit-service-rest-server-1.0-SNAPSHOT
cp inherit-service/inherit-service-rest-server/target/inherit-service-rest-server-1.0-SNAPSHOT.war \
   /opt/inherit-platform/BOS-5.9-Tomcat-6.0.35/webapps/

pushd /opt/inherit-platform/BOS-5.9-Tomcat-6.0.35/bin
  ./startup.sh
  sleep 5
popd

pushd inherit-portal
  mvn -P cargo.run
popd


