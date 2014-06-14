# Motrice Task Form Engine #

## Database Design ##

This section provides an overview of the Motrice Task Form Engine database, relationships and semantics. Details of the current schema are not documented here since they are best found out with a database tool like *pgAdmin*.

Task Form Engine table names are prefixed by `mtf_`.

![Untitled image](motrice-02.png)

// Attribut med vinklar `>>` kan diskuteras. Förslag följer. 
Utöver ändringar i nuvarande schema behövs nya begrepp för versionshantering av processer tillsammans med sina formulär. //

* **processdefinitionuuid, activitydefinitionuuid**  
 Byt *uuid* mot *id*. Har aldrig varit uuid, det var Bonita som kallade det uuid utan anledning.
* **formpath**  
 Mycket semantik inbakad i detta attribut. Delas upp i flera attribut (prel namnförslag): *action* (none, form, sign...), *handler* (typ av formulär, Orbeon, ...), *parameter* (olika beroende på handler). Ett sätt att inte låsa upp gränssnittet mot processmotorn.

// Jag föredrar kortare namn, exempelvis *formdef*, *forminst*.
I första hand en smakfråga, men över en viss längd minskar läsbarheten. /Håkan //

