# Orbeon/plattformen bra att ha #

## Visa alla startformulär ##
Börja med

```
http://eservices.malmo.se:8080/site
```
och logga in. För att se alla startformulär, gå till (obs: inget / på slutet!):

```
http://eservices.malmo.se:8080/site/mycases/startforms
```

## Logga ut i stand-alone miljö ##


```
http://localhost:8080/site/login/logout
```

## Visa formulär i webbläsare ##

Visa tomt formulär: ```
http://eservices.malmo.se:8080/orbeon/fr/$APP/$FORM/new
```
där APP är application name (ex: start) och FORM form name (ex: demo-ansokan).


Visa ifyllt formulär: ```
http://eservices.malmo.se:8080/orbeon/fr/miljoforvaltningen/
inventeringsprotokoll_pcb_fogmassor/view/7115b130-16ea-443e-affe-bc4979d28c34
```
Byt *view* till *pdf* för att få Orbeons standard-pdf.

## Orbeons val av språk (Runner) ##
Om man vill spåra logiken i hur Orbeon väljer språk, ändra i `WEB-INF/resources/config/log4j.xml` Ta bort kommentar runt `category name="org.orbeon.oxf.properties.Properties"` Det blir mycket utskrifter, men man kan söka på *language* eller *preferences*.

## Köra Form Builder ##
Följande url leder till tabellen med alla formulär:

```
http://eservices.malmo.se:8080/orbeon/fr/orbeon/builder/summary
```

**Obs:** Inget snedstreck på slutet i urlen!

## Orbeon fristående ##

Visa lista på formulär:

```
http://localhost:8080/orbeon/fr/orbeon/builder/summary
```

Skapa nytt formulär:

```
http://localhost:8080/orbeon/fr/orbeon/builder/new
```

Redigera existerande formulär (måste veta dess uuid):

```
http://localhost:8080/orbeon/fr/orbeon/builder/edit/a342fef0dc9ea79517745304320dd292d6701684
```

Fyll i publicerat formulär resp editera tidigare ifyllt formulär:

```
http://localhost:8080/orbeon/fr/bells/ding--v001/new
http://localhost:8080/orbeon/fr/bells/ding--v001/edit/a38e257950fc571f78122a5e844ed2ba6d241526
```
där `a38e2...41526` är ett exempel på *formDataUuid*.

## Problem ##

I ett foruminlägg mars 2013 säger Alessandro att tjänster måste returnera `application/xml`. Det går inte att skicka `text/html`.

