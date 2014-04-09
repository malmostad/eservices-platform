#!/bin/bash

# ROOT of build directory
BUILD_DIR=${HOME}/workspaces/motrice/pawap

# ROOT of directory holding the j2ee containers
CONTAINER_ROOT=${HOME}/motrice-14.4

# ROOT of Hippo jcr content repository
CONTENT_ROOT=${CONTAINER_ROOT}/jcr-inherit-portal

TOMCAT_DIR=apache-tomcat-7.0.53
TOMCAT_TGZ=${TOMCAT_DIR}.tar.gz
TOMCAT_DOWNLOAD_URL=http://apache.mirrors.spacedump.net/tomcat/tomcat-7/v7.0.53/bin/${TOMCAT_TGZ}

FORGEROCK_POLICY_AGENT_ZIP=tomcat_v6_agent_3.1.0-Xpress.zip
FORGEROCK_POLICY_AGENT_URL=file:///home/bjmo/H%C3%A4mtningar/${FORGEROCK_POLICY_AGENT_ZIP}
OPENAM_POLICY_AGENT_PWD_FILE=~/tmp/pwd.txt

# Name of container roots
BOS=BOS-5.9-Tomcat-6.0.35
ESERVICE=hippo-eservice-tomcat
KSERVICE=hippo-kservice-tomcat

ESERVICEPATCH=eminburk.malmo.se
KSERVICEPATCH=kminburk.malmo.se
PROPERTIES_LOCAL_BEFOREPATCH=properties-local.xml.beforepatch 

BOS_PORT=58080
ESERVICE_PORT=38080
KSERVICE_PORT=8080

WITH_KSERVICES=true
