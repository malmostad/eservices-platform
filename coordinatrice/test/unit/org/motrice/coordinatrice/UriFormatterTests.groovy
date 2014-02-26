package org.motrice.coordinatrice

import grails.test.mixin.*

import org.junit.*
import org.motrice.coordinatrice.UriFormatter

/**
 * Uri formatter tests
 */
class UriFormatterTests {
  final shouldFail = new GroovyTestCase().&shouldFail

  final static PROCESS_KEY = 'ForenkladDelgivning'
  final static PROCESS_VER = 1
  final static ACTIVITY_NAME = 'Handläggning'
  final static LOCALE = 'sv'
  final static LONG_TEXT =
'''Battle re-enactments are for the romantically inclined. When the Battle of Naseby (1645) is staged by the Sealed Knot society it is a family fun weekend complete with Roundheads, Cavaliers and the blank-firing “Sweaty Betty” cannon.
STUART GULLIVER, chief executive of HSBC, is to be offered a huge salary increase to get round new European rules that curb bankers’ bonuses.
The 54-year-old former trader is believed to have been paid about 7.5m for last year after being handed bonuses and benefits worth five times his basic salary.
New rules imposed by Brussels block bankers from being paid bonuses worth more than two times salary--a restriction that has prompted banks to develop new pay schemes.
The rules came into effect in January, meaning that 2013 was the last year in which such big bonuses could be paid.
It is understood that HSBC’s annual report, to be published this week, will indicate that the bank is considering raising Gulliver’s basic pay this year to compensate for the restrictions.'''

  void testBasics() {
    def f = null
    f = new UriFormatter('')
    assert f.format(PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == ''
    f = new UriFormatter('%')
    assert f.format(PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == '%25'
    f = new UriFormatter('%P')
    assert f.format(PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == PROCESS_KEY
    f = new UriFormatter('%V')
    assert f.format(PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == '01'
    f = new UriFormatter('%A')
    assert f.format(PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == 'Handl%C3%A4ggning'
    f = new UriFormatter('%L')
    assert f.format(PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == LOCALE
    f = new UriFormatter('Hej%%%PProcess%VVersion%AActivity%LLocale')
    assert f.format(PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) ==
      'Hej%25ForenkladDelgivningProcess01VersionHandl%C3%A4ggningActivitysvLocale'
  }

  void testALittleMore() {
    def f = null
    f = new UriFormatter('Hej%%%PProcess%VVersion%AActivity%LLocale')
    assert f.format(PROCESS_KEY, ACTIVITY_NAME, LOCALE) ==
      'Hej%25ForenkladDelgivningProcessVersionHandl%C3%A4ggningActivitysvLocale'
    f = new UriFormatter('Hej%%%PProcess%VVersion%AActivity%LLocale')
    assert f.format(PROCESS_KEY, LOCALE) ==
      'Hej%25ForenkladDelgivningProcessVersionActivitysvLocale'
    f = new UriFormatter(LONG_TEXT)
    assert f.format(PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == LONG_TEXT
    f = new UriFormatter('Hej%%%PProcess%VVersion%AActivity%LLocale')
    shouldFail(IllegalArgumentException) {
      f.format(null, PROCESS_VER, ACTIVITY_NAME, LOCALE)
    }
    f = new UriFormatter('Hej%%%PProcess%VVersion%AActivity%LLocale')
    shouldFail(IllegalArgumentException) {
      f.format(PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, null)
    }
  }

}
