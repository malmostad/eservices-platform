package org.motrice.docbox

/**
 * Methods for checking the environment
 */
class EnvService {
  // Required packages for running docbox
  public static final String PACKAGES =
    'docbook-xml docbook-xsl xsltproc fop libxml2-utils xmlstarlet fonts-liberation fonts-opensymbol'
  // Shell command to check if packages are installed
  public static final String CHECK = "for p in ${PACKAGES} ; " +
    'do dpkg -s "$p" >/dev/null 2>&1 && { echo "OK/$p" ; } || { echo "ERR/$p" ; } ; done'

    /**
     * Validate the environment where docbox is installed
     * @return a map containing the following items
     */
    Map validate() {
      // Check that packages are installed
      def process = ["/bin/bash", "-c", CHECK].execute()
      def pkgok = []
      def pkgerr = []
      process.in.eachLine {line -> 
	if (line.startsWith('ERR')) {
	  pkgerr << line
	} else {
	  pkgok << line
	}
      }

      def map = [:]
      map.put('errorCount', pkgerr.size())
      String msg = "Packages: ${(pkgerr + pkgok).join(', ')}"
      map.put('pkgMessage', msg)
      return map
    }
}
