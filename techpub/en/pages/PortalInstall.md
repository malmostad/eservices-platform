# Installation av inherit-platform #

För att installera en Motrice server produktionslikt så finns det några script som hjälper samt några magiska handgrepp med muntlig tradition. Här förutsätts man egentligen ha installerat beroenden som för t.ex. docbook generering som anges i dokumentation av respektive modul.

## Skapa server installation, kompilera och deploya lösningen ## 


 * create-new-deploy-environment.sh skapar en server att installera motrice på med t.ex. openam policy agenter.


 * deploy-inherit-platform.sh. Kompilerar, deployar och startar om motrice servrarna.

Båda är beroende av att filen current_config.sh innehåller korrekt konfiguration. För en lokal testinstallation definieras lämpligen länken current_config.sh -> config_deploy_minburk.sh

## Förberedelser ##

### Apache2 konfiguration ###


 * Kopiera filerna conf/apache2/eminburk.malmo.se-ssl.conf och conf/apache2/kminburk.malmo.se.conf till /etc/apache2/sites-available
 * Kopiera filerna med egenutfärdat ssl cert till eminburk.pem och eminburk.key till /etc/apache2/ssl/


```
sudo a2enmod ssl proxy_ajp
sudo a2ensite eminburk.malmo.se-ssl.conf
sudo a2ensite kminburk.malmo.se.conf

# För orbeon måste egen utfärdade certikatet läggas till i javas keystore
sudo keytool -importcert -trustcacerts -alias eminburk -file /etc/apache2/ssl/eminburk.pem  -keystore /etc/ssl/certs/java/cacerts
```

### Open DJ ###


### Hippo site config ###

inherit-portal/site/src/main/webapp/WEB-INF/hst-config.properties skall vara konfigurerad för RMI.

inherit-portal/site/src/main/webapp/WEB-INF/web.xml skall ha ett filter enligt nedan:

```
<!-- NB!! The following OpenAM j2ee agent filter and mapping must come before
       other filters, if not white screen when trying to reach openam protected pages OPENAM_FILTER_BEGIN -->
  <filter>
    <filter-name>Agent</filter-name>
    <filter-class>com.sun.identity.agents.filter.AmAgentFilter</filter-class>
  </filter>

  <filter-mapping>
    <filter-name>Agent</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>
    <dispatcher>INCLUDE</dispatcher>
    <dispatcher>FORWARD</dispatcher>
    <dispatcher>ERROR</dispatcher>
  </filter-mapping>
<!-- OPENAM_FILTER_END of openAM filter -->

```

Notera att filtret är utkommenterat i git eftersom vi inte vill ha openam konfiguration i uteckling med cargo.run.

### Konfiguration av motrice  ###

Kopiera conf/motrice.properties till /usr/local/etc/motrice/motrice.properties Redigera den med lösenord och var noga med att extern url:ar stämmer med din installation. Typiskt är till exempel att orbeon laddas från fel url och därmed blockeras av webbläsaren. 

Fyll på med viktiga parametrar här!

## Äldre dokumentation ##

Det finns information om att installera Motrice i dokumentet dev-deploy.html/dev-deploy.pdf som gäller. Dock börjar den bli rejält utdaterad.

## Generera dev-deploy.htm/dev-deploy.pdf från projekt-katalogen ##

Installera följande:

 * sudo apt-get install docbook
 * sudo apt-get install docbook-xsl
 * sudo apt-get install xsltproc
 * sudo apt-get install fop

Flytta till doc/dev-deploy-katalogen.

För att generera pdf: ./xsltproc.sh -pdf dev-deploy

För att generera html: ./xsltproc.sh -html dev-deploy



