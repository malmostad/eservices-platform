#!/bin/bash

# ROOT of build directory
BUILD_DIR=${HOME}/workspaces/motrice/pawap

# ROOT of directory holding the j2ee containers
CONTAINER_ROOT=${HOME}/motriceserver

# ROOT of Hippo jcr content repository
CONTENT_ROOT=${CONTAINER_ROOT}/jcr-inherit-portal

# Name of container roots
BOS=BOS-5.9-Tomcat-6.0.35
ESERVICE=hippo-eservice-tomcat-7.0.42
KSERVICE=hippo-kservice-tomcat-7.0.42

ESERVICEPATCH=eminburk.malmo.se
KSERVICEPATCH=kminburk.malmo.se
PROPERTIES_LOCAL_BEFOREPATCH=properties-local.xml.beforepatch 

BOS_PORT=58080
ESERVICE_PORT=38080
KSERVICE_PORT=8080

WITH_KSERVICES=true
