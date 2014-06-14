# Orbeon 4.5 What's New #

## Versioning ##

Versioning is a major new feature in Orbeon 4.5 (still not released at the time of writing, 2014-02-25). Orbeon has identified the same issues as we have for the Motrice project. Versioning has been part of the relational database schemas for quite a while.

The main design decision is not to touch app/form names. The version is passed out-of-band via a HTTP header. Relational tables will get a new `form_version` column. Exist-db will get an additional nesting level.

Versioning will not be supported in exist-db to begin with.

[Blog post](http://blog.orbeon.com/2014/02/form-versioning.html)
[GitHub issue](https://github.com/orbeon/orbeon-forms/issues/1157)

## Note after Orbeon 4.5 Release ##

Orbeon 4.5 was released on 2014-05-07. Versioning seems to follow the outline above. The eXist-db interface most likely is not affected by the versioning feature. To be verified, of course.

There are new features related to repeated grids and sections. We have to check if those features affect the XML in such a way that DocBook generation has to be revised. A number of XML format assumptions are built into the transformation to DocBook. The transformation is defined procedurally in Groovy (not XSL).

