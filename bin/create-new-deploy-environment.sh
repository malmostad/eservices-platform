#!/bin/bash

# CONFIG #######################################################
# Select one config to deploy below                            #
################################################################

###### Prod in Malmo ###########################################
# . config_deploy_eservice.sh

###### Test in Malmo ###########################################
# . config_deploy_eservicetest.sh

###### Funkar på min burk - developer's workstation config #####
. config_deploy_minburk.sh

TMP_DIR=tmp

if [ -d ${CONTAINER_ROOT} ];
then
   echo "Cannot create deploy environment at ${CONTAINER_ROOT}. Directory already exists."
   exit 0;
fi

if [ ! -f ${OPENAM_POLICY_AGENT_PWD_FILE} ]; then 
  echo "Missing file OPENAM_POLICY_AGENT_PWD_FILE=${OPENAM_POLICY_AGENT_PWD_FILE}"
   exit 0;
fi

mkdir -p ${CONTAINER_ROOT}

################################################################
# Download and extract to deployment environment               #
################################################################

# Work from temporary directory
mkdir -p ${TMP_DIR}
pushd ${TMP_DIR}
mkdir -p downloads

# download not already downloaded stuff 
if [ ! -f downloads/${TOMCAT_TGZ} ]; then
    curl -o downloads/${TOMCAT_TGZ} ${TOMCAT_DOWNLOAD_URL}
fi

if [ ! -f downloads/${FORGEROCK_POLICY_AGENT_ZIP} ]; then
    curl -o downloads/${FORGEROCK_POLICY_AGENT_ZIP} ${FORGEROCK_POLICY_AGENT_URL}
fi

tar xzf downloads/${TOMCAT_TGZ}
################################################################
# Hippo Tomcat config                                          #
################################################################
mv ${TOMCAT_DIR}/conf/catalina.properties ${TOMCAT_DIR}/conf/catalina.properties.orig
sed -e 's/common.loader=${catalina.base}\/lib,${catalina.base}\/lib\/*.jar,${catalina.home}\/lib,${catalina.home}\/lib\/*.jar/common.loader=${catalina.base}\/lib,${catalina.base}\/lib\/*.jar,${catalina.home}\/lib,${catalina.home}\/lib\/*.jar,${catalina.base}\/common\/classes,${catalina.base}\/common\/lib\/*.jar/g' -e 's/shared.loader=/shared.loader=${catalina.base}\/shared\/classes,${catalina.base}\/shared\/lib\/*.jar/g' ${TOMCAT_DIR}/conf/catalina.properties.orig > ${TOMCAT_DIR}/conf/catalina.properties

################################################################
# copy to eservice and kservice tomcats                        #
################################################################

cp -r  ${TOMCAT_DIR} ${CONTAINER_ROOT}/${ESERVICE}
mv  ${TOMCAT_DIR} ${CONTAINER_ROOT}/${KSERVICE}

# copy one policy agent config per tomcat
unzip -uq downloads/${FORGEROCK_POLICY_AGENT_ZIP}
mv j2ee_agents  ${CONTAINER_ROOT}/
cp -r ${CONTAINER_ROOT}/j2ee_agents/tomcat_v6_agent ${CONTAINER_ROOT}/j2ee_agents/eservice-tomcat_v6_agent
mv ${CONTAINER_ROOT}/j2ee_agents/tomcat_v6_agent ${CONTAINER_ROOT}/j2ee_agents/kservice-tomcat_v6_agent
# copy the top secret pwd file to j2ee_agents
cp ${OPENAM_POLICY_AGENT_PWD_FILE} ${CONTAINER_ROOT}/j2ee_agents/
popd

# prepare a directory for hippo jcr
mkdir ${CONTAINER_ROOT}/jcr-inherit-portal

