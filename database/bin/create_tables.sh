#!/bin/bash

if [ $# != 0 ] && [ $# != 2 ] ; then
  echo 'Input database name and user or leave them blank!'
  echo 'exit...'
  exit
fi

if [ -z "$1" ]
  then
    export DB_NAME=motricedb
    export DB_USER=motriceuser
  else
    export DB_NAME=$1
    export DB_USER=$2    
fi

su -c 'sh -c "./execute_file.sh ${DB_NAME} ../create/motrice.postgres.create.hibernate.sql"'
su -c 'sh -c "./execute_file.sh ${DB_NAME} ../create/motrice.postgres.create.box.sql"'
su -c 'sh -c "./execute_file.sh ${DB_NAME} ../create/motrice.postgres.create.sig.sql"'
su -c 'sh -c "./execute_file.sh ${DB_NAME} ../create/motrice.postgres.create.crd.sql"'
su -c 'sh -c "./execute_file.sh ${DB_NAME} ../create/motrice.postgres.create.mtf.sql"'
su -c 'sh -c "./execute_file.sh ${DB_NAME} ../create/motrice.postgres.create.pxd.sql"'
su -c 'sh -c "./execute_file.sh ${DB_NAME} ../create/motrice.postgres.create.mig.sql"'
su -c 'sh -c "./execute_file.sh ${DB_NAME} ../create/activiti.postgres.create.engine.sql"'
su -c 'sh -c "./execute_file.sh ${DB_NAME} ../create/activiti.postgres.create.history.sql"'
su -c 'sh -c "./execute_file.sh ${DB_NAME} ../create/activiti.postgres.create.identity.sql"'

#
# postgres user owns the created tables. Change ownership to DB_USER for all tables
#
tables=`su -c "psql -qAt  -U postgres -d ${DB_NAME} -c 'select tablename from pg_tables where schemaname = '\''public'\'';'" postgres`
for tbl in $tables ; do
  echo "Change ownership on table ${tbl} to ${DB_USER}"
  su -c "psql -c 'alter table $tbl owner to $DB_USER' $DB_NAME;" postgres
done

#
# postgres user owns the created sequences. Change ownership to DB_USER for all sequences
#
sequences=`su -c "psql -qAt  -U postgres -d ${DB_NAME} -c 'SELECT c.relname FROM pg_class c WHERE c.relkind = '\''S'\'';'" postgres`
for sequence in $sequences; do
  echo "Change ownership on sequence ${sequence} to ${DB_USER}"
  su -c "psql -c 'alter sequence $sequence owner to $DB_USER' $DB_NAME;" postgres
done

exit

