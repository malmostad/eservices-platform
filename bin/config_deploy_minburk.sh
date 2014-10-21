#!/bin/bash
# Setting of maven test flag
MVN_SKIP_TEST=true
MVN_SKIP_TEST=false

# ROOT of build directory
#BUILD_DIR=${HOME}/workspaces/motrice/pawap
BUILD_DIR=${HOME}/workspaces/inheritsource-develop/pawap

# ROOT of directory holding the j2ee containers
CONTAINER_ROOT=${HOME}/motrice-0.6

# ROOT of Hippo jcr content repository
CONTENT_ROOT=${CONTAINER_ROOT}/jcr-inherit-portal

TOMCAT_DIR=apache-tomcat-7.0.56
TOMCAT_TGZ=${TOMCAT_DIR}.tar.gz
TOMCAT_DOWNLOAD_URL=http://apache.mirrors.spacedump.net/tomcat/tomcat-7/v7.0.56/bin/${TOMCAT_TGZ}

#####################################################################
# Container config 
#####################################################################

ESERVICE=hippo-eservice-tomcat        # PATH in CONTAINER_ROOT
ESERVICE_SSL=true                     # true/false
ESERVICE_HOST=eminburk.malmo.se       # DNS name
ESERVICE_EXTERNAL_PORT=443            # external port normally 80 or 443


ESERVICEPATCH=${ESERVICE_HOST}

ESERVICE_PORT=8080                   # internal port i.e. tomcat port

WITH_OPENDJ=false

OPENDJ_SETUP_PROPERTIES=/usr/local/etc/motrice/opendj_setup.properties
