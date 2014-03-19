/*
 * Crude script for comparing i18n/*.properties files.
 * Assumes messages.properties is the original.
 * Command line arguments: one or more locales (only the language part)
 * separated by white space.
 */

MSGDIR = new File('grails-app/i18n')

def doit(String locale) {
  def enProps = readMessages()
  def otherProps = readMessages('sv')
  def missingSet = new TreeSet()
  enProps.each {entry ->
    println "${entry.key} || ${entry.value}"
    def other = otherProps.getProperty(entry.key)
    if (other) {
      println "   ${other.value}"
    } else {
      println "   *****"
      missingSet << entry.key
    }
  }

  println "Missing entries:"
  missingSet.each {println "${it}"}
}

Properties readMessages(String locale) {
  def props = new Properties()
  def fileName = locale? "messages_${locale}.properties" : "messages.properties"
  def file = new File(MSGDIR, fileName)
  file.withReader {reader ->
    props.load(reader)
  }

  return props
}

/**
 * For the default locale
 */
Properties readMessages() {
  readMessages(null)
}

println ""
if (args.size() < 1) {
  println "At least one locale required"
} else {
  def argList = args.collect {it}
  argList.each {locale ->
    doit(locale)
  }
}
