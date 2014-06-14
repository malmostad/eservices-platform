# Orbeon and PostgreSQL Persistence #

Abbreviations:


*PostgreSQL:* An open source relational database management system.


*RDBMS:* A relational database management system (in contrast to XML databases). Examples: Oracle, MySQL, PostgreSQL.


*XML-db:* A native XML database management system, mainly eXist-db.

## Objective ##

This document is intended to clarify design decisions for introducing PostgreSQL persistence in Orbeon Forms. Orbeon Forms comes with slightly incomplete MySQL support in its community edition, and with full Oracle support in the professional edition.

## Application Environment ##

Orbeon Forms is targeted as a key component of an application with many concurrent users. The forms database created by the Orbeon persistence layer will be used by other (non-Orbeon) components for managing the forms library.

## Executive Summary ##

The main issues to solve when integrating PostgreSQL with Orbeon Forms are,


1. The RDBMS interface
2. Semantic issues
3. Performance

A deeper look into these issues leads to the conclusion that the current Orbeon RDBMS interface must be redesigned to be a viable solution. The current design has intrinsic problems, semantic problems, and substantial performance problems.

If a redesign is unfeasible we propose a drop-in replacement for the current XML-db backed by PostgreSQL. This solution avoids the interface issues and may work around the semantic issues. Unfortunately it does not immediately solve all performance issues.

## Intrinsic RDBMS Interface Issues ##

Orbeon Forms exposes a simple RESTful interface for database operations. A number of persistence engines are supported. Changing from an XML-db to a supported RDBMS is a matter of configuration. However, adding a new RDBMS cannot be done without changes to the Orbeon source code and recompilation. The choices are hardwired.

### Persistence Pipeline Definition ###

Each persistence engine is represented by a collection of `.xpl` (pipeline definition) files. There is one file per REST operation, except that the crud operations (create, retrieve, update, delete) can be combined into a single file.

A pipeline definition controls how a chain of processors are connected to solve a specific problem. On this overview level the architecture is easy to understand.

As just mentioned, each RDBMS has its own set of pipeline definitions. Their task is, given a REST url, to generate SQL for a database query as specified by the url. There are small differences between the various RDBMS brands leading to slightly different SQL generated. Although the REST operations are extremely basic, semantic issues get in the way. The generated SQL is convoluted. Complex SQL for very straightforward operations is a warning signal that there is a problem with the database conceptual model. More about this in the Semantic Issues section.

### Database Query Execution ###

After being generated the SQL query is handed to a *SQLProcessor*, the heart of RDBMS pipelines. At this point the Java call stack is over 400 stack frames deep, which is a lot. This is another warning signal. Heavyweight machinery is used for a trivial task. More about this in the Performance Issues section.

So far we see that adding another RDBMS means adding a number of `.xpl` files that control SQL generation. SQL is created by means of XML transformations, not Java or Scala. The database query is like generating a batch job. Only *SQLProcessor* has direct contact with the database. Database connections are managed and pooled by the servlet engine.

### The Result Interpreter ###

The response from the RDBMS is processed by a result interpreter. This is questionable architecture. The database query is known, and hence also the nature of the result. The result interpreter is completely detached from query generation. It processes each response with zero knowledge of its origin. In order to cope with this situation the interpreter has a number of hardwired assumptions on datatypes, among other things. In the case of PostgreSQL it is not possible to use datatypes natural to this RDBMS because of the result interpreter. There is an interface called *DatabaseDelegate* presumably intended to bridge the differences between various RDBMS. We implemented a complete database delegate for PostgreSQL and found that none of its methods are ever called.

### RDBMS Interface Conclusions ###

By means of `.xpl` files an integrator controls the SQL generated for a persistence engine. However, tacit assumptions built into the result interpreter limit design choices for PostgreSQL persistence. The almost trivial conversion of a RESTful url to a database query is run by heavyweight machinery for every database request even though queries are picked from a handful of canned SQL templates with placeholders. A cache softens the performance hit if an object is requested repeatedly.

## Semantic Issues ##

Persistence in Orbeon Forms brings up a number of semantic issues,


* Save/Publish semantics
* Version management
* Ownership
* Language

### Save/Publish Semantics ###

Being able to edit form definitions without having them published is a useful feature offered by Orbeon Forms. The idea would be that only published form definitions are generally accessible.

The difference between a saved and a published Orbeon form definition is a matter of state. One may imagine a *published* flag that is set when a form definition is published. However, no such flag is implemented. Setting a flag would affect the REST interface. Probably the PUT operation would take a parameter, `published#false` or `published#true`.

Unfortunately the solution taken by Orbeon Form Builder is to store the form definition as form data under the *orbeon/builder* app/form names. This means that *orbeon/builder* are magic names. There is nowhere to store metadata (not even the true app/form names) because the form definition is stored in a foreign context. Without easily accessible metadata database searches must use XML queries to extract basic facts from form definitions.

When the form is published it is stored under its normal app/form names. If a form definition has a logo it will remain in the *orbeon/builder* hierarchy indefinitely, even when the form definition is published.

The problem here is that even though data is highly structured there is a mixup of form definitions and form data in the database. The mixup causes database queries to become unnecessarily complex. This complexity propagates to all uses of the database.

### Version Management ###

A form definition may be edited and stored back to the database. This is quite natural. Using an XML-db the edited definition simply replaces the original. The available RDBMS adaptions store each modified version with a timestamp, a kind of version management. The various versions are never really deleted, only a *deleted* flag is set.

A minor issue with storing versions based on a timestamp is that the *created* and *updated* timestamps are always equal. From a user experience point of view, the last version appears to be the only version that ever existed. In MySQL the timestamp resolution is second. As a result the database easily contains items with identical identifiers. Version selection is random between such items.

The existing user interface provides no way to select a version of a form definition. The latest version is always used.

A crucial point here is that form data does not identify its form definition except by its name. As an example, assume there is a *myapp/myform* form definition. A form being filled in will be stored under *myapp/myform* form data. Only the path name relates to the form definition. The original form definition could easily be edited such that form data later seems to be a completely different form than the one the user saw when filling it in.

In our application environment it is vital that form data relates exactly to the form definition used when filling in the form. A possible solution is to add a version or serial number to the form name. From Orbeon's point of view every version would become an independent form definition.

### Logos and Attachments ###

Logos and attachments are stored in form definitions and form data, respectively, using a special url. This is not a problem but is noted for future reference.

### Ownership ###

We have incomplete knowledge about how form definitions are associated with users. The XML-db does not seem to register the user producing a form. The RDBMS adaptions have a *username* field in their table definitions.

### Language ###

The form language is currently not included in form definition metadata but has to be extracted by an XML query.

### Semantic Issues: Conclusion ###

The save/publish semantics need a clean solution, perhaps by intercepting operations on the magic *orbeon/builder* hierarchy.

Version management needs a solution that allows form data to relate to the exact form definition used when filling in the form.

## Performance Issues ##

The pipeline generating SQL queries takes much longer than the database needs for executing the queries. Considering the functionality one would expect the reverse situation.

A major performance issue, as far as persistence is concerned, is how to cut down on the pipeline, or even avoiding it completely for minor operations like generating canned SQL.

