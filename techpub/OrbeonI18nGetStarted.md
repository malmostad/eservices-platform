# Orbeon I18n Utility #

A temporary utility for patching `orbeon.war` with the Swedish language. We expect that from version 4.5 Orbeon will support Swedish.

## Running the Utility ##

The utility generates a new `orbeon.war` from an existing one. The main input is the full path to the original `orbeon.war` released by Orbeon.

Under the `orbeon-i18n` directory you will find a directory for a number of Orbeon releases. Enter the relevant one, i.e. make it the current directory. Run the shell script `localize-sv.sh` with the full path of `orbeon.war` as command line parameter.

The outcome is a new `orbeon.war` located in a temporary directory. The script prints the full path of the modified file.

## Supplying a Translation ##

The `sv` directory must contain a jar file named `orbeon-fr-swedish.jar`. The file tree in that jar originates from the Orbeon source repository. Translations are needed for,

```
src/resources-packaged/ops/javascript/orbeon/xforms/control/CalendarResources.js
src/resources/apps/fr/i18n/resources.xml
```
and for around 20 `.xbl` files in subdirectories of

```
src/resources-packaged/xbl/orbeon
```

