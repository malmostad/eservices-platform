# Coordinatrice API #

This chapter describes the Coordinatrice REST API. We use the symbol `$PREFIX` to denote the normal Coordinatrice URL followed by `rest`.

For example, if the normal URL is

```
http://localhost:8080/coordinatrice
```
then `$PREFIX` is

```
http://localhost:8080/coordinatrice/rest
```

## Process Definition State ##

Get the state of a BPMN process definition. The state is maintained by Coordinatrice.

```
GET $PREFIX/procdef/state/$id
```
where `$id` is the process definition id as defined by Activiti (name and version plus some digits).

Returns 200 and the process definition state as a JSON data structure, or 404 if not found. The returned JSON text is a dictionary with the following components.


* **procdefId**  
 Process definition id, the id used in the call
* **procdefVer**  
 The process definition version (in Activiti), an integer
* **stateCode**  
 An integer code for the state of this process definition
* **stateName**  
 The locale-independent name of the state of this process definition
* **editable**  
 Answers the question: can this process definition be edited? Either `true` or `false`.
* **startableCode**  
 An integer code indicating if this process definition may be instantiated 
* **startableName**  
 A locale-independent name indicating if this process definition may be instantiated

Process definition states:

| State Code | State Name |
|------------|------------|
| 101|Edit |
| 102|Trial |
| 103|Approved |
| 104|Published |
| 105|Retired |

Startable values:

| Startable Code | Startable Name | Description |
|----------------|----------------|-------------|
| 1|NotStartable |May not be started/instantiated |
| 2|TestOnly |May be started/instantiated in test mode |
| 3|Production |Ok to start/instantiate |

Output sample:

```
{
  "procdefId": "TestFunctionProcess1:20:1705",
  "procdefVer": 20,
  "stateCode": 101,
  "stateName": "Edit",
  "editable": true,
  "startableCode": 1,
  "startableName": "NotStartable"
}
```

## Locale-Dependent Activity Labels ##

Get one or more locale-dependent activity labels.

```
GET $PREFIX/activitylabel/$procdefkey/$locale[?version=$procdefversion]
GET $PREFIX/activitylabel/$procdefkey/$locale/$activityname[?version=$procdefversion]
```
The first form returns all activity labels for the process definition. The second form returns a single activity label.

Input parameters:

* **procdefkey**  
 The process definition key (Activiti terminology), the process name without version and without "funny" characters
* **locale**  
 The language for which activity labels are needed. A two-letter language code.
* **activityname**  
 The name of an activity as defined in the BPMN diagram. Must be URL-encoded if the name contains special characters.
* **procdefversion**  
 The version of the process definition (Activiti terminology).

Returns 200 and a JSON data structure, an array of dictionaries. Returns 404 if you specify an activity name and that activity is not found, or if no label has been defined for the activity.

The dictionaries have the following entries (most are input parameters),

* **procdefKey**  
 Process definition key
* **procdefVer**  
 Process definition version (an integer). This is the version number from the label definition, not necessarily the version used in the request.
* **actdefName**  
 Activity definition name (as defined in the BPMN source)
* **locale**  
 Locale/language code
* **label**  
 Activity label, locale-dependent

Output is suppressed if the activity label is null.

Output sample:

```
[
  {
     "procdefKey": "TestFunctionProcess1",
     "procdefVer": 0,
     "actdefName": "Handlägg",
     "locale": "en",
     "label": "Manage"
  },
  {
     "procdefKey": "TestFunctionProcess1",
     "procdefVer": 0,
     "actdefName": "Komplettera",
     "locale": "en",
     "label": "Supplement"
  },
  {
     "procdefKey": "TestFunctionProcess1",
     "procdefVer": 4,
     "actdefName": "Obstruera",
     "locale": "en",
     "label": "Obstruct"
  },
  {
     "procdefKey": "TestFunctionProcess1",
     "procdefVer": 0,
     "actdefName": "Refusera",
     "locale": "en",
     "label": "Refuse"
  }
]
```

### The Meaning of Process Version ###

Note in the example above that most entries include `procdefVer: 0` but one has `procdefVer: 4`. The meaning of the process version number included in internationalization labels is:

*As from this process version the label is xxx.*

There is no limit on the number of redefinitions. In practice the most common case is a single label definition using version number 0.

In the REST API you input the actual process version number. The API does the selection and responds with a single matching label definition. This means that the process version number in the response may be different from the version number in the request. In the example above the version number supplied in the request probably was at least 4.

There is a similar mechanism for start form labels. In that case the version number refers to the form version.

## Guide URLs ##

Get a guide URL.

```
GET $PREFIX/guideurl/$procdefkey/$locale[?version=$procdefversion]
GET $PREFIX/guideurl/$procdefkey/$locale/$activityname[?version=$procdefversion]
```
The first form returns a URL for a guide to the BPMN process as a whole. The second form returns a URL for an activity guide.

The method finds a URL and populates its placeholders with request parameters.

Input parameters:

* **procdefkey**  
 The process definition key (Activiti terminology), the process name without version and without "funny" characters
* **locale**  
 The language for which activity labels are needed. A two-letter language code.
* **activityname**  
 The name of an activity as defined in the BPMN diagram. Must be URL-encoded if the name contains special characters. The name is not checked for existence.
* **procdefversion**  
 The version of the process definition (Activiti terminology).

Output sample:

```
{
   "procdefKey": "TestFunctionProcess1",
   "procdefVer": 1,
   "actdefName": "Komplettera",
   "locale": "en",
   "url": "http://allguides/TestFunctionProcess1/Komplettera/en.html"
}
```

Comments on the example:


* **procdefVer**  
 The input version number, or the default (1)
* **actdefName**  
 This entry becomes `actdefName: null` in case the request does not contain an activity name

## Start Form Labels ##

Get a start form label for an Orbeon form.

```
GET $PREFIX/startformlabel/$app-name/$form-name/$locale[?version=$formdefversion]
```
Returns a start form label.

Input parameters:

* **app-name**  
 The *app name* in Orbeon terminology
* **form-name**  
 The *form name* in Orbeon terminology
* **locale**  
 The language for which the start form label is needed. A two-letter language code.
* **formdefversion**  
 The version of the form definition

Output sample:

```
{
   "appName": "basprocess",
   "formName": "registrera",
   "formdefId": 114,
   "formdefVer": 0,
   "locale": "it",
   "label": "Registrato"
}
```

Comments on the example:


* **formdefVer**  
 The form definition version of the guide url pattern used

## Install Migration Package ##

Install a migration package file. The file must reside in the Tomcat root directory.

```
PUT $PREFIX/rest/migration/file/$filepath[?mode=$installMode]
```
Returns a installation report. The report is also stored in the database and may be examined through the Coordinatrice gui.

Input parameters:

* **filepath**  
 The file name of the migration package to install. The file must reside in the Tomcat root directory. Any directories present in the file path are removed.
* **mode (optional)**  
 The optional mode parameter, if present, must be one of the keywords below

Install modes (default: *compareVersions*):

* **latestPublished**  
 Install the latest published version of each form (and adjoining drafts)
* **allPublished**  
 Install all published versions of each form (and adjoining drafts)
* **compareVersions**  
 Take version numbers literally, install only versions that are not already in the database

