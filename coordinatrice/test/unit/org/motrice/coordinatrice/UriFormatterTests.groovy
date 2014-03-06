package org.motrice.coordinatrice

import grails.test.mixin.*

import org.junit.*
import org.motrice.coordinatrice.UriFormatter

/**
 * Uri formatter tests
 */
class UriFormatterTests {
  final shouldFail = new GroovyTestCase().&shouldFail

  final static BASEURI = 'http://localhost:8080/site'
  final static PROCESS_KEY = 'ForenkladDelgivning'
  final static PROCESS_VER = 3
  final static ACTIVITY_NAME = 'Handläggning'
  final static LOCALE = 'sv'

  void testBasics() {
    def f = null
    f = new UriFormatter('')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == ''
    f = new UriFormatter('%')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == '%25'
    f = new UriFormatter('%P')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == PROCESS_KEY
    f = new UriFormatter('%V')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == '03'
    f = new UriFormatter('%A')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == 'Handl%C3%A4ggning'
    f = new UriFormatter('%L')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == LOCALE
    f = new UriFormatter('Hej%%%PProcess%VVersion%AActivity%LLocale')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) ==
      'Hej%25ForenkladDelgivningProcess03VersionHandl%C3%A4ggningActivitysvLocale'
    f = new UriFormatter('%H/Hej%%%PProcess%VVersion%AActivity%LLocale')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) ==
      'http://localhost:8080/site/Hej%25ForenkladDelgivningProcess03VersionHandl%C3%A4ggningActivitysvLocale'
  }

  void testALittleMore() {
    def f = null
    f = new UriFormatter('Hej%%%PProcess%VVersion%AActivity%LLocale')
    assert f.format(BASEURI, PROCESS_KEY, ACTIVITY_NAME, LOCALE) ==
      'Hej%25ForenkladDelgivningProcessVersionHandl%C3%A4ggningActivitysvLocale'
    f = new UriFormatter('Hej%%%PProcess%VVersion%AActivity%LLocale')
    assert f.format(BASEURI, PROCESS_KEY, LOCALE) ==
      'Hej%25ForenkladDelgivningProcessVersionActivitysvLocale'
    f = new UriFormatter('Hej%%%PProcess%VVersion%AActivity%LLocale')
    shouldFail(IllegalArgumentException) {
      f.format(BASEURI, null, PROCESS_VER, ACTIVITY_NAME, LOCALE)
    }
    f = new UriFormatter('Hej%%%PProcess%VVersion%AActivity%LLocale')
    shouldFail(IllegalArgumentException) {
      f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, null)
    }
    // Check for no double slashes
    f = new UriFormatter('%H/%P/%A/%L.html')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) ==
      'http://localhost:8080/site/ForenkladDelgivning/Handl%C3%A4ggning/sv.html'
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, null, LOCALE) ==
      'http://localhost:8080/site/ForenkladDelgivning/sv.html'
      // Double slashes are 
  }

}
