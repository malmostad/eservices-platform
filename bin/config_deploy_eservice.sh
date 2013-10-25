#!/bin/bash

# ROOT of build directory
BUILD_DIR=${HOME}/inherit-platform-gitclone/eservices-platform

# ROOT of directory holding the j2ee containers
CONTAINER_ROOT=${HOME}/inherit-platform

# ROOT of Hippo jcr content repository
CONTENT_ROOT=${CONTAINER_ROOT}/jcr-inherit-portal

# Name of container roots
BOS=BOS-5.9-Tomcat-6.0.35
ESERVICE=hippo-eservice-tomcat-6.0.36
KSERVICE=hippo-kservice-tomcat-6.0.36

ESERVICEPATCH=eservice.malmo.se
KSERVICEPATCH=kservice.malmo.se
PROPERTIES_LOCAL_BEFOREPATCH=properties-local.xml.beforepatch 

BOS_PORT=58080
ESERVICE_PORT=38080
KSERVICE_PORT=8080

WITH_KSERVICES=true
