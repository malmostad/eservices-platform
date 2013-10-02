This directory contains database schema upgrade scripts. 

1) Make sure you make a backup of your database before you run an upgrade. 
2) Run the upgrade database scripts. It is important to run the upgrade scripts in the correct succesion. Make sure to always run a script where your database schema version is "oldVersionNo" 

The upgrade scripts is named with the following convention:
motrice.[databaseType].upgradestep.[oldVersionNo].to.[newVersionNo].[module].sql

Example)
motrice.postgres.upgradestep.0_2_0.to.0_3_0.box.sql

