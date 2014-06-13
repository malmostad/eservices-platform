##Jmeter

###Intro
Jmeter is  an open source project written in java for doing load tests of webservices. 
http://jmeter.apache.org
The purpose to use it in this project is to make load tests, but also some 
other tests. 

###Resources 
There are a number of extensions available 
to jmeter.
http://jmeter-plugins.org/

No extensions have been used in the scripts. 

Some starting point to learn about jmeter:
http://blazemeter.com/blog
http://blazemeter.com/blog/jmeter-tutorial-video-series
http://www.youtube.com/watch?v=cv7KqxaLZd8
http://www.youtube.com/watch?v=T3Ysb9O3EWI
http://www.metaltoad.com/blog/jmeter-test-plan-drupal

###Setup of motrice 
The setup is without bankid identification. This is to make the scripts fully 
automatically. A simplistic start form is used, demo-ansokan--v002. If 
an other form is used the test plan TestPlanCreateCases.jmx needs to be updated. 

The rest call to lookup labels in CoordinatriceFacade.java has been disabled to increase speed. 
This should probalbly be cached in a future version. 


###Test scenarios 
* Loading of the inbox :
    -The scripts start by trying to complete the existing cases in the inbox.
    -and then register a number of new cases. 

    -Then the tests with different number of threads are carried out. 
    -And finally the script tries to complete the existing cases in the inbox.

* Register of a new case:
    -The script start by trying to complete the existing cases in the inbox .

    -And then after each test for a specific number of threads the 
    -inbox is emptied again. 

###Creating test scripts
The jmeter scripts were developed by using the jmeter gui as a webproxy and recorder of commands
while the cases were made in the normal browser using this webproxy. 

Then bash scripts are used to run jmeter from the command line for different 
number of threads. After the simulations the results are plotted with another script. 

###Running the scripts 
jmeter version 2.11 from 
http://jmeter.apache.org has been used 



./test_inbox_loading.sh -p MYPASSWORD -u MYUSER

./test_create_cases.sh  -p MYPASSWORD -u MYUSER

###Plotting 
There is some plotting capabilities build into jmeter and to 
the extensions. The plots in the scripts are however made in python with 
inspirations from 
http://www.metaltoad.com/blog/jmeter-test-plan-drupal 
Some extra packages are needed to make the plots:
python-libxml2  python-matplotlib python-numpy


###Further studies

* Other orbeon form ? 
* Other test cases ? 
* Realistic load 
* Make tests running the webservice and jmeter on separate machines. 
* .. 

