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

  println "Missing entries in locale '${locale}':"
  missingSet.each {println "${it}"}

  missingSet.clear()
  otherProps.each {entry ->
    def en = enProps.getProperty(entry.key)
    if (!en) {
      missingSet << entry.key
    }
  }

  println "Missing entries in locale 'en':"
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
