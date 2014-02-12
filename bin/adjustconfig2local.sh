#!/bin/bash

# ROOT of build directory
BUILD_DIR=${HOME}/inherit-platform-gitclone/eservices-platform

echo -n "pushd: " ;  pushd $BUILD_DIR
echo
echo

for i in bin/deploy-inherit-platform.sh \
    inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config/properties-local.xml \
    inherit-portal/site/src/main/webapp/WEB-INF/hst-config.properties \
    inherit-portal/site/src/main/webapp/WEB-INF/web.xml
do
    echo -n "Restoring local ${i}.safe to $i"
    cp ${i}.safe $i
    echo ""
done
echo
echo
popd
