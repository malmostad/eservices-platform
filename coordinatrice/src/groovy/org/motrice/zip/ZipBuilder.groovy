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
package org.motrice.zip

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

// For @InheritConstructors
import groovy.transform.*

/**
 * Create a zip file the Groovy way.
 * The ZipBuilder provides two methods:
 * - zip(): creates and manages the ZipOutputStream
 * - entry() (nested): creates and adds a ZipEntry to the zip stream
 * Example:
 * new ZipBuilder(new FileOutputStream(zipFile)).zip {
 *   new File(dir).traverse(type: FileType.FILES) {File file ->
 *     entry(file.path, size: file.length(), time: file.lastModified()) {
 *       it << file.bytes
 *     }
 *   }
 * }
 */
class ZipBuilder {
 
  @InheritConstructors
  static class NonClosingOutputStream extends FilterOutputStream {
    void close() {
      // Do nothing to prevent the stream from being closed
    }
  }
 
  ZipOutputStream zos
 
  ZipBuilder(OutputStream os) {
    zos = new ZipOutputStream(os)
  }
 
  void zip(Closure closure) {
    closure.delegate = this
    closure.call()
    zos.close()
  }
 
  void entry(Map props, String name, Closure closure) {
    def entry = new ZipEntry(name)
    props.each {k, v -> entry[k] = v}
    zos.putNextEntry(entry)
    NonClosingOutputStream ncos = new NonClosingOutputStream(zos)
    closure.call(ncos)
  }
 
  void entry(String name, Closure closure) {
    entry([:], name, closure)
  }
}
