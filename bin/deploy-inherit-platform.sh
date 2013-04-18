#!/bin/sh

# ROOT of build directory
BUILD_DIR=${HOME}/inherit-platform-gitclone/inherit-platform

# ROOT of directory holding the j2ee containers
CONTAINER_ROOT=${HOME}/inherit-platform
CONTENT_ROOT=${CONTAINER_ROOT}/jcr-inherit-portal
CONTENT_ROOT_WORKAROUND=${CONTAINER_ROOT}/jcr-inherit-portal-extra-workaround-kservice

# Name of container roots
EXIST=orbeon-tomcat-6.0.36
BOS=BOS-5.9-Tomcat-6.0.35
ESERVICE=hippo-eservice-tomcat-6.0.36
KSERVICE=hippo-kservice-tomcat-6.0.36

ESERVICEPATCH=eservicetest.malmo.se
KSERVICEPATCH=kservicetest.malmo.se

EXIST_PORT=48080
BOS_PORT=58080
ESERVICE_PORT=8080
KSERVICE_PORT=38080

WITH_KSERVICES=true

ERRORSTATUS=0

# 1. Stop servers (containers, not Apache for the moment) 
pushd ${CONTAINER_ROOT}
cd $EXIST/bin/
EXIST_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${EXIST_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
if [ "${EXIST_PID}" ] 
then 
    echo "Shutting down eXist, pid: " ${EXIST_PID}
    ./shutdown.sh
    sleep 1
    LOOPVAR=0
    while [ -n "${EXIST_PID}" -a  ${LOOPVAR} -lt 6  ]
    do
	LOOPVAR=$(expr ${LOOPVAR} + 1)
	sleep 1
	EXIST_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${EXIST_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
    done
fi

EXIST_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${EXIST_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
  # If proper shutdown did not bite
if [ "${EXIST_PID}" ] 
then 
    echo "Force shutting down eXist, pid: " ${EXIST_PID}
    kill  ${EXIST_PID}
    sleep 1
fi

EXIST_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${EXIST_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
  # If still did not bite
if [ "${EXIST_PID}" ] 
then 
    echo "Failed to shut down eXist, pid: " ${EXIST_PID}
    ERRORSTATUS=1
fi

cd ../../${BOS}/bin/
BOS_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${BOS_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
if [ "${BOS_PID}" ] 
then 
    echo "Shutting down BOS, pid: " ${BOS_PID}
    ./shutdown.sh
    sleep 1
    LOOPVAR=0
    while [ "${BOS_PID}" -a  ${LOOPVAR} -lt 6  ]
    do
	LOOPVAR=$(expr ${LOOPVAR} + 1)
	sleep 1
	BOS_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${BOS_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
    done
fi

BOS_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${BOS_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
  # If proper shutdown did not bite
if [ "${BOS_PID}" ] 
then 
    echo "Force shutting down BOS, pid: " ${BOS_PID}
    kill  ${BOS_PID}
    sleep 1
fi

BOS_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${BOS_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
  # If still did not bite
if [ "${BOS_PID}" ] 
then 
    echo "Failed to shut down BOS, pid: " ${BOS_PID}
    ERRORSTATUS=1
fi

cd ../../${ESERVICE}/bin/
ESERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${ESERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
if [ "${ESERVICE_PID}" ] 
then 
    echo "Shutting down eservice, pid: " ${ESERVICE_PID}
    ./shutdown.sh
    sleep 1
    LOOPVAR=0
    while [ "${ESERVICE_PID}" -a  ${LOOPVAR} -lt 6  ]
    do
	LOOPVAR=$(expr ${LOOPVAR} + 1)
	sleep 1
	ESERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${ESERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
    done
fi

  # If proper shutdown did not bite
ESERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${ESERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
if [ "${ESERVICE_PID}" ] 
then 
    echo "Force shutting down eservice, pid: " ${ESERVICE_PID}
    kill  ${ESERVICE_PID}
fi

  # If still did not bite
ESERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${ESERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
if [ "${ESERVICE_PID}" ] 
then 
    echo "Failed to shut down eservice, pid: " ${ESERVICE_PID}
    ERRORSTATUS=1
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
	while [ "${KSERVICE_PID}" -a  ${LOOPVAR} -lt 6  ]
	do
  	    LOOPVAR=$(expr ${LOOPVAR} + 1)
  	    sleep 1
  	    KSERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${KSERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
	done
    fi

    KSERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${KSERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
    # If proper shutdown did not bite
    if [ "${KSERVICE_PID}" ] 
    then 
	echo "Force shutting down kservice, pid: " ${KSERVICE_PID}
	kill  ${KSERVICE_PID}
	sleep 1
    fi

    KSERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${KSERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
   # If still did not bite
    if [ "${KSERVICE_PID}" ] 
    then 
	echo "Failed to shut down kservice, pid: " ${KSERVICE_PID}
	ERRORSTATUS=1
    fi
fi
popd

if [ ${ERRORSTATUS} -eq 1 ]
then
    exit ${ERRORSTATUS}
fi

# 2. Patching properties-local.xml for eservicetest
pushd ${BUILD_DIR}/inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config
sed s/eservices.malmo.se/${ESERVICEPATCH}/g properties-local.xml.ingit > properties-local.xml
popd

# 3. Build eservice-platform
pushd ${BUILD_DIR}
echo "Executing mvn clean install - patched for eservicetest..."
mvn clean install
cd inherit-portal
echo "Creating eservicetest snapshot distribution tar.gz..."
mvn -P dist
popd

# 4. Install on eservice container
echo "Installing on eservice container"
pushd ${CONTAINER_ROOT}/${ESERVICE}
tar xzfv ${BUILD_DIR}/inherit-portal/target/inherit-portal-1.01.00-SNAPSHOT-distribution.tar.gz
cd webapps
rm -fr cms site orbeon
popd

if ${WITH_KSERVICES}
then
# 5. Patching properties-local.xml for kservicetest
    pushd ${BUILD_DIR}/inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config
    sed s/eservices.malmo.se/${KSERVICEPATCH}/g properties-local.xml.ingit > properties-local.xml
    popd

# 6. Build kservice-platform
    pushd ${BUILD_DIR}
    echo "Executing mvn clean install - patched for kservicetest..."
    mvn clean install
    cd inherit-portal
    echo "Creating kservicetest snapshot distribution tar.gz..."
    mvn -P dist
    popd

# 7. Install on kservice container
    echo "Installing on kservice container"
    pushd ${CONTAINER_ROOT}/${KSERVICE}
    tar xzfv ${BUILD_DIR}/inherit-portal/target/inherit-portal-1.01.00-SNAPSHOT-distribution.tar.gz
    cd webapps
    rm -fr cms site orbeon
    popd
fi

# 8. Install TASKFORM engine on BOS container
echo "Installing taskform engine on BOS"
pushd ${CONTAINER_ROOT}/${BOS}/webapps
cp ${BUILD_DIR}/inherit-service/inherit-service-rest-server/target/inherit-service-rest-server-1.0-SNAPSHOT.war .
rm -rf inherit-service-rest-server-1.0-SNAPSHOT
popd

# 9. Clean up content repository
echo "Clean up content repository..."
pushd ${CONTENT_ROOT}
rm -fr repository version workspaces 
popd
pushd ${CONTENT_ROOT_WORKAROUND}
rm -fr repository version workspaces 
popd


# 10. Restart containers
pushd ${CONTAINER_ROOT}
echo "Restart eXist container..."
cd ${EXIST}/bin/
./startup.sh 
LOOPVAR=0
EXIST_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${EXIST_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
while [ -z "${EXIST_PID}" -a  ${LOOPVAR} -lt 20  ]
do
    LOOPVAR=$(expr ${LOOPVAR} + 1)
    sleep 1
    EXIST_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${EXIST_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
done

if [ -z "${EXIST_PID}" ]
then
    echo "Error: could not start Exist service"
    ERRORSTATUS=1
fi

cd ../..

echo "Restart BOS container..."
cd ${BOS}/bin/
./startup.sh 
LOOPVAR=0
BOS_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${BOS_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
while [ -z "${BOS_PID}" -a  ${LOOPVAR} -lt 20  ]
do
    LOOPVAR=$(expr ${LOOPVAR} + 1)
    sleep 1
    BOS_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${BOS_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
done

if [ -z "${BOS_PID}" ]
then
    echo "Error: could not start BOS service"
    ERRORSTATUS=1
fi
cd ../..

echo "Restart eservice container..."
cd ${ESERVICE}/bin/
./startup.sh 
LOOPVAR=0
ESERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${ESERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
while [ -z "${ESERVICE_PID}" -a  ${LOOPVAR} -lt 20  ]
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
    while [ -z "${KSERVICE_PID}" -a  ${LOOPVAR} -lt 20  ]
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
