#
# Generate and compile Java for "Funktionstjänster"
# This version for the test environment (plain HTTP)
# NOTE: Must set JAVA_HOME to have access to trust store
#
$JAVA_HOME/bin/wsimport -p org.motrice.signatrice.cgi -s src/java/ -d target/classes/ http://grpt.funktionstjanster.se:18899/grp/v1?wsdl
