package org.motrice.postxdb

import grails.test.mixin.*

import org.junit.*
import org.motrice.postxdb.MetaExtractor;

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 * XML files used by the tests are located in the main project directory
 */
class MetaExtractorTests {
  final shouldFail = new GroovyTestCase().&shouldFail

  void testBasics() {
    def formFile = new File('submarine-rescue-form.xhtml')
    def meta = MetaExtractor.extract(formFile.text)
    assert meta
    assert meta.app == 'submarine'
    assert meta.form == 'rescue'
    assert meta.title.trim() == 'Rescue Application'
    assert meta.description == 'Fill in this form for submarine rescue action. Application is promptly processed the next day. In most cases. Obviously not during bank holidays.'
    assert meta.author == null
    assert meta.language == 'en'
    assert meta.logo == '/fr/service/exist/crud/orbeon/builder/data/277f56f04a9dc32b3cc316843a5414fb/75b304166dc3354af84f8e6bad327f6f.png'
  }

}
