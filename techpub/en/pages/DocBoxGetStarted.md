# DocBox Get Started #

The service comes as a war file, *docbox.war*, ready to be dropped into a servlet container. Before you can do so successfully, a few other things have to be in place.

As for DocBox functionality, see DocBox.

## Required Ubuntu Packages ##

The following Ubuntu packages have to be installed.

XML-related packages:

* docbook-xml
* docbook-xsl
* xsltproc
* fop
* libxml2-utils
* xmlstarlet

Font packages:

* fonts-liberation
* fonts-opensymbol

## Dependency to Postxdb ##

DocBox reads form data from the database created by Postxdb.

## Database Configuration ##

DocBox works against a relational database. PostgreSQL is the current main candidate. DocBox has two datasources,

* The default datasource where PDF documents are stored
* The forms datasource, equivalent to read-only access to a postxdb database.

Make sure a suitable Postgres database is available. Docbox expects a database configuration file to tell it how to access the database. You may define a system property or an environment variable to contain the full path of a configuration file. They are examined in the following order. The first one that is defined is used.


1. A system property named `DOCBOX_CONF` (note the new spelling)
2. An environment variable named `DOCBOX_CONF`
3. A system property named `MOTRICE_CONF`
4. An environment variable named `MOTRICE_CONF`
5. The file `/usr/local/etc/motrice/motrice.properties` is used if none of the above is defined

Here is a sample configuration file. Replace items in braces with actual data. Uncomment the `logSql` properties if you want to debug the generated SQL.

```
#
# Docbox: convert forms to PDF/A, insert signature
#
# Document datasource (read/write)
#
dataSource.driverClassName = org.postgresql.Driver
dataSource.url = jdbc:postgresql://${host}:${port}/${database name}
dataSource.username = ${username}
dataSource.password = ${password}
dataSource.dbCreate = update
#dataSource.logSql = true
#
# Forms datasource (read-only)
#
dataSource_forms.driverClassName = org.postgresql.Driver
dataSource_forms.url = jdbc:postgresql://${host}:${port}/${database name}
dataSource_forms.username = ${username}
dataSource_forms.password = ${password}
dataSource_forms.dbCreate = validate
dataSource_forms.readOnly = true
#dataSource_forms.logSql = true

# Strict check that signed text contains the document number and the checksum?
# Lenient if false, otherwise strict.
docbox.signed.text.check.strict = false

# Base url used in QR code. A docboxRef is appended for a complete url.
# The QR code is not generated if this property is empty.
docbox.signed.doc.base.url = https://eservices.malmo.se/site/public/mycases/signform/view
```

## Tomcat Config ##

Make sure Tomcat is configured to use a suitable port for DocBox. Drop the war file into the `webapps` directory. `bin/startup.sh` and you are on your way.

## Validate Installation ##

Use the following URL to validate the DocBox installation.

```
http://<host>:<port>/docbox/env/validate
```

This operation is mainly for m2m use, so the output is JSON.

