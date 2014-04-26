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
    f = new UriFormatter('%H/%P/%A/%L.html')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) ==
      'http://localhost:8080/site/ForenkladDelgivning/Handl%C3%A4ggning/sv.html'
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, null, LOCALE) ==
      'http://localhost:8080/site/ForenkladDelgivning//sv.html'
  }

  void testConditionals() {
    def f = null
    f = new UriFormatter('%H/%P%?A{/}%A/%L.html')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, null, LOCALE) ==
      'http://localhost:8080/site/ForenkladDelgivning/sv.html'
    f = new UriFormatter('%H/%P%?A{/|-generic}%A/%L.html')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, null, LOCALE) ==
      'http://localhost:8080/site/ForenkladDelgivning-generic/sv.html'
    f = new UriFormatter('%H/%?P{|process}%P%?P{-descr}%?A{/|-generic}%A/doco.html%?L{?locale=|?locale=ru}%L')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) ==
      'http://localhost:8080/site/ForenkladDelgivning-descr/Handl%C3%A4ggning/doco.html?locale=sv'
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, null, LOCALE) ==
      'http://localhost:8080/site/ForenkladDelgivning-descr-generic/doco.html?locale=sv'
    assert f.format(BASEURI, null, PROCESS_VER, ACTIVITY_NAME, LOCALE) ==
      'http://localhost:8080/site/process/Handl%C3%A4ggning/doco.html?locale=sv'
    assert f.format(BASEURI, null, PROCESS_VER, null, LOCALE) ==
      'http://localhost:8080/site/process-generic/doco.html?locale=sv'
    assert f.format(BASEURI, null, PROCESS_VER, null, null) ==
      'http://localhost:8080/site/process-generic/doco.html?locale=ru'
  }

  void testMoreConditionals() {
    def f = null
    f = new UriFormatter('%?H{!"#¤%&/()=?+|<>;.-:_{|}')
    assert f.format(null, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == '<>;.-:_{|'
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == '!\"#¤&/()=?+'
    f = new UriFormatter('%?P{<>;.-:_{|!"#¤%&/()=?+}')
    assert f.format(BASEURI, null, PROCESS_VER, ACTIVITY_NAME, LOCALE) == '!\"#¤&/()=?+'
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == '<>;.-:_{'
    f = new UriFormatter('wow-%?V{hejsan|hoppsan}')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == 'wow-hejsan'
    assert f.format(BASEURI, PROCESS_KEY, null, ACTIVITY_NAME, LOCALE) == 'wow-hoppsan'
    f = new UriFormatter('wow-%?H{|}%?P{|}%?V{|}%?A{|}%?L{|}-how')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == 'wow--how'
    assert f.format(null, null, null, null, null) == 'wow--how'
    f = new UriFormatter('wow-%?Hextra{|}%?P#¤%/()={|}%?V?><,.-{|}%?A_:;{|}%?L{|}-how')
    assert f.format(BASEURI, PROCESS_KEY, PROCESS_VER, ACTIVITY_NAME, LOCALE) == 'wow--how'
    assert f.format(null, null, null, null, null) == 'wow--how'
  }

}
