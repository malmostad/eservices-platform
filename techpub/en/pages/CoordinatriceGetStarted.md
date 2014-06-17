# Coordinatrice Get Started #

Coordinatrice is a web-based tool for managing BPMN processes and their forms. Configuration details are found near the end of this page.

The migration parts of Coordinatrice are [documented here](MigratriceGetStarted.md).

## Internationalization (I18n) ##

Motrice is designed to operate in an environment where several languages must be offered to users. Coordinatrice provides the following features to help support internationalization.


* Start form labels
* Activity labels
* Guide URLs

The labels are short pieces of text that serve as headings for start forms and activities, respectively. Guides, in this case, are pieces of text or arbitrary media intended as end user guides. The guides are served by the CRM Motrice component. Coordinatrice to configure and manage guide URL patterns.

### I18n Basics ###

A couple of concepts are used for identifying BPMN processes.


* **Process id**  
 Uniquely identifies a single process version
* **Process key**  
 A name common to all versions of a process
* **Version number**  
 Process version numbers are integers. The first version is 1 (one).

I18n in Coordinatrice is based on process keys, i.e. it is basically versionless. It is not necessary to configure i18n just because you create a new process version.

On the other hand, sometimes things do change between versions. In order to support those cases all i18n items contain a version number. The version number should be understood this way,


*As from this process (or form) version a different definition should be used*

By default the version number is 0 (zero). The effect is that the definition will be applied to all process versions unless overridden.

Assume, for example, that from process version 4 a slightly different label should be used. To achieve this, duplicate the label and specify 4 in the version number field of one of the labels. A label or guide URL may have an arbitrary number of version-dependent redefinitions.

### Guide URL Patterns ###

A single BPMN process may require a considerable number of guides. There may be a guide for every activity of the process. That number is multiplied by the number of supported languages.

With this background, guide URLs are not specified individually but as a pattern. A process basically has a single guide URL pattern. Placeholders in the pattern define the variable parts. The following placeholders are recognized by Coordinatrice.


* **%H**  
 The root URL defined in the Coordinatrice configuration
* **%P**  
 BPMN process definition key, a name without version number
* **%V**  
 Process definition version (not commonly used)
* **%A**  
 Activity name as defined in the BPMN XML definition
* **%L**  
 Locale

When a guide URL is needed the placeholders are replaced by actual text. The text is URL-encoded, except for %H. Characters from the pattern itself are not URL-encoded.

Example: URL pattern `http://allguides/%P/%A/%L.html` and the following input,

* Process key: `TestFunctionProcess1`
* Activity name: `Komplettera`
* Locale: `en`

The resulting guide URL becomes

```
http://allguides/TestFunctionProcess1/Komplettera/en.html
```

A pattern may contain **conditional text**. This is useful to cover cases where, for instance, the activity name may be omitted. In the example above the resulting URL contains ugly double slashes if the activity name is omitted.

Conditional text has the form `%?x{if|else}` where 'x' is one of the placeholder letters, 'if' and 'else' is arbitrary text. If text for the placeholder is present the result is the 'if' text. If text for the placeholder is omitted the result is the 'else' text. The vertical bar (|) may be omitted if there is no 'else' text. Note that the conditional construct only inserts fixed text. Placeholders may not occur in the replacement text.

Example: `http://allguides/%P%?A{/}%A/%L.html`, a slight modification of the example above. Omitting the activity, but otherwise with the same input as above the result is,

```
http://allguides/TestFunctionProcess1/en.html
```
because the conditional construct `%?A{/}` means that a slash is inserted only if an activity name is supplied.

## Process Categories ##

The Activiti BPMN process engine supports process categories. There is only one built-in process category: `http://www.activiti.org/test`. A category may look like a URL, but any string is valid.

Conventions for the use of process categories have not yet been established. Coordinatrice allows you to define categories and to assign them to processes.

## Database Configuration ##

Coordinatrice works with the Motrice database. It reads the Pxd tables, reads and may write Mtf tables. Coordinatrice also reads from the process engine.

Coordinatrice expects a configuration file to tell it how to access the database and Orbeon Builder. You may define a system property or an environment variable to contain the full path of a configuration file. They are examined in the following order. The first one that is defined is used.


1. A system property named `COORDINATRICE_CONF` (note the new spelling)
2. An environment variable named `COORDINATRICE_CONF`
3. A system property named `MOTRICE_CONF`
4. An environment variable named `MOTRICE_CONF`
5. The file `/usr/local/etc/motrice/motrice.properties` is used if none of the above is defined

Here is a sample configuration file. Replace items in braces with actual data. The usernames are Motrice defaults. Replace if necessary. Note the properties for cooperating with Orbeon Builder and with the local postxdb instance.

```
#
# coordinatrice -- tying processes and activities to forms
#
dataSource.driverClassName = org.postgresql.Driver
dataSource.url = jdbc:postgresql://[host]:[port]/motricedb
dataSource.username = motriceuser
dataSource.password = [password]
dataSource.dbCreate = validate
#dataSource.logSql = true

# Define the name of this Motrice instance.
# Part of the generic Motrice configuration, this is a reminder.
motrice.site.name = {informative-name}@{organization}.se

# Base uri for Orbeon connection
coordinatrice.orbeon.builder.base.uri = http://localhost:8080/orbeon/fr/orbeon/builder

# Base uri for guide URLs (available as the %H placeholder)
coordinatrice.guide.base.uri = http://localhost:8080/site

# Migration in Coordinatrice needs to know where to find the local
# postxdb instance. Legacy property name.
migratrice.postxdb.uri = http://localhost:48080/exist
```

## Tomcat Config ##

Make sure Tomcat is configured to use a suitable port for Coordinatrice. Drop the war file into the `webapps` directory. `bin/startup.sh` and you are on your way.

