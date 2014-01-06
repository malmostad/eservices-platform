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
