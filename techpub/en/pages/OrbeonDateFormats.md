# XSLT datumformat i Orbeon (Kurze Einführung) #

Det här gäller `resources.xml`, alltså den fil som innehåller alla meddelanden från Form Runner på alla språk. I senare versioner av Orbeon har det tillkommit format för datum och klockslag. Sättet att speca format är inte det minsta likt *java.text.SimpleDateFormat*. Formatkoderna kommer från XSLT 2 (en av nyheterna i 2:an). En formatsträng kallas där *picture* precis som i Cobol.

En formatsträng (picture) börjar med en *component specifier*, i princip en bokstav inom fyrkantiga parenteser.


|Kod |Betyder |Presentation |
|----|--------|-------------|
|Y |Year |1 |
|M |Month in year |1 |
|D |Day in month |1 |
|d |Day in year |1 |
|F |Day of week |n |
|W |Week in year |1 |
|w |Week in month |1 |
|H |Hour of day (24h) |1 |
|h |Hour of day (12h) |1 |
|P |AM/PM |n |
|m |Minute of hour |01 |
|s |Second of minute |01 |
|f |Fractional second |1 |
|Z |Time zone (numeric or alphabetic) |1 |
|z |GMT time zone (ex: GMT+1) |1 |
|C |Calendar |n |
|E |Era (Reign of emperor) |n |

Exempel: `[Y]` är en giltig formatsträng som resulterar i fullt årtal.

Bokstäverna `C`och `E` får anses vara överkurs. Utom de kalendrar vi vanligen hör talas om ska dessa täcka alla tänkbara tideräkningar.

Presentationen av en *component specifier* styrs av de följande tecknen, som kallas *modifier*. I tabellen ovan innehåller kolumnen *Presentation* standardpresentationen, den man får utan *modifier*.


|Modifier |Betyder |Exempel (3, 30, 300)|
|---------|--------|--------------------|
| 1|Decimalt med så många siffror som behövs |3, 30, 300  |
| 01|Decimalt med nollutfyllnad |03, 30, 300 |
| 001|Decimalt med nollutfyllnad |003, 030, 300 |
| i|Romerska siffror |iii, xxx, ccc |
| I|Romerska siffror |III, XXX, CCC |
| n|Namn (på veckodagar, månader etc) |måndag |
| N|Namn (versaler) |MÅNDAG |
| Nn|Namn ("camel case") |Måndag |
| w|Ord |tre, trettio, trehundra |
| W|Ord |TRE, TRETTIO, TREHUNDRA |
| Ww|Ord |Tre, Trettio, Trehundra |

Vilket språk används när resultatet är ord? Funktionerna `format-dateTime` och dess variationer kan ha olika antal argument. I den korta versionen ingår inte språk. Då används ett implementeringsberoende standardvärde. Den långa versionen har explicita argument för språk (och land).

Presentationen kan följas av

|Modifier |Betyder |Exempel 1, 2, 3 |
|---------|--------|----------------|
|t |Grundtal (när behövs det?) |1, 2, 3 |
|O |Ordningstal |1:a, 2:a, 3:e |

Med eller utan presentation kan man alltid tillfoga en längdangivelse som börjar med ett kommatecken.


|Modifier |Betyder |
|---------|--------|
|,1 |Minst en siffra eller en bokstav |
|,1-1 |Antal tecken minst ett, högst ett (= exakt en siffra) |
|,2 |Minst två tecken |
|,2-3 |Antal tecken minst två, högst tre |
|,*-3 |Obestämd minsta längd, största längd är tre |
|osv |Fungerar alltså även för namn på veckodagar etc |

Om presentationen och längdangivelsen är oförenliga så gäller den explicita längdangivelsen. Exempel: `[0001,1-2]` tolkas som `[1,1-2]`.

Exempel på vanliga format som gäller datum och klockslag. Vi antar att språket är svenska.


|Format |2014-01-16 kl 15:17:09 |
|-------|-----------------------|
|[Y]-[M]-[D] |2014-1-16 |
|[Y0001]-[M01]-[D01] |2014-01-16 |
|[D] [Mn] [Y] |16 januari 2014 |
|[F] [D] [Mn] |Torsdag 16 januari |
|[Fn,*-3] [D] [Mn,*-3] |tor 16 jan |
|[H]:[m]:[s] |15:17:09 |

Kan nämnas att den intelligens som får tal att skrivas ut som ord kommer från en klass som heter `Numberer` som finns i en massa varianter. Vi har bidragit med en `Numberer_sv` till Orbeon. Stöd för namn på dagar och månader behövs också för Javascript, därav `CalendarResources.js`.




