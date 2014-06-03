#!/bin/bash
### BEGIN INIT INFO
# Provides: Auto start of motrice services
# Required-Start: $remote_fs $syslog
# Required-Stop: $remote_fs $syslog
# Default-Start: 2 3 4 5
# Default-Stop: 0 1 6
# Short-Description: Start motrice services
# Description: Starts the containers for openam,opendj,kservice,eservice
#  NB!! The containers are started as user 'inherit'
### END INIT INFO

# Source function library.
. /lib/lsb/init-functions

################################################################
# CONFIG                                                #
################################################################

###### current_config.sh  #####
# symlink to actual config of current installation
. current_config.sh

################################################################
# END OF CONFIG                                                #
################################################################

ERRORSTATUS=0

function getPidByPort () {
   local _outvar=$1
   local _result # Use some naming convention to avoid OUTVARs to clash
   _result=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${2} | awk '{print substr($7,1,match($7,"/")-1)}')
   eval $_outvar=\$_result # Instead of just =$_result
}

function startTomcatContainer () {
  local SERVICE_PID
  local LOOPCOUNTER=0

  case $1 in
    ${ESERVICE} )   CURRENT_PORT=${ESERVICE_PORT}   ;;
    ${KSERVICE} )   CURRENT_PORT=${KSERVICE_PORT}   ;;
    ${CMSSERVICE} ) CURRENT_PORT=${CMSSERVICE_PORT} ;;
    *) echo "Unknown Service, halting..." ; exit 1 ;;
  esac

  pushd ${CONTAINER_ROOT}
    echo "Starting container $1..."
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

function shutdownTomcatContainer () { 
    #if no argument, then NOOP
    pushd ${CONTAINER_ROOT}
    if [ -n "$2" ] 
    then 
	echo "Shutting down service $1, pid: " $2
	cd ${1}/bin/
	./shutdown.sh
	sleep 1
	LOOPVAR=0
	while ps -p $2 && [ ${LOOPVAR} -lt 6  ]
	do
	    LOOPVAR=$(expr ${LOOPVAR} + 1)
	    sleep 1
	done

    # If proper shutdown did not bite
	if ps -p $2
	then 
	    echo "Force shutting down service $1, pid: " $2
	    kill  $2
	    sleep 6
	fi

    # If still did not bite
	if ps -p $2
	then 
	    echo "Failed to shut down service $1, pid: " $2
	    ERRORSTATUS=1;
	fi
    else
	echo "shutdownTomcatContainer: No container argument"
    fi
    popd
}

function restartTomcatContainer () {
  local SERVICE_PID
  local LOOPCOUNTER=0

  case $1 in
    ${ESERVICE} )   CURRENT_PORT=${ESERVICE_PORT}   ;;
    ${KSERVICE} )   CURRENT_PORT=${KSERVICE_PORT}   ;;
    ${CMSSERVICE} ) CURRENT_PORT=${CMSSERVICE_PORT} ;;
    *) echo "Unknown Service, halting..." ; exit 1 ;;
  esac

  getPidByPort SERVICE_PID ${CURRENT_PORT}
  shutdownTomcatContainer $1 ${SERVICE_PID}
  startTomcatContainer ${SERVICE}
}

function listContainers () {
    local CURRENT_PID

    for i in  ${ESERVICE}  ${KSERVICE}  ${CMSSERVICE}
    do
        case $i in
	    ${ESERVICE} )  getPidByPort CURRENT_PID ${ESERVICE_PORT}
                           CURRENT_PORT=${ESERVICE_PORT}
			   ;;

            ${KSERVICE} )  getPidByPort CURRENT_PID ${KSERVICE_PORT}
                           CURRENT_PORT=${KSERVICE_PORT}
			   ;;

            ${CMSSERVICE} ) getPidByPort CURRENT_PID ${CMSSERVICE_PORT}
                           CURRENT_PORT=${CMSSERVICE_PORT}
			   ;;

	    *) echo "Unknown Service, halting..." ; exit 1 ;;
	esac
	    
	if [ -n "${CURRENT_PID}" ] 
	then 
	    echo -e "Service: ${i} \t port: ${CURRENT_PORT} \t pid: ${CURRENT_PID}"
	fi
   done

   if ${WITH_OPENDJ}
   then 
       # TODO
       # get opendj port number by looking up ${OPENDJ_SETUP_PROPERTIES} file
       getPidByPort CURRENT_PID 1389
       if [ -n "${CURRENT_PID}" ] 
       then 
	   echo -e "Service: opendj \t\t port: 1389 \t pid: ${CURRENT_PID}"
       fi
   fi
}

function shutdownContainers() {
    local CURRENT_PID

    for i in  ${ESERVICE}  ${KSERVICE}  ${CMSSERVICE}
    do
        case $i in
	    ${ESERVICE} )  getPidByPort CURRENT_PID ${ESERVICE_PORT}
                           CURRENT_PORT=${ESERVICE_PORT}
			   ;;

            ${KSERVICE} )  getPidByPort CURRENT_PID ${KSERVICE_PORT}
                           CURRENT_PORT=${KSERVICE_PORT}
			   ;;

            ${CMSSERVICE} ) getPidByPort CURRENT_PID ${CMSSERVICE_PORT}
                           CURRENT_PORT=${CMSSERVICE_PORT}
			   ;;

	    *) echo "Unknown Service, halting..." ; exit 1 ;;
	esac

	echo -e "Shutting down service container $i with pid: $CURRENT_PID" at port ${CURRENT_PORT}
	shutdownTomcatContainer $i $CURRENT_PID
    done

    if ${WITH_OPENDJ}
    then 
      $CONTAINER_ROOT/opendj/bin/stop-ds
    fi
}

function startContainers() {
    if ${WITH_OPENDJ}
    then 
      $CONTAINER_ROOT/opendj/bin/start-ds
    fi

    for i in  ${ESERVICE}  ${KSERVICE}  ${CMSSERVICE}
    do
      startTomcatContainer $i
    done
}

function restartContainers() {
  echo "Shutting down containers"
  shutdownContainers
  echo "Restarting containers"
  startContainers
  echo "Containers restarted"
}

function usage () {
  echo "Usage: $1 {start|stop|restart|status}"
  exit 1 ;
}

if [ $# != 1 ]
then
 usage $0
fi

case "$1" in
    start )
    startContainers
  ;;

  stop )
    shutdownContainers
  ;;

  status )
    listContainers
  ;;

  restart )
    shutdownContainers
    startContainers
  ;;

  reload )
    echo "Not applied to service"
  ;;

  condrestart )
    echo "Not applied to service"
  ;;

  probe )
    echo "Not applied to service"
  ;;

  *)
    usage $1
    exit 1
  ;;
esac

exit ${ERRORSTATUS}
