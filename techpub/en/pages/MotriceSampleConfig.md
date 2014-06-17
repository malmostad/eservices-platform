# Motrice Sample Configuration #

Motrice uses a properties file for configuration. The location of the configuration file is taken from an environment variable named `MOTRICE_CONF`. If the environment variable is not defined the file `/usr/local/etc/motrice/motrice.properties` is used.

A sample configuration file follows. Latest version - git conf/motrice.properties

It assumes PostgreSQL is used for storage. Replace items in curly braces with the appropriate data.

```
#
# Motrice configuration
#
dataSource.driverClassName = org.postgresql.Driver
dataSource.url = jdbc:postgresql://{host}:{database port}/{database name}
dataSource.username = {username}
dataSource.password = {password}
dataSource.dbCreate = update
#dataSource.logSql = true

# Forms database (read-only for Docbox)
dataSource_forms.driverClassName = org.postgresql.Driver
dataSource_forms.url = jdbc:postgresql://{host}:{database port}/{database name}
dataSource_forms.username = {username}
dataSource_forms.password = {password}
dataSource_forms.dbCreate = validate
dataSource_forms.readOnly = true

# Coordinatrice: Base uri for Orbeon connection
coordinatrice.orbeon.builder.base.uri = http://{host}:{Orbeon port}/orbeon/fr/orbeon/builder
# Migration: Where to find the local postxdb instance
# The property name reflects the old tool name, to be changed 
migratrice.postxdb.uri = http://{host}:{postxdb port}/exist

# Docbox:
# Strict check that signed text contains the document number and the checksum?
# Lenient if false, otherwise strict.
docbox.signed.text.check.strict = false
# Base url used in QR code. A docboxRef is appended for a complete url.
# The QR code is not generated if this property is empty.
docbox.signed.doc.base.url = https://eservices.malmo.se/site/public/mycases/signform/view

# Name of this Motrice instance
# Must be universally unique. No spaces.
motrice.site.name = {informative-name}@{organization}.se

# Motrice mail server config
mail.smtp.host=mailrelay1.bredband.net
mail.smtp.port=25    

# Hippo site config
# NOTE: The trailing slash is required
orbeon.base.uri=http://localhost:8080/orbeon/fr/
orbeon.base.dataUri=http://orbeon:orb@localhost:8080/exist/rest/db/orbeon-pe/fr/

# Example (local) user directory config
# Comment out the *baseDn entries to turn off the service
userDirectoryService.host = localhost
userDirectoryService.port = 1389
userDirectoryService.protocol = ldap
userDirectoryService.pwd = {password}
userDirectoryService.securityPrincipal = {security principal}
userDirectoryService.queryBaseDn # OU#Personal,OU#Organisation,OU#Malmo
userDirectoryService.baseDn # dc#adm, dc#malmo, dc#se
userDirectoryService.keystorePwd={keystorepwd}
userDirectoryService.userBaseDn # OU#Personal,OU#Organisation,OU#Malmo
userDirectoryService.groupBaseDn # ou#eserviceRoller,OU#074 Milj\u00f6f\u00f6rvaltningen,OU#IDMGroups,ou#Organisation,ou#Malmo

```

