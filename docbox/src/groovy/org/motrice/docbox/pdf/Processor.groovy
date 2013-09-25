package org.motrice.docbox.pdf

/**
 * Process DocBook input to Pdf or Html
 * Create an instance for each conversion, copy all resources there
 * Then run the conversion
 */
class Processor {
  // Buffer size for stdout, stderr
  static final BUFSIZE = 4096

  // Milliseconds in a second
  static final SECOND = 1000L

  // Root dir for temp directories
  static final TMPROOT = new File(System.getProperty('java.io.tmpdir'))

  // Prefix for temp directories
  static final TMPPREFIX = 'pdfconv'

  // HTML stylesheet standard path
  static final HTMLSTYLE = '/usr/share/xml/docbook/stylesheet/docbook-xsl/html/docbook.xsl'

  // Print stylesheet standard path
  static final PRINTSTYLE = '/usr/share/xml/docbook/stylesheet/docbook-xsl/fo/docbook.xsl'

  // Name of DocBook XML file
  static final DOCBOOKFILE = 'docbook.xml'

  // Names of original FO file and modified (RDF metadata added)
  static final FOFILE1 = 'publish1.fo'
  static final FOFILE2 = 'publish2.fo'

  // Name of the generated HTML file
  static final HTMLFILE = 'publish.html'

  // Name of the generated PDF file
  static final PDFFILE = 'publish.pdf'

  // Name of the metadata file
  static final RDFFILE = 'rdf.xml'

  // Name of the log file
  static final LOGFILE = 'publish.log'

  // xsltproc for generating PDF
  static final XSLTPDF = ['/usr/bin/xsltproc',
			   '--stringparam', 'use.extensions', '0',
			   '--stringparam', 'paper.type', 'A4',
			   '--stringparam', 'page.margin.inner', '28mm',
			   '--stringparam', 'page.margin.outer', '18mm',
			   '--stringparam', 'body.start.indent', '0pt',
			   '--stringparam', 'generate.toc', '',
			   '--stringparam', 'body.font.family', 'LiberationSerif',
			   '--stringparam', 'title.font.family', 'LiberationSans',
			   '--stringparam', 'monospace.font.family', 'LiberationMono',
			   '--output'
			  ];

  // xsltproc for generating HTML
  static final XSLTHTML = ['/usr/bin/xsltproc',
			   '--stringparam', 'use.extensions', '0',
			   '--stringparam', 'generate.toc', '',
			   '--stringparam', 'chunker.output.encoding', 'UTF-8',
			   '--stringparam', 'body.font.family', 'LiberationSerif',
			   '--stringparam', 'title.font.family', 'LiberationSans',
			   '--stringparam', 'monospace.font.family', 'LiberationMono',
			   '--output'
			  ];
  
  // fop command
  static final FOP = ['/usr/bin/fop', '-pdfprofile', 'PDF/A-1a', '-c']

  // Tag after which the FO file is split
  static final SPLIT_TAG = '</fo:layout-master-set>'

  // Should temp files be kept on cleanup?
  boolean keepTempFiles

  // Temp dir used for file output
  File tempDir

  // FOP config file path
  String fopConfigPath

  Processor(boolean keepTempFiles) {
    this.keepTempFiles = keepTempFiles
    tempDir = new File(TMPROOT, TMPPREFIX + System.nanoTime())
    tempDir.mkdirs()
    def fopConfig = new File(tempDir, FOP_CONFIG_FILE_NAME)
    fopConfig << FOP_CONFIG
    fopConfigPath = fopConfig.absolutePath
  }

  /**
   * Conditionally wipe temp files
   */
  def cleanUp() {
    if (keepTempFiles) return
    def files = tempDir.list()
    // DEBUG
    //println "cleanUp ${tempDir.name}: ${files}"
    files.each {new File(abspath(it)).delete()}
    tempDir.delete()
  }

  /**
   * Convert DocBook input to Pdf output
   * ASSUMES all resources have been copied to the file system into the
   * temp directory defined in the constructor
   * RETURN a map containing the following entries:
   * pdf: the full path of the generated Pdf file
   * log: the full path of the process log file
   * exc: null on success, an exception and its message if conversion failed
   */
  def toPdf() {
    // Standard out goes directly to the log file
    def log = new FileWriter(abspath(LOGFILE))
    // Standard error is collected here and then added to the log file
    def err = new StringBuilder()
    def foPath1 = abspath(FOFILE1)
    def foPath2 = abspath(FOFILE2)
    // Process exit code
    Integer exitCode
    // Std error from a process (String)
    def procMessage = null
    try {
      // DocBook XML to FO
      def cmd = XSLTPDF.collect {it}
      cmd << foPath1
      cmd << PRINTSTYLE
      cmd << abspath(DOCBOOKFILE)
      def proc = cmd.execute()
      proc.consumeProcessOutput(log, err)
      proc.waitForOrKill(30 * SECOND)
      exitCode = proc?.exitValue()
    } catch (Exception exc) {
      procMessage = exc.toString()
      err.append(procMessage)
      exitCode = 1
    } finally {
      procMessage = err.toString()
      log.append(procMessage)
      log.append "xml -> FO: ${exitCode} (${(exitCode == 0)? 'OK' : 'CONFLICT'})\n\n"
      log.flush()
      err.length = 0
    }

    // FO to PDF
    def foFile1 = new File(foPath1)
    def foFile2 = new File(foPath2)
    def pdfPath = abspath(PDFFILE)

    if (success(exitCode) && foFile1.exists()) {
      insertMetadata(foFile1, foFile2)
      try {
	def cmd = FOP.collect {it}
	cmd << fopConfigPath
	cmd << '-fo'
	cmd << foPath2
	cmd << '-pdf'
	cmd << pdfPath
	def proc = cmd.execute()
	proc.consumeProcessOutput(log, err)
	proc.waitForOrKill(60 * SECOND)
	exitCode = proc?.exitValue()
      } catch (Exception exc) {
	procMessage  = exc.toString()
	log.append(procMessage)
	exitCode = 1
      } finally {
	procMessage = err.toString()
	log.append(procMessage)
	log.append "FO -> PDF: ${exitCode} (${(exitCode == 0)? 'OK' : 'CONFLICT'})\n\n"
	log.flush()
      }
    } else if (success(exitCode)) {
      // In case the failure went undetected (should not happen)
      exitCode = 1
      procMessage = 'xml -> FO: Conversion failed for unknown reason'
    }

    log.close()
    return [pdf: pdfPath, log: abspath(LOGFILE), exc: (success(exitCode)? null : procMessage)]
  }

  /**
   * Convert DocBook input to HTML output
   * RETURN a map containing the following entries:
   * html: the full path of the generated Pdf file
   * log: the full path of the process log file
   * exc: null on success, an exception and its message if conversion failed
   * TBD: The output is ISO-8859-1. Needs zipping resources together.
   */
  def toHtml() {
    // Standard out appends to the log file
    def log = new FileWriter(abspath(LOGFILE), true)
    // Standard error is collected here and then added to the log file
    def err = new StringBuilder()
    // Process exit code
    Integer exitCode
    // Std error from a process (String)
    def procMessage = null
    def htmlPath = abspath(HTMLFILE)
    try {
      // DocBook XML to HTML
      def cmd = XSLTHTML.collect {it}
      cmd << htmlPath
      cmd << HTMLSTYLE
      cmd << abspath(DOCBOOKFILE)
      def proc = cmd.execute()
      proc.consumeProcessOutput(log, err)
      proc.waitForOrKill(30 * SECOND)
      exitCode = proc?.exitValue()
    } catch (Exception exc) {
      procMessage = exc.toString()
      err.append(procMessage)
      exitCode = 1
    } finally {
      procMessage = err.toString()
      log.append(procMessage)
      log.append "xml -> HTML: ${exitCode} (${(exitCode == 0)? 'OK' : 'CONFLICT'})\n"
      log.flush()
      err.length = 0
    }

    log.close()
    return [html: htmlPath, log: abspath(LOGFILE), exc: (success(exitCode)? null : procMessage)]
  }

  private insertMetadata(File infile, File outfile) {
    // Split the FO input where metadata is going to be
    def fo = infile.text
    def idx = fo.indexOf(SPLIT_TAG) + SPLIT_TAG.length()
    def rdfFile = new File(abspath(RDFFILE))
    outfile.text = fo.substring(0, idx)
    outfile << rdfFile.text
    outfile << fo.substring(idx)
  }

  def abspath(String fileName) {
    new File(tempDir, fileName).absolutePath
  }

  // Checks an exit code for success
  Boolean success(Integer exitCode) {
    exitCode == 0
  }

  static final FOP_CONFIG_FILE_NAME = 'fop-config.xml'

  /**
   * FOP configuration file
   * We use Liberation as the main font, OpenSymbol as the symbol font
   */
  static final FOP_CONFIG = '''\
<?xml version="1.0" encoding="UTF-8"?>
<fop version="1.0">
  <font-base>file:///usr/share/fonts/truetype/</font-base>
  <default-page-settings height="11.692in" width="8.267in"/>
  <renderers>
    <renderer mime="application/pdf">
      <filterList>
        <!-- Compression using zlib flate (default is on). -->
        <value>flate</value>
      </filterList>
      <fonts>

	<!-- Times -->
        <font kerning="yes" embed-url="file:liberation/LiberationSerif-Regular.ttf">
          <font-triplet name="LiberationSerif" style="normal" weight="normal"/>
        </font>
        <font kerning="yes" embed-url="file:liberation/LiberationSerif-Bold.ttf">
          <font-triplet name="LiberationSerif" style="normal" weight="bold"/>
        </font>
        <font kerning="yes" embed-url="file:liberation/LiberationSerif-Italic.ttf">
          <font-triplet name="LiberationSerif" style="italic" weight="normal"/>
        </font>
        <font kerning="yes" embed-url="file:liberation/LiberationSerif-BoldItalic.ttf">
          <font-triplet name="LiberationSerif" style="italic" weight="bold"/>
        </font>

	<!-- Helvetica -->
        <font kerning="yes" embed-url="file:liberation/LiberationSans-Regular.ttf">
          <font-triplet name="LiberationSans" style="normal" weight="normal"/>
        </font>
        <font kerning="yes" embed-url="file:liberation/LiberationSans-Bold.ttf">
          <font-triplet name="LiberationSans" style="normal" weight="bold"/>
        </font>
        <font kerning="yes" embed-url="file:liberation/LiberationSans-Italic.ttf">
          <font-triplet name="LiberationSans" style="italic" weight="normal"/>
        </font>
        <font kerning="yes" embed-url="file:liberation/LiberationSans-BoldItalic.ttf">
          <font-triplet name="LiberationSans" style="italic" weight="bold"/>
        </font>

	<!-- Courier -->
        <font kerning="yes" embed-url="file:liberation/LiberationMono-Regular.ttf">
          <font-triplet name="LiberationMono" style="normal" weight="normal"/>
        </font>
        <font kerning="yes" embed-url="file:liberation/LiberationMono-Bold.ttf">
          <font-triplet name="LiberationMono" style="normal" weight="bold"/>
        </font>
        <font kerning="yes" embed-url="file:liberation/LiberationMono-Italic.ttf">
          <font-triplet name="LiberationMono" style="italic" weight="normal"/>
        </font>
        <font kerning="yes" embed-url="file:liberation/LiberationMono-BoldItalic.ttf">
          <font-triplet name="LiberationMono" style="italic" weight="bold"/>
        </font>

	<!-- Symbol: OpenSymbol -->
        <font kerning="yes" embed-url="file:openoffice/opens___.ttf">
          <font-triplet name="OpenSymbol" style="normal" weight="normal"/>
        </font>

      </fonts>
    </renderer>
  </renderers>
</fop>'''
}
