# docbox Releases #

## docbox 0.7.1 ##

Copyright notice added to source files.

## docbox 0.7.0 ##

Add the Motrice site name to PDF documents.

## docbox 0.6.0 ##

New configuration conventions (aligned with other Motrice components). JMX control.

## docbox 0.5.7 ##

Two new methods added to the API, getting form data, getting all signatures.


**Incompatibility:** Can only read form data or signatures from PDF created by docbox 0.5.7 or later.


* **PDF document**  
 Improved format for storing DocBox data in PDF
* **Signature storage**  
 Improved format for storing DocBox data in PDF
* **Administrative gui**  
 --

## docbox 0.5.6 ##


* **PDF document**  
 --
* **Signature storage**  
 --
* **Administrative gui**  
 One of the features in the previous release was too progressive, reverted.

## docbox 0.5.5 ##


* **PDF document**  
 --
* **Signature storage**  
 --
* **Administrative gui**  
 Added sorting to domains, slightly improved display

## docbox 0.5.4 ##


* **PDF document**  
 QR code added to signature page (configurable)
* **Signature storage**  
 Check signed text for document number and checksum (configurable)
* **Administrative gui**  
 --

## docbox 0.5.3 ##


* **PDF conversion**  
 --
* **PDF document**  
 Checksum added to visible signature info
* **Signature storage**  
 Signature (semantic) validation added
* **Administrative gui**  
 --

## docbox 0.5.2 ##


* **PDF conversion**  
 --
* **PDF document**  
 --
* **Signature storage**  
 The signature syntax is validated before being stored
* **Administrative gui**  
 All downloads generate meaningful file names

## docbox 0.5.1 ##


* **PDF conversion**  
 --
* **PDF document**  
 Original form data XML is stored in the document.
* **Signature storage**  
 --
* **Administrative gui**  
 --

## docbox 0.5.0 ##

Adding a signature to a document is now for real.


* **PDF conversion**  
 --
* **PDF document**  
 --
* **Signature storage**  
 Adding a signature adds a page to the document. The page contains readable information about the added signature. The page also contains the signature itself as an invisible resource.
* **Administrative gui**  
 --

## docbox 0.4.0 ##

Operation for adding a signature to a document added.


* **PDF conversion**  
 Metadata visible in the document
* **PDF document**  
 The checksum is now real
* **Signature storage**  
 **DUMMY** operation implemented for testing, see the API doc. This version returns 404 if the document is not found, otherwise does nothing and always returns the same result.
* **Administrative gui**  
 --

## docbox 0.3.3 ##


* **PDF conversion**  
 Should work with Orbeon 3.9, accepts most of Orbeon 4.3 controls and attachments
* **PDF document**  
 PDF/A-1a. Contains an RDF metadata stream. Metadata also visible in document and includes document number.
* **Signature storage**  
 Not implemented
* **Administrative gui**  
 An unpolished version implemented

Caution:

* Author and publisher missing in metadata (info not available)
* The document checksum is a dummy, a random string unrelated to document contents
* No authentication required for the administrative gui

