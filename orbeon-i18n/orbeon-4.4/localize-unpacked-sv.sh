#!/bin/bash

#
# Add Swedish to Orbeon Forms
# This version for Orbeon Forms 4.4 CE
#
# Usage: Run this script from here, no special privileges required
# Command line parameters:
# - Absolute path to the directory WEB-INF of the unpacked Orbeon war
#   It is usually ../../inherit-portal/orbeon/src/main/webapp/WEB-INF
#   but you must enter an absolute path
#
# The script modifies some of the jars in WEB-INF/lib
#
# ASSUMES all localized files are found in sv/orbeon-fr-swedish.jar
# which was built from the source code tree.
# Its structure is a little bit different from the Orbeon jars.
#

WEB_INF_DIR=${1:-../../inherit-portal/orbeon/src/main/webapp/WEB-INF}
TMP_DIR=`date '+/tmp/orbeon-%Y%m%d-%H%M%S'`
LIB="$WEB_INF_DIR/lib"

FORM_RUNNER_JAR="$LIB/orbeon-form-runner.jar"
PRIVATE_RESOURCES_JAR="$LIB/orbeon-resources-private.jar"
PUBLIC_RESOURCES_JAR="$LIB/orbeon-resources-public.jar"
SWEDISH_JAR="orbeon-fr-swedish.jar"
ORIG_DIR="$TMP_DIR/orig"
TARGET_DIR="$TMP_DIR/tgt"

if [ ! -d "$WEB_INF_DIR" ]; then
    echo "The directory '$WEB_INF_DIR' does not exist"
    exit 1
fi
if [ -d "$TMP_DIR" ]; then
    echo "Temp dir '$TMP_DIR' already exists"
    exit 1
fi

# Check that the input looks like Orbeon's lib directory
JARCOUNT=`ls $LIB/*.jar | wc -l`
if (( $JARCOUNT < 50 )); then
    echo "Is this really the Orbeon WEB_INF/lib directory?"
    exit 1
fi

# Check that the files to modify exist
if [ ! -f "$FORM_RUNNER_JAR" ]; then
    echo "Could not find $FORM_RUNNER_JAR"
    exit 1
fi
if [ ! -f "$PRIVATE_RESOURCES_JAR" ]; then
    echo "Could not find $PRIVATE_RESOURCES_JAR"
    exit 1
fi
if [ ! -f "$PUBLIC_RESOURCES_JAR" ]; then
    echo "Could not find $PUBLIC_RESOURCES_JAR"
    exit 1
fi

# Create a temp directory, copy files there
# Files in the tgt directory will be modified
# Those in the orig directory will remain unchanged
mkdir "$TMP_DIR" "$TARGET_DIR" "$ORIG_DIR"
cp "sv/$SWEDISH_JAR" "$TMP_DIR"
cp "$FORM_RUNNER_JAR" "$ORIG_DIR"
cp "$FORM_RUNNER_JAR" "$TARGET_DIR"
cp "$PRIVATE_RESOURCES_JAR" "$ORIG_DIR"
cp "$PRIVATE_RESOURCES_JAR" "$TARGET_DIR"
cp "$PUBLIC_RESOURCES_JAR" "$ORIG_DIR"
cp "$PUBLIC_RESOURCES_JAR" "$TARGET_DIR"

# Unpack the source jar
( cd "$TMP_DIR" ; jar xf "$SWEDISH_JAR" )

# Replacement in orbeon-form-runner.jar
echo "--- Replacement in `basename $FORM_RUNNER_JAR`"
( cd "$TARGET_DIR" ; \
  jar ufv "$FORM_RUNNER_JAR" -C "$TMP_DIR/src/resources" apps )
# Replacement in orbeon-resources-private.jar
echo "--- Replacement in `basename $PRIVATE_RESOURCES_JAR`"
( cd "$TARGET_DIR" ; \
  jar ufv "$PRIVATE_RESOURCES_JAR" -C "$TMP_DIR/src/resources-packaged" xbl )
# Replacement in orbeon-resources-public.jar
echo "--- Replacement in `basename $PUBLIC_RESOURCES_JAR`"
( cd "$TARGET_DIR" ; \
  jar ufv "$PUBLIC_RESOURCES_JAR" -C "$TMP_DIR/src/resources-packaged" ops )

# Copy the updated jars back to their original location
# A backup copy is made to the 'orig' subdirectory
cp "$TARGET_DIR"/orbeon-form-runner.jar "$FORM_RUNNER_JAR"
cp "$TARGET_DIR"/orbeon-resources-private.jar "$PRIVATE_RESOURCES_JAR"
cp "$TARGET_DIR"/orbeon-resources-public.jar "$PUBLIC_RESOURCES_JAR"

echo "--- Localization finished"
echo "--- Original jars in $ORIG_DIR"
echo "--- Jars have been replaced in $LIB"
