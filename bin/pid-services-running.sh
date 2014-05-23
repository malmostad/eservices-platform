#!/bin/bash

function getPidByPort () {
   local _outvar=$1
   local _result # Use some naming convention to avoid OUTVARs to clash
   _result=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${2} | awk '{print substr($7,1,match($7,"/")-1)}')
   eval $_outvar=\$_result # Instead of just =$_result
}

function services_running () {
    getPidByPort ESERVICE_PID ${ESERVICE_PORT}
    if [ -n "${ESERVICE_PID}" ] 
    then 
	echo -e "Eservice:   ${ESERVICE} \t port: ${ESERVICE_PORT} \t pid: ${ESERVICE_PID}"
    fi

    getPidByPort KSERVICE_PID ${KSERVICE_PORT}
    if [ -n "${KSERVICE_PID}" ] 
    then 
	echo -e "Kservice:   ${KSERVICE} \t port: ${KSERVICE_PORT} \t pid: ${KSERVICE_PID}"
    fi

    getPidByPort CMSSERVICE_PID ${CMSSERVICE_PORT}
    if [ -n "${CMSSERVICE_PID}" ] 
    then 
	echo -e "CMSservice: ${CMSSERVICE} \t port: ${CMSSERVICE_PORT} \t pid: ${CMSSERVICE_PID}"
    fi
}

###### current_config.sh  #####
# symlink to actual config of current installation
. current_config.sh

services_running
