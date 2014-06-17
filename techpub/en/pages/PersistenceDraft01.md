# Persistence in Orbeon #
The top level persistence interface in Orbeon is RESTful. It's a valuable asset because it is possible to hide the persistence implementation completely. In some respects it's too simple. In order to store forms being saved but not published by the Form Builder, a magic app/form name is used (*orbeon/builder*) to have it stored in the form data table (not form definition), burying all metadata in XML. Very kludgy.

The semantics of the REST interface is undocumented. The above note shows that the meaning of the operations is not obvious. In addition there is a kind of version management when persisting to a relational database. Entries are never deleted. On the other hand there is no way to access versions other than the latest through the REST interface. If this is a feature it should be documented.

There is also the question of the username column present in the database tables, but seemingly never used.

Persistence performance suffers enormously from being wrapped in the pipeline machinery. My measurements indicate that only 1-5% of the time of a REST call is spent in the relational database. This is remarkable because the generated SQL is extremely convoluted. The remaining time is consumed by the pipeline.

The convoluted SQL is a strong indicator that there is something wrong with the data model. For instance, paging the list of available forms in the Form Builder, is a very standard operation, but uses extremely complex queries.

PostgreSQL easily handles all the relevant datatypes. However, the SQLProcessor contains tacit assumptions that force unnatural datatypes. There is a Java interface called DatabaseDelegate. I wrote an implementation for PostgreSQL, but it turned out none of its methods are ever called. The DatabaseDelegate interface is completely undocumented, not even a single comment. Its design seems to have been affected by quirks in Oracle.

## Conclusion ##
A new persistence implementation should decouple itself as much as possible from the pipeline machinery. The main reasons are:

* protect itself from severe performance drag
* be able to work with natural datatypes
* freedom to use an appropriate data model
* minimize maintenance cost by avoiding unwarranted complexity

There are mainly two ways to achieve this:

* A barebones custom processor with an absolutely minimum of logic
* A self-contained persistence node ("eXist emulator")

