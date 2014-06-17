# Motrice Release 0.5 #

Kort vad som ska göras och lite ledtrådar.

## Activiti ## 

### TODO ###

 * Lös möjlighet att skicka med startparametrar till subprocess som t.ex. underlättar förenklad delgivning. 
 * Förbättra felhantering och loggning för att möjliggöra [och [https://github.com/malmostad/eservices-platform/issues/15](https://github.com/malmostad/eservices-platform/issues/38])
 * Uppstädning och förbättringar så mycket vi hinner. Ganska rörigt i  TaskFormService f.n.
 * bpmn20.xml filer och formulär som vi skall klara i releasen


### Buggar ###

 * Som det är nu kan kandidaten admin låsa en uppgift och sedan kan kandidaten Jack trycka på send, och detta ger texten "Laddar formulär... Vänta" men inget händer. Borde ge en bra feldialog.
 * Säkerhetsfråga: Följande URL syns i browser och är editerbar. http://eservices.malmo.se/site/mycases/form?taskUuid=xxxxx
 * Exeption i början när man bygger motrice som har att göra med Timeline och att det är ett interface.
 * save/send buggen. Kan vara cargo som spelar in?


### Ej utredda frågeställningar ###

 * User validering på någon av följande metoder i Activiti? executeTask, startProcess, assignTask, addComment, addCandidate, removeCandidate. Möjligen är det redan löst då en användare enbart kan göra saker på det som den har i sin inkorg.
 * User validering på någon av följande sökmetoder i Activiti? getProcessInstancesWithInvolvedUser, getProcessInstancesStartedBy, getPagedProcessInstanceSearchResult, getProcessInstancesByUuids. En användare kan inte göra saker i systemet på de processer sökningen hittar. Men frågan är om det är illa att en viss användare kan se processer som den inte har att göra med?

### Test ###

 * Skapa ett lite större testflöde som tar med fler finesser som vi använder. 
 * Skapa automatiserade tester med  testflödet. 

### Refactoring ###

 * Namnförbättringar: uuid -> Id för många olika id'n
 * ProcessInstanceDetails-objektet activities kan man kanske optimera bort?
 * Felhantering vad gäller exceptions och returvärden så det blir enhetligt och snyggt i Activi-implementationen. Ibland returneras null och ibland passar det bäst med ett exception. Går säkert att snygga till mer enhetligt.


## Hippo Site (inherit-portal-site) ##

### Aktivitetsguider ###
[https://github.com/malmostad/eservices-platform/issues/30]
Inför fall back när inte guide finns i Hippo. Då ska dokumentationen från aktivitet visas från BPMN diagrammet. Guiden laddas i /inherit-portal-site/src/main/java/org/inheritsource/portal/components/mycases/Form.java och där behöver man då komplettera med anrop mot inherit-service där BPMN dokumentationen för aktiviteten hämtas.

### Komin användare ###

 * Starta process där viss invånare ingår (med personnr/komin user name) [https://github.com/malmostad/eservices-platform/issues/46]
 * Begär komplettering av inskickad ansökan (startformulär) och skicka med meddelande om vad som behöver kompletteras. [https://github.com/malmostad/eservices-platform/issues/45] 
 * Lägg ett startformulär i en viss användares inkorg (med personnr/komin user name. Kanske även möjligt att fylla i delar? Kanske även submitta? [https://github.com/malmostad/eservices-platform/issues/19]

#### Javascript (JQuery) och LDAP ####
Skapa en dialog där man kan söka komin användare i LDAP katalogen t.ex. utifrån avdelning roll och namn för att på så sätt ange t.ex. kandidater till en aktivitet. Denna dialog kommer till användning på flera ställen.

 * [https://github.com/malmostad/eservices-platform/issues/48]
 * [https://github.com/malmostad/eservices-platform/issues/21]
 * [https://github.com/malmostad/eservices-platform/issues/20] (kanske)

Utöver det gör refresh efter kandidater ändras och detta inkluderar även [https://github.com/malmostad/eservices-platform/issues/44]

Det är mest i denna jsp sida som det ska in mer finesser: /inherit-portal-site/src/main/webapp/WEB-INF/jsp/mycases/internal/web/form/main/right.jsp

### Ersätt Mina sidor med ny version ###
 inspirerad av studentarbetet i Malmö: [https://github.com/malmostad/mima]

### Felsidor ###
[och [https://github.com/malmostad/eservices-platform/issues/15](https://github.com/malmostad/eservices-platform/issues/38])
Förbättra felsidor så att de blir snygga och innehåller någon lämplig information om man kontaktar support.

## Orbeon ##

 * Svensk lokal [https://github.com/malmostad/eservices-platform/issues/43]
 * Shell script för att starta om orbeon server [https://github.com/malmostad/eservices-platform/issues/40]
 * Kolla att återanvändbara komponenter fungerar i Orbeon (tror den heter library i en Application)

## Coordinatrice ##

 * Deploya processer eller redigera processer. Antingen med Activiti designer (eclipse plugin) med uppladdning till server. Alternativt Activiti Modeler som är javascriptbaserad och körs i webbläsaren. Lite klurigt vad som är bäst här tyvärr. [https://github.com/malmostad/eservices-platform/issues/23]
 * Hitta på något fiffigt sätt att redigera aktiviteters guider i Hippo från Coordinatrice
 * Dito för redigering av startformulär i Hippo
 * Få till flödet med redigering av formulär och processer så att körande instanser har kvar sina formulär när ny processversion deployas eller nya formulärkopplingar publiceras.
 * Möjlighet att importera/exportera formulär, processerer och kanske även processinstanser.

## Företagsanvändare ##
Klura ut och fixa hur man får till det med användare som sköter företagsärenden. Något med att firmatecknare ger fullmakt till vissa användare att utföra e-tjänster i företagets namn. Det finns lite processer från Björn R för detta. Vi har stärkt våra möjligheter med signeringen. Nu kan vi ha flerparts underskrift av fullmakt. En tanke är att skapa poster för företag i LDAP katalogen och även ge roller där för att utföra olika saker. Firmatecknare kommer tyvärr inte finnas elektroniskt tillgängliga utan kommer innebära manuell kontroll i kommunen.


 * [https://github.com/malmostad/eservices-platform/issues/16]
 * [https://github.com/malmostad/eservices-platform/issues/17]
 * [https://github.com/malmostad/eservices-platform/issues/18]


