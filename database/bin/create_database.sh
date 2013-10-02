#!/bin/bash

if [ $# != 0 ] && [ $# != 2 ] ; then
  echo 'Input database name and user or leave them blank!'
  echo 'exit...'
  exit
fi

if [ -z "$1" ]
  then
    DB_NAME=motricedb
    DB_OWNER=motriceuser
  else
    DB_NAME=$1
    DB_OWNER=$2
fi

su -c 'sh -s' postgres  << EOF

# -U : User used for creating the new data
# -O : Owner
# -E : Encoding
# -D : Tablespace
# -l : locale

createdb -U postgres -O ${DB_OWNER} -E 'UTF8' -D pg_default -l 'sv_SE.UTF-8' ${DB_NAME}

EOF
exit

