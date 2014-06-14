# DocBook till PDF/A: Att få det att funka #

Detta gäller specifikt för konvertering av formulär till Pdf i plattformen. Verktygen går naturligtvis att använda mer generellt än så, men detta blir ett tillägg till installation av plattformen. Formuläret konverteras först till DocBook/XML och därefter till Pdf.

## Processen i sammanfattning ##


* Egen kod konverterar formuläret till en DocBook-artikel i XML. Med Groovy är det inte så många rader.
* `xsltproc` konverterar XML till FO (Formatting Objects). xsltproc är skriven i C, är snabb. Det funkar säkert med andra XSLT-processorer som Saxon (Java). Konverteringen styrs av ett stylesheet. Hittills har vi använt de som följer med DocBook utan modifiering. 
* `FOP` (Java) konverterar FO till PDF/A.

## Externa resurser ##

Paket som behövs:

* docbook-xml
* docbook-xslt
* xsltproc
* fop
* libxml2-utils
* xmlstarlet

Font-paket som behövs för vår tillämpning

* fonts-liberation
* fonts-opensymbol

## PDF/A och fonter ##

PDF/A kräver bland annat att alla fonter är inbäddade i dokumentet. Även de 14 fonter som ska finnas i varje PDF-läsare ("base 14").

Valet av fonter görs i XSLT-transformationen. Med standardinställningar genereras FO där fonterna heter "serif", "sans-serif" och "monospace". Alltså generiska namn. Då lämnar man åt omgivningen att plocka upp lämpliga fonter. En så löslig metod duger förstås inte om man siktar på PDF/A.

Därför måste man speca fonter vid XSLT-konverteringen. De traditionella Times-Roman, Helvetica och Courier kräver licens vilket inte funkar i vår affärsmodell. Jag valde (lite godtyckligt) **Liberation** för vanlig text och **OpenSymbol** för symboler (som punkterna i punktlistor). Båda är fria och installeras på vanligt sätt med *apt-get*.

Återstår att hitta fri motsvarighet till Zapf Dingbats, den sista fonten av de 14 grundläggande. Det verkar svårt. Eller snarare finns det en uppsjö av dingbats för olika ändamål, naturligtvis helt ostandardiserade. Kanske vi kan avhålla oss från att ha dingbats i formulären. Svårare än så är det inte. Om inga symboler finns i texten behövs inte heller någon font.

`xsltproc:` För att få med fonterna i stylesheet räcker det med parametrar på kommandoraden.
Man behöver inte meka med stylesheet av denna anledning.

`FOP:` Man måste tala om för FOP var den hittar fontinformationen.
Detta specas i en konfigurationsfil.

Återstår bara att speca `-pdfprofile PDF/A-1a` för FOP så snurrar det.

## PDF/A och metadata ##

PDF/A kräver också att metadata bäddas in i dokumentet. I skrivande stund har vi inget sådant.

Till skillnad från fonterna är metadata ingen teoretisk utmaning. Det går att stoppa in programvägen i FO-filen eftersom de inte påverkar layouten. Metadata visas i Properties i den färdiga PDF-filen.

FOP 1.1 accepterar XMP metadata i XML-filen. Ett paket som man stoppar in mellan `fo:layout-master-set` och `fo:page-sequence`. Exempel, varken komplett eller särskilt genomtänkt, men funkar.

```
...</fo:layout-master-set>
<fo:declarations>
  <x:xmpmeta xmlns:x="adobe:ns:meta/">
    <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
      <rdf:Description rdf:about=""
          xmlns:dc="http://purl.org/dc/elements/1.1/">
        <!-- Dublin Core properties go here -->
        <dc:title>Orbeon Bells &amp; Whistles</dc:title>
        <dc:description>Ett försök att pula in en räkmacka på Åland av Håkan Söderström</dc:description>
	<dc:identifier>22S7HR-0</dc:identifier>
	<dc:source>47986d65d0970124ab40da04cb146b0546ab3c85</dc:source>
	<dc:relation>bells/whistles--v002</dc:relation>
      </rdf:Description>
    </rdf:RDF>
  </x:xmpmeta>
</fo:declarations>
<fo:page-sequence ...>...
```

## Nivåer av PDF/A ##

Det finns varianter av PDF/A numrerade 1, 2, 3. Av dessa är bara PDF/A-1 aktuell för oss. De andra är så nya att de inte kommit i allmänt bruk ännu.

Inom PDF/A-1 finns två nivåer, PDF/A-1a och PDF/A-1b. PDF/A-1a är den mest krävande och innebär bland annat *Tagged PDF*, alltså att strukturinformation och inte bara visuellt utseende vidarebefordras till PDF. PDF/A-1b är en delmängd av PDF/A-1a.

Vi tillämpar PDF/A-1a.

## Kommandoraden ##

Konkret exempel på hur kommandoraden ser ut. Alla filer använder samma rotnamn. Rotnamnet är inparameter.


```
ROOT="$1"
PDFSTYLE=/usr/share/xml/docbook/stylesheet/docbook-xsl/fo/docbook.xsl

xsltproc \
--stringparam use.extensions 0 \
--stringparam paper.type A4 \
--stringparam page.margin.inner 28mm \
--stringparam page.margin.outer 18mm \
--stringparam body.start.indent 0pt \
--stringparam generate.toc "" \
--stringparam body.font.family LiberationSerif \
--stringparam title.font.family LiberationSans \
--stringparam monospace.font.family LiberationMono \
"$PDFSTYLE" "$ROOT.xml" > "$ROOT.fo"

fop -pdfprofile PDF/A-1b -c <fop-config-file.xml> -fo "$ROOT.fo" -pdf "$ROOT.pdf"
```

