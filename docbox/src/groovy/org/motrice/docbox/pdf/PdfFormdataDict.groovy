/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
 *
 * Copyright (C) 2011-2014 Motrice AB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * e-mail: info _at_ motrice.se
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
 * phone: +46 8 641 64 14
 */
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
