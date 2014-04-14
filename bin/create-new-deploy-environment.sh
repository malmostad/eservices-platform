#!/bin/bash

################################################################
# The great create deploy server config                        #
#
#
################################################################

# CONFIG #######################################################
# Select one config to deploy below                            #
################################################################

###### Prod in Malmo ###########################################
# . config_deploy_eservice.sh

###### Test in Malmo ###########################################
# . config_deploy_eservicetest.sh

###### Funkar på min burk - developer's workstation config #####
. config_deploy_minburk.sh


if [ "${ESERVICE_SSL}" = "TRUE" ]; then
   if [ -n "$ESERVICE_PORT" ]; then
     ESERVICE_SITE_URL=https://${ESERVICE_HOST}:${ESERVICE_PORT}/site
   else 
     ESERVICE_SITE_URL=https://${ESERVICE_HOST}:443/site
   fi
else
   if [ -n "$ESERVICE_PORT" ]; then
     ESERVICE_SITE_URL=https://${ESERVICE_HOST}:${ESERVICE_PORT}/site
   else 
     ESERVICE_SITE_URL=https://${ESERVICE_HOST}:80/site
   fi
fi

if [ "${KSERVICE_SSL}" = "TRUE" ]; then
   if [ -n "$KSERVICE_PORT" ]; then
     KSERVICE_SITE_URL=https://${KSERVICE_HOST}:${KSERVICE_PORT}/site
   else 
     KSERVICE_SITE_URL=https://${KSERVICE_HOST}:443/site
   fi
else
   if [ -n "$KSERVICE_PORT" ]; then
     KSERVICE_SITE_URL=https://${KSERVICE_HOST}:${KSERVICE_PORT}/site
   else 
     KSERVICE_SITE_URL=https://${KSERVICE_HOST}:80/site
   fi
fi

################################################################
# Check some requirements 
#    i.e. do not overwrite CONTAINER_ROOT
#         check that open am policy agent password file exists 
################################################################
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
HIPPO_APPEND_COMMON_LOADER=
mv ${TOMCAT_DIR}/conf/catalina.properties ${TOMCAT_DIR}/conf/catalina.properties.orig
sed -e 's/\(common\.loader=\)\(.*\)$/\1\2,\${catalina.base}\/common\/classes,\${catalina.base}\/common\/lib\/*.jar/' -e 's/shared.loader=/shared.loader=${catalina.base}\/shared\/classes,${catalina.base}\/shared\/lib\/*.jar/g' ${TOMCAT_DIR}/conf/catalina.properties.orig > ${TOMCAT_DIR}/conf/catalina.properties


################################################################
# copy to eservice and kservice tomcats                        #
################################################################

cp -r  ${TOMCAT_DIR} ${CONTAINER_ROOT}/${ESERVICE}
mv  ${TOMCAT_DIR} ${CONTAINER_ROOT}/${KSERVICE}
cp ${BUILD_DIR}/conf/repository.xml ${CONTAINER_ROOT}/${KSERVICE}/conf/
################################################################
# Write kservice Tomcat setenv.sh due to Hippo and Motrice requirements 
################################################################

echo "export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64" > ${CONTAINER_ROOT}/${KSERVICE}/bin/setenv.sh
echo REP_OPTS=\"-Drepo.upgrade=false -Drepo.path= ${CONTENT_ROOT} -Drepo.config=file:\${CATALINA_BASE}/conf/repository.xml\" >>  ${CONTAINER_ROOT}/${KSERVICE}/bin/setenv.sh
echo L4J_OPTS=\"-Dlog4j.configuration=file:\${CATALINA_BASE}/conf/log4j.xml\" >>  ${CONTAINER_ROOT}/${KSERVICE}/bin/setenv.sh
echo JVM_OPTS=\"-server -Xmx2048m -Xms1024m -XX:PermSize=256m\" >>  ${CONTAINER_ROOT}/${KSERVICE}/bin/setenv.sh
echo CATALINA_OPTS=\"\$CATALINA_OPTS -Dfile.encoding=UTF-8 \${JVM_OPTS} \${REP_OPTS} \${L4J_OPTS} -XX:+HeapDumpOnOutOfMemoryError\" >>  ${CONTAINER_ROOT}/${KSERVICE}/bin/setenv.sh
echo export CATALINA_OPTS >>  ${CONTAINER_ROOT}/${KSERVICE}/bin/setenv.sh

################################################################
# Write eservice Tomcat setenv.sh due to Hippo and Motrice requirements 
#   the diff is repo conf and policy agent conf
################################################################

echo "export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64" > ${CONTAINER_ROOT}/${ESERVICE}/bin/setenv.sh
echo L4J_OPTS=\"-Dlog4j.configuration=file:\${CATALINA_BASE}/conf/log4j.xml\" >>  ${CONTAINER_ROOT}/${ESERVICE}/bin/setenv.sh
echo JVM_OPTS=\"-server -Xmx2048m -Xms1024m -XX:PermSize=256m\" >>  ${CONTAINER_ROOT}/${ESERVICE}/bin/setenv.sh
echo CATALINA_OPTS=\"\$CATALINA_OPTS -Dfile.encoding=UTF-8 \${JVM_OPTS} \${L4J_OPTS} -XX:+HeapDumpOnOutOfMemoryError\" >>  ${CONTAINER_ROOT}/${ESERVICE}/bin/setenv.sh
echo export CATALINA_OPTS >>  ${CONTAINER_ROOT}/${ESERVICE}/bin/setenv.sh

# copy one policy agent config per tomcat
unzip -uq downloads/${FORGEROCK_POLICY_AGENT_ZIP}
mv j2ee_agents  ${CONTAINER_ROOT}/
cp -r ${CONTAINER_ROOT}/j2ee_agents/tomcat_v6_agent ${CONTAINER_ROOT}/j2ee_agents/eservice-tomcat_v6_agent
mv ${CONTAINER_ROOT}/j2ee_agents/tomcat_v6_agent ${CONTAINER_ROOT}/j2ee_agents/kservice-tomcat_v6_agent
# copy the top secret pwd file to j2ee_agents
cp ${OPENAM_POLICY_AGENT_PWD_FILE} ${CONTAINER_ROOT}/j2ee_agents/

################################################################
# eservice - Agent install configuration
#
################################################################
cd  ${CONTAINER_ROOT}/j2ee_agents/eservice-tomcat_v6_agent/bin
echo # Agent User Response File > eservice-openam-agent-config
echo CONFIG_DIR= ${CONTAINER_ROOT}/${ESERVICE}/conf >> eservice-openam-agent-config
echo AM_SERVER_URL= ${OPENAM_SERVER_URL_ESERVICE} >> eservice-openam-agent-config
echo CATALINA_HOME= ${CONTAINER_ROOT}/${ESERVICE} >> eservice-openam-agent-config
echo INSTALL_GLOBAL_WEB_XML= false >> eservice-openam-agent-config
echo AGENT_URL= ${ESERVICE_SITE_URL} >> eservice-openam-agent-config
echo AGENT_PROFILE_NAME= ${OPENAM_POLICY_AGENT_ESERVICE} >> eservice-openam-agent-config
echo AGENT_PASSWORD_FILE= ${OPENAM_POLICY_AGENT_PWD_FILE_ESERVICE} >> eservice-openam-agent-config

################################################################
# Install eservice agent
################################################################
./agentadmin --install --useResponse eservice-openam-agent-config <<< $'n\nyes\n'

################################################################
# Fix realm in eservice bootstrap config
################################################################
cd ../Agent_001/config/
mv OpenSSOAgentBootstrap.properties OpenSSOAgentBootstrap.properties.orig
sed -e "s/\(com\.sun\.identity\.agents\.config\.organization\.name\s=\s\/\).*$/\1${OPENAM_REALM_ESERVICE}/" OpenSSOAgentBootstrap.properties.orig > OpenSSOAgentBootstrap.properties

################################################################
# kservice - Agent install configuration
#
################################################################
cd  ${CONTAINER_ROOT}/j2ee_agents/kservice-tomcat_v6_agent/bin
echo # Agent User Response File > kservice-openam-agent-config
echo CONFIG_DIR= ${CONTAINER_ROOT}/${KSERVICE}/conf >> kservice-openam-agent-config
echo AM_SERVER_URL= ${OPENAM_SERVER_URL_KSERVICE} >> kservice-openam-agent-config
echo CATALINA_HOME= ${CONTAINER_ROOT}/${KSERVICE} >> kservice-openam-agent-config
echo INSTALL_GLOBAL_WEB_XML= false >> kservice-openam-agent-config
echo AGENT_URL= ${KSERVICE_SITE_URL} >> kservice-openam-agent-config
echo AGENT_PROFILE_NAME= ${OPENAM_POLICY_AGENT_KSERVICE} >> kservice-openam-agent-config
echo AGENT_PASSWORD_FILE= ${OPENAM_POLICY_AGENT_PWD_FILE_KSERVICE} >> kservice-openam-agent-config

################################################################
# Install kservice agent
################################################################
./agentadmin --install --useResponse kservice-openam-agent-config <<< $'n\nyes\n'

################################################################
# Fix realm in kservice bootstrap config
################################################################
cd ../Agent_001/config/
mv OpenSSOAgentBootstrap.properties OpenSSOAgentBootstrap.properties.orig
sed -e "s/\(com\.sun\.identity\.agents\.config\.organization\.name\s=\s\/\).*$/\1${OPENAM_REALM_KSERVICE}/" OpenSSOAgentBootstrap.properties.orig > OpenSSOAgentBootstrap.properties


popd

# prepare a directory for hippo jcr
mkdir ${CONTENT_ROOT}


