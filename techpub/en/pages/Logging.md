# Motrice logging #

The recommended way of logging in a Motrice component is to use the slf4j package. This 
facade is then bound to the concrete logger log4j. The logging level is controlled through 

* pawap/inherit-portal/conf/log4j-prod.xml for deployment
* pawap/inherit-portal/conf/log4j-dev.xml for cargo run

Most deployment logs will end up at hippo-cms.log or catalina.out at the moment.



