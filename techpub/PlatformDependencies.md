# Motrice Dependencies with Versions #

Platform releases are listed with main new features and dependencies with recommended versions.

Descriptions of future releases are tentative.

## Motrice Release 0.3 2013-09-30 ##

This relase is based on the following components and versions.


* Ubuntu 12.04 LTS
* Orbeon Forms 4.3
* Activiti 5.1.3
* OpenJDK 7u25
* PostgreSQL 9.2 via backport https://wiki.postgresql.org/wiki/Apt
* Tomcat 7.0.42
* Apache 2.2.22


**JDBC for PostgreSQL (2013-09-05):** The 9.2 version is not yet in the Maven Central Repository.

Include it in your project in one of the following ways.

### Automated ###

The latest available version is the following.

```
<dependency>
    <groupId>postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>9.2-1002.jdbc4</version>
</dependency>
```
You must add the following repository declaration to your POM.

```
<repository>
    <id>Useful for finding PostgreSQL stuff</id>
    <url>https://repository.sonatype.org/content/groups/forge</url>
</repository>
```

### Manual ###

The latest version available for download is as follows.

```
<dependency>
    <groupId>postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>9.2-1003.jdbc4</version>
</dependency>
```

Download it manually from http://jdbc.postgresql.org/download.html . Add it to your ~/.m2 cache this way.

```
mvn install:install-file -DgroupId#postgresql -DartifactId#postgresql -Dpackaging=jar \
-Dversion#9.2-1003.jdbc4 -Dfile#${downloads}/postgresql-9.2-1003.jdbc4.jar \
-DgeneratePom=true
```
where `${downloads}` is your download directory.

