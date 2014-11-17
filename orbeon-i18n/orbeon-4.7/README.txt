Script for localizing Orbeon Form Builder to Swedish.
The Form Runner is localized out of the box these days.
Original files were taken from Orbeon 4.7 CE.

The input is the tgz archive in the 'sv' directory.

Run the script 'localize-unpacked-sv.sh' right here.
The one and only command line parameter must be a directory where the Orbeon
war has been unpacked, usually $REPO/inherit-orbeon/src/main/webapp/WEB-INF

In more detail:

The script modifies one jar in the unpacked Orbeon war. In order to store the
changes in the source repo, do like this,
(2a) Create a git branch for the upcoming changes
(2b) Run 'localize-unpacked-sv.sh'
(2c) Commit the changes
