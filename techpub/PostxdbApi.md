# Postxdb API #

Postxdb emulates the REST interface of the eXist-db XML database, as used by Orbeon Forms. This means the postxdb API is a subset of the eXist API and that special semantics are added. It is not a general-purpose eXist-db replacement.

The URL used in the eXist-emulating methods has a constant prefix

```
/exist/rest/db/orbeon-pe/fr
```
This is indicated by the symbol `$PREFIX` below. There are a number of non-eXist methods with a different prefix, see below.

A dollar sign before a URL element indicates a variable.

Several operations have two cases, one for XML data (the form definition itself), one for resources. A resource is anything that is not XML, usually binary. Postxdb (and eXist-db) uses the Content-Type header to distinguish between the cases.

As from Orbeon 4.3 Orbeon only accepts empty 200 or 201 responses (200 and 201 being HTTP response codes). Postxdb earlier than 0.6 added some information.

The Orbeon wiki has been updated with information on the XML format used in searches and form lists. See http://wiki.orbeon.com/forms/doc/developer-guide/form-runner/persistence-api

## A Note on Resources ##

Orbeon generally stores resources before the XML form. This means that at the time a resource is stored postxdb has no idea what form it belongs to.

## A Note on Versions ##

Postxdb generally changes the version number in XML form definitions submitted to it. The new version number of the form definition will follow the latest version previously present in the database.

There is an exception to this rule. If no version exists previously the newly added form definition will keep its version number, if it has one.

## Form Definitions ##

Form definitions are either drafts (*Save* in the Orbeon form builder) or published (*Publish* in the builder). Their XML representations are quite different. This means that, after publishing, the first draft of the next version is not a copy of the published version. The first draft must be copied from the last draft of the previous version.

Postxdb supports editor form searches. It only supports the search that appears after you click *Show search options*. However, this function is a bit of a hack.

In a form search Orbeon posts XQuery snippets to eXist, based on assumptions about the implementation. XQuery is not relevant to postxdb, but it is possible to extract the search parameters and convert the search to plain SQL. Postxdb converts Unix style wildcards (`*` and `?`) to SQL LIKE expressions.

### List Editable Forms ###

List editable forms. Only the current draft of a form is editable. Paged output.


```
GET $PREFIX/orbeon/builder/data
```

Optional parameters:

* **page-size**  
 Max number of forms per page. Default: 15.
* **page-number**  
 Page number beginning with 1. Default: 1.

Returns an XML document containing a list of forms on the format required by Orbeon. The list may be empty. An example follows.

```
<exist:result xmlns:exist#'http://exist.sourceforge.net/NS/exist' exist:hits#'1' exist:start#'1' exist:count#'1'>
  <documents total#'26' search-total#'26' page-size#'15' page-number#'1' query=''>
    <document created#'2013-10-03T10:12:00.175' last-modified#'2013-10-03T10:12:00.175' draft#'false' name#'d7e132a0b8fadbfde372f8c1660e0bd2'>
      <details>
	<detail>anonymous</detail>
	<detail>authsubmit--v003_01</detail>
	<detail>Anonym till skicka in</detail>
	<detail>--</detail>
      </details>
    </document>
    <document created#'2013-12-27T20:58:24.620' last-modified#'2013-12-27T20:58:24.620' draft#'false' name#'623d3487dfd6776aeb148d41edcfb3d60beef344'>
      <details>
	<detail>bells</detail>
	<detail>whistles--v011_02</detail>
	<detail>Orbeon Bells &amp; Whistles</detail>
	<detail>Test case using most of Orbeon controls.</detail>
      </details>
    </document>
  </documents>
</exist:result>
```

### Store a Draft Form Resource ###

Store an XML draft form definition, or a resource belonging to a draft form definition. A logo image is a common example of a (binary) resource.

Every form definition (defined by app name/form name) is assigned a uuid by Orbeon. Repeated saves must save to the same uuid.


```
PUT $PREFIX/orbeon/builder/data/$uuid/$resource
BODY: The resource, draft form XML, or an arbitrary resource
```

Returns 201 on success, 409 on failure, and a plain text message. The message contains the exception message on failure.

#### XML ####

Form metadata (app name, form name, etc) is extracted from the draft form. A version and draft number is appended to the form name if it does not have one. Otherwise the draft number is incremented. The XML is updated to reflect the new version number.

A pxd_formdef record is created if this is the first draft of this form. A pxd_formdef_ver record is created for the new draft. The resulting XML is stored in pxd_item with the uuid and the form name. The path column will contain `app/form--vNNN_NN/form.xml`

Assume the original form name is `app/form--v001_05`. Then after being stored this is a summary of database contents.

* pxd_item contains the XML where `app/form--v001_06` has been inserted
* pxd_formdef_ver has an entry for `app/form--v001_06`
* pxd_formdef is updated to point to the new draft as the current draft 

The form language is not updated at this point, but remains English, or really, the current editor language. Otherwise Orbeon may assume the draft is out of scope.

#### Other Resources ####

Resource are usually uploaded to Orbeon from a file. The original file name is kept in the form definition but Orbeon identifies the resource as *{uuid}.{file name extension}*. The *uuid* uniquely identifies the resource. Postxdb stores resources in pxd_item using the resource name as path.

As from Orbeon 4.3 the file name extension is replaced by `.bin`.

Postxdb drops the uuid contained in the URL. The reason is that the resource name contains another uuid which is sufficient to identify the resource.

### Retrieve a Draft Form Resource ###

(Other resources are assigned a uuid.)
Retrieve an XML draft form definition, or a resource belonging to a draft form definition. If the resource name begins with `data` it is considered XML (i.e. a form definition). Orbeon has no idea about drafts, so the uuid is used to look up a form in pxd_formdef. A pxd_formdef has a pointer to the current draft which is retrieved from pxd_item using the path.


```
GET $PREFIX/orbeon/builder/data/$uuid/$resource
```

Returns 200 and the resource, or 404 depending on the outcome. The Content-Type is either `application/xml;charset=UTF-8` or `application/octet-stream`.

### Store a Published Form Definition ###

Store an XML published form definition, or a resource belonging to a published form definition. The Content-Type is used to distinguish between the XML form definition and other resources.


```
PUT $PREFIX/$app/$form/form/$resource
BODY: The resource, XML or an arbitrary resource
```

where `$app` is the Orbeon app name, `$form` is the Orbeon form name (including the version number inserted by postxdb).

Returns 201 on success, 409 on failure, and a plain text message. The message contains the exception message on failure.

#### XML ####

At least a draft must exist before you may publish.

The version of the published XML is relative to the current draft. The version stored in the XML string.

For example, assume the current draft is `app/form--v001_06`. Then after the operation the database contains two items.


* The published form: `app/form--v001`, a copy (except for the version number) of the XML sent to the operation
* The new current draft: `app/form--v002_01`, a copy (except for the version number of `app/form--v001_06`

The new current draft is not a copy of the published version because the XML of drafts is quite different from published forms.

The pxd_formdef and pxd_formdef_ver tables are updated to reflect the new items. The form XML is updated with the new form names (containing version numbers). Published form XML is stored in pxd_item with the path `app/form--v001/form.xhtml`.

#### Other Resources ####

Orbeon pre-4.3 left resources in their draft collection. As from 4.3 Orbeon actively stores them again using this operation. The resource name is the same as for draft resources. This means that the resource name in itself uniquely identifies the resource.

Postxdb checks if the resource is already present in the database. In most cases it is, and postxdb does nothing. If not present, postxdb stores the resource in pxd_item under its uuid.

### Retrieve a Published Form Definition ###

Retrieve an XML published form definition, or a resource belonging to a published form definition. If the resource name is `form.xhtml` the XML form definition is retrieved.


```
GET $PREFIX/$app/$form/form/$resource
```

where `$app` is the Orbeon app name, `$form` is the Orbeon form name (including the version number inserted by postxdb).

Returns 200 and the resource, or 404 depending on the outcome. The Content-Type is either `application/xml;charset=UTF-8` or `application/octet-stream`.

The `library` form name is magic. Orbeon reserves this name for reusable sections. When retrieved the API returns the latest published version regardless of the version in the request.

## Form Data ##

Orbeon assigns a uuid to each new form instance. The uuid is shared by all components of a filled-in form: XML (including field values) and attachments. Non-XML resources have their file names replaced by a uuid generated by Orbeon, except that the file name extension is kept. As from Orbeon 4.3 the file name extension is replaced by `bin`.

Form data XML is clueless as to what form it was generated from. This relationship can only be derived from the path used for storage. So how does postxdb manage the relationship between a form definition and all form data generated from it? Postxdb makes sure the (unique) form path is present when storing XML form data in pxd_item. However, this is not currently declared as a foreign key in the database. The reason is mainly that a foreign key affects performance and is only useful for the administration gui.

### Store Form Data ###

Store form data, either XML or an attachment.


```
PUT $PREFIX/$app/$form/data/$uuid/$resource
BODY: The resource, XML or an arbitrary resource
```

where `$app` is the Orbeon app name, `$form` is the Orbeon form name, `$uuid` is the uuid assigned to this form data by Orbeon, `$resource` is the resource name.

Returns 201 on success, 409 on failure, and a plain text message. The message contains the exception message on failure.

Form data only affects pxd_item. The XML is stored with a path `{uuid}/data.xml`. Other resources are stored using the resource name as path. It is unique because it is based on a uuid.

### Retrieve Form Data ###

Retrieve form data, either XML or an attachment.


```
GET $PREFIX/$app/$form/data/$uuid/$resource
```

where the variables are the same as the previous case.

Returns 200 and the resource, or 404 depending on the outcome. The Content-Type is either `application/xml;charset=UTF-8` or `application/octet-stream`.

## Postxdb Methods (from postxdb 0.7) ##

Postxdb has a number of methods that do not emulate eXist. The purpose of these methods is to read from the database according to the postxdb data model, including version management. The eXist-emulating methods are the only way to write to the database. For reading you may use either eXist-emulating methods or what we call postxdb methods. The postxdb methods have the prefix

```
/exist/postxdb
```
This is indicated by the symbol `$POSTXDB` below.

The current version of postxdb mainly supports reading form definitions through the postxdb methods. Full support for form instances may be added later.

All methods in this section, except the very last one, return metadata.

In all methods, `$id` denotes a 64-bit integer database id.

### Form Definitions ###

Get form definition metadata. A form definition is a versionless, generic entity without content. These methods return an XML structure.

```
GET $POSTXDB/formdef
GET $POSTXDB/formdef/$id
```

The first method lists all form definitions, all versions. The second method returns the same data for a specific form definition. It returns a list although it always contains a single form definition. Sample output from the first form:

```
<?xml version#"1.0" encoding#"UTF-8"?>
<list>
  <pxdFormdef>
    <ref gen="6">121</ref>
    <created>2013-09-19 22:08:52.619 CEST</created>
    <updated>2013-10-03 10:12:30.893 CEST</updated>
    <app>basprocess</app>
    <form>beslut</form>
    <uuid>3e67a1e277b054ed495b2b52f04b5682</uuid>
    <currentDraft>basprocess/beslut--v003_01</currentDraft>
  </pxdFormdef>
  <pxdFormdef>
    <ref gen="6">128</ref>
    <created>2013-09-19 22:12:07.957 CEST</created>
    <updated>2013-10-03 10:12:41.79 CEST</updated>
    <app>basprocess</app>
    <form>handlagga</form>
    <uuid>25750f24fb39da6e7085e12e0440da78</uuid>
    <currentDraft>basprocess/handlagga--v003_01</currentDraft>
  </pxdFormdef>
  <pxdFormdef>
    <ref gen="6">114</ref>
    <created>2013-09-19 22:04:54.225 CEST</created>
    <updated>2013-10-03 10:12:52.286 CEST</updated>
    <app>basprocess</app>
    <form>registrera</form>
    <uuid>9d04589a006d16ba1b48e1d96f48b82b</uuid>
    <currentDraft>basprocess/registrera--v003_01</currentDraft>
  </pxdFormdef>
</list>
```

The `ref` element contains the original unique id of an object. The `gen` attribute of the `ref` element is the Hibernate version number, the number of times this object has been updated.

### Form Definition Versions ###

Get form definition version metadata. A form definition version is concrete, having at least an XML file and possibly comes with other resources.

```
GET $POSTXDB/formdefver/$id
GET $POSTXDB/formdefver?{uuid#$uuid | formdef#$id}
```
The first method gets a specific form definition version. The second method gets all form definition versions of the given form definition sorted with the latest version first. The output format of the two methods are identical; the first method returns a list even though it always contains a single form definition version. Sample output from the second method:

```
<?xml version#"1.0" encoding#"UTF-8"?>
<list>
  <pxdFormdefVer>
    <ref>134</ref>
    <formref>128</formref>
    <created>2013-09-19 22:12:26.624 CEST</created>
    <app>basprocess</app>
    <form>handlagga</form>
    <path>basprocess/handlagga--v002_01</path>
    <verno published="false">2</verno>
    <draft>1</draft>
    <title>Handläggning</title>
    <description />
    <language>en</language>
  </pxdFormdefVer>
  <pxdFormdefVer>
    <ref>131</ref>
    <formref>128</formref>
    <created>2013-09-19 22:12:26.603 CEST</created>
    <app>basprocess</app>
    <form>handlagga</form>
    <path>basprocess/handlagga--v001</path>
    <verno published="true">1</verno>
    <draft />
    <title>Handläggning</title>
    <description />
    <language>sv</language>
  </pxdFormdefVer>
  <pxdFormdefVer>
    <ref>129</ref>
    <formref>128</formref>
    <created>2013-09-19 22:12:07.961 CEST</created>
    <app>basprocess</app>
    <form>handlagga</form>
    <path>basprocess/handlagga--v001_01</path>
    <verno published="false">1</verno>
    <draft>1</draft>
    <title>Handläggning</title>
    <description />
    <language>en</language>
  </pxdFormdefVer>
</list>
```

A few notes:

* The `ref` element contains the original unique id of an object
* Similarly, the `formref` contains the original id of the form definition object
* The `verno` element contains the form version number. Its `published` attribute indicates whether the form version is published. This fact can be derived from the version number, but the purpose is to avoid having clients decode version numbers.
* The `draft` element contains the draft number, or is empty for published versions

### Items ###

Get item data. An item is some content of a form, either XML or any resource. Items are the substance of form definitions and form instances. An item may be part of a form definition (defitem) or a form instance (institem).

The two first methods return metadata. The very last method gets item content regardless of where it occurs.

```
GET $POSTXDB/defitem?{uuid#$uuid | formdef#$id}
GET $POSTXDB/institem?formdefver=$id
GET $POSTXDB/item/$id
```
The first methods return a list containing item metadata in no particular order. The last method returns an item, regardless of whether it belongs to a form definition or a form instance. It may be an object of any size. Its *Content-Type* is either `application/xml;charset=UTF-8` or `application/octet-stream`.

Sample (truncated) output from `.../defitem?formdef=1222`:

```
<?xml version#"1.0" encoding#"UTF-8"?>
<list>
  <pxdItem>
    <ref>1232</ref>
    <formref>1222</formref>
    <created>2013-12-27 20:47:45.637 CET</created>
    <path>bells/whistles--v004_03/form.xml</path>
    <uuid>623d3487dfd6776aeb148d41edcfb3d60beef344</uuid>
    <formpath>bells/whistles--v004_03</formpath>
    <format>xml</format>
    <size>10026</size>
    <sha1>47e4c7d8fd7c013d076657aae812b03f703273ee</sha1>
  </pxdItem>
  <pxdItem>
    <ref>1234</ref>
    <formref>1222</formref>
    <created>2013-12-27 20:47:45.662 CET</created>
    <path>bells/whistles--v004_04/form.xml</path>
    <uuid>623d3487dfd6776aeb148d41edcfb3d60beef344</uuid>
    <formpath>bells/whistles--v004_04</formpath>
    <format>xml</format>
    <size>11162</size>
    <sha1>1792b896e4ddf42b4457892b775f01cd3149f6c1</sha1>
  </pxdItem>
  <pxdItem>
    <ref>1049</ref>
    <formref>1222</formref>
    <created>2013-12-21 16:36:40.393 CET</created>
    <path>45c9e9da767531f6a86f6e13986465a589634808.bin</path>
    <uuid>623d3487dfd6776aeb148d41edcfb3d60beef344</uuid>
    <formpath />
    <format>binary</format>
    <size>5514</size>
    <sha1>3d3c1d0c9f286755511912294acc0e96037d7de4</sha1>
  </pxdItem>
</list>
```

Notes:

* The `formref` element contains the original unique id of the form definition object
* The `uuid` element contains the same uuid as the form definition object
* The `format` can be *xml* or *binary*
* The `sha1` element contains a SHA1 hash sum of item contents in hex format

