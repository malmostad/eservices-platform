/*
 * Insert or edit a copyright notice in the source files of a Grails application.
 * The main input (besides application source code) is a set of copyright
 * notice templates.
 * The templates contain placeholders for several hard data.
 * 
 * Usage:
 * 1. Create a git branch for the application. The program edits in-place.
 * 2. Make sure you did (1). No other backup is made.
 * 3. Check the BINDINGS map below for accuracy. The current year is set automatically.
 * 4. Run in this directory. It contains this Groovy script and copyright templates.
 * Command line argument: The path of the top directory of the Grails application.
 * Example: groovy editGrailsCopyright.groovy /here/is/my/app
 * 5. Run mvn clean install to check that the application still compiles.
 * 6. Do any other tests to make sure the operation was successful.
 * 7. Commit and merge back.
 *
 * The new copyright notice will replace any existing copyright notice, regardless
 * of differences.
 *
 * TEMPLATE CONVENTIONS:
 * A copyright notice is assumed to begin on the first line of a source file.
 * It must contain the comment opening character sequence and the words
 * Motrice Copyright Notice
 * 
 * The copyright notice must end with a line containing an end-of-comment character
 * sequence, possibly preceded by white space.
 *
 * WARNING: Make sure you have a proper backup of the source files.
 * Files are modified in-place.
 */

import groovy.io.FileType
import groovy.text.GStringTemplateEngine

// Bind template symbols. Keys are all upper case by convention.
// The JSP comments must be inserted by substitution because JSP interferes with
// GString template placeholders.
THISYEAR = new Date().format('yyyy')
BINDINGS = [PRODUCT: 'Motrice Service Platform', YRFIRST: '2011', YRLAST: THISYEAR,
COMPANY: 'Motrice AB', EMAIL: 'info _at_ motrice.se',
POSTAL: 'Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN', PHONE: '+46 8 641 64 14',
JSPOPEN: '<%--', JSPCLOSE: '--%>']

// Copyright notice files to use depending on file name extension.
// Assumed to reside in this directory.
TEMPLATES = [css: 'copyright.css', groovy: 'copyright.java', gsp: 'copyright.jsp',
java: 'copyright.java', js: 'copyright.css', jsp: 'copyright.jsp']
// The copyright notice texts after template substitutions.
TEXTS = null

// Comment patterns
JAVABEGIN = ~/^\s*\/\*[ =-]+Motrice\s+Copyright\s+Notice/
JAVAEND = '*/'
JAVASTYLE = [JAVABEGIN, JAVAEND]
JSPBEGIN = ~/^\s*<%--[ =-]+Motrice\s+Copyright\s+Notice/
JSPEND = '--%>'
JSPSTYLE = [JSPBEGIN, JSPEND]
// Comment patterns per file extension.
// Each entry is a list containing a regex pattern for the opening comment
// and a literal end-of-comment string.
COMMENT = [css: JAVASTYLE, groovy: JAVASTYLE, gsp: JSPSTYLE, java: JAVASTYLE,
js: JAVASTYLE, jsp: JSPSTYLE]
EXTENSIONS = COMMENT.keySet()

// Top-level directories to exclude from action.
DIREXCLUDE = ['lib', 'plugins', 'target']

def doit(path) {
  TEXTS = bindTemplates(TEMPLATES)
  def topDir = new File(path)
  if (!topDir.directory) {
    println "Sorry, '${path}' is not a directory. Nothing edited."
    System.exit(1)
  }

  def fileCount = 0

  // Process top-level files
  topDir.eachFile(FileType.FILES) {file ->
    fileCount += processFile(topDir, file)
  }

  // Process top-level directories recursively
  topDir.eachFile(FileType.DIRECTORIES) {dir ->
    if (!DIREXCLUDE.contains(dir.name)) {
      fileCount += processDir(dir)
    }
  }

  println "Finished. Number of processed files: ${fileCount}"
}

// Creates a map from the copyright templates.
// Key: File name extension.
// Value: Copyright notice text.
Map bindTemplates(Map fileNameMap) {
  def map = [:]
  def engine = new GStringTemplateEngine()
  fileNameMap.each {entry ->
    def template = new File(entry.value)
    def text = engine.createTemplate(template).make(BINDINGS).toString()
    // Trim to remove any trailing newline.
    // "Intelligent" editors typically add a newline automatically in java mode.
    // As a side effect, any leading whitespace is trimmed off.
    map[entry.key] = text.trim()
  }

  return map
}

// Given a file, return the file type (really file name extension) if it is
// one of the extensions we should process.
// Return null otherwise.
String fileTypeFlag(File file) {
  def ext = null
  def fileName = file.name
  def idx = fileName.lastIndexOf('.')
  if (idx >= 0 && (idx + 1) < fileName.length()) ext = fileName.substring(idx + 1)
  return (ext && EXTENSIONS.contains(ext))? ext : null
}

// Process a directory recursively.
// Return the number of processed files.
int processDir(File dir) {
  def fileCount = 0
  dir.eachFile(FileType.FILES) {file ->
    fileCount += processFile(dir, file)
  }
  dir.eachFile(FileType.DIRECTORIES) {subdir ->
    fileCount += processDir(subdir)
  }

  return fileCount
}

// Process a plain file (must not be a directory)
// Return the number of files processed (0 or 1)
int processFile(File dir, File file) {
  def fileCount = 0
  def flag = fileTypeFlag(file)
  if (flag) {
    doEditFile(dir, file, flag)
    fileCount = 1
  }

  return fileCount
}

// Do the real work, insert or replace a copyright notice.
def doEditFile(File dir, File file, String fileType) {
  def style = COMMENT[fileType]
  def fileName = file.name
  def backupName = fileName + '~'
  def backupFile = new File(dir, backupName)
  // If there is already a file with the backup file name, delete it
  backupFile.delete()
  file.renameTo(backupFile)
  
  // Write the file again
  file.withPrintWriter('UTF-8') {pw ->
    // The first part of the file is always the copyright notice
    pw.println(TEXTS[fileType])

    // Add old contents, but skip over any initial copyright notice
    def copyrightFlag = false
    backupFile.eachLine('UTF-8', 1) {text, lno ->
      if (lno == 1) {
	def m = style[0].matcher(text)
	copyrightFlag = m.find() as Boolean
      }

      if (copyrightFlag) {
	boolean endOfComment = text.trim().indexOf(style[1]) == 0
	if (endOfComment) copyrightFlag = false
      } else {
	pw.println(text)
      }
    }
  }

  // Delete the backup file
  backupFile.delete()
}

// Start the script
if (args.length > 0) {
  doit(args[0])
}
