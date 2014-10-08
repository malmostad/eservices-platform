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
LOOP=10 
JMETERSETTINGS="jmeterSetting_deploy" 
if test -f "${JMETERSETTINGS}" 
then 
   echo "reading ${JMETERSETTINGS}" 
   source  ./${JMETERSETTINGS}
else
   echo "using defaults"
   # The host under test.
   HOST="eminburk.malmo.se"
   PROTOCOL="https"
   PORT="443"
   STARTFORM="demo-ansokan--v002"
fi 

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
OUTPUTDIR=`pwd`/jmeterResults
OUTPUTSLASK=`pwd`/slask
 
TESTCLEANPLAN=TestPlanDoAllInInbox.jmx 
COMMANDOCLEAN="${JMETER} -n -t ${TESTCLEANPLAN} -Jthreads=1 -Jcasename='DoAllInInbox' -Juser=$USER -Jpassword=$PASS -Jport=$PORT -Jprotocol=$PROTOCOL -Jhost=$HOST -JoutputDir=${OUTPUTSLASK}"

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
cleanUpInbox 
exit 0 



TESTPLAN=TestPlanCreateCases.jmx 
CASENAME="CreateCases" 

if [ ! -f ${TESTPLAN} ]; then
  echo "Missing file TESTPLAN=${TESTPLAN}"
   exit 1;
fi

LOOP=3
LOOP=11
/bin/rm  ${OUTPUTDIR}/${CASENAME}/*jtl
for thread_count in 001 002 004 010 
# for thread_count in 001 002 004 010 050 100
do
  COMMANDO="${JMETER} -n -t ${TESTPLAN} -Jthreads=$thread_count -Jcasename=$CASENAME -Juser=$USER -Jpassword=$PASS -Jport=$PORT -Jprotocol=$PROTOCOL -Jhost=$HOST -JoutputDir=${OUTPUTDIR} -Jloop=${LOOP}"
  echo ${COMMANDO}
  ${COMMANDO}

  # clean up between the tests 
  cleanUpInbox 
done


PLOTSCRIPT="./plotResults.py" 
if [ ! -f ${PLOTSCRIPT} ]; then
  echo "Missing file PLOTSCRIPT=${PLOTSCRIPT}"
   exit 1;
fi


${PLOTSCRIPT} ${OUTPUTDIR}/${CASENAME}/*jtl

echo "results are found in   ${OUTPUTDIR}/${CASENAME}   "

display ${OUTPUTDIR}/${CASENAME}/_orbeon_fr_start_demo-ansokan--v002_edit_ef08e823-78f3-4c25-a744-31e9958c05ae.png    & 
