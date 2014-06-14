# Hantera MySQL #
I exemplet heter både användaren och databasen *orbeon*, vilket inte är så pedagogiskt. Deras namn är naturligtvis helt oberoende av varandra.


* Köra klient på kommandoraden. Avslutande semikolon krävs alltid efter kommandon. Avsluta med \G för att få en vertikal lista.

```
mysql -p -u orbeon orbeon
```

## Skapa användare och schema ##
Typisk sekvens för att skapa ett schema med en användare. Databasen antas existera sedan tidigare.

```
mysql -p --user=root mysql
create user 'orbeon'@'localhost' identified by 'PW';
create schema orbeon character set='utf8';
grant all privileges on orbeon.* to 'orbeon'@'localhost';
```

## Dump & restore ##

* Dump

```
mysqldump -p --user=orbeon orbeon > orbeon-mysql.dump
```

* Restore from dump

```
mysql -p --user=orbeon orbeon < orbeon-mysql.dump
```

