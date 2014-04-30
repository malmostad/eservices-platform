#!/bin/bash

# CONFIG #######################################################
# Select one config to deploy below                            #
################################################################

###### current_config.sh  #####
# symlink to actual config of current installation
. current_config.sh

################################################################
# END OF CONFIG                                                #
################################################################

ERRORSTATUS=0

# 1. Sanity check of supplied parameters

echo "BUILD_DIR: $BUILD_DIR"
echo "CONTAINER_ROOT: $CONTAINER_ROOT"
echo "CONTENT_ROOT: $CONTENT_ROOT"
#echo "CONTENT_ROOT_WORKAROUND: $CONTENT_ROOT_WORKAROUND"

if [ ! -d "${BUILD_DIR}" ] || [ ! -d "${CONTAINER_ROOT}" ] || [ ! -d "${CONTENT_ROOT}" ]
then
    echo "Either of $BUILD_DIR, $CONTAINER_ROOT or $CONTENT_ROOT do not exist, aborting execution of $0"
    ERRORSTATUS=1
    exit $ERRORSTATUS
fi

echo "CONTAINER_ROOT/ESERVICE: $CONTAINER_ROOT/$ESERVICE"
echo "CONTAINER_ROOT/KSERVICE: $CONTAINER_ROOT/$KSERVICE"

if [ ! -d ${CONTAINER_ROOT}/${ESERVICE} ] || [ ! -d ${CONTAINER_ROOT}/${KSERVICE} ]
then
    echo "Either of ${CONTAINER_ROOT}/${ESERVICE} ${CONTAINER_ROOT}/${KSERVICE} do not exist, aborting execution of $0"
    ERRORSTATUS=1
    exit $ERRORSTATUS
fi

echo "ESERVICE_PORT: $ESERVICE_PORT"
echo "KSERVICE_PORT: $KSERVICE_PORT"

if [ -z "${ESERVICE_PORT}" ] || [ -z "${KSERVICE_PORT}" ]
then
    echo "Either of parameters ESERVICE_PORT or KSERVICE_PORT unset, aborting execution of $0"
    ERRORSTATUS=1
    exit $ERRORSTATUS
fi

echo "ESERVICEPATCH: $ESERVICEPATCH"
echo "KSERVICEPATCH: $KSERVICEPATCH"

if [ -z "${ESERVICEPATCH}" ] || [ -z "${KSERVICEPATCH}" ]
then
    echo "Either of parameters ESERVICEPATCH or KSERVICEPATCH unset, aborting execution of $0"
    ERRORSTATUS=1
    exit $ERRORSTATUS
fi

# 2. Patching properties-local.xml for eservicetest and kservicetest
if [ -d ${BUILD_DIR}/inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config ]
then
    pushd ${BUILD_DIR}/inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config
       mv properties-local.xml $PROPERTIES_LOCAL_BEFOREPATCH
       cp $PROPERTIES_LOCAL_BEFOREPATCH properties-local.xml # thereby conserving mod date of properties-local.xml
                                                             # when $PROPERTIES_LOCAL_BEFOREPATCH is renamed
                                                             # back to properties-local.xml in step 8
if ${ESERVICE_SSL}; then
   sed -e "s/https:\/\/localhost:8080\/site\/mycases\/form\/confirmdispatcher/http:\/\/${ESERVICE_HOST}:${ESERVICE_PORT}\/site\/mycases\/form\/confirmdispatcher/g" properties-local.xml > properties-local.xml.eservicepatch
else
    sed -e "s/http:\/\/localhost:8080\/site\/mycases\/form\/confirmdispatcher/http:\/\/${ESERVICE_HOST}:${ESERVICE_PORT}\/site\/mycases\/form\/confirmdispatcher/g" properties-local.xml > properties-local.xml.eservicepatch
fi

if ${KSERVICE_SSL}; then
   sed -e "s/https:\/\/localhost:8080\/site\/mycases\/form\/confirmdispatcher/http:\/\/${KSERVICE_HOST}:${KSERVICE_PORT}\/site\/mycases\/form\/confirmdispatcher/g" properties-local.xml > properties-local.xml.kservicepatch
else
    sed -e "s/http:\/\/localhost:8080\/site\/mycases\/form\/confirmdispatcher/http:\/\/${KSERVICE_HOST}:${KSERVICE_PORT}\/site\/mycases\/form\/confirmdispatcher/g" properties-local.xml > properties-local.xml.kservicepatch
fi

       mv properties-local.xml.eservicepatch properties-local.xml
    popd
else
    echo "${BUILD_DIR}/inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config does not exist. Aborting execution"
    ERRORSTATUS=1
    exit $ERRORSTATUS
fi

# 3. Build eservice-platform
pushd ${BUILD_DIR}
if mvn -DskipTests=true clean install
then
    echo "Executing mvn -DskipTests=true clean install - patched for eservicetest..."
else
    echo "Compilation failed. Aborting execution"
    ERRORSTATUS=$?
    exit $ERRORSTATUS
fi

# 4. Build eservice-platform distribution snapshot
cd inherit-portal
if mvn -P dist
then
    echo "Creating eservicetest snapshot distribution tar.gz..."
    mv target/inherit-portal-1.01.00-SNAPSHOT-distribution.tar.gz target/inherit-portal-1.01.00-SNAPSHOT-distribution-eservices.tar.gz
else
    echo "Building of snapshot distribution failed. Aborting execution"
    ERRORSTATUS=$?
    exit $ERRORSTATUS
fi
popd

if ${WITH_KSERVICES}
then
# 5. Preparing properties-local.xml for kservicetest (patching done in step #2)
    pushd ${BUILD_DIR}/inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config
       mv properties-local.xml.kservicepatch properties-local.xml
    popd

# 6. Build kservice-platform
    pushd ${BUILD_DIR}/inherit-portal
    if mvn -DskipTests=true install      # NB no clean here because
                                         # inherit-portal-1.01.00-SNAPSHOT-distribution-eservices.tar.gz
                                         # will otherwise be removed, but is used at a later stage...
    then
	echo "Executing mvn -DskipTests=true install - patched for kservicetest..."
    else
	echo "Compilation failed. Aborting execution"
	ERRORSTATUS=$?
	exit $ERRORSTATUS
    fi

# 7. Build kservice-platform distribution snapshot
    if mvn -P dist
    then
	echo "Creating eservicetest snapshot distribution tar.gz..."
        mv target/inherit-portal-1.01.00-SNAPSHOT-distribution.tar.gz target/inherit-portal-1.01.00-SNAPSHOT-distribution-kservices.tar.gz
    else
	echo "Building of snapshot distribution failed. Aborting execution"
	ERRORSTATUS=$?
	exit $ERRORSTATUS
    fi
    popd
fi

# 8. Restore original properties-local.xml to original state. Necessary to make step 2 (patching properties-local.xml)
#     work correctly next time script is run
    pushd ${BUILD_DIR}/inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config
       mv $PROPERTIES_LOCAL_BEFOREPATCH properties-local.xml
    popd

# 9. Stop j2ee containers
    pushd ${CONTAINER_ROOT}

cd ${ESERVICE}/bin/

ESERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${ESERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
if [ "${ESERVICE_PID}" ] 
then 
    echo "Shutting down eservice, pid: " ${ESERVICE_PID}
    ./shutdown.sh
    sleep 1
    LOOPVAR=0
    while ps -p ${ESERVICE_PID} && [ ${LOOPVAR} -lt 6  ]
    do
	LOOPVAR=$(expr ${LOOPVAR} + 1)
	sleep 1
    done

    # If proper shutdown did not bite
    if ps -p ${ESERVICE_PID}
    then 
	echo "Force shutting down eservice, pid: " ${ESERVICE_PID}
	kill  ${ESERVICE_PID}
	sleep 6
    fi

    # If still did not bite
    if ps -p ${ESERVICE_PID}
    then 
	echo "Failed to shut down eservice, pid: " ${ESERVICE_PID}
	ERRORSTATUS=1
    fi
fi

if ${WITH_KSERVICES}
then
    cd ../../${KSERVICE}/bin/
    KSERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${KSERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
    if [ "${KSERVICE_PID}" ] 
    then 
	echo "Shutting down kservice, pid: " ${KSERVICE_PID}
	./shutdown.sh
	sleep 1
	LOOPVAR=0
	while ps -p ${KSERVICE_PID} &&  [ ${LOOPVAR} -lt 6  ]
	do
  	    LOOPVAR=$(expr ${LOOPVAR} + 1)
  	    sleep 1
	done
    # If proper shutdown did not bite
	if ps -p ${KSERVICE_PID}
	then 
	    echo "Force shutting down kservice, pid: " ${KSERVICE_PID}
	    kill  ${KSERVICE_PID}
	    sleep 6
	fi

   # If still did not bite
	if ps -p ${KSERVICE_PID}
	then 
	    echo "Failed to shut down kservice, pid: " ${KSERVICE_PID}
	    ERRORSTATUS=1
	fi
    fi
fi

popd

if [ ${ERRORSTATUS} -eq 1 ]
then
    echo "Failed to shutdown all containers, aborting execution of script"
    exit ${ERRORSTATUS}
fi

# 10. Install on eservice container
echo "Installing on eservice container"
if [ -d  ${CONTAINER_ROOT}/${ESERVICE} ]
then
    pushd ${CONTAINER_ROOT}/${ESERVICE}
    tar xzfv ${BUILD_DIR}/inherit-portal/target/inherit-portal-1.01.00-SNAPSHOT-distribution-eservices.tar.gz
    cd webapps
    rm -fr cms site orbeon exist docbox coordinatrice
    rm cms.war coordinatrice.war # deploy cms and coordinatrice only at kservice and share the same JCR
    popd
else
    echo "Directory ${CONTAINER_ROOT}/${ESERVICE} does not exist. Halting."
    exit 1
fi

# 11. Install on kservice container
if ${WITH_KSERVICES}
then
    if [ -d ${CONTAINER_ROOT}/${KSERVICE} ]
    then
	echo "Installing on kservice container"
	pushd ${CONTAINER_ROOT}/${KSERVICE}
	tar xzfv ${BUILD_DIR}/inherit-portal/target/inherit-portal-1.01.00-SNAPSHOT-distribution-kservices.tar.gz
	cd webapps
	rm -fr cms site orbeon exist docbox coordinatrice
	popd
    else
	echo "Directory ${CONTAINER_ROOT}/${KSERVICE} does not exist. Halting."
	exit 1
    fi
fi

# 13. Clean up content repositories
echo "Clean up content repository..."
pushd ${CONTENT_ROOT}
rm -fr repository version workspaces 
popd

#pushd ${CONTENT_ROOT_WORKAROUND}
#rm -fr repository version workspaces 
#popd

# 14. Restart containers
pushd ${CONTAINER_ROOT}

echo "Restart eservice container..."
cd ${ESERVICE}/bin/
./startup.sh 
LOOPVAR=0
ESERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${ESERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
while [ -z "${ESERVICE_PID}" -a  ${LOOPVAR} -lt 30  ]
do
    LOOPVAR=$(expr ${LOOPVAR} + 1)
    sleep 1
    ESERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${ESERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
done

if [ -z "${ESERVICE_PID}" ]
then
    echo "Error: could not start Eservicetest"
    ERRORSTATUS=1
fi
cd ../..

if ${WITH_KSERVICES}
then
    echo "Restart kservice container..."
    cd ${KSERVICE}/bin/
    ./startup.sh 
    LOOPVAR=0
    KSERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${KSERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
    while [ -z "${KSERVICE_PID}" -a  ${LOOPVAR} -lt 30  ]
    do
	LOOPVAR=$(expr ${LOOPVAR} + 1)
	sleep 1
	KSERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${KSERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
    done

    if [ -z "${KSERVICE_PID}" ]
    then
	echo "Error: could not start Kservicetest"
	ERRORSTATUS=1
    fi
fi
popd

exit ${ERRORSTATUS}
