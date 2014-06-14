# JMX Get Started #

Several Motrice components and tools may be managed via JMX, *Java Management Extensions*. Controlling the log level is an important feature initially. More may be added over time.

This is a how-to for getting started with JMX with *jconsole*, a tool that comes with Java. There are many other ways of connecting with JMX.

Motrice by default does not allow remote JMX connections. We assume you have access to the host where Motrice runs (localhost).


* Start `jconsole` on localhost
* Find the relevant Java process. If there are many Java processes you may have to use `ps -ef` in a terminal window to sort them out.
* Connect locally by accepting an *Insecure connection*
* Select the *MBeans* tab

In the *MBeans* tab you will find the names of Motrice components, like *coordinatrice*, *postxdb* and *docbox* as well as many other names (*Catalina* etc).

Expand the component tree by clicking on the relevant component. Motrice components typically expand into *basicMgmt*, *config*. The *Operations* page is all you need for checking and setting log levels. The same features are also available, one by one, in the *Attributes* page.

The *Notifications* page collects server errors if you click the *Subscribe* button. The *jconsole* gui for notifications is somewhat primitive, but all data is there.


