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
package org.motrice.postxdb

import grails.test.mixin.*

import org.junit.*
import org.motrice.postxdb.FormdefPath;

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
class FormdefPathTests {
  final shouldFail = new GroovyTestCase().&shouldFail

  void testPathConstruct() {
    def fdp = new FormdefPath('alpha/beta')
    assert !fdp.versioned
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta'
    assert fdp.unversioned == 'alpha/beta'
    assert fdp.toString() == 'alpha/beta'

    fdp = new FormdefPath('alpha/beta--v001_01')
    assert fdp.versioned
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta'
    assert fdp.unversioned == 'alpha/beta'
    assert fdp.version == 1
    assert fdp.draftState
    assert fdp.draft == 1
    assert !fdp.published
    assert !fdp.withdrawn
    assert fdp.toString() == 'alpha/beta--v001_01'
    assert fdp.toString(false) == 'beta--v001_01'

    fdp = new FormdefPath('alpha/beta--v097_999')
    assert fdp.versioned
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta'
    assert fdp.unversioned == 'alpha/beta'
    assert fdp.version == 97
    assert fdp.draftState
    assert fdp.draft == 999
    assert !fdp.published
    assert !fdp.withdrawn
    assert fdp.toString() == 'alpha/beta--v097_999'

    fdp = new FormdefPath('alpha/beta--v017')
    assert fdp.versioned
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta'
    assert fdp.unversioned == 'alpha/beta'
    assert fdp.version == 17
    assert !fdp.draftState
    assert fdp.published
    assert !fdp.withdrawn
    assert fdp.toString() == 'alpha/beta--v017'

    fdp = new FormdefPath('alpha/beta--v017_X')
    assert fdp.versioned
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta'
    assert fdp.unversioned == 'alpha/beta'
    assert fdp.version == 17
    assert !fdp.draftState
    assert !fdp.published
    assert fdp.withdrawn
    assert fdp.toString() == 'alpha/beta--v017_X'
  }

  void testComponentConstruct() {
    def fdp = new FormdefPath('alpha', 'beta')
    assert !fdp.versioned
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta'
    assert fdp.unversioned == 'alpha/beta'
    assert fdp.toString() == 'alpha/beta'

    fdp = new FormdefPath('alpha', 'beta', 1, 1)
    assert fdp.versioned
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta'
    assert fdp.unversioned == 'alpha/beta'
    assert fdp.version == 1
    assert fdp.draftState
    assert fdp.draft == 1
    assert !fdp.published
    assert !fdp.withdrawn
    assert fdp.toString() == 'alpha/beta--v001_01'
    assert fdp.toString(false) == 'beta--v001_01'

    fdp = new FormdefPath('alpha', 'beta', 97, 11)
    assert fdp.versioned
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta'
    assert fdp.unversioned == 'alpha/beta'
    assert fdp.version == 97
    assert fdp.draftState
    assert fdp.draft == 11
    assert !fdp.published
    assert !fdp.withdrawn
    assert fdp.toString() == 'alpha/beta--v097_11'

    fdp = new FormdefPath('alpha', 'beta', 17)
    assert fdp.versioned
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta'
    assert fdp.unversioned == 'alpha/beta'
    assert fdp.version == 17
    assert !fdp.draftState
    assert fdp.published
    assert !fdp.withdrawn
    assert fdp.toString() == 'alpha/beta--v017'

    fdp = new FormdefPath('alpha', 'beta', 17)
    fdp.withdraw()
    assert fdp.versioned
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta'
    assert fdp.unversioned == 'alpha/beta'
    assert fdp.version == 17
    assert !fdp.draftState
    assert !fdp.published
    assert fdp.withdrawn
    assert fdp.toString() == 'alpha/beta--v017_X'
  }

  void testActions() {
    def fdp = new FormdefPath('alpha/beta--v097_11')
    fdp.publish()
    assert fdp.version == 97
    assert !fdp.draftState
    assert fdp.published
    assert !fdp.withdrawn
    assert fdp.toString() == 'alpha/beta--v097'

    fdp = new FormdefPath('alpha/beta--v097_11')
    fdp.nextDraft()
    assert fdp.toString() == 'alpha/beta--v097_12'
    fdp.nextVersion()
    assert fdp.toString() == 'alpha/beta--v098_01'
  }

  void testStaticMethods() {
    assert FormdefPath.app('alpha/beta') == 'alpha'
    assert FormdefPath.app('alpha/beta--v001_01') == 'alpha'
    assert FormdefPath.app('alpha/beta--v001') == 'alpha'
    assert FormdefPath.app('alpha/beta--v001_X') == 'alpha'

    assert FormdefPath.form('alpha/beta') == 'beta'
    assert FormdefPath.form('alpha/beta--v001_01') == 'beta'
    assert FormdefPath.form('alpha/beta--v001') == 'beta'
    assert FormdefPath.form('alpha/beta--v001_X') == 'beta'
  }

  void testWeirdCases() {
    def fdp = new FormdefPath('alpha/beta--v');
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta--v'
    assert !fdp.versioned

    fdp = new FormdefPath('alpha/beta--vURRK');
    assert fdp.appName == 'alpha'
    assert fdp.formName == 'beta--vURRK'
    assert !fdp.versioned

    shouldFail(IllegalArgumentException) {
      new FormdefPath('alpha/beta--v2222222222222222222222222222222222222222217')
    }

    shouldFail(IllegalArgumentException) {
      new FormdefPath('alpha/beta--v004_111112222222222333333333444444444445555555555')
    }

    shouldFail(IllegalArgumentException) {
      new FormdefPath('alpha/beta--v004_1000')
    }

    shouldFail(IllegalArgumentException) {
      new FormdefPath('alpha/beta--v1000_1')
    }

  }

  void testIncrements() {
    def fdp = new FormdefPath('alpha/beta')
    fdp.nextDraft()
    assert fdp.toString() == 'alpha/beta--v001_01'

    fdp = new FormdefPath('alpha/beta')
    fdp.nextVersion()
    assert fdp.toString() == 'alpha/beta--v001_01'

    fdp.nextDraft()
    assert fdp.toString() == 'alpha/beta--v001_02'

    fdp.nextDraft()
    assert fdp.toString() == 'alpha/beta--v001_03'

    fdp.nextVersion()
    assert fdp.toString() == 'alpha/beta--v002_01'

    fdp.nextVersion()
    assert fdp.toString() == 'alpha/beta--v003_01'

    fdp.publish()
    assert fdp.toString() == 'alpha/beta--v003'

    fdp.nextDraft()
    assert fdp.toString() == 'alpha/beta--v004_01'

    fdp.publish()
    assert fdp.toString() == 'alpha/beta--v004'

    fdp.nextVersion()
    assert fdp.toString() == 'alpha/beta--v005_01'

    fdp.withdraw()
    assert fdp.toString() == 'alpha/beta--v005_X'
  }
}
