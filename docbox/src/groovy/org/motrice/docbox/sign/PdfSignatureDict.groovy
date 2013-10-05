package org.motrice.docbox.sign

import com.itextpdf.text.pdf.PdfDate
import com.itextpdf.text.pdf.PdfDictionary
import com.itextpdf.text.pdf.PdfName

/**
 * Read/write a Pdf dictionary containing a signature.
 * Construct from elements by using Groovy speciality
 * [checksum: '...', docNo: '...', ...] as PdfSignatureDict
 * or
 * PdfSignatureDict variable = [map]
 */
class PdfSignatureDict {
  // Dictionary keys
  static final CHECKSUM_KEY = new PdfName('Checksum')
  static final DOCNO_KEY = new PdfName('DocNo')
  static final FORMAT_KEY = new PdfName('Format')
  static final SIGNATURE_KEY = new PdfName('Signature')
  static final TIMESTAMP_KEY = new PdfName('Timestamp')

  // Checksum
  String checksum

  // Document number
  String docNo

  // Format of this dictionary, an uri pointing to a web page
  String format

  // A Base64-encoded signature
  String signature

  // Timestamp
  Date timestamp

  /**
   * Construct from a Pdf dictionary.
   * Used when reading a Pdf.
   * The dictionary is the one stored in a resource dictionary under the key defined
   * by the docbox.dictionary.key config property
   */
  PdfSignatureDict(PdfDictionary dict) {
    checksum = dict.getAsString(CHECKSUM_KEY).toString()
    docNo = dict.getAsString(DOCNO_KEY).toString()
    format = dict.getAsString(FORMAT_KEY).toString()
    signature = dict.getAsString(SIGNATURE_KEY).toString()
    // PdfDate extends PdfString
    def dateString = dict.getAsString(TIMESTAMP_KEY).toString()
    timestamp = PdfDate.decode(dateString)?.time
  }

  String toString() {
    def dateStr = timestamp?.format('yyyy-MM-dd HH:mm:ss')
    "[SignatureDict ${docNo} ${format} ${dateStr} [${checksum}] sig(${signature?.length()})]"
  }
}
