#!/bin/sh

BASEDIR=$(dirname $0)
echo $BASEDIR

if [ "$1" = "-html" ]
then
    echo "Generating HTML file"
    xsltproc -o $2.html $BASEDIR/xsl/pr_html.xsl $2.xml
else 
    echo "Generating FO file"
    xsltproc -o $2.fo   $BASEDIR/xsl/pr_fop.xsl  $2.xml
fi

if [ "$1" = "-pdf" ]
then
    echo "Generating PDF file"
    fop $2.fo $2.pdf
fi
