#!/bin/bash
#
#== Motrice Copyright Notice == 
#  
# Motrice Service Platform 
#  
# Copyright (C) 2011-2014 Motrice AB 
#  
# This program is free software: you can redistribute it and/or modify 
# it under the terms of the GNU Affero General Public License as published by 
# the Free Software Foundation, either version 3 of the License, or 
# (at your option) any later version. 
# 
# This program is distributed in the hope that it will be useful, 
# but WITHOUT ANY WARRANTY; without even the implied warranty of 
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
# GNU Affero General Public License for more details. 
#  
# You should have received a copy of the GNU Affero General Public License 
# along with this program. If not, see <http://www.gnu.org/licenses/>. 
#  
# e-mail: info _at_ motrice.se 
# mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
# phone: +46 8 641 64 14 

function cleanUpInbox() {

echo "cleaning up in inbox "
echo ${COMMANDOCLEAN}
nloops=3   # number of users tasks : registrering - handlägging - expediering 
for i in `seq 1 ${nloops} `;
do 
  echo "cleaning up step ${i} "
  ${COMMANDOCLEAN}
done 
}


function readSettings() { 
JMETERSETTINGS="${HOME}/workspaces/inheritsource-develop/pawap/jmeter/jmeterSetting_deploy" 
JMETERSETTINGSEXAMPLE="${HOME}/workspaces/inheritsource-develop/pawap/jmeter/jmeterSetting_example" 
if test -f "${JMETERSETTINGS}" 
then 
    echo "reading ${JMETERSETTINGS}" 
    source  ${JMETERSETTINGS}

    # check that variables are set 
    if [[ -z "${HOST}" ]] ; then 
        echo "HOST is not set in ${JMETERSETTINGS}"
        exit 1;
    fi 

    if [[ -z "${PROTOCOL}" ]] ; then 
        echo "PROTOCOL is not set in ${JMETERSETTINGS}"
        exit 1;
    fi 

    if [[ -z "${PORT}" ]] ; then 
        echo "PORT is not set in ${JMETERSETTINGS}"
        exit 1;
    fi 

    if [[ -z "${STARTFORM}" ]] ; then 
        echo "STARTFORM is not set in ${JMETERSETTINGS}"
        exit 1;
    fi 

    if [[ -z "${JMETER}" ]] ; then 
        echo "JMETER is not set in ${JMETERSETTINGS}"
        exit 1;
    fi 

    if [[ -z "${OUTPUTDIR}" ]] ; then 
        echo "OUTPUTDIR is not set in ${JMETERSETTINGS}"
        exit 1;
    fi 

    if [[ -z "${OUTPUTSLASK}" ]] ; then 
        echo "OUTPUTSLASK is not set in ${JMETERSETTINGS}"
        exit 1;
    fi 

    if [[ -z "${TESTPLANDIRECTORY}" ]] ; then 
        echo "TESTPLANDIRECTORY is not set in ${JMETERSETTINGS}"
        exit 1;
    fi 

    if [[ -z "${TESTCLEANPLAN}" ]] ; then 
        echo "TESTCLEANPLAN is not set in ${JMETERSETTINGS}"
        exit 1;
    fi 

    if [[ -z "${TESTPLAN}" ]] ; then 
        echo "TESTPLAN is not set in ${JMETERSETTINGS}"
        exit 1;
    fi 


    # check if files exists     

    if [ ! -f ${JMETER} ]; then
        echo "Missing file JMETER=${JMETER}"
        exit 1;
    fi

    if [ ! -f ${TESTCLEANPLAN} ]; then
        echo "Missing file TESTCLEANPLAN=${TESTCLEANPLAN}"
        exit 1;
    fi

    if [ ! -f ${TESTPLAN} ]; then
      echo "Missing file TESTPLAN=${TESTPLAN}"
       exit 1;
    fi

   #####  
else
    echo ${JMETERSETTINGS} does not exist
    if test -f "${JMETERSETTINGSEXAMPLE}" 
    then 
        echo use ${JMETERSETTINGSEXAMPLE} as
        echo an example and create one.
        exit 1 
    else 
        echo use ${JMETERSETTINGSEXAMPLE} as
        echo an example and create one.
        echo However ${JMETERSETTINGSEXAMPLE} does 
        echo exist. Might be at a different path. 
        exit 1 
    fi 
fi 
}


