Script for localizing Orbeon Forms (the Form Runner) to Swedish

The input is the jar file in the 'sv' directory.
It originates from the Orbeon source tree.

Two methods are available.
(1) Run the script 'localize-sv.sh' on the main Orbeon war (orbeon.war).
(2) Run the script 'localize-unpacked-sv.sh' on a directory where the Orbeon
war has been unpacked. In more detail:

The script modifies 3 jars in the unpacked Orbeon war. In order to store the
changes in the source repo, do like this,
(2a) Create a git branch for the upcoming changes
(2b) Run 'localize-unpacked-sv.sh'
(2c) Commit the changes
