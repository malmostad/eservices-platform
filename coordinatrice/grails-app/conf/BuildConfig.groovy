/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
 *
 * Copyright (C) 2011-2014 Motrice AB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * e-mail: info _at_ motrice.se
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
 * phone: +46 8 641 64 14
 */
grails.server.port.http = 28080

grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]

grails.war.resources = { stagingDir ->
  // Servlet JAR should not be included in the war, causes trouble
  // See: http://jira.grails.org/browse/GRAILS-9483
  delete(file:"${stagingDir}/WEB-INF/lib/javax.servlet-api-3.0.1.jar")
}

grails.project.dependency.resolution = {
  pom true
  // inherit Grails' default dependencies
  inherits("global") {
    // specify dependency exclusions here; for example, uncomment this to disable ehcache:
    // excludes 'ehcache'
  }
  log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
  checksums true // Whether to verify checksums on resolve
  legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

  repositories {
    inherits true // Whether to inherit repository definitions from plugins

    grailsPlugins()
    grailsHome()
    grailsCentral()

    mavenLocal()
    mavenCentral()

    // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
    //mavenRepo "http://snapshots.repository.codehaus.org"
    //mavenRepo "http://repository.codehaus.org"
    //mavenRepo "http://download.java.net/maven/2/"
    //mavenRepo "http://repository.jboss.com/maven2/"

    // For Activiti
    mavenRepo name: "Activiti",
      root: "https://maven.alfresco.com/nexus/content/repositories/activiti/"
  }

  dependencies {
    // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

    // runtime 'mysql:mysql-connector-java:5.1.22'
    runtime "postgresql:postgresql:9.2-1002.jdbc4"

    // Activiti dependencies
    compile ("org.activiti:activiti-engine:5.14") {
      excludes "livetribe-jsr223"
    }
    runtime "org.activiti:activiti-bpmn-model:5.14"
    runtime "org.activiti:activiti-bpmn-converter:5.14"
    runtime "org.activiti:activiti-spring:5.14"
    runtime "org.mybatis:mybatis:3.2.3"
  }

  plugins {
    //compile(':rest-client-builder:2.0.0') {
    //  export = false
    //}
    runtime ":hibernate:$grailsVersion"
    runtime ":jquery:1.8.3"
    runtime ":resources:1.2"

    // Uncomment these (or add new ones) to enable additional resources capabilities
    //runtime ":zipped-resources:1.0"
    //runtime ":cached-resources:1.0"
    //runtime ":yui-minify-resources:0.1.5"

    build ":tomcat:$grailsVersion"

    runtime ":database-migration:1.3.2"

    compile ':cache:1.0.1'
  }
}
