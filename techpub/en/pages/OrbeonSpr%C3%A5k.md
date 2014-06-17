# Språk i Orbeon #

Presentationsspråk för formulär är en kedja av val som börjar i Form Builder. I Form Builder kan man välja ett språk uppe i högra hörnet. Dåligt med dokumenterade ledtrådar om vad detta val innebär.


* Man kan bara välja något av de språk för vilka det finns en översättning av Form Builder.
* Det valda språket används för Form Builder själv, dess knappar och meddelanden.
* Dessutom används det det valda språket om man skapar ett nytt formulär.
* Dessutom listas bara de formulär som har det valda språket (ev. databasberoende).

Det valda språket används alltså för flera olika ändamål som egentligen är oberoende av varandra.

Att ett formulär *har* ett språk betyder att det finns ett element i formuläret (förenklat):

```
<instance id="fr-form-attachments">
  <resources>
    <resource xml:lang="en">
      (ledtexter etc)
    </resource>
  </resources>
</instance>
```
Med reservation: Vissa tecken tyder på att Orbeon tittar på språket i elementet instance/metadata/title. Attributet *xml:lang* finns på många olika ställen, så det är inte självklart vilket som är avgörande.

Det borde vara möjligt att ha mer än en *resource* i ett formulär och därmed göra det flerspråkigt. Form Builder verkar inte stödja detta. Jag gjorde ett formulär på engelska och sparade. Därefter tog jag upp det i editorn och bytte språk till franska, gjorde en ändring, sparade och stängde. Det hade ingen effekt på formulärets språk.

I Form Runner gäller i allmänhet att språket blir det som finns i formuläret. Det påverkar inte formuläret själv, men avgör vilket språk som används för knappar och meddelanden i Form Runner. Lokaliseringen av Form Runner finns dels i de paketerade resurserna som Björn översatte, dels i JavaScript-bibliotek och någon Java-klass.

Det finns inmatningar som presenteras av webbläsaren, exempelvis filväljare. Den knapp som aktiverar filväljaren får texten *Browse* oavsett av formulärets språk.

Det finns andra aspekter av lokalisering som Orbeon kanske inte hanterar, i första hand format på datum och tid. Datum kan visas som M/D/Y på amerikanskt sätt. Det spelar ingen roll vilket språk formuläret har. Däremot är datumväljaren (JavaScript) korrekt anpassad till språket.


**Slutsats:** Det mest rättframma sättet att stödja svenska är att översätta även Form Builder till svenska. Med lämplig konfigurering kan det då bli ett valbart språk.

