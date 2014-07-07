#!/bin/bash

################################################################
# CONFIG                                                #
################################################################

###### current_config.sh  #####
# symlink to actual config of current installation
. current_config.sh

################################################################
# END OF CONFIG                                                #
################################################################

PROPERTIES_LOCAL_BEFOREPATCH=properties-local.xml.beforepatch 
SITE_WEB_XML_BEFOREPATCH=web.xml.beforepatch 
SITE_HST_CONFIG_PROPERTIES_BEFOREPATCH=hst-config.properties.beforepatch

ERRORSTATUS=0

function getPidByPort () {
   local _outvar=$1
   local _result # Use some naming convention to avoid OUTVARs to clash
   _result=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${2} | awk '{print substr($7,1,match($7,"/")-1)}')
   eval $_outvar=\$_result # Instead of just =$_result
}

function shutdownContainer () { 
    #if no argument, then NOOP
    if [ -n "$1" ] 
    then 
	echo "Shutting down eservice, pid: " $1
	./shutdown.sh
	sleep 1
	LOOPVAR=0
	while ps -p $1 && [ ${LOOPVAR} -lt 6  ]
	do
	    LOOPVAR=$(expr ${LOOPVAR} + 1)
	    sleep 1
	done

    # If proper shutdown did not bite
	if ps -p $1
	then 
	    echo "Force shutting down eservice, pid: " $1
	    kill  $1
	    sleep 6
	fi

    # If still did not bite
	if ps -p $1
	then 
	    echo "Failed to shut down eservice, pid: " $1
	    ERRORSTATUS=1;
	fi
    else
	echo "shutdownContainer: No container argument"
    fi
}

function installInContainer () {
    echo "Installing on $1 container"
    if [ -d  ${CONTAINER_ROOT}/$1 ]
    then
	pushd ${CONTAINER_ROOT}/$1
	if [[ "$1" == "${ESERVICE}" ]]
	then
	    tar xzfv ${BUILD_DIR}/inherit-portal/target/inherit-portal-1.01.00-SNAPSHOT-distribution-eservices.tar.gz
	    cd webapps
	    rm -fr cms site orbeon exist docbox coordinatrice
	     # deploy cms at cmsservice and coordinatrice only at kservice and share the same JCR
	    rm cms.war coordinatrice.war
	elif [[ "$1" == "${KSERVICE}" ]]
        then
	    tar xzfv ${BUILD_DIR}/inherit-portal/target/inherit-portal-1.01.00-SNAPSHOT-distribution-kservices.tar.gz
	    cd webapps
	    rm -fr cms site orbeon exist docbox coordinatrice
            rm cms.war 
        elif  [[ "$1" == "${CMSSERVICE}" ]]
        then
	    tar xzfv ${BUILD_DIR}/inherit-portal/target/inherit-portal-1.01.00-SNAPSHOT-distribution-kservices.tar.gz
	    cd webapps
	    rm -fr site orbeon exist docbox coordinatrice cms  restrice
            rm site.war orbeon.war exist.war docbox.war coordinatrice.war restrice.war
        else
  	  echo "Unknown Directory ${CONTAINER_ROOT}/$1. Halting."
	  exit 1
        fi
	popd
    else
	echo "Directory ${CONTAINER_ROOT}/$1 does not exist. Halting."
	exit 1
    fi
}

function restartContainer () {
  local SERVICE_PID
  local LOOPCOUNTER=0

  case $1 in
    ${ESERVICE} )   CURRENT_PORT=${ESERVICE_PORT}   ;;
    ${KSERVICE} )   CURRENT_PORT=${KSERVICE_PORT}   ;;
    ${CMSSERVICE} ) CURRENT_PORT=${CMSSERVICE_PORT} ;;
    *) echo "Unknown Service, halting..." ; exit 1 ;;
  esac

  pushd ${CONTAINER_ROOT}
    echo "Restarting container $1..."
    cd ${1}/bin/
    ./startup.sh 
    getPidByPort SERVICE_PID ${CURRENT_PORT}
    while [ -z "${SERVICE_PID}" -a  ${LOOPCOUNTER} -lt 30  ]
    do
      LOOPCOUNTER=$(expr ${LOOPCOUNTER} + 1)
      sleep 1
      getPidByPort SERVICE_PID ${CURRENT_PORT}
    done

    if [ -z "${SERVICE_PID}" ]
    then
      echo "Error: could not start $1"
      ERRORSTATUS=1
    fi
  popd
}

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
echo "CONTAINER_ROOT/CMSSERVICE: $CONTAINER_ROOT/$CMSSERVICE"

if [ ! -d ${CONTAINER_ROOT}/${ESERVICE} ] || [ ! -d ${CONTAINER_ROOT}/${KSERVICE} ] || [ ! -d ${CONTAINER_ROOT}/${CMSSERVICE} ]
then
    echo "Either of ${CONTAINER_ROOT}/${ESERVICE} ${CONTAINER_ROOT}/${KSERVICE} or ${CONTAINER_ROOT}/${CMSSERVICE} do not exist, aborting execution of $0"
    ERRORSTATUS=1
    exit $ERRORSTATUS
fi

echo "ESERVICE_PORT: $ESERVICE_PORT"
echo "KSERVICE_PORT: $KSERVICE_PORT"
echo "CMSSERVICE_PORT: $CMSSERVICE_PORT"

if [ -z "${ESERVICE_PORT}" ] || [ -z "${KSERVICE_PORT}" ] || [ -z "${CMSSERVICE_PORT}" ]
then
    echo "Either of parameters ESERVICE_PORT or KSERVICE_PORT or CMSSERVICE_PORTunset, aborting execution of $0"
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

# 2. Patching files for usage in production like environment
#    (the original versions are suitable in a test environment with
#    mvn -P cargo.run)

# 2a. Patching properties-local.xml for eservicetest and kservicetest
if [ -d ${BUILD_DIR}/inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config ]
then
    pushd ${BUILD_DIR}/inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config
    mv properties-local.xml $PROPERTIES_LOCAL_BEFOREPATCH
    cp $PROPERTIES_LOCAL_BEFOREPATCH properties-local.xml # thereby conserving mod date of properties-local.xml
                                                             # when $PROPERTIES_LOCAL_BEFOREPATCH is renamed
                                                             # back to properties-local.xml in step 8
    if ${ESERVICE_SSL}; then
	sed -e "s/http:\/\/localhost:8080\/site\/mycases\/form\/confirmdispatcher/https:\/\/${ESERVICE_HOST}:${ESERVICE_EXTERNAL_PORT}\/site\/mycases\/form\/confirmdispatcher/g" properties-local.xml > properties-local.xml.eservicepatch
    else
	sed -e "s/http:\/\/localhost:8080\/site\/mycases\/form\/confirmdispatcher/http:\/\/${ESERVICE_HOST}:${ESERVICE_EXTERNAL_PORT}\/site\/mycases\/form\/confirmdispatcher/g" properties-local.xml > properties-local.xml.eservicepatch
    fi

    if ${KSERVICE_SSL}; then
	sed -e "s/http:\/\/localhost:8080\/site\/mycases\/form\/confirmdispatcher/https:\/\/${KSERVICE_HOST}:${KSERVICE_EXTERNAL_PORT}\/site\/mycases\/form\/confirmdispatcher/g" properties-local.xml > properties-local.xml.kservicepatch
    else
	sed -e "s/http:\/\/localhost:8080\/site\/mycases\/form\/confirmdispatcher/http:\/\/${KSERVICE_HOST}:${KSERVICE_EXTERNAL_PORT}\/site\/mycases\/form\/confirmdispatcher/g" properties-local.xml > properties-local.xml.kservicepatch
    fi
    mv properties-local.xml.eservicepatch properties-local.xml
    popd
else
    echo "Directory ${BUILD_DIR}/inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config does not exist. Aborting execution"
    ERRORSTATUS=1
    exit $ERRORSTATUS
fi

# 2b. Patching web.xml - Including OpenAM filter in site.war for eservicetest and kservicetest
if ${WITH_OPENAM}
then
  if [ -f  ${BUILD_DIR}/inherit-portal/site/src/main/webapp/WEB-INF/web.xml ]
  then
      pushd ${BUILD_DIR}/inherit-portal/site/src/main/webapp/WEB-INF
      mv web.xml $SITE_WEB_XML_BEFOREPATCH
      cp $SITE_WEB_XML_BEFOREPATCH web.xml  # thereby conserving mod date of web.xml
					    # when $SITE_WEB_XML_BEFOREPATCH is renamed
					    # back to web.xml in step 8b
      sed -i -e 's/\(OPENAM_FILTER_BEGIN.*$\)/\1 -->/g' -e 's/\(^.*OPENAM_FILTER_END\)/<!-- \1/g' web.xml
      popd
  else
      echo "File ${BUILD_DIR}/inherit-portal/site/src/main/webapp/WEB-INF/web.xml does not exist. Aborting execution"
      ERRORSTATUS=1
      exit $ERRORSTATUS
  fi
fi

# 2c. Patching hst-config.properties - changing to rmi for access to hipporepository
if [ -f  ${BUILD_DIR}/inherit-portal/site/src/main/webapp/WEB-INF/hst-config.properties ]
then
    pushd ${BUILD_DIR}/inherit-portal/site/src/main/webapp/WEB-INF
    mv hst-config.properties $SITE_HST_CONFIG_PROPERTIES_BEFOREPATCH
    cp $SITE_HST_CONFIG_PROPERTIES_BEFOREPATCH hst-config.properties  # thereby conserving mod date of
                                          # hst-config.properties when $SITE_HST_CONFIG_PROPERTIES_BEFOREPATCH
                                          # is renamed back to hst-config.properties in step 8c
    sed -i -e 's/=\s\+vm:\/\//= rmi:\/\/127.0.0.1:1099\/hipporepository/g' hst-config.properties
    popd
else
    echo "File ${BUILD_DIR}/inherit-portal/site/src/main/webapp/WEB-INF/hst-config.properties does not exist. Aborting execution"
    ERRORSTATUS=1
    exit $ERRORSTATUS
fi

# 3. Build eservice-platform
if [ -z ${MVN_SKIP_TEST} ] 
then 
    MVN_SKIP_TEST=true 
fi 
pushd ${BUILD_DIR}
if mvn -DskipTests=${MVN_SKIP_TEST} clean install
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

#8. Restore to original state the patched files from step 2 in order to be able to run
#   the deployment script multiple times

# 8a. Restore original properties-local.xml to original state. Necessary to make step 2a
#    (patching properties-local.xml) work correctly next time script is run
    pushd ${BUILD_DIR}/inherit-portal/orbeon/src/main/webapp/WEB-INF/resources/config
       mv $PROPERTIES_LOCAL_BEFOREPATCH properties-local.xml
    popd

# 8b. Restore original web.xml.beforepatch  to original state. Necessary to make step 2b
#     (patching web.xml of site.war) work correctly next time script is run
if ${WITH_OPENAM}
then
  pushd ${BUILD_DIR}/inherit-portal/site/src/main/webapp/WEB-INF
    mv $SITE_WEB_XML_BEFOREPATCH web.xml
  popd
fi

# 8c. Restore original hst-config.properties.beforepatch to original state. Necessary to
#      make step 2c (patching hst-config.properties of site.war) work correctly next time
#      script is run
    pushd ${BUILD_DIR}/inherit-portal/site/src/main/webapp/WEB-INF
       mv $SITE_HST_CONFIG_PROPERTIES_BEFOREPATCH hst-config.properties
    popd

# 9. Stop j2ee containers
    pushd ${CONTAINER_ROOT}

cd ${ESERVICE}/bin/

getPidByPort ESERVICE_PID ${ESERVICE_PORT}
shutdownContainer "${ESERVICE_PID}"

if ${WITH_KSERVICES}
then
    cd ../../${KSERVICE}/bin/
    getPidByPort KSERVICE_PID ${KSERVICE_PORT}
    shutdownContainer "${KSERVICE_PID}"
fi

if ${WITH_CMSSERVICES}
then
    cd ../../${CMSSERVICE}/bin/
    getPidByPort CMSSERVICE_PID ${CMSSERVICE_PORT}
    shutdownContainer "${CMSSERVICE_PID}"
fi

popd

if [ ${ERRORSTATUS} -eq 1 ]
then
    echo "Failed to shutdown all containers, aborting execution of script"
    exit ${ERRORSTATUS}
fi

# 10. Install on eservice container
echo "Installing wars in ${ESERVICE} container"
installInContainer ${ESERVICE}

# 11. Install on kservice container
if ${WITH_KSERVICES}
then
  echo "Installing wars in ${KSERVICE} container"
  installInContainer ${KSERVICE}
fi

# 12   Install on cmsservice container
if ${WITH_CMSSERVICES}
then
  echo "Installing wars in ${CMSSERVICE} container"
  installInContainer ${CMSSERVICE}
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
#pushd ${CONTAINER_ROOT}

if ${WITH_OPENDJ}
then
  $CONTAINER_ROOT/opendj/bin/stop-ds
  $CONTAINER_ROOT/opendj/bin/start-ds
fi

restartContainer ${ESERVICE}

if ${WITH_KSERVICES}
then
  restartContainer ${KSERVICE}
fi

if ${WITH_CMSSERVICES}
then
  restartContainer ${CMSSERVICE}
fi

exit ${ERRORSTATUS}
