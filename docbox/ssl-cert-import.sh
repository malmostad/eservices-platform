#!/bin/sh

#
# Add a certificate from (for example) a web service to the Java trust store.
# sudo will ask your for your password.
#
# USAGE:
#
# NOTE: You MUST set JAVA_HOME
#
# ssl-cert-import.sh <host> <alias>
#
# The host usually consists of a domain and a port number.
#

HOST="$1"
ALIAS="$2"


TEMPROOT=`date '+/tmp/ssl-%y%m%d-%H%M%S'`
TEMPFILE1="$TEMPROOT.txt"
echo "Temp file 1: $TEMPFILE1"
TEMPFILE2="$TEMPROOT.crt"
echo "Temp file 2: $TEMPFILE2"

echo QUIT | openssl s_client -connect grpt.funktionstjanster.se:18898 -prexit 2>/dev/null > "$TEMPFILE1"
openssl x509 -in "$TEMPFILE1" -out "$TEMPFILE2"
sudo keytool -importcert -file "$TEMPFILE2" -alias "$ALIAS" -keystore "$JAVA_HOME/jre/lib/security/cacerts" -storepass changeit
