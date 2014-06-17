# Certifikat och signering i Adobe Reader 9 #

Den heter alltså inte Acrobat Reader.

## Problem med Rendering Library ##

I flera menyval, exempelvis *Document / Security Settings...* får jag varningen *Unable to find the HTML rendering library*. Jag har lagt in rätt katalog i *Edit / Preferences / Internet*, nämligen `/usr/lib/firefox`, för där finns `libxul.so` som efterfrågas. Jag kör Ubuntu 12.10.

## Standard rotcertifikat ##

Adobe Reader har en bunt inbyggda rotcertifikat som den litar på. Man kan installera ytterligare, men det intressanta är vilka som följer med som standard. Visas med menyval *Document / Manage Trusted Identities* + välj *Certificates* i drop-down-listan längst upp. Här finns *Adobe Root CA*, men även kända namn som Verisign och Globalsign. Det är en betydligt kortare lista än i vanliga webbläsare.

## Installera rotcertifikat ##

Se till att ha rotcertet på PEM-format (vilket det troligen redan är). Alltså en textfil där första raden innehåller `BEGIN CERTIFICATE` och sedan innehållet i base64.

Menyval: *Document / Manage Trusted Identities*. Välj *Certificates* i den översta drop-down-listan, därefter knappen *Add Contacts*. Bredvid rutan *Contacts* finns knappen *Browse*. Välj filen med rotcert. Nu dyker certet upp i den undre rutan.

Se till att sätta *Trust* på det nya certet. Kan även fixas senare med *Edit Trust* i den första dialogen (*Manage Trusted Identities*). Kryssa för *Use this certificate as a trusted root* och *Certified documents*.

Hmm. När jag skriver detta är jag inte säker på skillnaden mellan *signed document* och *certified document*.

## Flaggning av signatur ##

Om dokumentet innehåller ett signaturfält visar Reader en blå remsa under menyerna. Till höger finns en knapp *Signature Panel*. Den är en av alla paneler som kan visas till vänster om dokumentinnehållet. Kan även nås från verktygspanelen längst ut till vänster. Signaturpanelen innehåller alla detaljer om signeringen och den certifikatkedja som används.

Ett signaturfält behöver inte vara synligt.

## Att signera en pdf ##

Det finns ett antal menyval under *Document / Sign*, men jag har inte lyckats få något att hända. Har bara signerat programvägen.

