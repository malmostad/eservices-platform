#!/bin/bash

if [ -z "$1" ]
  then
    USER=motriceuser
  else
    USER=$1
fi

su -c 'sh -s' postgres  << EOF

# -U : User used for creating the new user

dropuser -U postgres ${USER}

EOF
exit

