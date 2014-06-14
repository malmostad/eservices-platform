# Hantera PostgreSQL #
Exemplen antar att flera cluster körs på servern ifråga. Man väljer cluster genom att välja port. I exemplen används port 5435.


* Kör klient på kommandoraden.

```
psql -p 5435 -U inherit InheritPlatform
```

## Börja om med nya Bonita-databaser ##
Som user postgres, i ett shell.

```
dropdb -p 5435 bonita_history
dropdb -p 5435 bonita_journal
createdb -p 5435 -E UTF8 -O bonita bonita_history
createdb -p 5435 -E UTF8 -O bonita bonita_journal
```

