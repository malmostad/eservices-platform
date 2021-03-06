#!/bin/bash
# Setting of maven test flag
MVN_SKIP_TEST=false

# ROOT of build directory
BUILD_DIR=${HOME}/inherit-platform-gitclone/eservices-platform

# ROOT of directory holding the j2ee containers
CONTAINER_ROOT=${HOME}/motrice-0.6

# ROOT of Hippo jcr content repository
CONTENT_ROOT=${CONTAINER_ROOT}/jcr-inherit-portal

TOMCAT_DIR=apache-tomcat-7.0.54
TOMCAT_TGZ=${TOMCAT_DIR}.tar.gz
TOMCAT_DOWNLOAD_URL=http://apache.mirrors.spacedump.net/tomcat/tomcat-7/v7.0.54/bin/${TOMCAT_TGZ}

#####################################################################
# Container config 
#####################################################################

ESERVICE=hippo-eservice-tomcat        # PATH in CONTAINER_ROOT
ESERVICE_SSL=true                     # true/false
ESERVICE_HOST=eservice.malmo.se       # DNS name
ESERVICE_EXTERNAL_PORT=443                     # external port normally 80 or 443

KSERVICE=hippo-kservice-tomcat        # PATH in CONTAINER_ROOT
KSERVICE_SSL=true                     # true/false
KSERVICE_HOST=kservice.malmo.se       # DNS name
KSERVICE_EXTERNAL_PORT=443                     # external port normally 80 or 443

CMSSERVICE=hippo-cmsservice-tomcat    # PATH in CONTAINER_ROOT
CMSSERVICE_SSL=false                  #
CMSSERVICE_HOST=kservice.malmo.se     # DNS name
CMSSERVICE_EXTERNAL_PORT=80                    # external port normally 80 or 443

ESERVICEPATCH=${ESERVICE_HOST}
KSERVICEPATCH=${KSERVICE_HOST}

ESERVICE_PORT=38080                   # internal port i.e. tomcat port
KSERVICE_PORT=8080                    # internal port i.e. tomcat port
CMSSERVICE_PORT=48080                 # internal port i.e. tomcat port

WITH_KSERVICES=true
WITH_CMSSERVICES=true

#####################################################################
# Open AM config
#####################################################################
FORGEROCK_POLICY_AGENT_ZIP=tomcat_v6_agent_3.1.0-Xpress.zip
FORGEROCK_POLICY_AGENT_URL=file:///home/inherit/download/${FORGEROCK_POLICY_AGENT_ZIP}

# OpenAM EService config
OPENAM_POLICY_AGENT_PWD_FILE_ESERVICE=~/inherit-platform-gitclone/pwd.txt
OPENAM_SERVER_URL_ESERVICE=https://eservicetest.malmo.se:443/openam
OPENAM_POLICY_AGENT_ESERVICE=eserviceTestAgent
OPENAM_REALM_ESERVICE=medbrealm

# OpenAM KService config
OPENAM_POLICY_AGENT_PWD_FILE_KSERVICE=~/tmp/pwd.txt
OPENAM_SERVER_URL_KSERVICE=https://eservicetest.malmo.se:443/openam
OPENAM_POLICY_AGENT_KSERVICE=kserviceTestAgent
OPENAM_REALM_KSERVICE=kominrealm
