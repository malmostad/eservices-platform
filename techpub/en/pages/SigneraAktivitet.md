
# Signering GUI  #


## Förkrav ##
DocBox enligt separat spec samt pdf/a generering av underlag som skall signeras

## Mål ##
Skapa BPMN aktivitetformulär för signering av pdf/a och när det är ifyllt skall signaturen läggas in i pdf/a dokumentet.

Två fall: 

 * Aktivitet för att signera startformuläret (ansökan)
 * Aktivitet för att signera 

## Beskrivning ##
De inloggade användaren får upp en signeringsaktivitet i sin inkorg. Klicka på den och då får man steg för steg:


 1. Sammanställning av vad som signeras i form av länk till en pdf/a som man kan ladda ner (identifieras med ett id som identifierar docId+löpnr), kontrollsumma på pdf samt förklarande text till vad som pågår. En knapp/länk till signera
 2. Signeringsdialog från nexus för BankID öppnas,
 3. Användaren landar på en bekräftelsesida i vårat system

Efter detta kommer ett callback anrop till vår server med själva signaturen. 

 1. Hämta ut dokumentet som signerats från DocBox (identifieras med ett id som identifierar docId+löpnr)
 2. Signaturen verifieras?
 3. Infoga signaturen i PDF/A dokumentet
 4. Spara det signerade dokumentet i DocBox och däri blir löpnr uppstegat. Löpnr kan användas för att göra optimistisk låsning för signeringar.
 5. Rapportera (om allt har gått bra) aktiviteten som utförd till processmotorn.

