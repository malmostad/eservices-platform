#!/bin/bash

# ROOT of build directory
BUILD_DIR=${HOME}/inherit-platform-gitclone/eservices-platform

#while true; do
#done

pushd $BUILD_DIR
for i in bin/deploy-inherit-platform.sh inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config/properties-local.xml inherit-portal/site/src/main/webapp/WEB-INF/hst-config.properties inherit-portal/site/src/main/webapp/WEB-INF/web.xml ; do

    echo  ""
    echo -n  $i ; read -p ":   Do you wish to discard local changes of this file(i.e. git checkout)... "  yn
    echo  ""

    case $yn in
	[Yy]* ) echo "git checkout " $i ; git checkout $i ;;
	[Nn]* ) echo "do nothing with " $i ;;
	* ) echo "Please answer yes or no.";;
    esac

    echo  ""
done

#    git checkout bin/deploy-inherit-platform.sh
#    git checkout inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config/properties-local.xml
#    git checkout inherit-portal/site/src/main/webapp/WEB-INF/hst-config.properties
#    git checkout inherit-portal/site/src/main/webapp/WEB-INF/web.xml
popd
