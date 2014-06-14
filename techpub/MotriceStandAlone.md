# Motrice Stand-Alone Deployment #

This note contains instructions for deploying the Motrice portal on a stand-alone Tomcat on a Linux platform. The starting point is source code pulled from the Motrice github. We assume you have access to the source code tree.

At the time of writing this note the root directory is named `pawap`. The branch containing the next Motrice release is `develop`. At the time of writing this branch marks the shift to Activiti from Bonita. For the purpose of this note we refer to the root directory as `$ROOT`.

Create a Motrice configuration. For details, see [Motrice Sample Configuration](MotriceSampleConfig.md). If the configuration file does not use the default path, create an environment variable named `MOTRICE_CONF` containing the full path of the configuration file.

Go to `$ROOT`, checkout the appropriate branch. Build all Motrice components with `mvn clean install`.

Go to `$ROOT/inherit-portal`. Generate the distribution with `mvn -P dist`. The outcome is 

```
target/inherit-portal-1.01.00-SNAPSHOT-distribution.tar.gz
```
where the version number may vary over time.

We assume you have prepared a suitable Tomcat 7 under a directory of your choice. We refer to the Tomcat root as `$CATALINA_BASE`. In this directory, unpack the distribution tarball.

```
tar xzf target/inherit-portal-1.01.00-SNAPSHOT-distribution.tar.gz -C $CATALINA_BASE
```


* In `$CATALINA_BASE/webapps` make sure you purge directories created when the previous war files were expanded

Follow the [general instructions](http://www.onehippo.org/7_7/library/deployment/create-and-deploy-a-project-distribution.html) for installing Hippo. Begin from the heading *Deploying your project to a Tomcat 6 installation* (even though we now use Tomcat 7). You will have to modify `conf/catalina.properties` by adding to the `common.loader` and `shared.loader` properties.

Decide on a directory for the JCR repository to live.

The next step is to create a file `setenv.sh` in `$CATALINA_BASE/bin`. Here is a sample.

```
# Clarify which Java to use, if necessary
export JAVA_HOME=/usr/lib/jvm/java-7-oracle

# Add JVM options, memory in particular
export CATALINA_OPTS#"$CATALINA_OPTS -server -Xms512m -Xmx1024m -XX:PermSize#512m"

# Add repository conf
export CATALINA_OPTS#"$CATALINA_OPTS -Drepo.upgrade#false -Drepo.config=file:${CATALINA_BASE}/conf/repository.xml"

# Add Log4j conf
export CATALINA_OPTS#"$CATALINA_OPTS -Dlog4j.configuration#file:${CATALINA_BASE}/conf/log4j.xml"

# Add path to the repo. For production deployment, choose a different disk.
export CATALINA_OPTS#"$CATALINA_OPTS -Drepo.path#${CATALINA_BASE}/jcr"

# Motrice config.
export MOTRICE_CONF=/usr/local/etc/motrice/motrice-test-config.properties
```

## Temporary Modifications ##

Apply these modifications if needed. The files to modify are accessible after you start and stop Tomcat once to have the war files expanded.

`webapps/orbeon/WEB-INF/resources/config/properties-local.xml`.
To make *Send* work for filled-out forms you may have to edit The original configuration

```
<property as#"xs:anyURI" name#"oxf.fr.detail.send.success.uri.*.*"
    value="http://eservices.malmo.se/site/mycases/form/confirmdispatcher"/>
```
must be changed to

```
<property as#"xs:anyURI" name#"oxf.fr.detail.send.success.uri.*.*"
    value="http://localhost:8080/site/mycases/form/confirmdispatcher"/>
```

`webapps/orbeon/WEB-INF/resources/config/log4j.xml`.
If you start Tomcat from `$CATALINA_BASE` you should edit Change two instances of the log file name to read

```
<param name#"File" value#"logs/orbeon.log"/>
```

## Startup ##

From `$CATALINA_BASE`, start Tomcat with `bin/startup.sh`.

You may want to monitor what happens in a different terminal. For example, `tailf logs/catalina.out` and `tailf logs/orbeon.log`.

You will note that the `repo.config` command line argument points to a file names `repository.xml`. After starting Tomcat and the cms webapp has been expanded you will find a starting point for this file in

```
$CATALINA_BASE/webapps/cms/WEB-INF/classes/org/hippoecm/repository/repository.xml
```

