#!/bin/bash

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

ERRORSTATUS=0

function getPidByPort () {
   local _outvar=$1
   local _result # Use some naming convention to avoid OUTVARs to clash
   _result=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${2} | awk '{print substr($7,1,match($7,"/")-1)}')
   eval $_outvar=\$_result # Instead of just =$_result
}

function startContainer () {
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

function shutdownContainer () { 
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
	echo "shutdownContainer: No container argument"
    fi
    popd
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

  getPidByPort SERVICE_PID ${CURRENT_PORT}
  shutdownContainer $1 ${SERVICE_PID}
  startContainer ${SERVICE}
}

function listContainers () {
    local CURRENT_SERVICE
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
	    echo -e "Service:   ${CURRENT_SERVICE} \t port: ${CURRENT_PORT} \t pid: ${CURRENT_PID}"
	fi
   done
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
	shutdownContainer $i $CURRENT_PID
    done
}

function startContainers() {
    for i in  ${ESERVICE}  ${KSERVICE}  ${CMSSERVICE}
    do
	startContainer $i
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
  echo "Usage: $1 l|s|r|x (One of:  -l = list, -x = stop, -r = restart, -s = start )"
  exit 1 ;
}

if [ $# != 1 ]
then
 usage $0
fi

arglength=${#1}

if [ $arglength != 2 ]
then
 usage $0
fi

while getopts lxsr opt
do
	case $opt in
		l )  listContainers ; break     ;;
		s )  startContainers ; break    ;;
		r )  restartContainers ; break  ;;
		x )  shutdownContainers ; break ;;
	       \? )  usage $0 ; exit 1 ;;
	esac
done
