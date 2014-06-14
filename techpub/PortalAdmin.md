# Portalen: Hantering #

## Stor omstart ##

* Rensa alla Postgres-databaser
* Kompilera och kopiera över plattformens restserver

```
cd inherit-platform; mvn clean install
# Single long line
cp $PLATFORM_ROOT/inherit-service/inherit-service-rest-server/target/
inherit-service-rest-server-1.0-SNAPSHOT $BONITA_ROOT/webapps
rm -rf $BONITA_ROOT/webapps/inherit-service-rest-server-1.0-SNAPSHOT/
```

* Ladda om databasen (Postgres InheritPlatform). Ev måste man ändra JDBC connection properties till `hibernate.hbm2ddl.auto = create` (normalt är den satt till update).

```
cd $PLATFORM_ROOT/inherit-service/inherit-taskform-engine
mvn exec:java -P standalone
```

## Lägg upp ett nytt startformulär ##


1. Skapa ett nytt eservice dokument i Hippo. Öppna http://localhost:8080/cms. Skapa ett eservicedocument t.ex. under inheritportal-sv/Startformulär/start (url-namnet ska för närvarande helst matcha formpath dvs formuläret start/test ska helst definieras av test i Startformulär/start katalogen). Fyll i titel och form path i eservicedokumentet, spara, publicera.


2. Definiera i pgsql InheritPlatform databasen. Lägg till rad i startformdefinition
* **startformdefinitionid**,  internt id
* **authtypereq**,  sätt till USERSESSION (processinstansens startedBy blir inloggad användare)
* **formpath**, identifierar orbeonformuläret som är startformulär
* **processdefinitionuuid**, anger vilken process som ska startas (ifall man inte skriver hela uuid:et i bonita så slår plattformen upp senaste version, kommer förmodligen att tas bort som funktionalitet senare....skriv gärna processdefinitionsuuid:t här.
* **userdataxpath**, används inte vid USERSESSION authtypereq



