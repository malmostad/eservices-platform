#!/bin/bash

# KEYSTORE_PATH=/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/cacerts
KEYSTORE_PATH=/etc/ssl/certs/java/cacerts

# Directory where the CA-certs are stored
CACERT_DIR=/home/rol/inherit/projects/malmo_pilot/tech/ssl-config/certrequest/certsMalmoBjornRobert/cacertsmalmo/pem

# Absolute path of file containing keystore password
PASSWD_PATH=/tmp/keytoolpwd.txt

if [[ ! -f $KEYSTORE_PATH ]]
then
  echo "Given keystore path does not exist"
  exit 1
fi

# De CA-cert som behövs för att autenticera mot openam-servern på eservicetest.malmo.se är
#         Malmö Stad Root CA 
# och 
#         Malmo Stad Issuing CA 02
if [[ ! -d $CACERT_DIR ]]
then
  echo "Given CA-cert dir does not exist"
  exit 1
elif  [[ ! -f $CACERT_DIR/malmostadrootca.pem ]]
then
  echo "Given CA-cert: $CACERT_DIR/malmostadrootca.pem does not exist"
  exit 1
elif [[ ! -f $CACERT_DIR/malmostadissuingca02.pem ]]
then
  echo "Given CA-cert: $CACERT_DIR/malmostadissuingca02.pem does not exist"
  exit 1
fi

if [[ ! -f $PASSWD_PATH ]]
then
  echo "Path to password file does not exist"
  exit 1
fi

echo "Sanity checks passed"

if keytool -list -storepass:file $PASSWD_PATH -keystore $KEYSTORE_PATH | grep malmostadrootca
then
  echo "CA-cert: malmostadrootca already installed"
else
  openssl x509 -in $CACERT_DIR/malmostadrootca.pem -inform pem -outform der | 
    keytool -importcert -trustcacerts -alias malmostadrootca -storepass:file $PASSWD_PATH -keystore $KEYSTORE_PATH  -noprompt 
fi

if keytool -list -storepass:file $PASSWD_PATH -keystore $KEYSTORE_PATH | grep malmostadissuingca02
then
  echo "CA-cert: malmostadissuingca02 already installed"
else
  openssl x509 -in $CACERT_DIR/malmostadissuingca02.pem -inform pem -outform der | 
    keytool -importcert -trustcacerts -alias malmostadissuingca02  -storepass:file $PASSWD_PATH -keystore $KEYSTORE_PATH -noprompt 
fi






