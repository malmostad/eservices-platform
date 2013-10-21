package org.motrice.docbox.pdf

import org.motrice.docbox.PdfDictFactory

import com.itextpdf.text.pdf.PdfDate
import com.itextpdf.text.pdf.PdfDictionary
import com.itextpdf.text.pdf.PdfName
import com.itextpdf.text.pdf.PdfString

/**
 * Read/write a Pdf dictionary containing form data.
 * Construct from elements by using Groovy speciality
 * [checksum: '...', docNo: '...' etc] as PdfSignatureDict
 * or
 * PdfSignatureDict variable = [map as above]
 */
class PdfFormdataDict {
  // Dictionary keys
  static final FORMDATA_KEY = new PdfName('FormData')
  static final FORMXREF_KEY = new PdfName('FormXref')

  // Form data XML
  String formData

  // Form cross-reference (XML)
  String formXref

  // Format of this dictionary, an uri pointing to a web page
  String format

  // Timestamp
  Date timestamp

  /**
   * Construct from a Pdf dictionary
   */
  PdfFormdataDict(PdfDictionary dict) {
    formData = dict.getAsString(FORMDATA_KEY).toString()
    formXref = dict.getAsString(FORMXREF_KEY).toString()
    format = dict.getAsString(PdfDictFactory.FORMAT_KEY).toString()
    def dateString = dict.getAsString(PdfDictFactory.TIMESTAMP_KEY)?.toString()
    if (dateString) timestamp = PdfDate.decode(dateString)?.time
  }

  /**
   * Construct from data
   */
  PdfFormdataDict(String formData, String formXref) {
    this.formData = formData
    this.formXref = formXref
  }

  static create(PdfDictionary dict) {
    def type = dict.getAsString(PdfDictFactory.DOCBOXTYPE_KEY)?.toString()
    return (type == PdfFormdataDict.class.name)? new PdfFormdataDict(dict) : null
  }

  PdfDictionary toDictionary(String format) {
    def dict = new PdfDictionary()
    dict.put(PdfDictFactory.DOCBOXTYPE_KEY, new PdfString(this.class.name))
    dict.put(PdfDictFactory.TIMESTAMP_KEY, new PdfDate())
    dict.put(PdfDictFactory.FORMAT_KEY, new PdfString(format))
    dict.put(FORMDATA_KEY, new PdfString(formData))
    dict.put(FORMXREF_KEY, new PdfString(formXref))
    return dict
  }

  String toString() {
    def dateStr = timestamp?.format('yyyy-MM-dd HH:mm:ss')
    def xformData = (formData?.length() > 24)? formData[0..23] + '...' : formData
    def xformXref = (formXref?.length() > 24)? formXref[0..23] + '...' : formXref
    "[FormdataDict ||${xformXref}||${xformData}|| ${dateStr}]"
  }

}
