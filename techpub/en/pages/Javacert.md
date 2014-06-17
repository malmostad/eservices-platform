# Signering av PDF med Java/iText: Bakgrund #

## Versioner av iText ##

Signering i iText är helt omgjort från och med version 5.3.0. Exemplen i boken (kap 12) fungerar inte. Fungerande exempel finns här: http://itextpdf.com/book/examples.php

En anledning till ändringarna är kommande PDF 2.0 där vissa mekanismer som fanns tidigare tas ur bruk.

PDF Advanced Electronic Signatures (PAdES):


* PAdES Part 1 är en översikt
* PAdES Part 2 PAdES Basic, Profile based on ISO 32000-1. Stöds av iText sedan version 5.0.0.
* PAdES Part 3 PAdES Enhanced, PAdES BES and PAdES-EPES (Basic resp Explicit Policy Electronic Signature). Stöds av iText sedan version 5.3.0.
* PAdES Part 4 PAdES Long Term, PAdES-LTV Profile. Stöds av iText sedan version 5.1.3.
* PAdES Part 5 PAdES for XML Content, Profiles for XAdES signatures. Stöds ännu inte av iText.
* PAdES Part 6 Visual representations of Electronic Signatures. Stöds av iText men beror även på andra faktorer.

ETSI CAdES (som är intressant för oss) stöds av iText från version 5.3.4. Adobe Reader klarar CAdES först från version X (alltså 10).

CMS (Cryptographic Message Syntax) är efterföljaren till PKCS!#7. CAdES betyder CMS Advanced Electronic Signatures. För den som är van vid tidigare standard är både CAdES och PAdES starkt PKCS!#7-centrerade.

Inga tecken tyder på att PortWise kan generera CMS/PKCS!#7.

Bouncycastle används genomgående i exemplen.

## Specialare i PDF ##

Enligt ISO 32000-2008 Annex E är det tillåtet att göra specialare.

 Private data may be added to PDF documents that enable conforming reader’s to change behavior based on this data. At the same time, users have certain expectations when opening a PDF document, no matter which conforming reader is being used. PDF enforces certain restrictions on private data in order to meet these expectations.

 A conforming writer or conforming reader may define new types of actions, destinations, annotations, security, and file system handlers. If a user opens a PDF document using a conforming reader for which the new type of object is not supported, the conforming reader shall behave as described in Annex I.

 A conforming writer **may also add keys to any PDF object that is implemented as a dictionary, except the file trailer dictionary** (see 7.5.5, "File Trailer"). In addition, a conforming writer may create tags that indicate the role of marked-content operators (PDF 1.2), as described in 14.6, "Marked Content". *(min markering)*

Det finns tre klasser av *private names*: First, Second, Third. First class är de som ingår i standarden. Man kan få ett second class namn registrerat (dock osäkert om detta register är aktivt). Det man registrerar är ett prefix. Privata namn ska ha formen AAAA_Namn, där AAAA är prefixet (fyra tecken). Third class innebär att det finns ett allmänt prefix XX_. Det registreras inte och används när det inte är risk för kollision.

## Invändningar mot standarden ##

Florian Zumbiehl menar att PDF-standarden (PDF/A-1 och, PDF 1.7) innehåller svagheter som gör att man kan ändra i ett dokument utan att påverka signaturen: http://pdfsig-collision.florz.de/

## Små saker att tänka på ##

För `import` är det bra att veta:


* Java har flera klasser som heter Certificate. Den man vanligtvis använder är `java.security.cert.Certificate`
* iText har en klass som heter `List`. Kan lätt bli förväxling med `java.util.List`.
* iText har även `Element`

Man skyddar sig lämpligen genom att undvika `import ...*`.

## Bouncycastle ##

Det verkar som att man behöver Bouncycastle. Kom då ihåg att göra följande:

```
public class Whatever {
    public static final BouncyCastleProvider PROVIDER = 
        new org.bouncycastle.jce.provider.BouncyCastleProvider();
    static {
        Security.addProvider(PROVIDER);
    }
...
}
```

