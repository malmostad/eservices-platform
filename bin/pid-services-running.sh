#!/bin/sh

# Name of container roots
   EXIST=orbeon-tomcat-6.0.36
     BOS=BOS-5.9-Tomcat-6.0.35
ESERVICE=hippo-eservice-tomcat-6.0.36
KSERVICE=hippo-kservice-tomcat-6.0.36

# Name of container roots
EXIST_PORT=48080
BOS_PORT=58080
ESERVICE_PORT=8080
KSERVICE_PORT=38080

EXIST_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${EXIST_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
if [ -n "${EXIST_PID}" ] 
then 
  echo "eXist service: "${EXIST}"          port: "${EXIST_PORT}"   pid: "${EXIST_PID}
fi

BOS_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${BOS_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
if [ -n "${BOS_PID}" ] 
then 
  echo "BOS service:   "${BOS}"         port: "${BOS_PORT}"   pid: "${BOS_PID}
fi

ESERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${ESERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
if [ -n "${ESERVICE_PID}" ] 
then 
  echo "Eservice:      "${ESERVICE}"  port: "${ESERVICE_PORT}"    pid: "${ESERVICE_PID}
fi

KSERVICE_PID=$(netstat -ntlp 2> /dev/null | grep '0 \:\:\:'${KSERVICE_PORT} | awk '{print substr($7,1,match($7,"/")-1)}')
if [ -n "${KSERVICE_PID}" ] 
then 
  echo "Kservice:      "${KSERVICE}"  port: "${KSERVICE_PORT}"   pid: "${KSERVICE_PID}
fi
