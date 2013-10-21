package org.motrice.docbox.sign

import org.motrice.docbox.PdfDictFactory

import com.itextpdf.text.pdf.PdfDate
import com.itextpdf.text.pdf.PdfDictionary
import com.itextpdf.text.pdf.PdfName
import com.itextpdf.text.pdf.PdfString

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
  static final SIGNATURE_KEY = new PdfName('Signature')

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
   * Construct from a Pdf dictionary
   */
  PdfSignatureDict(PdfDictionary dict) {
    checksum = dict.getAsString(CHECKSUM_KEY).toString()
    docNo = dict.getAsString(DOCNO_KEY).toString()
    format = dict.getAsString(PdfDictFactory.FORMAT_KEY).toString()
    signature = dict.getAsString(SIGNATURE_KEY).toString()
    // PdfDate extends PdfString
    def dateString = dict.getAsString(PdfDictFactory.TIMESTAMP_KEY)?.toString()
    if (dateString) timestamp = PdfDate.decode(dateString)?.time
  }

  /**
   * Construct from data
   */
  PdfSignatureDict(String checksum, String docNo, String signature) {
    this.checksum = checksum
    this.docNo = docNo
    this.signature = signature
  }

  static create(PdfDictionary dict) {
    def type = dict.getAsString(PdfDictFactory.DOCBOXTYPE_KEY)?.toString()
    // static method, cannot query this
    return (type == PdfSignatureDict.class.name)? new PdfSignatureDict(dict) : null
  }

  /**
   * Get a Pdf dictionary from the current state
   */
  PdfDictionary toDictionary(String format) {
    def dict = new PdfDictionary()
    dict.put(PdfDictFactory.DOCBOXTYPE_KEY, new PdfString(this.class.name))
    dict.put(PdfDictFactory.TIMESTAMP_KEY, new PdfDate())
    dict.put(PdfDictFactory.FORMAT_KEY, new PdfString(format))
    dict.put(CHECKSUM_KEY, new PdfString(checksum))
    dict.put(DOCNO_KEY, new PdfString(docNo))
    dict.put(SIGNATURE_KEY, new PdfString(signature))
    return dict
  }

  String toString() {
    def dateStr = timestamp?.format('yyyy-MM-dd HH:mm:ss')
    "[SignatureDict ${docNo} [${checksum}] ${dateStr} sig(${signature?.length()}) ${format}]"
  }
}
