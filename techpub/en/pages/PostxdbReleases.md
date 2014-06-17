# postxdb Releases #

## postxdb 0.8.4 ##

Copyright notice added to source files.

## postxdb 0.8.3 ##

Encoding issues in the "Postxdb" API methods. Corrected to always return UTF-8.

## postxdb 0.8.2 ##

Dependency change only. No functionality change (fingers crossed:).

## postxdb 0.8.1 ##

Make sure postxdb always responds with Content-Type: application/xml. Orbeon does not like text/xml.

## postxdb 0.8.0 ##

Modified behaviour for the `library` form name. It is reserved by Orbeon for reusable sections. Postxdb treats forms named `library` the same way as other forms (adding a version number), except when a published form is retrieved. The latest version is returned.

In order to retrieve a version other than the latest, use the postxdb API, or the gui.

## postxdb 0.7.8 ##

Added SHA1 hash sum when showing items, i.e. contents. Useful for Migratrice tests.

## postxdb 0.7.7 ##


* Bug correction when publishing forms. Would hit Migratrice, not other uses.
* New conventions for configuration. See [PostxdbRef Postxdb Getting Started].

## postxdb 0.7.5 ##

Basic JMX management added. Log levels may be set over JMX without halting the application. Server errors (HTTP 500) are published as JMX notifications. Also improved logging of server errors.

## postxdb 0.7.4 ##

Another round of small but important modifications of XML output from the new *postxdb* methods.

## postxdb 0.7.3 ##

Small but important modifications of XML output from the new *postxdb* methods.

## postxdb 0.7.2 ##

XML output from the new *postxdb* methods reworked.

## postxdb 0.7.1 ##

Implemented the new *postxdb* methods of the API. The new methods read the database according to the postxdb data model, different from eXist emulation.

## postxdb 0.7.0 ##

Oops, no good. Do not use.

## postxdb 0.6.4 ##

Resolved problem with Save and Publish in the same session (in Orbeon Form Builder). On Publish, the new current draft is copied from the previous current draft. There is still a potential concurrency issue.

## postxdb 0.6.3 ##


* Dates are included in the Orbeon summary page, for better or for worse. Two wide columns that are always the same.
* You may search forms in the summary page. Click the *Show search options* link. Use `*` and `?` wildcards.

There is no release postxdb 0.6.2.

## postxdb 0.6.1 ##

All operations implemented according to the API spec. Everything should work with Orbeon 3.9. Known issues with Orbeon 4.3,

* Missing date in *List Editable Forms*
* Seach does not work at all in Orbeon 4.3

Caution:

* More testing with Orbeon 4.3 required
* Orbeon treats form resources differently in 4.3 as compared to 3.9. They are explicitly copied from the draft catalog to the published form. However, postxdb does not implement catalogs which means that the name of a resource (a uuid) does not change, and copying becomes a no-op.

