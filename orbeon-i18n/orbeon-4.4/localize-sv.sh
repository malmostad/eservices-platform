#!/usr/bin/env bash

#
# Add Swedish to Orbeon Forms
# This version for Orbeon Forms 4.4 CE
#
# Usage: Run this script from here, no special privileges required
# Command line parameters:
# - full path to orbeon.war
#
# The script prints the path where you may find the modified orbeon.war
#
# ASSUMES all localized files are found in sv/orbeon-fr-swedish.jar
# which is built from the source code tree.
# Its structure is a little bit different from the Orbeon jars.
#

ORIG_WAR="$1"
TMP_DIR=`date '+/tmp/orbeon-%Y%m%d-%H%M%S'`
LIB="WEB-INF/lib"

COPY_WAR="$TMP_DIR/$ORBEON_WAR"
FORM_RUNNER_JAR="$LIB/orbeon-form-runner.jar"
ORBEON_WAR=orbeon.war
PRIVATE_RESOURCES_JAR="$LIB/orbeon-resources-private.jar"
PUBLIC_RESOURCES_JAR="$LIB/orbeon-resources-public.jar"
SHA1FILE="orbeon.war.sha1"
SWEDISH_JAR="orbeon-fr-swedish.jar"
SWEDISH_ORBEON_WAR=orbeon-sv.war
TARGET_DIR="$TMP_DIR/tgt"

if [ ! -f "$ORIG_WAR" ]; then
    echo "The file '$ORIG_WAR' (Orbeon main war) does not exist"
    exit 1
fi
if [ -d "$TMP_DIR" ]; then
    echo "Temp dir '$TMP_DIR' already exists"
    exit 1
fi

# Create a temp directory, copy files there
mkdir "$TMP_DIR" "$TARGET_DIR"
echo "Temp dir: $TMP_DIR"
cp "$ORIG_WAR" "$COPY_WAR"
cp "$SHA1FILE" "$TMP_DIR"
cp "sv/$SWEDISH_JAR" "$TMP_DIR"

# Check that we have the expected input
( cd "$TMP_DIR" ; \
  sha1sum --check "$SHA1FILE" --status || echo "Unexpected input file '$ORIG_WAR'" && exit 1 )
# The files to modify are jars within the big jar
( cd "$TARGET_DIR" ; \
  jar xf "../$ORBEON_WAR" "$FORM_RUNNER_JAR" "$PRIVATE_RESOURCES_JAR" "$PUBLIC_RESOURCES_JAR" )
# Unpack the source jar
( cd "$TMP_DIR" ; jar xf "$SWEDISH_JAR" )

# Replacement in orbeon-form-runner.jar
echo "--- Replacement in $FORM_RUNNER_JAR"
( cd "$TARGET_DIR" ; \
  jar ufv "$FORM_RUNNER_JAR" -C "$TMP_DIR/src/resources" apps )
# Replacement in orbeon-resources-private.jar
echo "--- Replacement in $PRIVATE_RESOURCES_JAR"
( cd "$TARGET_DIR" ; \
  jar ufv "$PRIVATE_RESOURCES_JAR" -C "$TMP_DIR/src/resources-packaged" xbl )
# Replacement in orbeon-resources-public.jar
echo "--- Replacement in $PUBLIC_RESOURCES_JAR"
( cd "$TARGET_DIR" ; \
  jar ufv "$PUBLIC_RESOURCES_JAR" -C "$TMP_DIR/src/resources-packaged" ops )

# Replacement in the main orbeon.war
echo "--- Replacement in $ORBEON_WAR"
( cd "$TMP_DIR" ; \
  jar ufv "$ORBEON_WAR" \
    -C "$TARGET_DIR" "$FORM_RUNNER_JAR" \
    -C "$TARGET_DIR" "$PRIVATE_RESOURCES_JAR" \
    -C "$TARGET_DIR" "$PUBLIC_RESOURCES_JAR" )

echo "--- Localization finished"
echo "--- The result is $TMP_DIR/$ORBEON_WAR"
