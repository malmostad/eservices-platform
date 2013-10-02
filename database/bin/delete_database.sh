#!/bin/bash

if [ -z "$1" ]
  then
    USER=motricedb
  else
    USER=$1
fi

su -c 'sh -s' postgres  << EOF

# -U : User used for creating the new user

dropdb -U postgres ${USER}

EOF
exit

