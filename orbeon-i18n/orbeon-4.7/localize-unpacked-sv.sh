#!/bin/bash -x

#
# Add Swedish to Orbeon Form Builder
# This version for Orbeon Forms 4.6 CE or earlier
#
# Usage: Run this script from here, no special privileges required
# Command line parameters:
# - Absolute path to the directory WEB-INF of the unpacked Orbeon war
#   It is usually ../../inherit-portal/orbeon/src/main/webapp/WEB-INF
#   but you MUST enter an absolute path
#
# The script modifies some of the jars in WEB-INF/lib
#
# ASSUMES all localized files are found in the sv directory.
#

WEB_INF_DIR=${1:-../../inherit-portal/orbeon/src/main/webapp/WEB-INF}
TMP_DIR=`date '+/tmp/orbeon-%Y%m%d-%H%M%S'`
LIB="$WEB_INF_DIR/lib"

RESOURCES_PRIVATE_JAR="orbeon-resources-private.jar"
RESOURCES_PRIVATE_JAR_ORIG="$LIB/$RESOURCES_PRIVATE_JAR"
SWEDISH_JAR="orbeon-resources-private-4.7-lang-sv-added.tgz"
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
if [ ! -f "$RESOURCES_PRIVATE_JAR_ORIG" ]; then
    echo "Could not find $RESOURCES_PRIVATE_JAR_ORIG"
    exit 1
fi

# Create a temp directory, copy files there
# Files in the tgt directory will be modified
# Those in the orig directory will remain unchanged
mkdir "$TMP_DIR" "$TARGET_DIR" "$ORIG_DIR"
cp "sv/$SWEDISH_JAR" "$TMP_DIR"
cp "$RESOURCES_PRIVATE_JAR_ORIG" "$ORIG_DIR"
cp "$RESOURCES_PRIVATE_JAR_ORIG" "$TARGET_DIR"

# Unpack the source jar
( cd "$TMP_DIR" ; tar zxf "$SWEDISH_JAR" )

# Replacement in orbeon-form-builder.jar
echo "--- Replacement in `basename $RESOURCES_PRIVATE_JAR`"
( cd "$TARGET_DIR" ; \
  jar ufv "$RESOURCES_PRIVATE_JAR" -C "$TMP_DIR" xbl )

# Copy the updated jars back to their original location
# A backup copy is made to the 'orig' subdirectory
cp "$TARGET_DIR"/"$RESOURCES_PRIVATE_JAR" "$RESOURCES_PRIVATE_JAR_ORIG"

echo "--- Localization finished"
echo "--- Original jars in $ORIG_DIR"
echo "--- Jars have been replaced in $LIB"
