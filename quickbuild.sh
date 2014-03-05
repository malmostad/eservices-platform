#!/bin/bash
pushd inherit-service/
mvn -Dmaven.test.skip=true clean install
popd

cd inherit-portal/
mvn -Dmaven.test.skip=true clean install
mvn -P cargo.run
