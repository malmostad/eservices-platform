# POSTGRESQL PERSISTENCE FOR ORBEON FORMS #

[The acronym RDBMS is used here to denote a relational database system
like Oracle, MySQL and PostgreSQL in contrast to a pure XML database like eXist-db.]

Summary: After digging around in Orbeon persistence we see two ways of adding PostgreSQL for persistence in Orbeon Forms:


1. Add a PostgreSQL choice for SQLProcessor (and friends) similar to Oracle and MySQL


2. Implement an eXist drop-in replacement based on PostgreSQL

Why is (2) proposed at all? There is a pattern for relational persistence in Orbeon, so why not just add another selection?

The main reasons are,

a. The Orbeon-RDBMS connection does not separate abstraction from implementation. Implementation details mix in on all levels. The result is that the persistence machinery becomes entangled with Orbeon code.

In contrast, the eXist interface is a loosely coupled black box.

It is important to be able to build other applications on top of the database. A loose coupling is very much preferred.

b. Performance and complexity. The Orbeon-RDBMS connection uses the Orbeon pipeline which is overkill. As a result a database query spends most of its time in the pipeline. Only a fraction of the time is spent in the database. One would expect the reverse situation considering the functionality of each step.

Unfortunately the eXist interface also uses the pipeline. Its advantage is mainly lower complexity compared to the RDBMS connection. One would expect slightly less pipeline overhead.

For persistence performance no issue is more important than trimming down the Orbeon pipeline.

## Why Is the Pipeline Overkill? ##

The main objection to the pipeline approach is that it does too much at runtime. Each persistence mechanism (eXist-db, MySQL, etc) has a small number of fixed operations. Only parameter values have to be filled in before executing a query.

A common practice in the RDBMS world is to define a prepared statement for each such operation. This is done only once.

Using the Orbeon pipeline means that the query is created from scratch every time even though it turns out the same (except for parameters). Queries have a predictable result. The Orbeon RDBMS connection uses a result interpreter as if the result is completely unknown.

This is not necessarily bad. However, the tools used for dynamic processing are very powerful. They incur a performance drag to the extent that pipeline time overshadows the RDBMS response time. No added benefit to the end user is gained.

It should be noted that a powerful pipeline machinery may be appropriate for more complex tasks.

## RDBMS Connection Details ##

A new RDBS can not be added to Orbeon Forms without changes to the source code. The current selection (Oracle, MySQL) is hardcoded. A new selection would also be hardcoded.

## Exist Drop-In Replacement Details ##

An eXist drop-in replacement backed by PostgreSQL can be added transparently without any change to Orbeon Forms. Even without any optimizations the response time can be halved according to preliminary indications.

EXist-db can be run in a servlet in the same container as Orbeon Forms. It would be better for a replacement to likewise install itself as a servlet in the same container. The database connects over the network in any case.

## Persistence Semantics: Questions ##

### Username ###

Q.1 Existing RDBMS table definitions have a username column. Is it ever used? What does it signify?

Q.2 For eXist-db the question is, can all users see and edit all forms in Form Builder?

### Temporary storage ###

`orbeon/builder`. After publishing the form it is stored under its
A user may save an edited form. It is saved as *data* of normal app and form names. However, if the form contains a logo it remains in `orbeon/builder/data`.

Q.3 What is the intended semantics of Save/Publish?

Q.4 Is there any way of telling (in the Form Builder GUI) if a form has been published? Is there any way of accessing the published version if there is a saved version?

### Version management ###

Q.5 When using an RDBMS a modified item never replaces an existing one. The item is timestamped and stored as a new record. We would like to understand the background of this design decision.

(except by examining the xml contents).
In MySQL timestamps have second precision. This means that identical items occur frequently, i.e. there is no way to differentiate the items

Another side effect is that the created and last updated timestamps are always equal because no item is ever updated. In database terms, there is no reliable primary key.

Q.6 Form data seems to be stored independently of its form definition. What happens if a form definition is edited? Without form definitions having a reliable primary key, how can form data refer back to its form definition?

### Language ###

Q.7 Is the list of forms in Form Builder filtered on the current language selection?

Q.8 What is a reliable definition of the language of a form? (We have used the language of resources, but it seems some Orbeon code uses the title language.)

### Others ###

Q.9 What is the *library* when used in an url? It appears in the position of form name. Example: `.../fr/myapp/library/form/form.xhtml` or `.../fr/orbeon/library/form/form.xhtml`.

Q.10 Hex strings are generated as database identifiers, typically for form data. The identifier is created as a digest of a random number. This is just an expensive way of padding the random number to 32 hex characters, it does not add randomness. Suggestion: add the current time to the random number before creating the digest. The resulting identifier would be random enough to be trusted as unique.


