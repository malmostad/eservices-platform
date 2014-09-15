#
# Generate and compile Java for "Funktionstjänster"
# This version for the test environment (plain HTTP)
#
wsimport -p org.motrice.signatrice.cgi -s src/java/ -d target/classes/ http://grpt.funktionstjanster.se:18899/grp/v1?wsdl
