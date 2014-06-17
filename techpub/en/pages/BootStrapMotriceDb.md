### Skapa initial Motrice databas ###

1. Ta backup av existerande databasen (för säkerhets skull om sådan finns)


2. Skapa ny, tom databas med existerande demodata

   gå till $MOTRICE_HOME/database/bin och kör scripten

```
   sudo sh -c "./delete_database.sh"
   sudo sh -c "./create_database.sh"
   sudo sh -c "./create_tables.sh"
```
### Insert av demodata i tom databas ###

1. gå till $MOTRICE_HOME/database/bin och kör scripten

```
   sudo sh -c "./execute_file.sh motricedb ../create/motrice.postgres.demodata.sql"
```

2. Kör main metoden i org.inheritsource.service.processengine.ActivitiEngineService för att deploya processer som förutsätts i scriptet motrice.postgres.demodata.sql.

   ```
cd inherit-service/inherit-service-activiti-engine/
mvn exec:java -P standalone
```

### Att skapa nytt eller ändrat demodata för motricedb ###


1. Skapa initial motricedatabas och ladda med demodata enligt ovan.


2. Lokalisera högsta Hibernate sequence ( max(id) ) i någon av tabellerna med prefix

   pxd, och ställ ner hibernate-sequence för databasen till detta värde +1


3. Lägg till och ändra formulär. Notera noggrannt ny formpath version (applikation/formname--<version>


4. Ta backup av databasen


5. Ersätt COPY statements från motrice.postgres.demodata.sql med de motsvarande från backupen nyss skapat (orbeon formulär i postxdb)


6. Om enbart nya formulärversioner har skapats, justera versionsnummer för dessa i motrice.postgres.demodata.sql


7. Om nya formulär, och aktiviteter lägg till dessa enligt "mönstret".

