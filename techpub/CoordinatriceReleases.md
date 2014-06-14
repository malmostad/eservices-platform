# Coordinatrice Releases #

## coordinatrice 1.0.1 (on branch develop) ##

Added support for the case when a process definition disappears from the Activiti database, but has a start form assigned. This is something that theoretically should not happen in normal use, only after manual hacking.

## coordinatrice 1.0.0 (on branch develop) ##

Added REST operation to import and install a migration package. For details, see [Coordinatrice API](CoordinatriceApi.md).

## coordinatrice 0.9.13 (on branch develop) ##

Copyright notice added to gui screen.

## coordinatrice 0.9.12 (on branch develop) ##

Copyright notice added to all source files.

## coordinatrice 0.9.11 (on branch develop) ##

Bug fix: Missing localized messages.

## coordinatrice 0.9.10 (on branch develop) ##

More robust configuration checks.

## coordinatrice 0.9.9 (on branch develop) ##

Bug correction: Empty form database was not handled correctly. Gui snag: No warning when creating an empty migration package. Added a menu choice for checking the configuration.

## coordinatrice 0.9.8 (on branch develop) ##

Added start form label icon to the "Start Forms" and "All Forms" lists. Bug correction: You got a 404 instead of a decent error message when editing activity form connections after incomplete initialization of mtf_start_form_definition.

## coordinatrice 0.9.7 (on branch develop) ##

Allow upload new version from BPMN in all states. (Bug correction.)

## coordinatrice 0.9.6 (on branch develop) ##

Bug correction only.

## coordinatrice 0.9.5 (on branch develop) ##

Total makeover.


* Menus introduced for navigation because the number of functions grew out of bounds
* Migratrice merged into Coordinatrice

NOTE: Unfortunately there was an encoding bug in Migratrice prior to this version. This means that migration packages with a format beginning with *migratrice* are not safe to import.

## coordinatrice 0.9.4 (on branch develop) ##

Added uploading arbitrary BPMN. Previous releases include the option to upload BPMN to create a new version of an existing process.

## coordinatrice 0.9.3 (on branch develop) ##


* New gui for start forms
* Manage BPMN processes without name (weird, but happens). The key is used in such case.
* Various minor gui corrections

Unresolved design decision: When are you allowed to change the start form? In this release you may not change start forms after you pass the Published state.

## coordinatrice 0.9.2 (on branch develop) ##

Now possible to change the state of a BPMN process. Note: You cannot go back from state Published, only by making a new version.

## coordinatrice 0.9.1 (on branch develop) ##

Bug correction: delete in edit start form connection.

## coordinatrice 0.9.0 (on branch develop) ##

Support for i18n: activity labels, start form labels, guide URLs. A straightforward REST interface that may develop as needs from other components become known. I18n [documented here](CoordinatriceGetStarted.md), the [REST interface here](CoordinatriceApi.md).

Two supported locales: English (default) and Swedish.

Still incomplete: Manage all process states.

## coordinatrice 0.8.4 ##

(Released to branch develop)
REST interface for BPMN process definition states and locale-dependent activity labels.

## coordinatrice 0.8.3 ##

Locale-dependent activity labels added. Gui only, REST interface NOT implemented yet.

## coordinatrice 0.8.2 ##

Bug corrections only: New version did not reconnect properly.

## coordinatrice 0.8.1 ##

Big change: state management. Affects how a BPMN process may be instantiated. Theory explained in [Motrice Process Management](MotriceProcessManagement.md).

Database schema changes in tables

* mtf_activity_process_definition: new column `processdefinitionuuid` refers to the Activiti process definition id
* crd_procdef (new table): a cache that maintains the state of process definitions

## coordinatrice 0.8.0 ##

Modified to work with Activiti. Same essential functionality as the previous version.

Configuration is now unified with other Motrice components. See [Coordinatrice Get Started](CoordinatriceGetStarted.md).

## coordinatrice 0.7.2 ##

JMX basic management added. Control log levels, improved server error logging. Server errors also published as JMX notifications. Still Bonita.

## coordinatrice 0.7.1 ##

The connection to Orbeon Builder requires a new setting in the config file. See [Coordinatrice Get Started](CoordinatriceGetStarted.md).


* Footer background color changed. Actually the header background color has also changed. Purpose is more harmony with the Motrice logo.
* Activity connections: Only `Human` activities are considered for connections. Others are listed for completeness.
* The `Start` label changed to `Process List`
* Form list: New link to create new form, new link to edit form in Orbeon Builder

## coordinatrice 0.3.1 ##

Feature complete for the time being.

## coordinatrice 0.2.1 ##

Connect activities. Does not set start form.

## coordinatrice 0.1 ##

Proof of concept. Show processes, activities and forms. No editing.

