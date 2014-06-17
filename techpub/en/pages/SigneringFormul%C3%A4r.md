# (Signering av formulär) #

(Edit: **Ej genomfört.** Vi gör konvertering till PDF/A. Denna text dokumenterar en del av tankeprocessen.)
Detta kan bli en del av förstudien. Resonemangsvis sammanfattning av läget midsommar 2013.

## Ursprunglig vision ##

Det ifyllda formuläret konverteras till PDF/A för att kunna långtidslagras. När formuläret är underskrivet/signerat visar Adobe Reader *blue ribbon*, alltså visuellt framträdande blått band där det framgår om dokumentet är signerat och om signaturen är giltig. Man kan också undersöka detaljerna i signaturen.

## PDF/A ##

Att konvertera formulär till PDF/A är fullt möjligt, men måste göras från grunden. Troligen ett par veckors utveckling.

Vi nöjer oss för tillfället med Orbeons PDF och förbättrar utseendet genom att trycka in förbättringar i stylesheet.

## Signatur lagrad i PDF ##

PortWise levererar en signatur enligt XML DSIG (en W3C-rekommendation). De bibliotek vi använder för att hantera PDF (iText) stöder bara CMS (efterföljare till PKCS!#7-formatet). Det är inga teoretiska problem att vi själva implementerar stöd för XML-signatur (känt som XAdES). Det hjälper antagligen inte eftersom Adobe Reader troligen inte klarar denna standard ännu. Dessutom krävs en avsevärd insats och iText kommer antagligen att stödja denna standard så småningom.

## Signatur visas i Adobe Reader ##

Utöver problemet med format på signaturen krävs att rotcertifikatet deltar i Adobe Approved Trust List för att automatiskt komma med i Adobe Reader. Adobe Reader kan bara flagga giltig signatur om rotcertifikatet är känt.

För närvarande är rotcertifikatet från Finansiell ID-Teknik inte publikt över huvud taget. Kommersiell licens krävs. Detta är det största hindret för hantering av signatur i Adobe Reader.

## Slutsats ##

För att komma vidare överger vi tanken på PDF som samlande hanteringsenhet. Formulär konverteras fortfarande till PDF. Det som brukaren signerar är denna PDF plus formulärdefinitionen i original (XML) plus eventuella bilagor. Det blir tekniskt sett ett knippe filer.

Vad ska detta knippe filer kallas i dialog med brukaren? Förslag: *dokumentcell*.

