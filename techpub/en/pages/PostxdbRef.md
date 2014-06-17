# Postxdb Get Started #

Postxdb is a drop-in replacement for the eXist XML database for use in combination with Orbeon forms. In that role it introduces form version management to Orbeon Forms by modifying form names. It needs a servlet container and a SQL database. It has been tested with the following software.


* Tomcat 6
* PostgreSQL 9.1

Postxdb responds to REST requests, but also comes with a bare-bones administration interface.

## What It Does ##

Postxdb has two main goals.


1. Introduce form version management in Orbeon Forms
2. Provide a useful data model and relational database persistence to Orbeon Forms

Orbeon Forms in itself has no forms version management. A form definition may be edited beyond recognition even if there is existing form data. With postxdb every modification of a form definition creates a new version of the form. Each *Save* in the editor steps a draft number. Each *Publish* publishes the form after removing the draft number. Publishing also creates the first draft of the next version.

Orbeon Forms has no idea about what is going on behind its back. See [#limitations Limitations and Gotchas] below.

Postxdb is implemented in Grails, Grails relies on Hibernate. This means that it may be configured to use a wide selection of database backends. At this point it has only been tested with PostgreSQL.

## Getting Started ##

You receive postxdb as a war file named `exist.war`. The file name is a reminder that postxdb replaces eXist.

Make sure Tomcat is configured to use a suitable port. Our environment uses port 48080.

Make sure a suitable Postgres database is available. Postxdb expects a database configuration file to tell it how to access the database. You may define a system property or an environment variable to contain the full path of a configuration file. They are examined in the following order. The first one that is defined is used.


1. A system property named `POSTXDB_CONF` (note the new spelling)
2. An environment variable named `POSTXDB_CONF`
3. A system property named `MOTRICE_CONF`
4. An environment variable named `MOTRICE_CONF`
5. The file `/usr/local/etc/motrice/motrice.properties` is used if none of the above is defined

The database configuration file looks like this. Replace items in braces with actual data. Uncomment the `logSql` property if you want to debug the generated SQL.

```
dataSource.driverClassName = org.postgresql.Driver
dataSource.url = jdbc:postgresql://{host}:{port}/{database name}
dataSource.username = {username}
dataSource.password = {password}
dataSource.dbCreate = update
#dataSource.logSql = true
```

Drop the war file into the `webapps` directory of a Tomcat installation. `bin/startup.sh` and you are on your way.

## Using the Admin Interface ##

Point your browser to `localhost:48080/exist` to see the start page of the admin interface. The two main entities are *Form* and *Form Version*. Both contain metadata only. There is a third entity type for contents. Use the *Browse Contents* link in the start page.

Very little effort has been spent on the admin interface, so don't expect too much.

## Limitations and Gotchas ## #limitations


* Postxdb uses no authentication
* After saving or publishing you have to manually refresh the list of editable forms (unless you open it again)
* The REST interface URL begins with `/exist/rest/db/orbeon-pe/fr`, hardwired
* DELETE is currently a no-op

## Working On postxdb Source Code ##

You need Grails at least version 2.2.4 for developing postxdb. This version fixed a problem with Maven integration that we need.

