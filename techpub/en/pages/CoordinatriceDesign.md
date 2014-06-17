# Coordinatrice #

Ett administrativt gui för att hantera kopplingarna mellan processer och formulär. I första hand för demoändamål, men ger säkert mycket input för en produktifierad variant.

## Databasschema ##

![Untitled image](motricedb_131028.png)
Skiss på motricedb ungefär de delar som används av coordinatrice (ej process activity form instance och taggarna).

## Funktioner ##


* HS1: Se vilka processer som finns
* HS2: Klicka på en process och få upp en lista över deras aktiviteter och vilka formulär de är kopplade till
* HS3: För en aktivitet, ändra koppling till formulär/tjänst (attributet `formpath` i `mtf_activity_form_definition`)

De formulär och tjänster som kan kopplas till en aktivitet är,


* Formulär i postxdb
* `none` inget fomulär
* `signstartform`
* `signactivity/$AKTIVITETSNAMN`
* `paystartform`
* `payactivity/$AKTIVITETSNAMN`

`$AKTIVITETSNAMN` är en annan aktivitet i samma process. (??)

## Björns REST-tjänster ##

`$BASE` är en konstant med värdet
`http://localhost:58080/inherit-service-rest-server-1.0-SNAPSHOT`
Björn gör följande tjänster för de delar som hänger ihop med Bonita.

### BM1: Lista deployade processer ###


```
GET, POST $BASE/getProcessDefinitions
```

Tjänsten returnerar en XML-struktur bestående av ett godtyckligt antal `ProcessDefinitionInfo`.

```
<set>
  <ProcessDefinitionInfo>
    <uuid>ArendeprocessSignStartForm--1.0</uuid>
    <name>ArendeprocessSignStartForm</name>
    <label>ÄrendeprocessSignStartForm</label>
  </ProcessDefinitionInfo>
  ...
</set>
```

### BM2: Lista aktiviteter/formulär ###

Lista vilka aktiviteter finns för en viss process och vilka formulär som är kopplade till aktiviteterna.

```
GET, POST $BASE/getProcessDefinitionDetails/$processDefinitionUuid
```

* **processDefinitionUuid**  
 identifierar en process som hämtats med BM1.

Tjänsten returnerar en XML-struktur `ProcessDefinitionDetails`. Först kommer elementet `process` med detaljer om processen. Därefter elementet `activities` som är en sekvens av `ActivityDefinitionInfo`.

```
<ProcessDefinitionDetails>
  <process>
    <uuid>Arendeprocess--1.0</uuid>
    <name>Arendeprocess</name>
    <label>Ärendeprocess</label>
  </process>
  <activities>
    <ActivityDefinitionInfo>
      <uuid>Arendeprocess--1.0--Beslut</uuid>
      <name>Beslut</name>
      <label>Beslut</label>
    </ActivityDefinitionInfo>
    ...
  </activities>
</ProcessDefinitionDetails>
```

### BM3: Koppla aktivitet till formulär ###


```
GET, POST $BASE/setActivityForm/$activityDefinitionUuid/$formPath
```

* **activityDefinitionUuid**  
 identifierar en aktivitet (se BM2)
* **formPath**  
 identifierar det formulär som ska knytas till aktiviteten, eller `none` om inget formulär ska knytas

Kom ihåg att URL-encoda `formPath`. App/form-namn `banan/gurka` blir `banan%2Fgurka`.

