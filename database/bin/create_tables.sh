#!/bin/bash

if [ $# != 0 ] && [ $# != 1 ] ; then
  echo 'Input database name and user or leave them blank!'
  echo 'exit...'
  exit
fi

if [ -z "$1" ]
  then
    export DB_NAME=motricedb
  else
    export DB_NAME=$1    
fi

echo "./execute_file.sh $DB_NAME ../create/motrice.postgres.create.box.sql"
su -c 'sh -c "./execute_file.sh $DB_NAME ../create/motrice.postgres.create.box.sql"'
su -c 'sh -c "./execute_file.sh $DB_NAME ../create/motrice.postgres.create.hibernate.sql"'
su -c 'sh -c "./execute_file.sh $DB_NAME ../create/motrice.postgres.create.mtf.sql"'
su -c 'sh -c "./execute_file.sh $DB_NAME ../create/motrice.postgres.create.pxd.sql"'

exit

