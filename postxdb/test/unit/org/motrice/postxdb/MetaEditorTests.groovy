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
import org.motrice.postxdb.MetaEditor;
import org.motrice.postxdb.MetaExtractor;

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 * XML files used by the tests are located in the main project directory
 */
class MetaEditorTests {
  final shouldFail = new GroovyTestCase().&shouldFail

  void testBasics() {
    def formFile = new File('submarine-rescue-form.xhtml')

    // Make sure we know the initial state
    def meta = MetaExtractor.extract(formFile.text)
    assert meta.app == 'submarine'
    assert meta.form == 'rescue'
    assert meta.title.trim() == 'Rescue Application'
    assert meta.author == null
    assert meta.language == 'en'

    // Identity transformation
    def input = formFile.text
    def editor = new MetaEditor(input)
    editor.edit();
    def meta1 = editor.getMetadata()
    assert meta1.form == 'rescue'
    assert meta1.language == 'en'
    def meta2 = MetaExtractor.extract(editor.formdefOut)
    def comp = meta1.equals(meta)
    if (!comp) println meta1.diff(meta)
    assert comp
    assert meta2.equals(meta)

    // Add draft
    editor = new MetaEditor(input)
    editor.opIncrDraft()
    editor.edit()
    meta2 = editor.getMetadata()
    assert meta2.form == 'rescue--v001_01'
    assert meta2.language == 'en'

    // Increment draft
    editor = new MetaEditor(editor.formdefOut)
    editor.opIncrDraft()
    editor.edit()
    meta2 = editor.getMetadata()
    assert meta2.form == 'rescue--v001_02'
    assert meta2.language == 'en'

    // Increment version
    editor = new MetaEditor(editor.formdefOut)
    editor.opIncrVersion()
    editor.edit()
    meta2 = editor.getMetadata()
    assert meta2.form == 'rescue--v002_01'
    assert meta2.language == 'en'

    // Increment draft, change language
    editor = new MetaEditor(editor.formdefOut)
    editor.opIncrDraft()
    editor.opLangEdit('sv')
    editor.edit()
    meta2 = editor.getMetadata()
    assert meta2.form == 'rescue--v002_02'
    assert meta2.language == 'sv'

    // Publish
    editor = new MetaEditor(editor.formdefOut)
    editor.opPublish()
    editor.edit()
    meta2 = editor.getMetadata()
    assert meta2.form == 'rescue--v002'
    assert meta2.language == 'sv'

    // Withdraw
    editor = new MetaEditor(editor.formdefOut)
    editor.opWithdraw()
    editor.opLangEdit('en')
    editor.edit()
    meta2 = editor.getMetadata()
    assert meta2.form == 'rescue--v002_X'
    assert meta2.language == 'en'

    // Assign
    editor = new MetaEditor(editor.formdefOut)
    editor.opAssignVersion(7, 17)
    editor.opLangEdit('sv')
    editor.edit()
    meta2 = editor.getMetadata()
    assert meta2.form == 'rescue--v007_17'
    assert meta2.language == 'sv'
    
  }

}
