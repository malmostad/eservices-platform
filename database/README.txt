Default database name is always motricedb
Default user is always motriceuser


Create a database:
sudo sh -c "./create_database.sh"
sudo sh -c "./create_database.sh <dbname> <user>"

Delete a database:
sudo sh -c "./delete_database.sh"
sudo sh -c "./delete_database.sh <dbname>"

Create a user: (lirar inte på Björns burk)
sudo sh -c "./create_user.sh"
sudo sh -c "./create_user.sh <user>"

Delete a user:
sudo sh -c "./delete_user.sh"
sudo sh -c "./delete_user.sh <user>"

Create motrice tables:
sudo sh -c "./create_tables.sh"

Insert base demo data to motricedb:
sudo sh -c "./execute_file.sh motricedb ../create/motrice.postgres.demodata.sql"




