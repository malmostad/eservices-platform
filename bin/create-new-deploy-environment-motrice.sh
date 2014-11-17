#!/bin/bash

################################################################
# The great create deploy server config                        #
# Must be run from ${BUILD_DIR}/bin
#
################################################################

################################################################
# CONFIG                                                #
################################################################

###### current_config.sh  #####
# symlink to actual config of current installation
. current_config.sh
################################################################
# END OF CONFIG                                                #
################################################################

function checkPackage() {
RESPONSE=`apt-cache policy ${PACKAGE} | grep "Installed:" | cut -d : -f 2`
# RESPONSE=`whereis ${PACKAGE} | cut -d : -f 2`
if [ "${RESPONSE}" != "" ]
then
  echo "${PACKAGE} installed"
  return 
fi

  echo "${PACKAGE} not installed"
  exit 1 ;
}


################################################################
# Check some dependencies 
DEPENDENCYPACKAGE="postgresql apache2 git git-man openjdk-7-jre openjdk-7-jdk maven apache2 openssl ntp apache2 libapache2-mod-shib2 slapd ldap-utils docbook-xml xsltproc fop libxml2-utils xmlstarlet fonts-liberation fonts-opensymbol docbook-xsl curl"
for PACKAGE in ${DEPENDENCYPACKAGE}
do 
  checkPackage
done 




if ${ESERVICE_SSL}; then
   if [ -n "$ESERVICE_PORT" ]; then
     ESERVICE_SITE_URL=https://${ESERVICE_HOST}:${ESERVICE_PORT}/site
   else 
     ESERVICE_SITE_URL=https://${ESERVICE_HOST}:443/site
   fi
else
   if [ -n "$ESERVICE_PORT" ]; then
     ESERVICE_SITE_URL=http://${ESERVICE_HOST}:${ESERVICE_PORT}/site
   else 
     ESERVICE_SITE_URL=http://${ESERVICE_HOST}:80/site
   fi
fi

################################################################
# Check some requirements 
#    i.e. do not overwrite CONTAINER_ROOT
#         check that open am policy agent password file exists 
################################################################

if [ -d ${CONTAINER_ROOT} ];
then
   echo "Cannot create deploy environment at ${CONTAINER_ROOT}. Directory already exists."
   exit 0;
fi

mkdir -p ${CONTAINER_ROOT}

################################################################
# Download and extract to deployment environment               #
################################################################

# Work from temporary directory
TMP_DIR=${BUILD_DIR}/bin/tmp

if [ ! -d $TMP_DIR/downloads ]
then
  mkdir -p ${TMP_DIR}/downloads
fi

pushd ${TMP_DIR}

# download not already downloaded stuff 
if [ ! -f downloads/${TOMCAT_TGZ} ]; then
    response=$(curl --write-out %{http_code} --silent -o  downloads/${TOMCAT_TGZ}  ${TOMCAT_DOWNLOAD_URL})
    if [ "$response" = "404" ] ; 
    then
       echo "Failed to fetch ${TOMCAT_DOWNLOAD_URL}"
       echo "Maybe the version is not available and the config file needs to be updated."
       exit 1 
    fi
fi

tar xzf downloads/${TOMCAT_TGZ}
################################################################
# Hippo Tomcat config                                          #
################################################################
HIPPO_APPEND_COMMON_LOADER=
mv ${TOMCAT_DIR}/conf/catalina.properties ${TOMCAT_DIR}/conf/catalina.properties.orig
sed -e 's/\(common\.loader=\)\(.*\)$/\1\2,\${catalina.base}\/common\/classes,\${catalina.base}\/common\/lib\/*.jar/' -e 's/shared.loader=/shared.loader=${catalina.base}\/shared\/classes,${catalina.base}\/shared\/lib\/*.jar/g' ${TOMCAT_DIR}/conf/catalina.properties.orig > ${TOMCAT_DIR}/conf/catalina.properties
##   modification for shibboleth 
mv ${TOMCAT_DIR}/conf/server.xml ${TOMCAT_DIR}/conf/server.xml.orig
sed -e 's%protocol=\"AJP\/1.3\"\ redirectPort=\"8443\"%protocol=\"AJP\/1.3\" redirectPort=\"8443\"\ tomcatAuthentication=\"false\"\ packetSize=\"65536\"%g' ${TOMCAT_DIR}/conf/server.xml.orig > ${TOMCAT_DIR}/conf/server.xml


################################################################
# copy to eservice and kservice tomcats                        #
################################################################

cp -r  ${TOMCAT_DIR} ${CONTAINER_ROOT}/${ESERVICE}
cp ${BUILD_DIR}/conf/repository.xml ${CONTAINER_ROOT}/${ESERVICE}/conf/

################################################################
# Write kservice Tomcat setenv.sh due to Hippo and Motrice requirements 
################################################################

################################################################
# Write eservice Tomcat setenv.sh due to Hippo and Motrice requirements 
#   the diff is repo conf and policy agent conf
################################################################

##  use for KSERVICE and ESERVICE 
echo "export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64" > ${CONTAINER_ROOT}/${ESERVICE}/bin/setenv.sh
echo L4J_OPTS=\"-Dlog4j.configuration=file:\${CATALINA_BASE}/conf/log4j.xml\" >>  ${CONTAINER_ROOT}/${ESERVICE}/bin/setenv.sh
echo JVM_OPTS=\"-server -Xmx4096m -Xms2048m -XX:PermSize=512m\" >>  ${CONTAINER_ROOT}/${ESERVICE}/bin/setenv.sh
# echo JVM_OPTS=\"-server -Xmx2048m -Xms1024m -XX:PermSize=256m\" >>  ${CONTAINER_ROOT}/${ESERVICE}/bin/setenv.sh
echo CATALINA_OPTS=\"\$CATALINA_OPTS -Dfile.encoding=UTF-8 \${JVM_OPTS} \${L4J_OPTS} -XX:+HeapDumpOnOutOfMemoryError\" >>  ${CONTAINER_ROOT}/${ESERVICE}/bin/setenv.sh
echo export CATALINA_OPTS >>  ${CONTAINER_ROOT}/${ESERVICE}/bin/setenv.sh


popd
################################################################
# Install opendj
################################################################
if ${WITH_OPENDJ}
then
  pushd ${TMP_DIR}

  if [ ! -f downloads/${OPENDJ_ZIP} ]
  then
       curl -o downloads/${OPENDJ_ZIP} ${OPENDJ_URL}
  fi

  unzip -d $CONTAINER_ROOT downloads/${OPENDJ_ZIP} && cd $CONTAINER_ROOT/opendj

  ./setup --cli --propertiesFilePath $OPENDJ_SETUP_PROPERTIES --acceptLicense --no-prompt

  popd
fi

################################################################
# prepare a directory for hippo jcr
################################################################
mkdir -p ${CONTENT_ROOT}
