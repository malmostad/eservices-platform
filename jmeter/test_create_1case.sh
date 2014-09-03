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

function usage() {
echo "Usage: $0 -u USERNAME -p PASSWORD"
}


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



# load a number of activites 
LOOP=1 
# The host under test.
HOST=localhost

# A username.
USER=""
 
# USER's password
PASS=""
 
while getopts 'u:p:' flag; do
  case "${flag}" in
    u) USER=$OPTARG ;;
    p) PASS=$OPTARG ;;
    *) error "Unexpected option ${flag}" ; usage ;;
  esac
done

if [ -z "$USER" ] ; then 
  echo "Missing -u argument"
  usage 
   exit 1;
fi
 
if [ -z "$PASS" ] ; then 
  echo "Missing -p argument"
  usage 
   exit 1;
fi
 
 
JMETER=${HOME}/jmeter/apache-jmeter-2.11/bin/jmeter.sh
JMETER=${HOME}/jmeter/bin/jmeter.sh
OUTPUTDIR=`pwd`/jmeterResults
OUTPUTSLASK=`pwd`/slask
TESTPLANDIRECTORY=${HOME}/workspaces/inheritsource-develop/pawap/jmeter
TESTCLEANPLAN=${TESTPLANDIRECTORY}/TestPlanDoAllInInbox.jmx 
COMMANDOCLEAN="${JMETER} -n -t ${TESTCLEANPLAN} -Jthreads=1 -Jcasename='DoAllInInbox' -Juser=$USER -Jpassword=$PASS -JoutputDir=${OUTPUTSLASK}"

# 
if [ ! -f ${JMETER} ]; then
  echo "Missing file JMETER=${JMETER}"
   exit 1;
fi

if [ ! -f ${TESTCLEANPLAN} ]; then
  echo "Missing file TESTCLEANPLAN=${TESTCLEANPLAN}"
   exit 1;
fi


# first clean up inbox 
####cleanUpInbox 

TESTPLAN=${TESTPLANDIRECTORY}/TestPlanCreateCases.jmx 
CASENAME="CreateCases" 

if [ ! -f ${TESTPLAN} ]; then
  echo "Missing file TESTPLAN=${TESTPLAN}"
   exit 1;
fi

LOOP=11
LOOP=1
/bin/rm  ${OUTPUTDIR}/${CASENAME}/*jtl
for thread_count in 001 
# for thread_count in 001 002 004 010 050 100
do
  COMMANDO="${JMETER} -n -t ${TESTPLAN} -Jthreads=$thread_count -Jcasename=$CASENAME -Juser=$USER -Jpassword=$PASS -JoutputDir=${OUTPUTDIR} -Jloop=${LOOP}"
  echo ${COMMANDO}
  ${COMMANDO}

  # clean up between the tests 
  # cleanUpInbox 
done

