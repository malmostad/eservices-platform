#!/bin/bash

if [ -z "$1" ]
  then
    USER=motriceuser
  else
    USER=$1
fi

su -c 'sh -s' postgres  << EOF

# -U : User used for creating the new user
# -D : User will not be able to create a database
# -P : Prompt for the password of the new user

createuser -U postgres -D -P ${USER}

EOF
exit

