Default database name is always motricedb
Default user is always motriceuser


Create a database:
sudo sh -c "./create_database.sh"
sudo sh -c "./create_database.sh <dbname> <user>"

Delete a database:
sudo sh -c "./delete_database.sh"
sudo sh -c "./delete_database.sh <dbname>"

Create a user:
sudo sh -c "./create_user.sh"
sudo sh -c "./create_user.sh <user>"

Delete a user:
sudo sh -c "./delete_user.sh"
sudo sh -c "./delete_user.sh <user>"

Create motrice tables:
sudo sh -c "./create_tables.sh"

Insert base demo data to motricedb:
sudo sh -c "./execute_file.sh motricedb ../create/motrice.postgres.demodata.sql"

Backup of the data only:  Note: For the moment table mtf_tag_type is excluded
sudo ./backup_database.sh
sudo ./backup_database.sh <dbname>

Restore of data only:
sudo sh -c "./execute_file.sh <dbname> <backup file> ./motricedb.backup"
sudo sh -c "./execute_file.sh motricedb ./motricedb.backup"





