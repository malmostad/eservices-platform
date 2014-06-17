# Migration Get Started #

Migration is about transferring forms from one Motrice site to another. It used to be a stand-alone tool but is now merged with Coordinatrice. This text is a condensed user guide to the migration features of Coordinatrice. The name *Migratrice* refers to these features.

## Migration Concepts ##

The key term in migration is *packages*.  A package is a snapshot of one or more form definitions. All versions of included form definitions are included in the package. Packages remain in the database as long as you like.

Packages may be transferred between *sites*. A site corresponds to a Motrice installation. Even if you have more than one Motrice installation on the same host, each one is a site. The top banner of the screen shows the name of the site where Coordinatrice is running. This is called the *current site*.

Migration packages are flagged as *Local* or *Remote*. A *Local* package is one that was generated on the current site. A *Remote* package has been transferred from some other site. As a special case, you may generate and download a package and then delete the package in Coordinatrice. If you upload that package file it will be treated as *Remote*.

## Typical Migration Workflow ##

By definition a package begins its existence at a generating site. Any Motrice site may be a generating site. You create a package there and download it to a file. You may transfer the file to any other Motrice site, a receiving site.

The receiving site uploads the transferred file in a Coordinatrice session. Uploading does not install package contents, but makes it available for inspection in Coordinatrice. You may install it any time you prefer.

Installation installs data related to form versions not yet present at the receiving site.

At the generating site, after a package has been distributed to all recipients, an administrator may decide to delete the package from the database, or to keep it for the record.

### Migration Basic Screens ###

The migration start screen is a list of packages present in the Coordinatrice database. It contains some basic data about each package.

Click on a package name to see its details.

The package details screen shows a few new items. At the *Reports* heading there may be a list of installation reports, appearing as a list of time stamps. If the package has not been installed there are no reports. An installation report contains basic information about an installation attempt.

At the *Formdefs* heading there is a list of all form definitions included in the package. You may click on a formdef name to see its details. The details include all form definition versions and all items that make up the form definition.

### Create Package ###

The basic screens contain the *Create Package* button. When you click it a list appears containing all available form definitions. Each line has a check box to the left.

First of all, decide on the name of the package to be created. Enter the name in the field at the top of the screen. The name need not contain any date because a time stamp is automatically included in the package.

After entering the package name, check all form definitions to be included in the new package.

You may use the buttons at the bottom of the screen to include or exclude more than one form definition at a time.

Finally click the *Create* button to create the package.

### Download A Package ###

To download a package, click its name in the package list. The package details screen contains a *Download* button. The downloaded file will have a name consisting of the site name, the package name, and a time stamp. The standard file name extension is `.pwp`.

The downloaded file is a zip archive.

### Upload A Package ###

The upload package screen has a *Browse* button. Begin by clicking it to select a file to upload. Then click *Upload Migration Package* to make Migratrice import the package.

After uploading the package becomes visible in Coordinatrice. You may inspect its details as described above.

Uploading a package does not install it. It remains in the database until installed or deleted.

### Install A Package ###

Installing a package means that the form definitions in the package become available for general use.

Installation **never overwrites** any existing form definitions. When a form definition is installed that already exists, a new version is created subsequent to the highest existing one. This means that a form definition does not necessarily keep its version on installation. If there are local modifications the same form definition may have different versions at different sites.

The *installation mode* controls exactly how versions are treated.


* **Latest published version only**  
 Of each form definition in the package, install only the latest published version plus the subsequent drafts plus the preceding draft.
* **All published versions**  
 Install all published versions and all drafts
* **Compare version numbers**  
 Published versions higher than local ones are installed. In addition, all drafts following the published versions and the latest draft preceding the lowest installed version are installed. In case a form definition exists and has been locally modified there may be an offset in version numbering, but installation only compares the numbers. All versions are installed if a form definition does not exist locally. 

## Constraints ##

A form definition must have the same *app/form* name and the same uuid to be considered the same form. A form definition cannot be installed if the *app/form* names agree but not the uuid.

