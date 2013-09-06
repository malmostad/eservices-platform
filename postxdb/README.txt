postxdb is a drop-in replacement for eXist-db persistence in Orbeon Forms.
PostgreSQL is currently used for storage, but thanks to Hibernate many other
databases can easily be used.

Unit tests will fail if you remove the xhtml forms from this directory.

mvn clean install
will produce target/exist.war ready to be deployed in a servlet container.
The name is intentionally chosen to replace the eXist-db counterpart.
